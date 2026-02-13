#!/bin/bash
# ─────────────────────────────────────────────────────────
# Keycloak Realm Provisioning Script
# Runs once after Keycloak starts to create the la-poste
# realm, client, roles, users, and protocol mappers.
# Idempotent — skips if realm already exists.
# ─────────────────────────────────────────────────────────

KCADM=/opt/keycloak/bin/kcadm.sh
REALM=la-poste
CLIENT_ID=la-poste
CLIENT_SECRET="${KEYCLOAK_CLIENT_SECRET:-change-me-in-production}"
MARKER=/opt/keycloak/data/.provisioned

# Skip if already provisioned (persistent volume marker)
if [ -f "$MARKER" ]; then
    echo "✅ Keycloak already provisioned (marker found). Skipping."
    exit 0
fi

echo "⏳ Waiting for Keycloak to be ready..."
until $KCADM config credentials --server http://localhost:8080 --realm master --user "$KEYCLOAK_ADMIN" --password "$KEYCLOAK_ADMIN_PASSWORD" 2>/dev/null; do
    sleep 3
done
echo "✅ Keycloak is ready."

# ── Check if realm already exists ──
if $KCADM get realms/$REALM --fields realm 2>/dev/null | grep -q "$REALM"; then
    echo "ℹ️  Realm '$REALM' already exists. Ensuring roles & users are correct..."
else
    echo "📦 Creating realm '$REALM'..."
    $KCADM create realms -s realm=$REALM -s enabled=true -s displayName="La Poste Suisse"
fi

# ── Realm Roles ──
echo "🏷️  Ensuring realm roles..."
for ROLE in ADMIN BRANCH_MANAGER EMPLOYEE; do
    if ! $KCADM get roles -r $REALM --fields name 2>/dev/null | grep -q "\"$ROLE\""; then
        $KCADM create roles -r $REALM -s name="$ROLE"
        echo "   Created role: $ROLE"
    fi
done

# ── Client ──
echo "🔑 Ensuring client '$CLIENT_ID'..."
CLIENT_UUID=$($KCADM get clients -r $REALM -q clientId=$CLIENT_ID --fields id 2>/dev/null | grep '"id"' | head -1 | sed 's/.*: "\(.*\)".*/\1/')
if [ -z "$CLIENT_UUID" ]; then
    $KCADM create clients -r $REALM \
        -s clientId=$CLIENT_ID \
        -s enabled=true \
        -s publicClient=false \
        -s "secret=$CLIENT_SECRET" \
        -s 'redirectUris=["http://localhost:8080/*"]' \
        -s 'webOrigins=["http://localhost:8080"]' \
        -s directAccessGrantsEnabled=true \
        -s standardFlowEnabled=true
    CLIENT_UUID=$($KCADM get clients -r $REALM -q clientId=$CLIENT_ID --fields id 2>/dev/null | grep '"id"' | head -1 | sed 's/.*: "\(.*\)".*/\1/')
    echo "   Created client: $CLIENT_ID (UUID: $CLIENT_UUID)"
else
    echo "   Client exists: $CLIENT_ID (UUID: $CLIENT_UUID)"
fi

# ── Protocol Mapper: realm roles → ID token ──
# The default "roles" client scope has a realm roles mapper but it doesn't
# include roles in the ID token. We need to update it.
echo "🗺️  Ensuring realm roles are mapped to ID token..."
ROLES_SCOPE_ID=$($KCADM get client-scopes -r $REALM --fields id,name 2>/dev/null | python3 -c "
import sys,json
for s in json.load(sys.stdin):
    if s['name']=='roles': print(s['id']); break
" 2>/dev/null)

if [ -n "$ROLES_SCOPE_ID" ]; then
    REALM_ROLES_MAPPER_ID=$($KCADM get client-scopes/$ROLES_SCOPE_ID/protocol-mappers/models -r $REALM --fields id,name 2>/dev/null | python3 -c "
import sys,json
for m in json.load(sys.stdin):
    if m['name']=='realm roles': print(m['id']); break
" 2>/dev/null)

    if [ -n "$REALM_ROLES_MAPPER_ID" ]; then
        $KCADM update client-scopes/$ROLES_SCOPE_ID/protocol-mappers/models/$REALM_ROLES_MAPPER_ID \
            -r $REALM \
            -b '{
                "name": "realm roles",
                "protocol": "openid-connect",
                "protocolMapper": "oidc-usermodel-realm-role-mapper",
                "consentRequired": false,
                "config": {
                    "multivalued": "true",
                    "id.token.claim": "true",
                    "access.token.claim": "true",
                    "userinfo.token.claim": "true",
                    "claim.name": "realm_access.roles",
                    "jsonType.label": "String"
                }
            }' 2>/dev/null
        echo "   Updated 'realm roles' mapper to include roles in ID token"
    fi
fi

# ── Users ──
echo "👤 Ensuring users..."
create_user() {
    local USERNAME=$1
    local PASSWORD=$2
    local ROLE=$3
    local FIRST=$4
    local LAST=$5
    local EMAIL=$6

    if ! $KCADM get users -r $REALM -q username=$USERNAME --fields username 2>/dev/null | grep -q "$USERNAME"; then
        $KCADM create users -r $REALM \
            -s username="$USERNAME" \
            -s enabled=true \
            -s emailVerified=true \
            -s "firstName=$FIRST" \
            -s "lastName=$LAST" \
            -s "email=$EMAIL"
        $KCADM set-password -r $REALM --username "$USERNAME" --new-password "$PASSWORD"
        echo "   Created user: $USERNAME"
    fi
    # Always ensure role is assigned
    $KCADM add-roles -r $REALM --uusername "$USERNAME" --rolename "$ROLE" 2>/dev/null || true
    echo "   $USERNAME → $ROLE"
}

create_user "admin-laposte"    "pass123" "ADMIN"          "Hans"    "Müller"    "admin@laposte.ch"
create_user "manager1-laposte" "pass123" "BRANCH_MANAGER" "Marie"   "Dupont"    "marie.dupont@laposte.ch"
create_user "manager2-laposte" "pass123" "BRANCH_MANAGER" "Luca"    "Rossi"     "luca.rossi@laposte.ch"
create_user "manager3-laposte" "pass123" "BRANCH_MANAGER" "Sophie"  "Meier"     "sophie.meier@laposte.ch"
create_user "emp1-laposte"     "pass123" "EMPLOYEE"       "Thomas"  "Keller"    "thomas.keller@laposte.ch"
create_user "emp2-laposte"     "pass123" "EMPLOYEE"       "Anna"    "Fischer"   "anna.fischer@laposte.ch"
create_user "emp3-laposte"     "pass123" "EMPLOYEE"       "Marco"   "Bianchi"   "marco.bianchi@laposte.ch"
create_user "emp4-laposte"     "pass123" "EMPLOYEE"       "Elena"   "Weber"     "elena.weber@laposte.ch"
create_user "emp5-laposte"     "pass123" "EMPLOYEE"       "David"   "Brunner"   "david.brunner@laposte.ch"
create_user "emp6-laposte"     "pass123" "EMPLOYEE"       "Laura"   "Schmid"    "laura.schmid@laposte.ch"

# ── Write marker file ──
touch "$MARKER"
echo ""
echo "════════════════════════════════════════════════════"
echo "  ✅ Keycloak provisioning complete!"
echo "  Realm:  $REALM"
echo "  Client: $CLIENT_ID"
echo "  Roles:  ADMIN, BRANCH_MANAGER, EMPLOYEE"
echo "  Users:  10 (1 admin, 3 managers, 6 employees)"
echo "════════════════════════════════════════════════════"



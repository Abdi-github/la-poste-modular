#!/bin/bash
# ─────────────────────────────────────────────────────────
# Keycloak Entrypoint:
#   1. Generate self-signed TLS certificate (if needed)
#   2. Start Keycloak in dev mode with HTTPS
#   3. Run provisioning script in the background
# ─────────────────────────────────────────────────────────
set -e

KEYSTORE=/opt/keycloak/conf/server.keystore
KEYTOOL=/usr/lib/jvm/java-21-openjdk-21.0.7.0.6-1.el9.x86_64/bin/keytool

# Generate a self-signed keystore only if it doesn't exist
if [ ! -f "$KEYSTORE" ]; then
    echo "🔐 Generating self-signed TLS keystore for localhost..."
    $KEYTOOL -genkeypair \
        -alias server \
        -keyalg RSA \
        -keysize 2048 \
        -validity 365 \
        -dname "CN=localhost" \
        -keystore "$KEYSTORE" \
        -storepass password \
        -keypass password \
        -storetype PKCS12
    echo "✅ Keystore generated at $KEYSTORE"
fi

# Run realm provisioning in the background (waits for KC to be ready)
if [ -f /opt/keycloak/scripts/provision.sh ]; then
    echo "🚀 Starting background provisioning..."
    bash /opt/keycloak/scripts/provision.sh &
fi

# Start Keycloak in dev mode with HTTPS via keystore
exec /opt/keycloak/bin/kc.sh start-dev \
    --https-key-store-file="$KEYSTORE" \
    --https-key-store-password=password



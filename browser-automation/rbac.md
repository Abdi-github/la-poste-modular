# RBAC (Role-Based Access Control) Testing

## Test Users

| Username          | Password | Keycloak Role   | App Role        |
|-------------------|----------|-----------------|-----------------|
| admin-laposte     | pass123  | admin           | ADMIN           |
| manager1-laposte  | pass123  | branch_manager  | BRANCH_MANAGER  |
| emp1-laposte      | pass123  | employee        | EMPLOYEE        |

## Access Matrix

### Page Routes

| Route               | ADMIN  | BRANCH_MANAGER | EMPLOYEE |
|---------------------|--------|----------------|----------|
| `/dashboard`        | ✅ 200  | ✅ 200          | ✅ 200    |
| `/parcels`          | ✅ 200  | ✅ 200          | ❌ 403    |
| `/parcels/new`      | ✅ 200  | ✅ 200          | ❌ 403    |
| `/parcels/{id}`     | ✅ 200  | ✅ 200          | ❌ 403    |
| `/branches`         | ✅ 200  | ❌ 403          | ❌ 403    |
| `/branches/new`     | ✅ 200  | ❌ 403          | ❌ 403    |
| `/employees`        | ✅ 200  | ✅ 200          | ❌ 403    |
| `/employees/new`    | ✅ 200  | ✅ 200          | ❌ 403    |
| `/customers`        | ✅ 200  | ✅ 200          | ❌ 403    |
| `/customers/new`    | ✅ 200  | ✅ 200          | ❌ 403    |
| `/deliveries`       | ✅ 200  | ✅ 200          | ✅ 200    |
| `/deliveries/new`   | ✅ 200  | ✅ 200          | ✅ 200    |
| `/pickups`          | ✅ 200  | ✅ 200          | — (not tested) |
| `/tracking`         | ✅ 200  | ✅ 200          | ✅ 200    |
| `/addresses`        | ✅ 200  | ❌ 403          | ❌ 403    |
| `/notifications`    | ✅ 200  | ✅ 200          | ✅ 200    |
| `/admin/audit-log`  | ✅ 200  | ❌ 403          | ❌ 403    |
| `/admin/reports`    | ✅ 200  | ❌ 403          | ❌ 403    |
| `/admin/health`     | ✅ 200  | ❌ 403          | ❌ 403    |

### Sidebar Visibility

Each role sees only the menu items they have access to:

**ADMIN** (all items): Dashboard, Pakete, Filialen, Mitarbeitende, Kunden, Zustellung, Abholaufträge, Sendungsverfolgung, Adressen, Benachrichtigungen, Posteingang, Nachricht verfassen, Administration (Prüfprotokoll, Berichte, Systemstatus)

**BRANCH_MANAGER** (9 items): Dashboard, Pakete, Mitarbeitende, Kunden, Zustellung, Abholaufträge, Sendungsverfolgung, Posteingang, Nachricht verfassen

**EMPLOYEE** (4 items): Dashboard, Zustellung, Sendungsverfolgung, Posteingang

### 403 Error Page
The 403 "Zugriff verweigert" page shows:
- Lock icon
- "Zugriff verweigert" heading
- "Sie haben keine Berechtigung, auf diese Seite zuzugreifen." message
- "Dashboard" button to return home
- "Zurück" (back) button

## Authentication Flow

1. User clicks "Sign in with Keycloak" on `/login`
2. Redirected to Keycloak OIDC auth endpoint
3. User enters credentials on Keycloak login form
4. Upon success, redirected back to app with auth code
5. App exchanges code for tokens, creates session
6. Logout: POST form to `/logout` → clears app session → redirects to Keycloak logout

## Screenshots
- `screenshots/24-branch-manager-dashboard.png` — BRANCH_MANAGER sidebar
- `screenshots/25-branch-manager-403-branches.png` — 403 error page
- `screenshots/26-employee-dashboard.png` — EMPLOYEE sidebar

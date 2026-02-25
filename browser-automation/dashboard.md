# Dashboard Testing

## Route: `/dashboard`

**Access**: All roles (ADMIN, BRANCH_MANAGER, EMPLOYEE)

## Features Verified

### Stat Cards (7 total)
- Pakete Total: 16
- Aktive Zustellungen: 2
- Filialen: 8
- Mitarbeitende: 10
- Kunden: 5
- Geplante Zustellungen: 1
- Benachrichtigungen: 12

### Charts
- "Pakete nach Status" — Chart.js bar/doughnut chart
- "Zustellungen nach Status" — Chart.js bar/doughnut chart

### Tables
- "Letzte Pakete" — Recent parcels with tracking number, type, status, sender
- "Letzte Benachrichtigungen" — Recent notifications with message, priority, date

### Sidebar Differences by Role
| Menu Item             | ADMIN | BRANCH_MANAGER | EMPLOYEE |
|-----------------------|-------|----------------|----------|
| Dashboard             | ✅    | ✅              | ✅        |
| Pakete                | ✅    | ✅              | ❌        |
| Filialen              | ✅    | ❌              | ❌        |
| Mitarbeitende         | ✅    | ✅              | ❌        |
| Kunden                | ✅    | ✅              | ❌        |
| Zustellung            | ✅    | ✅              | ✅        |
| Abholaufträge         | ✅    | ✅              | ❌        |
| Sendungsverfolgung    | ✅    | ✅              | ✅        |
| Adressen              | ✅    | ❌              | ❌        |
| Benachrichtigungen    | ✅    | ❌              | ❌        |
| Posteingang           | ✅    | ✅              | ✅        |
| Nachricht verfassen   | ✅    | ✅              | ❌        |
| Administration section| ✅    | ❌              | ❌        |

## Screenshots
- `screenshots/01-dashboard-admin.png`
- `screenshots/24-branch-manager-dashboard.png`
- `screenshots/26-employee-dashboard.png`

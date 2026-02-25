# Browser Automation Test Results — La Poste

Automated browser testing performed via Playwright MCP against the running Docker environment.

## Environment

| Component     | Version / URL                          |
|---------------|----------------------------------------|
| App           | Spring Boot 3.5.3, `http://localhost:8080` |
| Keycloak      | 26.2.4, `https://localhost:8071`       |
| PostgreSQL    | 17, port 5432                          |
| Redis         | port 6379                              |
| Mailpit       | `http://localhost:8028`                |
| pgAdmin       | `http://localhost:5050`                |

## Test Users

| Username          | Password | Role            |
|-------------------|----------|-----------------|
| admin-laposte     | pass123  | ADMIN           |
| manager1-laposte  | pass123  | BRANCH_MANAGER  |
| emp1-laposte      | pass123  | EMPLOYEE        |

## Summary of Bugs Fixed

### 1. `lower(bytea)` PostgreSQL Error (5 repositories)

All list pages returned 500 because Hibernate mapped null `:search` as `bytea`, causing `LOWER(CONCAT('%', bytea, '%'))` to fail.

**Fix**: Added `CAST(:search AS string)` in JPQL queries in:
- `BranchRepository.java`
- `ParcelRepository.java`
- `EmployeeRepository.java`
- `CustomerRepository.java`
- `DeliveryRouteRepository.java`

### 2. Parcel Create Form — Missing Model Attributes

`/parcels/new` threw `Neither BindingResult nor plain target object for bean name 'parcel'`.

**Fix**: Added `parcel`, `parcelTypes`, `customers` attributes to `ParcelWebController.newForm()`.

### 3. SpEL Expression Errors in Thymeleaf Templates

`branches/form.html` and `customers/form.html` had invalid expressions: `${editMode ? #{...} : #{...}}` — message expressions `#{}` cannot be nested inside variable expressions `${}`.

**Fix**: Changed to `${editMode} ? #{...} : #{...}` (ternary outside `${}`).

## RBAC Verification Matrix

| Route               | ADMIN | BRANCH_MANAGER | EMPLOYEE |
|---------------------|-------|----------------|----------|
| `/dashboard`        | ✅ 200 | ✅ 200          | ✅ 200    |
| `/parcels`          | ✅ 200 | ✅ 200          | ❌ 403    |
| `/parcels/new`      | ✅ 200 | ✅ 200          | ❌ 403    |
| `/branches`         | ✅ 200 | ❌ 403          | ❌ 403    |
| `/branches/new`     | ✅ 200 | ❌ 403          | ❌ 403    |
| `/employees`        | ✅ 200 | ✅ 200          | ❌ 403    |
| `/employees/new`    | ✅ 200 | ✅ 200          | ❌ 403    |
| `/customers`        | ✅ 200 | ✅ 200          | ❌ 403    |
| `/customers/new`    | ✅ 200 | ✅ 200          | ❌ 403    |
| `/deliveries`       | ✅ 200 | ✅ 200          | ✅ 200    |
| `/deliveries/new`   | ✅ 200 | ✅ 200          | ✅ 200    |
| `/pickups`          | ✅ 200 | ✅ 200          | ❌ —      |
| `/tracking`         | ✅ 200 | ✅ 200          | ✅ 200    |
| `/addresses`        | ✅ 200 | ❌ 403          | ❌ 403    |
| `/notifications`    | ✅ 200 | ✅ 200          | ✅ 200    |
| `/admin/audit-log`  | ✅ 200 | ❌ 403          | ❌ 403    |
| `/admin/reports`    | ✅ 200 | ❌ 403          | ❌ 403    |
| `/admin/health`     | ✅ 200 | ❌ 403          | ❌ 403    |

## Screenshots

All screenshots are in `screenshots/` and numbered sequentially:

| #  | File                                  | Description                        |
|----|---------------------------------------|------------------------------------|
| 01 | `01-dashboard-admin.png`              | Admin dashboard with stats         |
| 02 | `02-parcels-admin.png`                | Parcels list page                  |
| 03 | `03-branches-admin.png`               | Branches list page                 |
| 04 | `04-employees-admin.png`              | Employees list page                |
| 05 | `05-customers-admin.png`              | Customers list page                |
| 06 | `06-deliveries-admin.png`             | Deliveries list page               |
| 07 | `07-notification-bell.png`            | Notification bell dropdown         |
| 08 | `08-parcels-sorted.png`               | Parcels sorted by status ASC       |
| 09 | `09-parcels-search.png`               | Parcels search filter (1 result)   |
| 10 | `10-parcels-create-form.png`          | Parcel create form (before fix)    |
| 11 | `11-parcels-create-form-fixed.png`    | Parcel create form (after fix)     |
| 12 | `12-parcels-form-filled.png`          | Parcel form filled with data       |
| 13 | `13-parcel-created-detail.png`        | Parcel detail after creation       |
| 14 | `14-parcel-cancelled.png`             | Parcel after cancellation          |
| 15 | `15-branch-create-form.png`           | Branch create form (before fix)    |
| 16 | `16-branch-create-form.png`           | Branch create form (after fix)     |
| 17 | `17-employee-create-form.png`         | Employee create form               |
| 18 | `18-delivery-create-form.png`         | Delivery create form               |
| 19 | `19-pickups-list.png`                 | Pickups list page                  |
| 20 | `20-tracking-page.png`                | Tracking page                      |
| 21 | `21-notifications.png`                | Notifications list                 |
| 22 | `22-addresses.png`                    | Addresses list                     |
| 23 | `23-admin-audit-log.png`              | Admin audit log                    |
| 24 | `24-branch-manager-dashboard.png`     | Branch Manager dashboard + sidebar |
| 25 | `25-branch-manager-403-branches.png`  | 403 error for Branch Manager       |
| 26 | `26-employee-dashboard.png`           | Employee dashboard + sidebar       |

## Feature Test Details

See individual feature docs:
- [Dashboard](dashboard.md)
- [Parcels](parcels.md)
- [Branches](branches.md)
- [Employees](employees.md)
- [Customers](customers.md)
- [Deliveries](deliveries.md)
- [RBAC](rbac.md)

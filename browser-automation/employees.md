# Employees Testing

## Routes

| Route                        | Method | Description           | Access                    |
|------------------------------|--------|-----------------------|---------------------------|
| `/employees`                 | GET    | List with pagination  | ADMIN, BRANCH_MANAGER     |
| `/employees/{id}`            | GET    | Employee detail       | ADMIN, BRANCH_MANAGER     |
| `/employees/new`             | GET    | Create form           | ADMIN, BRANCH_MANAGER     |
| `/employees`                 | POST   | Submit create         | ADMIN, BRANCH_MANAGER     |
| `/employees/{id}/edit`       | GET    | Edit form             | ADMIN, BRANCH_MANAGER     |
| `/employees/{id}/status`     | POST   | Change status         | ADMIN, BRANCH_MANAGER     |

## Features Verified

### List Page (`/employees`)
- ✅ Table renders with columns: Mitarbeiter-Nr., Name, E-Mail, Rolle, Filiale, Status
- ✅ Pagination working
- ✅ "Neuer Mitarbeiter" button

### Create Form (`/employees/new`)
- ✅ Form renders with fields: First Name, Last Name, Email, Phone, Role (dropdown), Branch (dropdown), Address fields

### RBAC
- ✅ ADMIN: Full access
- ✅ BRANCH_MANAGER: Full access
- ✅ EMPLOYEE: 403 Zugriff verweigert

## Bugs Found & Fixed
1. **500 on `/employees`**: `lower(bytea)` PostgreSQL error — fixed with `CAST(:search AS string)` in `EmployeeRepository.java` (4 fields: firstName, lastName, email, employeeNumber)

## Screenshots
- `screenshots/04-employees-admin.png`
- `screenshots/17-employee-create-form.png`

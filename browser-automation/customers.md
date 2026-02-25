# Customers Testing

## Routes

| Route                  | Method | Description           | Access                    |
|------------------------|--------|-----------------------|---------------------------|
| `/customers`           | GET    | List with pagination  | ADMIN, BRANCH_MANAGER     |
| `/customers/{id}`      | GET    | Customer detail       | ADMIN, BRANCH_MANAGER     |
| `/customers/new`       | GET    | Create form           | ADMIN, BRANCH_MANAGER     |
| `/customers`           | POST   | Submit create         | ADMIN, BRANCH_MANAGER     |
| `/customers/{id}/edit` | GET    | Edit form             | ADMIN, BRANCH_MANAGER     |

## Features Verified

### List Page (`/customers`)
- ✅ Table renders with columns: Kunden-Nr., Name, E-Mail, Telefon, Status
- ✅ Pagination working
- ✅ "Neuer Kunde" button

### Create Form (`/customers/new`)
- ✅ Form renders with all fields after SpEL fix

### RBAC
- ✅ ADMIN: Full access
- ✅ BRANCH_MANAGER: Full access
- ✅ EMPLOYEE: 403 Zugriff verweigert

## Bugs Found & Fixed
1. **500 on `/customers`**: `lower(bytea)` PostgreSQL error — fixed with `CAST(:search AS string)` in `CustomerRepository.java` (4 fields: firstName, lastName, email, customerNumber)
2. **SpEL error on `/customers/new`**: Same `${editMode ? #{...}}` pattern as branches — fixed on line 43

## Screenshots
- `screenshots/05-customers-admin.png`

# Branches Testing

## Routes

| Route              | Method | Description           | Access     |
|--------------------|--------|-----------------------|------------|
| `/branches`        | GET    | List with pagination  | ADMIN only |
| `/branches/{id}`   | GET    | Branch detail         | ADMIN only |
| `/branches/new`    | GET    | Create form           | ADMIN only |
| `/branches`        | POST   | Submit create         | ADMIN only |
| `/branches/{id}/edit` | GET | Edit form             | ADMIN only |

## Features Verified

### List Page (`/branches`)
- ✅ Table renders with columns: Name, Typ, Kanton, Adresse, Telefon, Status
- ✅ Pagination working
- ✅ "Neue Filiale" button links to `/branches/new`

### Create Form (`/branches/new`)
- ✅ Form renders correctly after SpEL fix
- ✅ Fields: Name (DE/FR/IT/EN), Description (DE/FR/IT/EN), Branch Type, Canton, Address fields, Contact info
- ✅ Multilingual input fields for name and description

### RBAC
- ✅ ADMIN: Full access
- ✅ BRANCH_MANAGER: 403 Zugriff verweigert
- ✅ EMPLOYEE: 403 Zugriff verweigert

## Bugs Found & Fixed
1. **500 on `/branches`**: `lower(bytea)` PostgreSQL error — fixed with `CAST(:search AS string)` in `BranchRepository.java`
2. **Blank form on `/branches/new`**: SpEL parse error `${editMode ? #{...}}` — fixed by changing to `${editMode} ? #{...} : #{...}` on lines 4 and 129

## Known Issue
- Branch edit form has locale-dependent name binding: `branch.name` resolves to the current locale but is placed in the DE field. FR/IT/EN name/description fields show no value in edit mode.

## Screenshots
- `screenshots/03-branches-admin.png`
- `screenshots/15-branch-create-form.png` (before fix)
- `screenshots/16-branch-create-form.png` (after fix)

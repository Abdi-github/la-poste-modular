# Deliveries Testing

## Routes

| Route                                | Method | Description           | Access                              |
|--------------------------------------|--------|-----------------------|-------------------------------------|
| `/deliveries`                        | GET    | List with pagination  | ADMIN, BRANCH_MANAGER, EMPLOYEE     |
| `/deliveries/{id}`                   | GET    | Delivery detail       | ADMIN, BRANCH_MANAGER, EMPLOYEE     |
| `/deliveries/new`                    | GET    | Create form           | ADMIN, BRANCH_MANAGER               |
| `/deliveries`                        | POST   | Submit create         | ADMIN, BRANCH_MANAGER               |
| `/deliveries/{id}/start`             | POST   | Start delivery        | ADMIN, BRANCH_MANAGER, EMPLOYEE     |
| `/deliveries/{id}/complete`          | POST   | Complete delivery     | ADMIN, BRANCH_MANAGER, EMPLOYEE     |
| `/deliveries/{id}/slots/{slotId}/status` | POST | Update slot status | ADMIN, BRANCH_MANAGER, EMPLOYEE     |

## Features Verified

### List Page (`/deliveries`)
- ✅ Table renders with columns: Route-Code, Filiale, Fahrer, Datum, Status, Slots
- ✅ Pagination working

### Create Form (`/deliveries/new`)
- ✅ Form renders with fields: Branch (dropdown), Driver (dropdown), Date, Slots (parcels)

### RBAC
- ✅ ADMIN: Full access
- ✅ BRANCH_MANAGER: Full access
- ✅ EMPLOYEE: List and detail access (can start/complete deliveries)

## Bugs Found & Fixed
1. **500 on `/deliveries`**: `lower(bytea)` PostgreSQL error — fixed with `CAST(:search AS string)` in `DeliveryRouteRepository.java`

## Screenshots
- `screenshots/06-deliveries-admin.png`
- `screenshots/18-delivery-create-form.png`

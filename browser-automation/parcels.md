# Parcels Testing

## Routes

| Route              | Method | Description           | Access                    |
|--------------------|--------|-----------------------|---------------------------|
| `/parcels`         | GET    | List with pagination  | ADMIN, BRANCH_MANAGER     |
| `/parcels/{id}`    | GET    | Parcel detail         | ADMIN, BRANCH_MANAGER     |
| `/parcels/new`     | GET    | Create form           | ADMIN, BRANCH_MANAGER     |
| `/parcels`         | POST   | Submit create         | ADMIN, BRANCH_MANAGER     |
| `/parcels/{id}/cancel` | POST | Cancel parcel      | ADMIN, BRANCH_MANAGER     |

## Features Verified

### List Page (`/parcels`)
- ✅ Table renders with columns: Tracking-Nr., Typ, Status, Gewicht, Absender, Empfänger, Datum
- ✅ Pagination (20 per page default)
- ✅ Sorting by column (tested Status ASC)
- ✅ Search filter (tested "LP-10000BK" → 1 result)
- ✅ "Neues Paket" button links to `/parcels/new`
- ✅ Row click links to `/parcels/{id}`

### Create Form (`/parcels/new`)
- ✅ Form fields: parcel type (dropdown), weight (kg), description, sender (dropdown), receiver (dropdown)
- ✅ Dropdowns populated with ParcelType enum values and customer list
- ✅ Form submission creates parcel with tracking number LP-MGJD1XM2Q-CH
- ✅ Redirect to parcel detail page after creation

### Detail Page (`/parcels/{id}`)
- ✅ Shows tracking number, type, status, weight, price, description
- ✅ Shows sender and receiver information
- ✅ Shows tracking history timeline
- ✅ "Stornieren" (Cancel) button available for non-cancelled parcels

### Cancel Action
- ✅ POST `/parcels/{id}/cancel` changes status to CANCELLED
- ✅ Tracking history updated with cancellation event
- ✅ Cancel button removed from detail page after cancellation

## CRUD Test Flow
1. Created parcel: Type=EXPRESS, Weight=2.5kg, Price=CHF 21.00
2. Tracking number: LP-MGJD1XM2Q-CH
3. Cancelled parcel → Status changed to CANCELLED
4. Audit log shows both CREATED and CANCELLED events

## Bugs Found & Fixed
1. **500 on `/parcels`**: `lower(bytea)` PostgreSQL error — fixed with `CAST(:search AS string)` in `ParcelRepository.java`
2. **500 on `/parcels/new`**: Missing model attributes — fixed by adding `parcel`, `parcelTypes`, `customers` to `ParcelWebController.newForm()`

## Screenshots
- `screenshots/02-parcels-admin.png`
- `screenshots/08-parcels-sorted.png`
- `screenshots/09-parcels-search.png`
- `screenshots/11-parcels-create-form-fixed.png`
- `screenshots/12-parcels-form-filled.png`
- `screenshots/13-parcel-created-detail.png`
- `screenshots/14-parcel-cancelled.png`

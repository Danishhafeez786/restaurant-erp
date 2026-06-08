# Restaurant POS API - Quick Reference Guide

## Base URL
```
http://localhost:8080/api
```

## Authentication
All endpoints require JWT token in Authorization header:
```
Authorization: Bearer {token}
```

---

## Branch Management

### List Branches by Organization
```http
GET /api/branches/org/{organizationId}
```

### Get Branch Details
```http
GET /api/branches/{branchId}
```

### Create Branch
```http
POST /api/branches
Content-Type: application/json

{
  "organizationId": "org123",
  "name": "Main Branch",
  "code": "MB001",
  "address": "123 Main St",
  "city": "Mumbai",
  "state": "Maharashtra",
  "zipCode": "400001",
  "country": "India",
  "phoneNumber": "+91-1234567890",
  "email": "branch@restaurant.com",
  "latitude": 19.0760,
  "longitude": 72.8777,
  "totalTables": 50,
  "numberOfFloors": 2
}
```

### Update Branch
```http
PUT /api/branches/{branchId}
Content-Type: application/json
{...branch updates...}
```

### Delete Branch
```http
DELETE /api/branches/{branchId}
```

---

## Menu Management

### Get Categories
```http
GET /api/menu/categories?branchId={branchId}
```

### Create Category
```http
POST /api/menu/categories
{
  "name": "Appetizers",
  "code": "APP",
  "displayOrder": 1
}
```

### Get Menu Items
```http
GET /api/menu/items?branchId={branchId}
```

### Create Menu Item
```http
POST /api/menu/items
{
  "categoryId": "cat123",
  "name": "Butter Chicken",
  "price": 250,
  "costPrice": 100,
  "preparationTime": 15,
  "isVegetarian": false,
  "isSpicy": true
}
```

### Update Menu Item
```http
PUT /api/menu/items/{itemId}
{...item updates...}
```

### Delete Menu Item
```http
DELETE /api/menu/items/{itemId}
```

---

## Order Management

### Create Order
```http
POST /api/orders
{
  "branchId": "branch123",
  "tableId": "table123",
  "customerId": "cust123",
  "items": [
    {
      "menuItemId": "item123",
      "quantity": 2,
      "modifierIds": ["mod123"],
      "specialInstructions": "No onions"
    }
  ],
  "deliveryType": "DINE_IN",
  "waiterName": "John"
}
```

### Get Order by ID
```http
GET /api/orders/{orderId}
```

### List Orders
```http
GET /api/orders?branchId={branchId}
```

### Update Order Status
```http
PUT /api/orders/{orderId}/status
{
  "status": "CONFIRMED"
}
```

### Apply Discount
```http
POST /api/orders/{orderId}/apply-discount
{
  "discountAmount": 50
}
```

### Cancel Order
```http
DELETE /api/orders/{orderId}
```

---

## Kitchen Operations

### Get Kitchen Orders
```http
GET /api/kitchen/orders?branchId={branchId}
```

### Update Kitchen Order Status
```http
PUT /api/kitchen/orders/{orderId}/status
{
  "status": "IN_PROGRESS"
}
```

### Assign Order to Chef
```http
POST /api/kitchen/orders/{orderId}/assign
{
  "chefId": "chef123",
  "chefName": "Chef John"
}
```

### Get Kitchen Stations
```http
GET /api/kitchen/stations?branchId={branchId}
```

### Update Station Status
```http
PUT /api/kitchen/stations/{stationId}/status
{
  "status": "ACTIVE"
}
```

---

## Table Management

### Get Table by ID
```http
GET /api/tables/{tableId}
```

### Get Tables by Floor
```http
GET /api/tables/floor/{floorId}?branchId={branchId}
```

### Get Available Tables
```http
GET /api/tables/available?branchId={branchId}
```

### Update Table Status
```http
PUT /api/tables/{tableId}/status
{
  "status": "OCCUPIED"
}
```

### Get Floors
```http
GET /api/tables/floors?branchId={branchId}
```

---

## Inventory Management

### Get Inventory
```http
GET /api/inventory?branchId={branchId}
```

### Get Inventory Item
```http
GET /api/inventory/{inventoryId}
```

### Adjust Stock
```http
POST /api/inventory/adjust-stock
{
  "inventoryId": "inv123",
  "quantity": 50,
  "movementType": "IN",
  "notes": "Purchase from supplier",
  "movedBy": "manager123"
}
```

### Get Stock Movements
```http
GET /api/inventory/movements?branchId={branchId}
```

### Get Low Stock Alerts
```http
GET /api/inventory/alerts?branchId={branchId}
```

---

## Payment Processing

### Process Payment
```http
POST /api/payments/process
{
  "orderId": "order123",
  "amount": 500,
  "amountReceived": 500,
  "paymentMethod": "CASH",
  "paymentGateway": "STRIPE",
  "customerName": "John Doe",
  "customerPhone": "+91-1234567890"
}
```

### Get Payments
```http
GET /api/payments?branchId={branchId}
```

### Get Payment by ID
```http
GET /api/payments/{paymentId}
```

### Process Refund
```http
POST /api/payments/{paymentId}/refund
```

---

## Attendance Management

### Check In
```http
POST /api/attendance/check-in
{
  "employeeId": "emp123",
  "employeeName": "John Doe",
  "branchId": "branch123",
  "checkInMethod": "BIOMETRIC"
}
```

### Check Out
```http
POST /api/attendance/check-out
{
  "attendanceId": "att123"
}
```

### Get Attendance Records
```http
GET /api/attendance?branchId={branchId}&startDate={date}&endDate={date}
```

### Get Attendance Report
```http
GET /api/attendance/report?branchId={branchId}&startDate={date}&endDate={date}
```

---

## Employee Management

### List Employees
```http
GET /api/employees?branchId={branchId}
```

### Get Employee by ID
```http
GET /api/employees/{employeeId}
```

### Create Employee
```http
POST /api/employees
{
  "name": "John Doe",
  "email": "john@restaurant.com",
  "phone": "+91-1234567890",
  "role": "STAFF_MANAGER",
  "branchId": "branch123",
  "department": "Kitchen",
  "joinDate": "2024-01-01",
  "salary": 25000
}
```

### Update Employee
```http
PUT /api/employees/{employeeId}
{...employee updates...}
```

### Delete Employee
```http
DELETE /api/employees/{employeeId}
```

---

## Customer Management

### List Customers
```http
GET /api/customers?branchId={branchId}
```

### Get Customer by ID
```http
GET /api/customers/{customerId}
```

### Create Customer
```http
POST /api/customers
{
  "name": "Jane Doe",
  "email": "jane@email.com",
  "phone": "+91-9876543210",
  "address": "456 Oak Ave",
  "city": "Mumbai",
  "branchId": "branch123"
}
```

### Update Customer
```http
PUT /api/customers/{customerId}
{...customer updates...}
```

---

## Payroll Management

### Get Salaries
```http
GET /api/payroll/salaries?branchId={branchId}
```

### Calculate Payroll
```http
POST /api/payroll/calculate
{
  "branchId": "branch123",
  "startDate": "2024-01-01",
  "endDate": "2024-01-31"
}
```

### Get Salary Slip
```http
GET /api/payroll/slips/{salarySlipId}
```

### Get Payroll Report
```http
GET /api/payroll/report?branchId={branchId}&month=1&year=2024
```

---

## Loyalty Program

### Get Loyalty Account
```http
GET /api/loyalty/accounts/{customerId}
```

### Get Loyalty Transactions
```http
GET /api/loyalty/transactions?customerId={customerId}
```

### Redeem Points
```http
POST /api/loyalty/redeem
{
  "customerId": "cust123",
  "points": 100,
  "orderId": "order123"
}
```

### Add Points
```http
POST /api/loyalty/add-points
{
  "customerId": "cust123",
  "points": 50,
  "reason": "Purchase reward"
}
```

---

## Reports & Analytics

### Get Sales Report
```http
GET /api/reports/sales?branchId={branchId}&startDate={date}&endDate={date}
```

### Get Inventory Report
```http
GET /api/reports/inventory?branchId={branchId}
```

### Get Staff Report
```http
GET /api/reports/staff?branchId={branchId}&startDate={date}&endDate={date}
```

### Get Customer Report
```http
GET /api/reports/customers?branchId={branchId}&startDate={date}&endDate={date}
```

### Get Item Popularity
```http
GET /api/reports/item-popularity?branchId={branchId}&startDate={date}&endDate={date}
```

### Get Daily Revenue
```http
GET /api/reports/daily-revenue?branchId={branchId}&startDate={date}&endDate={date}
```

### Export Report
```http
GET /api/reports/export?reportType={type}&branchId={branchId}&format=pdf
```

---

## WebSocket Endpoints

### Kitchen Display System
```
ws://localhost:8080/ws/kitchen?branchId={branchId}&userId={userId}

Message Types:
- ORDER_ASSIGNED: New order for kitchen
- ORDER_STATUS_UPDATE: Order status changed
- ORDER_URGENT: Order marked as urgent
```

### Delivery Tracking
```
ws://localhost:8080/ws/delivery?branchId={branchId}&userId={userId}

Message Types:
- LOCATION_UPDATE: Delivery GPS update
- DELIVERY_STATUS_UPDATE: Delivery status change
- ROUTE_UPDATED: Optimized route
```

---

## Response Format

### Success Response
```json
{
  "success": true,
  "data": {...},
  "message": "Operation completed successfully",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

### Error Response
```json
{
  "success": false,
  "error": {
    "code": "ERROR_CODE",
    "message": "Error description",
    "details": {...}
  },
  "timestamp": "2024-01-15T10:30:00Z"
}
```

### Pagination Response
```json
{
  "success": true,
  "data": [...],
  "pagination": {
    "page": 1,
    "pageSize": 20,
    "totalRecords": 150,
    "totalPages": 8
  }
}
```

---

## Common Error Codes

- `400` - Bad Request (Validation error)
- `401` - Unauthorized (Invalid token)
- `403` - Forbidden (Insufficient permissions)
- `404` - Not Found (Resource doesn't exist)
- `500` - Internal Server Error
- `503` - Service Unavailable

---

## Status Enums

### Order Status
- `PENDING`, `CONFIRMED`, `IN_PREPARATION`, `READY`, `COMPLETED`, `CANCELLED`

### Payment Status
- `PENDING`, `COMPLETED`, `FAILED`, `REFUNDED`, `CANCELLED`

### Kitchen Status
- `PENDING`, `IN_PROGRESS`, `READY`, `CANCELLED`

### Table Status
- `AVAILABLE`, `OCCUPIED`, `RESERVED`, `MAINTENANCE`

### Inventory Status
- `IN_STOCK`, `LOW_STOCK`, `OUT_OF_STOCK`

### Delivery Status
- `ASSIGNED`, `PICKED_UP`, `IN_TRANSIT`, `DELIVERED`, `CANCELLED`

---

## Rate Limiting

API calls are limited to:
- 1000 requests per hour per user
- 100 requests per minute per endpoint

---

Last Updated: 2024
Generated for Restaurant POS System v1.0

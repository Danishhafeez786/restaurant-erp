# Restaurant POS System - Complete Architecture Documentation

## Table of Contents
1. [High-Level Architecture](#high-level-architecture)
2. [Backend Package Structure](#backend-package-structure)
3. [Frontend Component Structure](#frontend-component-structure)
4. [API Endpoints](#api-endpoints)
5. [Data Models](#data-models)
6. [Authentication & Authorization](#authentication--authorization)
7. [WebSocket Communications](#websocket-communications)
8. [Database Schema](#database-schema)

---

## High-Level Architecture

### Technology Stack
- **Backend**: Spring Boot 4.0.6, Java 25, MongoDB
- **Frontend**: React 18+, Vite, Tailwind CSS
- **Real-time Communication**: WebSocket (Kitchen Operations)
- **Authentication**: JWT-based Security
- **API Documentation**: Swagger/OpenAPI

### System Components Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                        RESTAURANT POS SYSTEM                     │
└─────────────────────────────────────────────────────────────────┘
         │                    │                      │
    ┌────▼────┐          ┌───▼───┐           ┌─────▼─────┐
    │ Frontend │          │Backend│           │  Database │
    │ (React)  │          │(Spring)           │(MongoDB)  │
    └──────────┘          └───────┘           └───────────┘
```

---

## Backend Package Structure

### Root Package: `com.devmasters.restaurant_erp`

```
com.devmasters.restaurant_erp/
│
├── config/                          # Configuration Classes
│   ├── SecurityConfig               # JWT Security Configuration
│   ├── MongoConfig                  # MongoDB Configuration
│   ├── SwaggerConfig                # Swagger/OpenAPI Configuration
│   ├── WebSocketConfig              # WebSocket Configuration
│   ├── AuditConfig                  # Audit Logging Configuration
│   └── MailConfig                   # Email Configuration
│
├── common/                          # Shared/Common Components
│   ├── entity/
│   │   ├── BaseEntity               # Base entity with audit fields
│   │   ├── AuditableEntity          # Entity with timestamp tracking
│   │   └── SoftDeleteEntity         # Soft delete support
│   │
│   ├── enums/
│   │   ├── UserStatus               # ACTIVE, INACTIVE, SUSPENDED
│   │   ├── UserRole                 # ADMIN, MANAGER, CHEF, WAITER, etc.
│   │   ├── OrderStatus              # PENDING, PREPARING, READY, SERVED, CANCELLED
│   │   ├── PaymentStatus            # PENDING, COMPLETED, FAILED, REFUNDED
│   │   ├── KitchenStatus            # IDLE, BUSY, URGENT
│   │   ├── TableStatus              # AVAILABLE, OCCUPIED, RESERVED, CLEANING
│   │   ├── InventoryStatus          # IN_STOCK, LOW_STOCK, OUT_OF_STOCK
│   │   └── DeliveryStatus           # PENDING, ASSIGNED, PICKED_UP, DELIVERED, FAILED
│   │
│   ├── dto/
│   │   ├── ApiResponse              # Generic API Response wrapper
│   │   ├── PaginationRequest        # Pagination parameters
│   │   └── PaginationResponse       # Paginated response wrapper
│   │
│   ├── exception/
│   │   ├── ApplicationException     # Base exception
│   │   ├── ResourceNotFoundException
│   │   ├── ValidationException
│   │   ├── UnauthorizedException
│   │   └── BusinessLogicException
│   │
│   ├── response/
│   │   ├── ErrorResponse            # Standardized error response
│   │   └── SuccessResponse          # Standardized success response
│   │
│   └── util/
│       ├── DateTimeUtil             # Date/Time utilities
│       ├── CurrencyUtil             # Currency formatting
│       ├── ValidationUtil           # Common validations
│       └── DateValidator            # Date validation rules
│
├── auth/                            # Authentication Module
│   ├── controller/
│   │   └── AuthController           # Auth endpoints
│   │
│   ├── service/
│   │   ├── AuthService              # Authentication logic
│   │   └── JwtService               # JWT token management
│   │
│   ├── repository/
│   │   └── UserRepository           # User data access
│   │
│   ├── dto/
│   │   ├── LoginRequest
│   │   ├── SignupRequest
│   │   ├── AuthResponse
│   │   └── RefreshTokenRequest
│   │
│   ├── domain/
│   │   └── User                     # User entity
│   │
│   └── security/
│       ├── JwtAuthenticationFilter
│       ├── JwtTokenProvider
│       └── SecurityContextUtil
│
├── organization/                    # Organization Management
│   ├── entity/
│   │   └── Organization
│   │
│   ├── repository/
│   │   └── OrganizationRepository
│   │
│   ├── service/
│   │   └── OrganizationService
│   │
│   ├── controller/
│   │   └── OrganizationController
│   │
│   ├── dto/
│   │   ├── OrganizationDTO
│   │   ├── CreateOrganizationRequest
│   │   └── UpdateOrganizationRequest
│   │
│   └── model/
│       ├── ContactInfo
│       └── SubscriptionInfo
│
├── branch/                          # Branch Management
│   ├── entity/
│   │   └── Branch
│   │
│   ├── repository/
│   │   └── BranchRepository
│   │
│   ├── service/
│   │   └── BranchService
│   │
│   ├── controller/
│   │   └── BranchController
│   │
│   ├── dto/
│   │   ├── BranchDTO
│   │   ├── CreateBranchRequest
│   │   └── UpdateBranchRequest
│   │
│   └── model/
│       ├── Location
│       └── BranchSettings
│
├── user/                            # User Management (Employees)
│   ├── entity/
│   │   └── User
│   │
│   ├── repository/
│   │   └── UserRepository
│   │
│   ├── service/
│   │   ├── UserService
│   │   └── UserValidationService
│   │
│   ├── controller/
│   │   └── UserController
│   │
│   ├── dto/
│   │   ├── UserDTO
│   │   ├── CreateUserRequest
│   │   ├── UpdateUserRequest
│   │   └── ChangePasswordRequest
│   │
│   └── model/
│       └── UserProfile
│
├── role/                            # Role & Permission Management
│   ├── entity/
│   │   ├── Role
│   │   └── Permission
│   │
│   ├── repository/
│   │   ├── RoleRepository
│   │   └── PermissionRepository
│   │
│   ├── service/
│   │   ├── RoleService
│   │   └── PermissionService
│   │
│   ├── controller/
│   │   └── RolePermissionController
│   │
│   └── dto/
│       ├── RoleDTO
│       ├── PermissionDTO
│       └── AssignRoleRequest
│
├── employee/                        # Employee Management
│   ├── entity/
│   │   └── Employee
│   │
│   ├── repository/
│   │   └── EmployeeRepository
│   │
│   ├── service/
│   │   └── EmployeeService
│   │
│   ├── controller/
│   │   └── EmployeeController
│   │
│   ├── dto/
│   │   ├── EmployeeDTO
│   │   ├── CreateEmployeeRequest
│   │   └── UpdateEmployeeRequest
│   │
│   └── model/
│       ├── EmployeeDetails
│       └── EmployeeStats
│
├── customer/                        # Customer Management
│   ├── entity/
│   │   └── Customer
│   │
│   ├── repository/
│   │   └── CustomerRepository
│   │
│   ├── service/
│   │   └── CustomerService
│   │
│   ├── controller/
│   │   └── CustomerController
│   │
│   ├── dto/
│   │   ├── CustomerDTO
│   │   ├── CreateCustomerRequest
│   │   └── UpdateCustomerRequest
│   │
│   └── model/
│       ├── CustomerProfile
│       └── CustomerStats
│
├── menu/                            # Menu Management
│   ├── entity/
│   │   ├── Category
│   │   ├── SubCategory
│   │   ├── MenuItem
│   │   ├── Modifier                 # Item add-ons (extra cheese, etc.)
│   │   └── ComboDeal
│   │
│   ├── repository/
│   │   ├── CategoryRepository
│   │   ├── SubCategoryRepository
│   │   ├── MenuItemRepository
│   │   ├── ModifierRepository
│   │   └── ComboDealRepository
│   │
│   ├── service/
│   │   ├── MenuService
│   │   ├── MenuItemService
│   │   ├── ModifierService
│   │   └── ComboDealService
│   │
│   ├── controller/
│   │   └── MenuController
│   │
│   └── dto/
│       ├── CategoryDTO
│       ├── MenuItemDTO
│       ├── ModifierDTO
│       └── ComboDealDTO
│
├── inventory/                       # Inventory Management
│   ├── entity/
│   │   ├── Inventory
│   │   ├── StockMovement           # Tracks all stock changes
│   │   ├── StockAdjustment         # Manual adjustments
│   │   └── InventoryAlert          # Low stock alerts
│   │
│   ├── repository/
│   │   ├── InventoryRepository
│   │   ├── StockMovementRepository
│   │   ├── StockAdjustmentRepository
│   │   └── InventoryAlertRepository
│   │
│   ├── service/
│   │   ├── InventoryService
│   │   ├── StockMovementService
│   │   └── LowStockAlertService
│   │
│   ├── controller/
│   │   └── InventoryController
│   │
│   └── dto/
│       ├── InventoryDTO
│       ├── StockMovementDTO
│       └── AdjustStockRequest
│
├── supplier/                        # Supplier Management
│   ├── entity/
│   │   └── Supplier
│   │
│   ├── repository/
│   │   └── SupplierRepository
│   │
│   ├── service/
│   │   └── SupplierService
│   │
│   ├── controller/
│   │   └── SupplierController
│   │
│   ├── dto/
│   │   ├── SupplierDTO
│   │   ├── CreateSupplierRequest
│   │   └── UpdateSupplierRequest
│   │
│   └── model/
│       └── SupplierPaymentTerms
│
├── purchase/                        # Purchase Management
│   ├── entity/
│   │   ├── Purchase
│   │   └── PurchaseItem
│   │
│   ├── repository/
│   │   ├── PurchaseRepository
│   │   └── PurchaseItemRepository
│   │
│   ├── service/
│   │   ├── PurchaseService
│   │   └── PurchaseValidationService
│   │
│   ├── controller/
│   │   └── PurchaseController
│   │
│   └── dto/
│       ├── PurchaseDTO
│       ├── CreatePurchaseRequest
│       ├── UpdatePurchaseRequest
│       └── PurchaseItemDTO
│
├── tablemanagement/                 # Table Management
│   ├── entity/
│   │   ├── Floor
│   │   ├── RestaurantTable         # Individual tables
│   │   └── Reservation             # Table reservations
│   │
│   ├── repository/
│   │   ├── FloorRepository
│   │   ├── RestaurantTableRepository
│   │   └── ReservationRepository
│   │
│   ├── service/
│   │   ├── TableService
│   │   ├── ReservationService
│   │   └── TableAvailabilityService
│   │
│   ├── controller/
│   │   └── TableManagementController
│   │
│   ├── dto/
│   │   ├── FloorDTO
│   │   ├── TableDTO
│   │   ├── ReservationDTO
│   │   └── UpdateTableStatusRequest
│   │
│   └── model/
│       └── TableLayout
│
├── order/                           # Order Management
│   ├── entity/
│   │   ├── Order
│   │   ├── OrderItem              # Items in the order
│   │   ├── OrderDiscount          # Discounts applied
│   │   ├── OrderTax               # Tax calculations
│   │   └── OrderHistory           # Order state changes
│   │
│   ├── repository/
│   │   ├── OrderRepository
│   │   ├── OrderItemRepository
│   │   ├── OrderDiscountRepository
│   │   └── OrderTaxRepository
│   │
│   ├── service/
│   │   ├── OrderService           # Main order operations
│   │   ├── BillingService         # Billing calculations
│   │   ├── DiscountService        # Discount calculations
│   │   ├── OrderValidationService
│   │   └── OrderNotificationService
│   │
│   ├── controller/
│   │   ├── OrderController
│   │   ├── BillingController
│   │   └── DiscountController
│   │
│   ├── dto/
│   │   ├── OrderDTO
│   │   ├── CreateOrderRequest
│   │   ├── OrderItemDTO
│   │   ├── BillingDTO
│   │   └── ApplyDiscountRequest
│   │
│   └── model/
│       ├── OrderSummary
│       ├── BillingDetails
│       └── DiscountDetails
│
├── kitchen/                         # Kitchen Operations (Real-time)
│   ├── entity/
│   │   ├── KitchenOrder           # Orders in kitchen
│   │   ├── KitchenStation         # Kitchen workstations
│   │   └── ChefPerformance        # Chef metrics
│   │
│   ├── repository/
│   │   ├── KitchenOrderRepository
│   │   ├── KitchenStationRepository
│   │   └── ChefPerformanceRepository
│   │
│   ├── service/
│   │   ├── KitchenService         # Order preparation management
│   │   ├── KitchenAssignmentService
│   │   ├── KitchenMetricsService
│   │   └── KitchenNotificationService
│   │
│   ├── websocket/
│   │   ├── KitchenSocketPublisher # Broadcasts to kitchen clients
│   │   ├── KitchenSocketListener  # Handles kitchen updates
│   │   ├── KitchenSocketController# WebSocket endpoints
│   │   └── KitchenMessageHandler
│   │
│   ├── controller/
│   │   └── KitchenController
│   │
│   └── dto/
│       ├── KitchenOrderDTO
│       ├── KitchenStationDTO
│       ├── UpdateKitchenStatusRequest
│       └── ChefPerformanceDTO
│
├── delivery/                        # Delivery Management
│   ├── entity/
│   │   ├── Delivery
│   │   ├── DeliveryAssignment
│   │   └── DeliveryTracking       # Real-time location tracking
│   │
│   ├── repository/
│   │   ├── DeliveryRepository
│   │   ├── DeliveryAssignmentRepository
│   │   └── DeliveryTrackingRepository
│   │
│   ├── service/
│   │   ├── DeliveryService
│   │   ├── DeliveryAssignmentService
│   │   └── DeliveryTrackingService
│   │
│   ├── controller/
│   │   └── DeliveryController
│   │
│   └── dto/
│       ├── DeliveryDTO
│       ├── AssignDeliveryRequest
│       ├── UpdateDeliveryStatusRequest
│       └── DeliveryTrackingDTO
│
├── attendance/                      # Employee Attendance
│   ├── entity/
│   │   └── Attendance
│   │
│   ├── repository/
│   │   └── AttendanceRepository
│   │
│   ├── service/
│   │   ├── AttendanceService
│   │   └── BiometricService        # Fingerprint/Face recognition
│   │
│   ├── controller/
│   │   └── AttendanceController
│   │
│   └── dto/
│       ├── AttendanceDTO
│       ├── CheckInRequest
│       └── AttendanceReportDTO
│
├── payroll/                         # Payroll Management
│   ├── entity/
│   │   ├── Salary
│   │   ├── SalarySlip
│   │   └── Deduction
│   │
│   ├── repository/
│   │   ├── SalaryRepository
│   │   ├── SalarySlipRepository
│   │   └── DeductionRepository
│   │
│   ├── service/
│   │   ├── PayrollService
│   │   ├── SalaryCalculationService
│   │   └── SalarySlipGenerator
│   │
│   ├── controller/
│   │   └── PayrollController
│   │
│   └── dto/
│       ├── SalaryDTO
│       ├── SalarySlipDTO
│       └── PayrollReportDTO
│
├── loyalty/                         # Loyalty Program
│   ├── entity/
│   │   ├── LoyaltyAccount         # Customer loyalty points
│   │   └── LoyaltyTransaction     # Points earned/redeemed
│   │
│   ├── repository/
│   │   ├── LoyaltyAccountRepository
│   │   └── LoyaltyTransactionRepository
│   │
│   ├── service/
│   │   ├── LoyaltyService
│   │   └── LoyaltyPointsCalculator
│   │
│   ├── controller/
│   │   └── LoyaltyController
│   │
│   └── dto/
│       ├── LoyaltyAccountDTO
│       ├── RedeemPointsRequest
│       └── LoyaltyTransactionDTO
│
├── payment/                         # Payment Processing
│   ├── entity/
│   │   ├── Payment
│   │   ├── Refund
│   │   └── CashDrawer             # Cash management
│   │
│   ├── repository/
│   │   ├── PaymentRepository
│   │   ├── RefundRepository
│   │   └── CashDrawerRepository
│   │
│   ├── service/
│   │   ├── PaymentService         # Payment processing
│   │   ├── RefundService
│   │   ├── PaymentGatewayService
│   │   └── CashDrawerService      # Cash reconciliation
│   │
│   ├── gateway/
│   │   ├── StripePaymentGateway
│   │   ├── RazorpayPaymentGateway
│   │   └── PayPalPaymentGateway
│   │
│   ├── controller/
│   │   ├── PaymentController
│   │   └── CashDrawerController
│   │
│   └── dto/
│       ├── PaymentDTO
│       ├── ProcessPaymentRequest
│       ├── RefundDTO
│       └── CashDrawerDTO
│
├── notifications/                   # Multi-channel Notifications
│   ├── email/
│   │   ├── EmailService
│   │   └── EmailTemplate
│   │
│   ├── sms/
│   │   ├── SmsService
│   │   └── SmsTemplate
│   │
│   ├── whatsapp/
│   │   ├── WhatsAppService
│   │   └── WhatsAppTemplate
│   │
│   ├── push/
│   │   ├── PushNotificationService
│   │   └── PushTemplate
│   │
│   ├── entity/
│   │   └── Notification
│   │
│   ├── repository/
│   │   └── NotificationRepository
│   │
│   ├── service/
│   │   └── NotificationOrchestrator
│   │
│   ├── controller/
│   │   └── NotificationController
│   │
│   └── dto/
│       ├── NotificationDTO
│       └── SendNotificationRequest
│
├── reports/                         # Business Reports & Analytics
│   ├── controller/
│   │   ├── SalesReportController
│   │   ├── InventoryReportController
│   │   └── StaffReportController
│   │
│   ├── service/
│   │   ├── SalesAnalyticsService
│   │   ├── InventoryAnalyticsService
│   │   └── StaffAnalyticsService
│   │
│   ├── dto/
│   │   ├── SalesReportDTO
│   │   ├── InventoryReportDTO
│   │   ├── StaffReportDTO
│   │   └── DateRangeRequest
│   │
│   └── model/
│       ├── DailyRevenue
│       ├── ItemPopularity
│       ├── StaffPerformance
│       └── InventorySummary
│
├── audit/                           # Audit Logging
│   ├── entity/
│   │   └── AuditLog
│   │
│   ├── repository/
│   │   └── AuditLogRepository
│   │
│   └── service/
│       ├── AuditService
│       └── AuditLogger
│
└── security/                        # Additional Security
    ├── filter/
    │   └── JwtAuthenticationFilter
    ├── handler/
    │   ├── JwtExceptionHandler
    │   └── GlobalExceptionHandler
    ├── provider/
    │   ├── JwtTokenProvider
    │   └── OAuth2Provider
    └── util/
        ├── SecurityContextUtil
        └── PasswordEncoder
```

---

## Frontend Component Structure

```
restaurant-erp-frontend/src/
│
├── components/
│   ├── common/
│   │   ├── Navbar.jsx
│   │   ├── Sidebar.jsx
│   │   ├── ProtectedRoute.jsx
│   │   ├── LoadingSpinner.jsx
│   │   └── ErrorBoundary.jsx
│   │
│   ├── layout/
│   │   ├── MainLayout.jsx
│   │   ├── AuthLayout.jsx
│   │   └── DashboardLayout.jsx
│   │
│   ├── auth/
│   │   ├── LoginForm.jsx
│   │   ├── SignupForm.jsx
│   │   ├── ForgotPasswordForm.jsx
│   │   └── ChangePasswordForm.jsx
│   │
│   ├── organization/
│   │   ├── OrganizationList.jsx
│   │   ├── OrganizationForm.jsx
│   │   └── OrganizationDetails.jsx
│   │
│   ├── branch/
│   │   ├── BranchList.jsx
│   │   ├── BranchForm.jsx
│   │   └── BranchDetails.jsx
│   │
│   ├── user/
│   │   ├── UserList.jsx
│   │   ├── UserForm.jsx
│   │   ├── UserProfile.jsx
│   │   └── ChangePassword.jsx
│   │
│   ├── employee/
│   │   ├── EmployeeList.jsx
│   │   ├── EmployeeForm.jsx
│   │   ├── EmployeeDetails.jsx
│   │   └── EmployeeStats.jsx
│   │
│   ├── customer/
│   │   ├── CustomerList.jsx
│   │   ├── CustomerForm.jsx
│   │   ├── CustomerDetails.jsx
│   │   └── LoyaltyInfo.jsx
│   │
│   ├── menu/
│   │   ├── MenuList.jsx
│   │   ├── MenuForm.jsx
│   │   ├── CategoryList.jsx
│   │   ├── MenuItemForm.jsx
│   │   ├── ModifierList.jsx
│   │   └── ComboDealForm.jsx
│   │
│   ├── inventory/
│   │   ├── InventoryList.jsx
│   │   ├── StockMovement.jsx
│   │   ├── StockAdjustment.jsx
│   │   ├── LowStockAlerts.jsx
│   │   └── InventoryForm.jsx
│   │
│   ├── purchase/
│   │   ├── PurchaseList.jsx
│   │   ├── CreatePurchase.jsx
│   │   ├── PurchaseDetails.jsx
│   │   └── PurchaseApproval.jsx
│   │
│   ├── supplier/
│   │   ├── SupplierList.jsx
│   │   ├── SupplierForm.jsx
│   │   └── SupplierDetails.jsx
│   │
│   ├── table/
│   │   ├── TableManagement.jsx
│   │   ├── FloorView.jsx
│   │   ├── TableStatusView.jsx
│   │   ├── ReservationList.jsx
│   │   └── ReservationForm.jsx
│   │
│   ├── order/
│   │   ├── OrderList.jsx
│   │   ├── CreateOrder.jsx
│   │   ├── OrderDetails.jsx
│   │   ├── OrderBilling.jsx
│   │   ├── ApplyDiscount.jsx
│   │   └── OrderHistory.jsx
│   │
│   ├── kitchen/
│   │   ├── KitchenDisplay.jsx        # Real-time order display
│   │   ├── KitchenOrderCard.jsx
│   │   ├── OrderQueue.jsx
│   │   ├── KitchenStats.jsx
│   │   └── StationManagement.jsx
│   │
│   ├── delivery/
│   │   ├── DeliveryList.jsx
│   │   ├── DeliveryAssignment.jsx
│   │   ├── DeliveryTracking.jsx
│   │   └── DeliveryMap.jsx
│   │
│   ├── attendance/
│   │   ├── AttendanceList.jsx
│   │   ├── CheckInCheckOut.jsx
│   │   ├── AttendanceReport.jsx
│   │   └── BiometricSync.jsx
│   │
│   ├── payroll/
│   │   ├── SalaryManagement.jsx
│   │   ├── PayrollList.jsx
│   │   ├── SalarySlipGenerator.jsx
│   │   └── PayrollReport.jsx
│   │
│   ├── loyalty/
│   │   ├── LoyaltyProgram.jsx
│   │   ├── PointsManagement.jsx
│   │   ├── RedeemPoints.jsx
│   │   └── LoyaltyReport.jsx
│   │
│   ├── payment/
│   │   ├── PaymentForm.jsx
│   │   ├── PaymentGateway.jsx
│   │   ├── RefundManagement.jsx
│   │   ├── CashDrawer.jsx
│   │   └── PaymentHistory.jsx
│   │
│   ├── reports/
│   │   ├── SalesReport.jsx
│   │   ├── InventoryReport.jsx
│   │   ├── StaffReport.jsx
│   │   ├── CustomerReport.jsx
│   │   └── AnalyticsDashboard.jsx
│   │
│   └── settings/
│       ├── GeneralSettings.jsx
│       ├── SecuritySettings.jsx
│       ├── NotificationSettings.jsx
│       └── SystemSettings.jsx
│
├── pages/
│   ├── Dashboard.jsx
│   ├── Login.jsx
│   ├── Signup.jsx
│   ├── NotFound.jsx
│   └── Unauthorized.jsx
│
├── context/
│   ├── AuthContext.jsx              # Authentication state
│   ├── NotificationContext.jsx      # Global notifications
│   ├── KitchenContext.jsx           # Real-time kitchen orders
│   ├── OrderContext.jsx             # Order management state
│   └── AppContext.jsx               # Global app state
│
├── services/
│   ├── api/
│   │   ├── authApi.js               # Authentication API calls
│   │   ├── organizationApi.js       # Organization API calls
│   │   ├── branchApi.js             # Branch API calls
│   │   ├── userApi.js               # User management API
│   │   ├── employeeApi.js           # Employee API
│   │   ├── customerApi.js           # Customer API
│   │   ├── menuApi.js               # Menu management API
│   │   ├── inventoryApi.js          # Inventory API
│   │   ├── purchaseApi.js           # Purchase API
│   │   ├── supplierApi.js           # Supplier API
│   │   ├── orderApi.js              # Order API
│   │   ├── billingApi.js            # Billing API
│   │   ├── kitchenApi.js            # Kitchen API
│   │   ├── deliveryApi.js           # Delivery API
│   │   ├── attendanceApi.js         # Attendance API
│   │   ├── payrollApi.js            # Payroll API
│   │   ├── loyaltyApi.js            # Loyalty API
│   │   ├── paymentApi.js            # Payment API
│   │   ├── tableApi.js              # Table management API
│   │   ├── reportsApi.js            # Reports API
│   │   └── notificationApi.js       # Notifications API
│   │
│   ├── websocket/
│   │   ├── kitchenSocket.js         # Kitchen WebSocket
│   │   ├── deliverySocket.js        # Delivery tracking WebSocket
│   │   └── notificationSocket.js    # Real-time notifications
│   │
│   ├── storage/
│   │   ├── localStorage.js          # LocalStorage utilities
│   │   └── sessionStorage.js        # SessionStorage utilities
│   │
│   └── utils/
│       ├── axiosConfig.js           # Axios configuration
│       ├── errorHandler.js          # API error handling
│       ├── dateFormatter.js         # Date/time formatting
│       ├── currencyFormatter.js     # Currency formatting
│       └── validators.js            # Form validators
│
├── hooks/
│   ├── useAuth.js
│   ├── useApi.js
│   ├── useForm.js
│   ├── useNotification.js
│   ├── useKitchenSocket.js
│   ├── useDeliveryTracking.js
│   └── useLocalStorage.js
│
├── styles/
│   ├── Auth.css
│   ├── Dashboard.css
│   ├── Menu.css
│   ├── Order.css
│   ├── Kitchen.css
│   ├── Table.css
│   ├── Report.css
│   └── Common.css
│
├── utils/
│   ├── constants.js
│   ├── enums.js
│   ├── routes.js
│   └── config.js
│
├── App.jsx
├── main.jsx
└── index.css
```

---

## API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/signup` - User registration
- `POST /api/auth/refresh` - Refresh JWT token
- `POST /api/auth/logout` - Logout
- `POST /api/auth/forgot-password` - Password reset request
- `POST /api/auth/reset-password` - Reset password

### Organization Management
- `GET /api/organizations` - List all organizations
- `GET /api/organizations/{id}` - Get organization details
- `POST /api/organizations` - Create organization
- `PUT /api/organizations/{id}` - Update organization
- `DELETE /api/organizations/{id}` - Delete organization

### Branch Management
- `GET /api/branches` - List all branches
- `GET /api/branches/{id}` - Get branch details
- `POST /api/branches` - Create branch
- `PUT /api/branches/{id}` - Update branch
- `DELETE /api/branches/{id}` - Delete branch

### User Management
- `GET /api/users` - List all users
- `GET /api/users/{id}` - Get user details
- `POST /api/users` - Create user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user
- `POST /api/users/{id}/change-password` - Change password

### Employee Management
- `GET /api/employees` - List all employees
- `GET /api/employees/{id}` - Get employee details
- `POST /api/employees` - Create employee
- `PUT /api/employees/{id}` - Update employee
- `DELETE /api/employees/{id}` - Delete employee
- `GET /api/employees/{id}/stats` - Get employee statistics

### Customer Management
- `GET /api/customers` - List all customers
- `GET /api/customers/{id}` - Get customer details
- `POST /api/customers` - Create customer
- `PUT /api/customers/{id}` - Update customer
- `DELETE /api/customers/{id}` - Delete customer

### Menu Management
- `GET /api/menu/categories` - List categories
- `GET /api/menu/items` - List menu items
- `POST /api/menu/items` - Create menu item
- `PUT /api/menu/items/{id}` - Update menu item
- `DELETE /api/menu/items/{id}` - Delete menu item
- `GET /api/menu/modifiers` - List modifiers
- `POST /api/menu/combo-deals` - Create combo deal

### Inventory Management
- `GET /api/inventory` - List inventory
- `POST /api/inventory/adjust-stock` - Adjust stock
- `GET /api/inventory/movements` - Stock movements
- `GET /api/inventory/alerts` - Low stock alerts

### Purchase Management
- `GET /api/purchases` - List purchases
- `POST /api/purchases` - Create purchase
- `GET /api/purchases/{id}` - Get purchase details
- `PUT /api/purchases/{id}` - Update purchase
- `DELETE /api/purchases/{id}` - Delete purchase

### Order Management
- `GET /api/orders` - List orders
- `POST /api/orders` - Create order
- `GET /api/orders/{id}` - Get order details
- `PUT /api/orders/{id}` - Update order
- `PUT /api/orders/{id}/status` - Update order status
- `DELETE /api/orders/{id}` - Cancel order
- `POST /api/orders/{id}/apply-discount` - Apply discount
- `GET /api/orders/{id}/billing` - Get billing details

### Kitchen Operations (WebSocket)
- `WS /ws/kitchen` - WebSocket connection for kitchen orders
- `POST /api/kitchen/orders` - List kitchen orders
- `PUT /api/kitchen/orders/{id}/status` - Update order status
- `PUT /api/kitchen/stations/{id}/status` - Update station status

### Table Management
- `GET /api/tables` - List all tables
- `GET /api/tables/{id}` - Get table details
- `PUT /api/tables/{id}/status` - Update table status
- `POST /api/reservations` - Create reservation
- `GET /api/reservations` - List reservations
- `PUT /api/reservations/{id}` - Update reservation

### Delivery Management
- `GET /api/deliveries` - List deliveries
- `POST /api/deliveries/{id}/assign` - Assign delivery
- `PUT /api/deliveries/{id}/status` - Update delivery status
- `GET /api/deliveries/{id}/tracking` - Get delivery tracking

### Payment Management
- `POST /api/payments/process` - Process payment
- `GET /api/payments` - List payments
- `POST /api/payments/{id}/refund` - Process refund
- `GET /api/cash-drawer` - Get cash drawer status
- `POST /api/cash-drawer/reconcile` - Reconcile cash

### Loyalty Program
- `GET /api/loyalty/accounts/{customerId}` - Get loyalty account
- `POST /api/loyalty/redeem` - Redeem points
- `GET /api/loyalty/transactions` - Get transactions

### Reports
- `GET /api/reports/sales` - Sales report
- `GET /api/reports/inventory` - Inventory report
- `GET /api/reports/staff` - Staff performance report
- `GET /api/reports/customers` - Customer analysis report

### Attendance
- `POST /api/attendance/check-in` - Employee check-in
- `POST /api/attendance/check-out` - Employee check-out
- `GET /api/attendance` - List attendance records
- `GET /api/attendance/report` - Attendance report

### Payroll
- `GET /api/payroll/salaries` - List salaries
- `POST /api/payroll/calculate` - Calculate payroll
- `GET /api/payroll/slips/{id}` - Get salary slip
- `GET /api/payroll/report` - Payroll report

---

## Authentication & Authorization

### JWT Token Structure
```json
{
  "header": {
    "alg": "HS256",
    "typ": "JWT"
  },
  "payload": {
    "userId": "user_id",
    "username": "username",
    "roles": ["ADMIN", "MANAGER"],
    "permissions": ["CREATE_ORDER", "MANAGE_INVENTORY"],
    "organizationId": "org_id",
    "branchId": "branch_id",
    "iat": 1706000000,
    "exp": 1706086400
  },
  "signature": "signature_hash"
}
```

### Role-Based Access Control (RBAC)
```
ADMIN
  - Manage organizations
  - Manage branches
  - Manage users & roles
  - View all reports
  - System configuration

MANAGER
  - Manage branch operations
  - Manage inventory
  - Manage staff
  - View branch reports
  - Approve transactions

STAFF_MANAGER
  - Employee management
  - Attendance tracking
  - Payroll management

OPERATION_MANAGER
  - Order management
  - Kitchen operations
  - Table management
  - Delivery management

KITCHEN_CHEF
  - View assigned orders
  - Update order status
  - Kitchen operations only

DELIVERY_PARTNER
  - View assigned deliveries
  - Update delivery status
  - Delivery tracking

WAITER
  - Create orders
  - Update order status
  - Manage tables
  - View menu

CASHIER
  - Process payments
  - View billing details
  - Cash management
```

---

## WebSocket Communications

### Kitchen Operations Channel
**Connection**: `WS /ws/kitchen`

**Subscription**: `SUBSCRIBE_KITCHEN_ORDERS`
```json
{
  "action": "SUBSCRIBE",
  "type": "KITCHEN_ORDERS"
}
```

**Message Format** (Server to Client):
```json
{
  "type": "ORDER_ASSIGNED",
  "data": {
    "orderId": "order_123",
    "items": [...],
    "station": "grill",
    "priority": "HIGH",
    "timestamp": "2026-06-05T10:30:00Z"
  }
}
```

**Client Actions**:
- `ORDER_STARTED` - Chef starts preparing
- `ORDER_READY` - Order is ready
- `ORDER_DELAYED` - Order delayed with reason
- `ORDER_URGENT` - Mark as urgent

### Delivery Tracking Channel
**Connection**: `WS /ws/delivery`

**Real-time Location Updates**:
```json
{
  "type": "LOCATION_UPDATE",
  "deliveryId": "delivery_123",
  "latitude": 28.6139,
  "longitude": 77.2090,
  "timestamp": "2026-06-05T10:30:00Z"
}
```

### Notification Channel
**Connection**: `WS /ws/notifications`

**Broadcast Message**:
```json
{
  "type": "ORDER_READY",
  "message": "Order #123 is ready for pickup",
  "priority": "HIGH",
  "timestamp": "2026-06-05T10:30:00Z"
}
```

---

## Database Schema Overview

### Collections Structure

#### users
```javascript
{
  _id: ObjectId,
  username: String (unique),
  email: String (unique),
  password: String (hashed),
  firstName: String,
  lastName: String,
  phone: String,
  status: ENUM,
  roles: [ObjectId],
  organizationId: ObjectId,
  branchId: ObjectId,
  profileImage: String,
  lastLogin: Date,
  createdAt: Date,
  updatedAt: Date,
  isDeleted: Boolean
}
```

#### orders
```javascript
{
  _id: ObjectId,
  orderNumber: String (unique),
  customerId: ObjectId,
  tableId: ObjectId,
  branchId: ObjectId,
  items: [{
    menuItemId: ObjectId,
    quantity: Number,
    price: Decimal,
    modifiers: [ObjectId],
    specialInstructions: String
  }],
  status: ENUM,
  totalAmount: Decimal,
  discounts: [{
    discountId: ObjectId,
    amount: Decimal
  }],
  tax: Decimal,
  paymentStatus: ENUM,
  deliveryType: ENUM (DINE_IN, TAKEAWAY, DELIVERY),
  preparationTime: Number,
  createdAt: Date,
  updatedAt: Date,
  completedAt: Date
}
```

#### kitchen_orders
```javascript
{
  _id: ObjectId,
  orderId: ObjectId,
  stationId: ObjectId,
  items: [Object],
  status: ENUM,
  priority: ENUM,
  assignedTo: ObjectId,
  startedAt: Date,
  completedAt: Date,
  createdAt: Date,
  updatedAt: Date
}
```

---

## Configuration Files

### Backend Configuration (application.properties)
```properties
spring.application.name=restaurant-erp
spring.data.mongodb.uri=mongodb://localhost:27017/restaurant_erp
spring.data.mongodb.database=restaurant_erp
server.port=8080
server.servlet.context-path=/api

# JWT Configuration
jwt.secret=${JWT_SECRET}
jwt.expiration=86400000
jwt.refreshExpiration=604800000

# WebSocket
server.servlet.session.tracking-modes=cookie
spring.websocket.message-size=8192

# File Upload
file.upload-dir=${FILE_UPLOAD_DIR}
file.max-size=10485760

# Email Configuration
mail.smtp.host=${MAIL_SMTP_HOST}
mail.smtp.port=${MAIL_SMTP_PORT}
mail.username=${MAIL_USERNAME}
mail.password=${MAIL_PASSWORD}
```

### Frontend Configuration (vite.config.js)
```javascript
export default {
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      },
      '/ws': {
        target: 'ws://localhost:8080',
        ws: true,
        changeOrigin: true
      }
    }
  }
}
```

---

## Development Roadmap

### Phase 1: Core Setup (Week 1-2)
- [ ] Authentication system
- [ ] Organization & Branch management
- [ ] User & Role management
- [ ] Basic menu management

### Phase 2: Operations (Week 3-5)
- [ ] Order management system
- [ ] Table management
- [ ] Kitchen operations with WebSocket
- [ ] Payment processing

### Phase 3: Advanced Features (Week 6-8)
- [ ] Inventory management
- [ ] Supplier & Purchase management
- [ ] Delivery system
- [ ] Loyalty program

### Phase 4: HR & Analytics (Week 9-10)
- [ ] Attendance system
- [ ] Payroll management
- [ ] Comprehensive reports & analytics
- [ ] Performance metrics

### Phase 5: Deployment & Optimization (Week 11-12)
- [ ] Performance optimization
- [ ] Security hardening
- [ ] Automated testing
- [ ] Deployment configuration

---

## Best Practices

### Backend
- Use dependency injection for all services
- Implement proper exception handling with custom exceptions
- Use DTOs for API requests/responses
- Implement pagination for list endpoints
- Add audit logging for critical operations
- Use MongoDB indexes for frequently queried fields
- Implement caching for frequently accessed data
- Use WebSocket for real-time features
- Validate all input data
- Use transactions for critical operations

### Frontend
- Component composition and reusability
- Custom hooks for logic reuse
- Context API for state management
- Error boundaries for error handling
- Loading states and skeleton loaders
- Form validation before submission
- Proper error messages and user feedback
- Responsive design with Tailwind CSS
- WebSocket auto-reconnection with exponential backoff
- Request debouncing and throttling

---

## Security Considerations

1. **JWT Token Management**
   - Secure token storage in httpOnly cookies
   - Token refresh mechanism
   - Logout and token invalidation

2. **Data Protection**
   - Encrypt sensitive data at rest
   - Use HTTPS for all communications
   - Implement rate limiting
   - Sanitize all user inputs

3. **Access Control**
   - Implement RBAC
   - Endpoint-level authorization
   - Field-level access control
   - Audit logging for sensitive operations

4. **API Security**
   - CORS configuration
   - Input validation and sanitization
   - SQL injection prevention (N/A for MongoDB, but query injection prevention)
   - CSRF protection

---

End of Architecture Documentation

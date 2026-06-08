# Restaurant POS System - Implementation Guide

## Quick Start

### Backend Setup

1. **Clone the repository**
   ```bash
   git clone <repo-url>
   cd restaurant-erp/restaurant-erp-backend
   ```

2. **Configure MongoDB**
   - Update `application.properties` with MongoDB connection
   - Set environment variables for JWT secret and other configs

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

### Frontend Setup

1. **Navigate to frontend**
   ```bash
   cd restaurant-erp/restaurant-erp-frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Configure environment**
   - Create `.env.local` with API URL
   ```env
   VITE_API_URL=http://localhost:8080/api
   ```

4. **Start development server**
   ```bash
   npm run dev
   ```

---

## Module Implementation Guide

### 1. Branch Management

**Backend Usage:**
```java
// Service
@Autowired
private BranchService branchService;

public void createNewBranch() {
    BranchDTO branchDTO = new BranchDTO();
    branchDTO.setOrganizationId("org123");
    branchDTO.setName("Main Branch");
    branchDTO.setCity("Mumbai");
    BranchDTO created = branchService.createBranch(branchDTO);
}
```

**Frontend Usage:**
```javascript
// Import the API
import { branchApi } from '@/services/api/branchApi';

// Use in component
const fetchBranches = async () => {
    const branches = await branchApi.getBranchesByOrganization(orgId);
    setBranches(branches);
};
```

### 2. Menu Management

**Backend - Create Menu Item:**
```java
@RestController
@PostMapping("/api/menu/items")
public MenuItem createMenuItem(@RequestBody MenuItem item) {
    // Set required fields
    item.setBranchId(branchId);
    item.setCategoryId(categoryId);
    return menuService.createMenuItem(item);
}
```

**Frontend - Display Menu:**
```javascript
import { menuApi } from '@/services/api/menuApi';

useEffect(() => {
    const loadMenu = async () => {
        const categories = await menuApi.getCategories(branchId);
        const items = await menuApi.getMenuItems(branchId);
        setCategories(categories);
        setMenuItems(items);
    };
    loadMenu();
}, [branchId]);
```

### 3. Order Management

**Creating an Order:**
```java
// Backend
Order order = new Order();
order.setBranchId(branchId);
order.setTableId(tableId);
order.setItems(orderItems);
order.setDeliveryType("DINE_IN");

Order createdOrder = orderService.createOrder(order);
```

**Frontend - Order Creation:**
```javascript
const handleCreateOrder = async (orderData) => {
    const response = await orderApi.createOrder({
        branchId,
        tableId,
        items: selectedItems,
        deliveryType: 'DINE_IN',
        waiterName: currentWaiter
    });
    
    setOrderId(response.data.id);
    
    // Send to kitchen
    kitchenSocket.send({
        type: 'ORDER_CREATED',
        orderId: response.data.id,
        items: selectedItems
    });
};
```

### 4. Kitchen Operations (Real-time)

**Backend - Kitchen Service:**
```java
@Service
public class KitchenService {
    public KitchenOrder createKitchenOrder(KitchenOrder order) {
        order.setStatus("PENDING");
        KitchenOrder saved = kitchenOrderRepository.save(order);
        
        // Broadcast via WebSocket
        kitchenSocketPublisher.publishNewOrder(saved);
        return saved;
    }
}
```

**Frontend - Kitchen Display:**
```javascript
import { kitchenSocket } from '@/services/websocket/kitchenSocket';

useEffect(() => {
    kitchenSocket.connect(branchId, userId);
    
    // Listen for new orders
    kitchenSocket.on('ORDER_ASSIGNED', (message) => {
        addOrderToQueue(message.data);
        playAlertSound();
    });
    
    return () => kitchenSocket.disconnect();
}, []);

const markOrderReady = (orderId) => {
    kitchenSocket.markOrderReady(orderId);
};
```

### 5. Table Management

**Get Available Tables:**
```javascript
const loadAvailableTables = async () => {
    const tables = await tableApi.getAvailableTables(branchId);
    displayTablesOnFloorMap(tables);
};

const updateTableStatus = async (tableId, status) => {
    await tableApi.updateTableStatus(tableId, status);
    // Refresh display
};
```

### 6. Payment Processing

**Backend - Process Payment:**
```java
Payment payment = new Payment();
payment.setOrderId(orderId);
payment.setAmount(totalAmount);
payment.setPaymentMethod(method);
payment.setCustomerName(customerName);

Payment processed = paymentService.processPayment(payment);
```

**Frontend - Payment Form:**
```javascript
const handlePayment = async (paymentDetails) => {
    try {
        const response = await paymentApi.processPayment({
            orderId,
            amount: totalAmount,
            amountReceived: receivedAmount,
            paymentMethod: selectedMethod,
            customerName,
            customerPhone
        });
        
        if (response.status === 'COMPLETED') {
            completeOrder(orderId);
            printReceipt(response);
        }
    } catch (error) {
        showErrorMessage('Payment failed');
    }
};
```

### 7. Inventory Management

**Adjust Stock:**
```java
// Backend
inventoryService.adjustStock(
    inventoryId,
    50,
    "IN",
    "Purchase from supplier",
    userId
);
```

**Frontend:**
```javascript
const adjustStock = async (itemId, quantity, type) => {
    await inventoryApi.adjustStock(
        itemId,
        quantity,
        type,
        'Manual adjustment',
        currentUser
    );
    
    // Refresh inventory
    loadInventory();
};

// Monitor low stock
useEffect(() => {
    const checkLowStock = async () => {
        const lowStockItems = await inventoryApi.getLowStockAlerts(branchId);
        if (lowStockItems.length > 0) {
            showNotification(`${lowStockItems.length} items are low in stock`);
        }
    };
    checkLowStock();
}, [branchId]);
```

### 8. Employee Attendance

**Frontend - Check In/Out:**
```javascript
const handleCheckIn = async () => {
    await attendanceApi.checkIn(
        employeeId,
        employeeName,
        branchId,
        'BIOMETRIC'
    );
    
    showSuccessMessage('Checked in successfully');
};

const handleCheckOut = async () => {
    const response = await attendanceApi.checkOut(attendanceId);
    showSuccessMessage(`Work duration: ${response.workDuration}`);
};
```

### 9. Reports & Analytics

**Get Sales Report:**
```javascript
const generateSalesReport = async (startDate, endDate) => {
    const report = await reportsApi.getSalesReport(
        branchId,
        startDate,
        endDate
    );
    
    displayCharts(report);
};

// Export to PDF
const exportReport = async (reportType) => {
    const blob = await reportsApi.exportReport(reportType, branchId, 'pdf');
    downloadFile(blob, `${reportType}_report.pdf`);
};
```

### 10. Loyalty Program

**Apply Loyalty Points:**
```javascript
const redeemLoyaltyPoints = async (customerId, points) => {
    const response = await loyaltyApi.redeemPoints(
        customerId,
        points,
        orderId
    );
    
    // Update order total
    updateOrderTotal(response.discountAmount);
};

// Add points after order
const awardPoints = async (customerId, orderTotal) => {
    const points = Math.floor(orderTotal / 100); // 1 point per 100 currency
    await loyaltyApi.addPoints(customerId, points, 'Purchase reward');
};
```

---

## Database Schema Key Collections

### Users Collection
```javascript
{
  _id: ObjectId,
  username: String,
  email: String,
  password: String (hashed),
  roles: [ObjectId],
  organizationId: ObjectId,
  branchId: ObjectId,
  status: String,
  createdAt: Date,
  updatedAt: Date
}
```

### Orders Collection
```javascript
{
  _id: ObjectId,
  orderNumber: String,
  customerId: ObjectId,
  tableId: ObjectId,
  branchId: ObjectId,
  items: [{
    menuItemId: ObjectId,
    quantity: Number,
    price: Decimal,
    modifiers: [ObjectId]
  }],
  status: String,
  totalAmount: Decimal,
  paymentStatus: String,
  createdAt: Date,
  completedAt: Date
}
```

### Kitchen Orders Collection
```javascript
{
  _id: ObjectId,
  orderId: ObjectId,
  stationId: ObjectId,
  items: [Object],
  status: String,
  priority: String,
  assignedChefId: ObjectId,
  startedAt: Date,
  completedAt: Date,
  createdAt: Date
}
```

---

## WebSocket Event Types

### Kitchen WebSocket

**Server to Client:**
- `ORDER_ASSIGNED` - New order assigned to kitchen
- `ORDER_URGENCY_UPDATE` - Order priority changed
- `STATION_STATUS_CHANGE` - Station status updated

**Client to Server:**
- `ORDER_STARTED` - Chef started preparing
- `ORDER_READY` - Order ready for pickup
- `ORDER_DELAYED` - Order delayed
- `STATION_BUSY` - Station busy status

### Delivery WebSocket

**Server to Client:**
- `DELIVERY_ASSIGNED` - Delivery assigned
- `LOCATION_UPDATE` - Real-time location

**Client to Server:**
- `UPDATE_LOCATION` - Send GPS coordinates
- `UPDATE_STATUS` - Delivery status change

---

## Error Handling

**Backend Exception Handling:**
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            ex.getMessage(),
            "RESOURCE_NOT_FOUND",
            404
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException ex) {
        ErrorResponse error = new ErrorResponse(
            ex.getMessage(),
            "VALIDATION_ERROR",
            400
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
```

**Frontend Error Handling:**
```javascript
import { useNotification } from '@/hooks/useNotification';

const handleApiError = (error) => {
    const { showError } = useNotification();
    
    if (error.response?.status === 404) {
        showError('Resource not found');
    } else if (error.response?.status === 400) {
        showError('Invalid input: ' + error.response.data.message);
    } else if (error.response?.status === 401) {
        redirectToLogin();
    } else {
        showError('An error occurred. Please try again.');
    }
};
```

---

## Performance Optimization Tips

1. **Backend:**
   - Enable MongoDB indexes on frequently queried fields
   - Use pagination for large result sets
   - Implement caching for menu items and categories
   - Use database aggregation for reports

2. **Frontend:**
   - Lazy load components and routes
   - Implement virtual scrolling for large lists
   - Cache API responses
   - Debounce search inputs
   - Optimize images and assets

3. **WebSocket:**
   - Implement heartbeat/ping mechanism
   - Auto-reconnect with exponential backoff
   - Clean up subscriptions on unmount

---

## Deployment Checklist

- [ ] Configure environment variables
- [ ] Set up MongoDB Atlas or self-hosted MongoDB
- [ ] Configure payment gateways (Stripe, Razorpay)
- [ ] Set up email service (SendGrid, AWS SES)
- [ ] Enable HTTPS/SSL certificates
- [ ] Configure CORS for frontend domain
- [ ] Set up CI/CD pipeline
- [ ] Configure automated backups
- [ ] Set up monitoring and logging
- [ ] Create admin user and test all features

---

## Support & Documentation

- API Documentation: See ARCHITECTURE.md for detailed API endpoints
- For WebSocket setup: Refer to WebSocket Configuration guide
- Database queries: Check MongoDB aggregation examples
- Testing: Unit tests in `src/test/java`

---

End of Implementation Guide

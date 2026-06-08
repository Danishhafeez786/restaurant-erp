# Frontend Development Guide - React + Vite + Tailwind

## Project Structure

```
restaurant-erp-frontend/
├── public/                    # Static assets
├── src/
│   ├── components/           # Reusable components
│   │   ├── common/          # Common UI components
│   │   │   ├── Header.jsx
│   │   │   ├── Sidebar.jsx
│   │   │   ├── Footer.jsx
│   │   │   └── Loader.jsx
│   │   ├── Auth/            # Auth components
│   │   │   ├── LoginForm.jsx
│   │   │   ├── SignupForm.jsx
│   │   │   └── ProtectedRoute.jsx
│   │   ├── Order/           # Order management
│   │   │   ├── OrderList.jsx
│   │   │   ├── OrderForm.jsx
│   │   │   ├── OrderDetail.jsx
│   │   │   └── OrderBill.jsx
│   │   ├── Kitchen/         # Kitchen display
│   │   │   ├── KitchenDisplay.jsx
│   │   │   ├── OrderCard.jsx
│   │   │   └── StationView.jsx
│   │   ├── Table/           # Table management
│   │   │   ├── TableLayout.jsx
│   │   │   ├── TableDetail.jsx
│   │   │   └── FloorPlan.jsx
│   │   ├── Inventory/       # Inventory
│   │   │   ├── InventoryList.jsx
│   │   │   ├── StockAdjustment.jsx
│   │   │   └── LowStockAlerts.jsx
│   │   ├── Reports/         # Reports & Analytics
│   │   │   ├── Dashboard.jsx
│   │   │   ├── SalesReport.jsx
│   │   │   ├── InventoryReport.jsx
│   │   │   └── StaffReport.jsx
│   │   └── Admin/           # Admin panels
│   │       ├── BranchManager.jsx
│   │       ├── UserManager.jsx
│   │       └── Settings.jsx
│   ├── context/             # React Context
│   │   ├── AuthContext.jsx
│   │   ├── OrderContext.jsx
│   │   ├── NotificationContext.jsx
│   │   └── AppContext.jsx
│   ├── hooks/               # Custom hooks
│   │   ├── useAuth.js
│   │   ├── useApi.js
│   │   ├── useForm.js
│   │   ├── useNotification.js
│   │   ├── useKitchenSocket.js
│   │   └── useLocalStorage.js
│   ├── services/            # API services
│   │   ├── api/            # API endpoints
│   │   ├── websocket/      # WebSocket services
│   │   └── utils/          # Utilities
│   ├── pages/               # Page components
│   │   ├── Login.jsx
│   │   ├── Dashboard.jsx
│   │   ├── Orders.jsx
│   │   ├── Kitchen.jsx
│   │   ├── Tables.jsx
│   │   ├── Inventory.jsx
│   │   ├── Reports.jsx
│   │   └── Admin.jsx
│   ├── styles/              # CSS files
│   │   ├── Auth.css
│   │   ├── Dashboard.css
│   │   ├── Order.css
│   │   ├── Kitchen.css
│   │   ├── Table.css
│   │   ├── Inventory.css
│   │   ├── Report.css
│   │   └── Common.css
│   ├── utils/               # Utility functions
│   │   ├── formatters.js
│   │   ├── validators.js
│   │   ├── dateHelper.js
│   │   └── constants.js
│   ├── App.jsx
│   ├── index.css
│   └── main.jsx
├── .env.example
├── .env.local
├── eslint.config.js
├── index.html
├── package.json
├── postcss.config.js
├── README.md
├── tailwind.config.js
└── vite.config.js
```

## Setup Instructions

### 1. Environment Configuration

Create `.env.local`:
```env
# API Configuration
VITE_API_URL=http://localhost:8080/api
VITE_WS_URL=ws://localhost:8080

# App Configuration
VITE_APP_NAME=Restaurant POS
VITE_APP_VERSION=1.0.0

# Feature Flags
VITE_ENABLE_KITCHEN_DISPLAY=true
VITE_ENABLE_DELIVERY_TRACKING=true
VITE_ENABLE_REPORTS=true
```

### 2. Install Dependencies

```bash
npm install

# Key dependencies
npm install axios                      # HTTP client
npm install zustand                    # State management (alternative to Context)
npm install react-router-dom           # Routing
npm install date-fns                   # Date handling
npm install chart.js react-chartjs-2  # Charts
npm install react-toastify            # Toast notifications
npm install framer-motion              # Animations
```

### 3. Development Server

```bash
npm run dev
# Opens http://localhost:5173
```

### 4. Build for Production

```bash
npm run build
npm run preview  # Preview production build
```

---

## Custom Hooks Reference

### useAuth - Authentication Management

```javascript
// hooks/useAuth.js
import { useContext } from 'react';
import { AuthContext } from '../context/AuthContext';

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
};

// Usage in component
const MyComponent = () => {
  const { user, login, logout, isAuthenticated } = useAuth();
  
  if (!isAuthenticated) {
    return <Navigate to="/login" />;
  }
  
  return (
    <div>
      <p>Welcome, {user.name}</p>
      <button onClick={logout}>Logout</button>
    </div>
  );
};
```

### useApi - API Call Management

```javascript
// hooks/useApi.js
import { useState, useCallback } from 'react';

export const useApi = (apiFunction) => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const execute = useCallback(async (...args) => {
    try {
      setLoading(true);
      setError(null);
      const result = await apiFunction(...args);
      setData(result);
      return result;
    } catch (err) {
      setError(err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  }, [apiFunction]);

  return { data, loading, error, execute };
};

// Usage
const OrderList = () => {
  const { data: orders, loading, execute: fetchOrders } = useApi(orderApi.getOrders);
  
  useEffect(() => {
    fetchOrders(branchId);
  }, [branchId]);
  
  if (loading) return <Loader />;
  return <div>{orders?.map(order => <OrderCard key={order.id} order={order} />)}</div>;
};
```

### useForm - Form Handling

```javascript
// hooks/useForm.js
import { useState, useCallback } from 'react';

export const useForm = (initialValues, onSubmit) => {
  const [values, setValues] = useState(initialValues);
  const [errors, setErrors] = useState({});
  const [touched, setTouched] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleChange = useCallback((e) => {
    const { name, value, type, checked } = e.target;
    setValues(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  }, []);

  const handleBlur = useCallback((e) => {
    const { name } = e.target;
    setTouched(prev => ({ ...prev, [name]: true }));
  }, []);

  const handleSubmit = useCallback(async (e) => {
    e.preventDefault();
    setIsSubmitting(true);
    try {
      await onSubmit(values);
    } catch (err) {
      setErrors({ form: err.message });
    } finally {
      setIsSubmitting(false);
    }
  }, [values, onSubmit]);

  return {
    values,
    errors,
    touched,
    isSubmitting,
    handleChange,
    handleBlur,
    handleSubmit,
    setValues,
    setErrors
  };
};

// Usage
const LoginForm = () => {
  const { values, errors, handleChange, handleSubmit } = useForm(
    { email: '', password: '' },
    async (values) => {
      await authApi.login(values);
    }
  );

  return (
    <form onSubmit={handleSubmit}>
      <input
        name="email"
        value={values.email}
        onChange={handleChange}
      />
      {errors.email && <span>{errors.email}</span>}
      <button type="submit">Login</button>
    </form>
  );
};
```

### useKitchenSocket - Real-time Kitchen Updates

```javascript
// hooks/useKitchenSocket.js
import { useEffect, useCallback } from 'react';
import { kitchenSocket } from '../services/websocket/kitchenSocket';
import { useNotification } from './useNotification';

export const useKitchenSocket = (branchId, userId) => {
  const { showNotification } = useNotification();

  useEffect(() => {
    kitchenSocket.connect(branchId, userId);

    kitchenSocket.on('ORDER_ASSIGNED', (message) => {
      showNotification(`New order: ${message.data.orderNumber}`, 'info');
    });

    kitchenSocket.on('ORDER_READY', (message) => {
      showNotification(`Order ready: ${message.data.orderNumber}`, 'success');
    });

    return () => kitchenSocket.disconnect();
  }, [branchId, userId]);

  const updateOrderStatus = useCallback((orderId, status) => {
    kitchenSocket.updateOrderStatus(orderId, status);
  }, []);

  return { updateOrderStatus };
};

// Usage in Kitchen Display
const KitchenDisplay = () => {
  const { user } = useAuth();
  const { updateOrderStatus } = useKitchenSocket(user.branchId, user.id);
  
  return (
    <div className="kitchen-display">
      {/* Kitchen display UI */}
    </div>
  );
};
```

---

## Component Examples

### Order Management Component

```javascript
// components/Order/OrderForm.jsx
import { useState } from 'react';
import { orderApi } from '@/services/api/orderApi';
import { useForm } from '@/hooks/useForm';
import { useNotification } from '@/hooks/useNotification';

export const OrderForm = ({ branchId, tableId, onOrderCreated }) => {
  const { showSuccess, showError } = useNotification();
  const [items, setItems] = useState([]);
  
  const { values, handleChange, handleSubmit } = useForm(
    { customerName: '', deliveryType: 'DINE_IN' },
    async (formValues) => {
      try {
        const orderData = {
          ...formValues,
          branchId,
          tableId,
          items
        };
        
        const response = await orderApi.createOrder(orderData);
        showSuccess('Order created successfully');
        onOrderCreated(response.data);
      } catch (error) {
        showError(error.message);
      }
    }
  );

  const addItem = (menuItem) => {
    setItems([...items, { ...menuItem, quantity: 1 }]);
  };

  return (
    <form onSubmit={handleSubmit} className="order-form">
      <div className="form-group">
        <label>Customer Name</label>
        <input
          name="customerName"
          value={values.customerName}
          onChange={handleChange}
          required
        />
      </div>

      <div className="form-group">
        <label>Delivery Type</label>
        <select name="deliveryType" value={values.deliveryType} onChange={handleChange}>
          <option value="DINE_IN">Dine In</option>
          <option value="TAKEAWAY">Takeaway</option>
          <option value="DELIVERY">Delivery</option>
        </select>
      </div>

      <div className="items-section">
        <h3>Order Items</h3>
        {items.map((item, idx) => (
          <div key={idx} className="item-row">
            <span>{item.name}</span>
            <span>₹{item.price}</span>
            <input
              type="number"
              min="1"
              value={item.quantity}
              onChange={(e) => {
                const newItems = [...items];
                newItems[idx].quantity = parseInt(e.target.value);
                setItems(newItems);
              }}
            />
          </div>
        ))}
      </div>

      <button type="submit" className="btn btn-primary">
        Create Order
      </button>
    </form>
  );
};
```

### Kitchen Display System Component

```javascript
// components/Kitchen/KitchenDisplay.jsx
import { useEffect, useState } from 'react';
import { kitchenApi } from '@/services/api/kitchenApi';
import { useKitchenSocket } from '@/hooks/useKitchenSocket';
import { useAuth } from '@/hooks/useAuth';

export const KitchenDisplay = () => {
  const { user } = useAuth();
  const { updateOrderStatus } = useKitchenSocket(user.branchId, user.id);
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadKitchenOrders();
  }, []);

  const loadKitchenOrders = async () => {
    try {
      const data = await kitchenApi.getKitchenOrders(user.branchId);
      setOrders(data);
    } catch (error) {
      console.error('Failed to load kitchen orders:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleStatusUpdate = async (orderId, newStatus) => {
    try {
      await kitchenApi.updateOrderStatus(orderId, newStatus);
      updateOrderStatus(orderId, newStatus);
      setOrders(orders.map(o =>
        o.id === orderId ? { ...o, status: newStatus } : o
      ));
    } catch (error) {
      console.error('Failed to update order status:', error);
    }
  };

  if (loading) return <div className="loader">Loading kitchen orders...</div>;

  return (
    <div className="kitchen-display grid grid-cols-3 gap-4">
      {orders.map(order => (
        <div key={order.id} className={`order-card ${order.status.toLowerCase()}`}>
          <h3 className="order-number">#{order.orderNumber}</h3>
          <p className="order-time">{new Date(order.createdAt).toLocaleTimeString()}</p>
          
          <div className="items">
            {order.items.map((item, idx) => (
              <div key={idx} className="item">
                <span>{item.quantity}x {item.itemName}</span>
              </div>
            ))}
          </div>

          <div className="actions">
            <button
              onClick={() => handleStatusUpdate(order.id, 'IN_PROGRESS')}
              disabled={order.status !== 'PENDING'}
              className="btn btn-blue"
            >
              Start
            </button>
            <button
              onClick={() => handleStatusUpdate(order.id, 'READY')}
              className="btn btn-green"
            >
              Ready
            </button>
          </div>
        </div>
      ))}
    </div>
  );
};
```

---

## Tailwind CSS Best Practices

### Common Utility Classes

```jsx
// Spacing
className="p-4"      // Padding
className="m-2"      // Margin
className="gap-4"    // Gap between flex/grid items

// Flexbox
className="flex flex-col gap-4"      // Column layout
className="flex justify-between"     // Space between
className="flex items-center"        // Vertical center

// Grid
className="grid grid-cols-3 gap-4"   // 3-column grid

// Colors
className="bg-blue-600"              // Background
className="text-gray-700"            // Text color
className="border-2 border-red-500"  // Border

// Responsive
className="md:grid-cols-2 lg:grid-cols-3"  // Responsive columns
className="hidden md:flex"                  // Hide on mobile

// States
className="hover:bg-blue-700"        // Hover state
className="focus:ring-2"             // Focus state
className="disabled:opacity-50"      // Disabled state
```

### Common Component Patterns

```jsx
// Button
<button className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">
  Click me
</button>

// Card
<div className="bg-white rounded-lg shadow-md p-6">
  <h3 className="text-xl font-bold mb-2">Title</h3>
  <p className="text-gray-600">Content</p>
</div>

// Form Input
<input
  className="w-full px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
  type="text"
  placeholder="Enter text"
/>

// Modal
<div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
  <div className="bg-white rounded-lg p-8 max-w-md w-full">
    <h2 className="text-xl font-bold mb-4">Modal Title</h2>
    <p className="mb-6">Modal content</p>
    <button className="bg-blue-600 text-white px-4 py-2 rounded">Close</button>
  </div>
</div>
```

---

## Performance Optimization

### Code Splitting
```javascript
import { lazy, Suspense } from 'react';

const Dashboard = lazy(() => import('./pages/Dashboard'));
const Reports = lazy(() => import('./pages/Reports'));

export const Routes = () => (
  <Suspense fallback={<Loader />}>
    <Route path="/dashboard" element={<Dashboard />} />
    <Route path="/reports" element={<Reports />} />
  </Suspense>
);
```

### Memoization
```javascript
import { memo, useMemo, useCallback } from 'react';

// Memoize component
const OrderCard = memo(({ order, onUpdate }) => (
  <div>{order.orderNumber}</div>
));

// Memoize expensive computation
const expensiveData = useMemo(() => {
  return orders.filter(o => o.status === 'PENDING');
}, [orders]);

// Memoize callback
const handleUpdate = useCallback(() => {
  updateOrder(orderId);
}, [orderId]);
```

---

End of Frontend Development Guide

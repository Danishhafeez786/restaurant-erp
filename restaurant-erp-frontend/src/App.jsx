import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css"; // 👈 required CSS
import AuthProvider from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import Login from './pages/Login';
import Signup from './pages/Signup';
import SystemSettings from './pages/SystemSetting/SystemSettings';
import CreateEmployee from './pages/CreateEmployee';
import Dashboard from './pages/Dashboard';
import Employee from './pages/employees/Employee';

import Settings from './pages/SystemSetting/Settings/Settings';
import SubscriptionPlans from './pages/SystemSetting/SubscriptionPlans/SubscriptionPlans';
import Organization from './pages/SystemSetting/Organizations/Organizations';
import Branch from './pages/SystemSetting/Branch/Branch';
import Role from './pages/SystemSetting/Role/Role';
import Permission from './pages/SystemSetting/Permission/Permission';

import RestaurantSettings from './pages/RestaurantSetting/RestaurantSettings';
import Category from './pages/RestaurantSetting/Category/Category';
import Tables from './pages/Tables/Tables';
import ModifierGroup from './pages/RestaurantSetting/ModifierGroup/ModifierGroup';
import Modifier from './pages/Modifier/Modifier';

import './App.css';

function App() {
  return (
    <>
      <Router>
        <AuthProvider>
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/signup" element={<Signup />} />
            <Route path="/system-settings" element={<ProtectedRoute><SystemSettings /></ProtectedRoute>}>
              <Route index element={<Navigate to="subscription-plans" replace />}/>
              <Route path="subscription-plans" element={<ProtectedRoute><SubscriptionPlans /></ProtectedRoute>}/>
              <Route path="organizations" element={<ProtectedRoute> <Organization /> </ProtectedRoute>} />
              <Route path="branch" element={<ProtectedRoute> <Branch /> </ProtectedRoute>} />
              <Route path="role" element={<ProtectedRoute> <Role /> </ProtectedRoute>} />
              <Route path="permission" element={<ProtectedRoute> <Permission /> </ProtectedRoute>} />
              <Route path="settings" element={<ProtectedRoute> <Settings /> </ProtectedRoute>} />
            </Route>

            <Route path="/restaurant-settings" element={<ProtectedRoute> <RestaurantSettings /> </ProtectedRoute>} >
              <Route index element={<Navigate to="category-management" replace />} />
              <Route path="category-management" element={<ProtectedRoute> <Category /> </ProtectedRoute>} />
              <Route path="table-management" element={<ProtectedRoute> <Tables /> </ProtectedRoute>} />
              <Route path="modifier-group" element={<ProtectedRoute> <ModifierGroup /> </ProtectedRoute>} />
              <Route path="modifier" element={<ProtectedRoute> <Modifier /> </ProtectedRoute>} />
            
            </Route>

            <Route path="/dashboard" element={<ProtectedRoute> <Dashboard /> </ProtectedRoute>} />
            <Route path="/create-employee" element={<ProtectedRoute> <CreateEmployee /> </ProtectedRoute>} />
            <Route path="/" element={<Navigate to="/dashboard" />} />
            <Route path="*" element={<Navigate to="/dashboard" />} />
            <Route path="/employee" element={<ProtectedRoute> <Employee /> </ProtectedRoute>} />
            <Route path="/category-management" element={<ProtectedRoute> <Category /> </ProtectedRoute>} />
            <Route path="/table-management" element={<ProtectedRoute> <Tables /> </ProtectedRoute>} />
            <Route path="/modifier-group" element={<ProtectedRoute> <ModifierGroup /> </ProtectedRoute>} />
            <Route path="/modifier" element={<ProtectedRoute> <Modifier /> </ProtectedRoute>} />
          </Routes>
        </AuthProvider>
        {/* ✅ Global Toast Container (always available) */}
        <ToastContainer position="top-right" autoClose={3000} hideProgressBar={false} newestOnTop={false}
          closeOnClick rtl={false} pauseOnFocusLoss draggable pauseOnHover theme="colored" // or "light" / "dark"
        />
      </Router>
      <ToastContainer position="top-right" autoClose={3000} />
    </>
  );
}

export default App;

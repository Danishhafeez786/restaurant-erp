import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css"; // 👈 required CSS
import AuthProvider from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import Login from './pages/Login';
import Signup from './pages/Signup';
import CreateEmployee from './pages/CreateEmployee';
import Dashboard from './pages/Dashboard';
import Employees from './pages/employees/Employees';
import SubscriptionPlans from './pages/SubscriptionPlans/SubscriptionPlans';
import Organization from "./pages/Organizations/Organizations";
import './App.css';

function App() {
  return (
    <Router>
      <AuthProvider>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/dashboard" element={ <ProtectedRoute> <Dashboard /> </ProtectedRoute>} />
          <Route path="/create-employee" element={ <CreateEmployee />} />
          <Route path="/subscription-plans" element={<SubscriptionPlans />} />
          <Route path="/organizations"element={<Organization />}/>
          <Route path="/" element={<Navigate to="/dashboard" />} />
          <Route path="*" element={<Navigate to="/dashboard" />} />
          <Route path="/employees" element={<Employees />} />
        </Routes>
      </AuthProvider>
      {/* ✅ Global Toast Container (always available) */}
      <ToastContainer position="top-right" autoClose={3000} hideProgressBar={false} newestOnTop={false}
        closeOnClick rtl={false} pauseOnFocusLoss draggable pauseOnHover theme="colored" // or "light" / "dark"
      />
    </Router>
  );
}

export default App;

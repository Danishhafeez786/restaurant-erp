import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css"; // 👈 required CSS
import AuthProvider from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import Login from './pages/Login';
import Signup from './pages/Signup';
import CreateEmployee from './pages/CreateEmployee';
import Dashboard from './pages/Dashboard';
<<<<<<< HEAD
import CreateEmployee from './pages/employees/CreateEmployee';
import Employees from './pages/employees/Employees';
import './App.css';
=======
>>>>>>> 00f26f8ba28b5ffa3b139efb56502a23b3393a66

function App() {
  return (
    <Router>
      <AuthProvider>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/dashboard" element={ <ProtectedRoute> <Dashboard /> </ProtectedRoute>} />
          <Route path="/create-employee" element={ <CreateEmployee />} />
          <Route path="/" element={<Navigate to="/dashboard" />} />
          <Route path="*" element={<Navigate to="/dashboard" />} />
          <Route path="/create-employee" element={<CreateEmployee />} />
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

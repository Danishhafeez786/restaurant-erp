import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css"; // 👈 required CSS
import AuthProvider from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import Login from './pages/Login';
import Signup from './pages/Signup';
import CreateEmployee from './pages/CreateEmployee';
import Dashboard from './pages/Dashboard';
import Employee from './pages/employees/Employee';
import SubscriptionPlans from './pages/SubscriptionPlans/SubscriptionPlans';
import Organization from "./pages/Organizations/Organizations";
import Branch from "./pages/Branch/Branch";
import Role from "./pages/Role/Role";
import Permission from './pages/Permission/Permission';
import Settings from './pages/Settings/Settings';
import Category from './pages/Category/Category';
import Tables from './pages/Tables/Tables';
import ModifierGroup from './pages/ModifierGroup/ModifierGroup';
import Modifier from './pages/Modifier/Modifier';
import './App.css';

function App() {
  return (
    <Router>
      <AuthProvider>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/settings" element={<Settings />} />
          <Route path="/dashboard" element={ <ProtectedRoute> <Dashboard /> </ProtectedRoute>} />
          <Route path="/create-employee" element={ <ProtectedRoute> <CreateEmployee /> </ProtectedRoute> } />
          <Route path="/subscription-plans" element={ <ProtectedRoute> <SubscriptionPlans /> </ProtectedRoute> } />
          <Route path="/organizations"element={ <ProtectedRoute> <Organization /> </ProtectedRoute>}/>
          <Route path="/branch"element={ <ProtectedRoute> <Branch /> </ProtectedRoute> }/>
          <Route path="/role"element={<Role />}/>
          <Route path="/permission"element={ <ProtectedRoute> <Permission /> </ProtectedRoute> }/>
          <Route path="/" element={<Navigate to="/dashboard" />} />
          <Route path="*" element={<Navigate to="/dashboard" />} />
          <Route path="/employee" element={ <ProtectedRoute> <Employee /> </ProtectedRoute> } />
          <Route path="/category-management" element={ <ProtectedRoute> <Category /> </ProtectedRoute> } />
          <Route path="/table-management" element={ <ProtectedRoute> <Tables /> </ProtectedRoute> } />
          <Route path="/modifier-group" element={ <ProtectedRoute> <ModifierGroup /> </ProtectedRoute> } />
          <Route path="/modifier" element={ <ProtectedRoute> <Modifier /> </ProtectedRoute> } />
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

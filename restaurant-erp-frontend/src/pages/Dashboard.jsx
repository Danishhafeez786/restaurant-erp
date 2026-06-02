import React from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

const Dashboard = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const features = [
    { icon: '📋', title: 'Orders', desc: 'Manage and track all orders' },
    { icon: '🍽️', title: 'Menu', desc: 'Create and customize menus' },
    { icon: '👥', title: 'Staff', desc: 'Manage your team' },
    { icon: '📊', title: 'Analytics', desc: 'View detailed reports' },
    { icon: '💰', title: 'Billing', desc: 'Track payments and invoices' },
    { icon: '⚙️', title: 'Settings', desc: 'Configure your system' },
  ];

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 to-slate-100">
      {/* Header */}
      <header className="bg-gradient-to-r from-primary-600 to-accent-600 text-white shadow-lg">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
          <div className="flex justify-between items-center">
            <div>
              <h1 className="text-3xl md:text-4xl font-bold">Restaurant ERP</h1>
              <p className="text-white/80 mt-1">{user?.restaurantName}</p>
            </div>
            <button
              onClick={handleLogout}
              className="px-6 py-3 bg-white/20 hover:bg-white/30 text-white font-semibold rounded-lg transition-all duration-300 transform hover:scale-105"
            >
              Sign Out
            </button>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        {/* Welcome Section */}
        <div className="card p-8 md:p-10 mb-12 bg-gradient-to-br from-primary-50 to-accent-50 border-2 border-primary-100">
          <h2 className="text-3xl font-bold text-slate-900 mb-2">
            Welcome, {user?.firstName}! 👋
          </h2>
          <p className="text-slate-600 text-lg">
            Your restaurant management system is ready to go. Let's make today productive!
          </p>
        </div>

        {/* User Info Cards */}
        <div className="grid md:grid-cols-3 gap-6 mb-12">
          <div className="card p-6 border-l-4 border-primary-500">
            <p className="text-slate-600 text-sm font-semibold uppercase tracking-wider mb-2">
              Owner Name
            </p>
            <p className="text-2xl font-bold text-slate-900">
              {user?.firstName} {user?.lastName}
            </p>
          </div>
          <div className="card p-6 border-l-4 border-accent-500">
            <p className="text-slate-600 text-sm font-semibold uppercase tracking-wider mb-2">
              Email
            </p>
            <p className="text-lg font-semibold text-slate-900 break-all">
              {user?.email}
            </p>
          </div>
          <div className="card p-6 border-l-4 border-blue-500">
            <p className="text-slate-600 text-sm font-semibold uppercase tracking-wider mb-2">
              Status
            </p>
            <div className="flex items-center gap-2">
              <span className="inline-block w-3 h-3 bg-green-500 rounded-full animate-pulse"></span>
              <p className="text-lg font-semibold text-green-600">Active</p>
            </div>
          </div>
        </div>

        {/* Features Grid */}
        <div>
          <h3 className="text-2xl font-bold text-slate-900 mb-8">Quick Access</h3>
          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
            {features.map((feature, index) => (
              <div
                key={index}
                className="card p-8 hover:shadow-xl transition-all duration-300 transform hover:-translate-y-2 cursor-pointer group bg-gradient-to-br from-slate-50 to-white"
              >
                <div className="text-5xl mb-4 transform group-hover:scale-110 transition-transform">
                  {feature.icon}
                </div>
                <h4 className="text-xl font-bold text-slate-900 mb-2">
                  {feature.title}
                </h4>
                <p className="text-slate-600">
                  {feature.desc}
                </p>
              </div>
            ))}
          </div>
        </div>

        {/* Stats Section */}
        <div className="grid md:grid-cols-4 gap-6 mt-12">
          <div className="card p-6 bg-gradient-to-br from-blue-500 to-blue-600 text-white">
            <p className="text-blue-100 text-sm font-semibold mb-2">Total Orders</p>
            <p className="text-4xl font-bold">0</p>
          </div>
          <div className="card p-6 bg-gradient-to-br from-green-500 to-green-600 text-white">
            <p className="text-green-100 text-sm font-semibold mb-2">Revenue Today</p>
            <p className="text-4xl font-bold">$0</p>
          </div>
          <div className="card p-6 bg-gradient-to-br from-purple-500 to-purple-600 text-white">
            <p className="text-purple-100 text-sm font-semibold mb-2">Menu Items</p>
            <p className="text-4xl font-bold">0</p>
          </div>
          <div className="card p-6 bg-gradient-to-br from-orange-500 to-orange-600 text-white">
            <p className="text-orange-100 text-sm font-semibold mb-2">Staff Members</p>
            <p className="text-4xl font-bold">0</p>
          </div>
        </div>

        {/* Footer Info */}
        <div className="mt-12 p-6 bg-blue-50 border-l-4 border-blue-500 rounded-lg">
          <h4 className="text-lg font-bold text-slate-900 mb-2">💡 Pro Tip</h4>
          <p className="text-slate-700">
            Your authentication is secured with JWT tokens. Your session will expire after 24 hours for security purposes. Log out when you're done working.
          </p>
        </div>
      </main>
    </div>
  );
};

export default Dashboard;


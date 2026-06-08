import {
  EyeIcon,
  EyeSlashIcon,
} from "@heroicons/react/24/outline";

import { useState } from "react";

import { Link, useNavigate } from "react-router-dom";

import { signupUser } from "../services/auth/authService";

const Signup = () => {

  const navigate = useNavigate();

  const [showPassword, setShowPassword] =
    useState(false);

  const [showConfirmPassword,
    setShowConfirmPassword] = useState(false);

  const [loading, setLoading] = useState(false);

  const [error, setError] = useState("");

  const [formData, setFormData] = useState({
    email: "",
    firstName: "",
    lastName: "",
    phone: "",
    address: "",
    city: "",
    state: "",
    zip: "",
    referredBy: "",
    password: "",
    confirmPassword: "",
  });

  const handleChange = (e) => {

    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });

  };

  const handleSubmit = async (e) => {

    e.preventDefault();

    setError("");

    if (
      formData.password !==
      formData.confirmPassword
    ) {
      setError("Passwords do not match");
      return;
    }

    try {

      setLoading(true);

      const response =
        await signupUser(formData);

      if (!response.success) {
        setError(
          response.message ||
          "Signup Failed"
        );
        return;
      }

      alert("Account Created Successfully");

      navigate("/");

    } catch (err) {

      console.error(err);

      if (err.response?.data?.message) {
        setError(err.response.data.message);
      } else {
        setError("Server Error");
      }

    } finally {

      setLoading(false);

    }

  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-[#062b27] via-[#0d4039] to-[#041816] flex items-center justify-center p-4">

      <div className="w-full max-w-7xl bg-white rounded-3xl overflow-hidden shadow-2xl grid grid-cols-1 lg:grid-cols-2">

        {/* LEFT SIDE */}
        <div className="hidden lg:flex bg-gradient-to-br from-[#0b3b35] to-[#06211d] text-white p-14 flex-col justify-between">

          <div>

            <h1 className="text-4xl font-extrabold">
              DevMasters
              <span className="text-yellow-400">
                POS
              </span>
            </h1>

            <p className="text-gray-300 mt-3">
              Restaurant ERP System
            </p>

          </div>

          <div>

            <h2 className="text-5xl font-bold leading-tight">
              Create Your Account
            </h2>

            <p className="text-gray-300 mt-6 text-lg leading-relaxed">
              Join the modern restaurant
              management platform for POS,
              kitchen, delivery, inventory and
              analytics.
            </p>

          </div>

          <div className="text-sm text-gray-400">
            © 2026 DevMasters - All rights reserved.
          </div>

        </div>

        {/* RIGHT SIDE */}
        <div className="bg-white p-6 sm:p-10 lg:p-16 overflow-y-auto">

          <div className="w-full max-w-xl mx-auto">

            <div className="mb-8">

              <h2 className="text-4xl font-bold text-gray-800">
                Sign Up
              </h2>

              <p className="text-gray-500 mt-2">
                Create your customer account
              </p>

            </div>

            <form
              onSubmit={handleSubmit}
              className="space-y-5"
            >

              {error && (
                <div className="bg-red-100 text-red-600 px-4 py-3 rounded-xl text-sm">
                  {error}
                </div>
              )}

              <div className="grid grid-cols-1 md:grid-cols-2 gap-5">

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    First Name
                  </label>

                  <input
                    type="text"
                    name="firstName"
                    value={formData.firstName}
                    onChange={handleChange}
                    placeholder="Muhammad"
                    className="w-full border border-gray-300 rounded-2xl px-5 py-4 outline-none focus:ring-2 focus:ring-[#0d4039]"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Last Name
                  </label>

                  <input
                    type="text"
                    name="lastName"
                    value={formData.lastName}
                    onChange={handleChange}
                    placeholder="Danish"
                    className="w-full border border-gray-300 rounded-2xl px-5 py-4 outline-none focus:ring-2 focus:ring-[#0d4039]"
                    required
                  />
                </div>

              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Email Address
                </label>

                <input
                  type="email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  placeholder="example@gmail.com"
                  className="w-full border border-gray-300 rounded-2xl px-5 py-4 outline-none focus:ring-2 focus:ring-[#0d4039]"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Phone Number
                </label>

                <input
                  type="text"
                  name="phone"
                  value={formData.phone}
                  onChange={handleChange}
                  placeholder="+92 300 1234567"
                  className="w-full border border-gray-300 rounded-2xl px-5 py-4 outline-none focus:ring-2 focus:ring-[#0d4039]"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Address
                </label>

                <input
                  type="text"
                  name="address"
                  value={formData.address}
                  onChange={handleChange}
                  placeholder="Street Address"
                  className="w-full border border-gray-300 rounded-2xl px-5 py-4 outline-none focus:ring-2 focus:ring-[#0d4039]"
                  required
                />
              </div>

              <div className="grid grid-cols-1 md:grid-cols-3 gap-5">

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    City
                  </label>

                  <input
                    type="text"
                    name="city"
                    value={formData.city}
                    onChange={handleChange}
                    placeholder="Karachi"
                    className="w-full border border-gray-300 rounded-2xl px-5 py-4 outline-none focus:ring-2 focus:ring-[#0d4039]"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    State
                  </label>

                  <input
                    type="text"
                    name="state"
                    value={formData.state}
                    onChange={handleChange}
                    placeholder="Sindh"
                    className="w-full border border-gray-300 rounded-2xl px-5 py-4 outline-none focus:ring-2 focus:ring-[#0d4039]"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    ZIP
                  </label>

                  <input
                    type="text"
                    name="zip"
                    value={formData.zip}
                    onChange={handleChange}
                    placeholder="74000"
                    className="w-full border border-gray-300 rounded-2xl px-5 py-4 outline-none focus:ring-2 focus:ring-[#0d4039]"
                    required
                  />
                </div>

              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Referral Code
                </label>

                <input
                  type="text"
                  name="referredBy"
                  value={formData.referredBy}
                  onChange={handleChange}
                  placeholder="Optional"
                  className="w-full border border-gray-300 rounded-2xl px-5 py-4 outline-none focus:ring-2 focus:ring-[#0d4039]"
                />
              </div>

              {/* PASSWORDS */}
              <div className="grid grid-cols-1 md:grid-cols-2 gap-5">

                <div>

                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Password
                  </label>

                  <div className="relative">

                    <input
                      type={showPassword ? "text" : "password"}
                      name="password"
                      value={formData.password}
                      onChange={handleChange}
                      placeholder="Password"
                      className="w-full border border-gray-300 rounded-2xl px-5 py-4 pr-14 outline-none focus:ring-2 focus:ring-[#0d4039]"
                      required
                    />

                    <button
                      type="button"
                      onClick={() =>
                        setShowPassword(!showPassword)
                      }
                      className="absolute right-4 top-1/2 -translate-y-1/2 text-gray-500"
                    >

                      {showPassword ? (
                        <EyeSlashIcon className="w-6 h-6" />
                      ) : (
                        <EyeIcon className="w-6 h-6" />
                      )}

                    </button>

                  </div>

                </div>

                <div>

                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Confirm Password
                  </label>

                  <div className="relative">

                    <input
                      type={
                        showConfirmPassword
                          ? "text"
                          : "password"
                      }
                      name="confirmPassword"
                      value={formData.confirmPassword}
                      onChange={handleChange}
                      placeholder="Confirm Password"
                      className="w-full border border-gray-300 rounded-2xl px-5 py-4 pr-14 outline-none focus:ring-2 focus:ring-[#0d4039]"
                      required
                    />

                    <button
                      type="button"
                      onClick={() =>
                        setShowConfirmPassword(
                          !showConfirmPassword
                        )
                      }
                      className="absolute right-4 top-1/2 -translate-y-1/2 text-gray-500"
                    >

                      {showConfirmPassword ? (
                        <EyeSlashIcon className="w-6 h-6" />
                      ) : (
                        <EyeIcon className="w-6 h-6" />
                      )}

                    </button>

                  </div>

                </div>

              </div>

              <button
                type="submit"
                disabled={loading}
                className="w-full bg-[#0d4039] hover:bg-[#0a2d28] text-white py-4 rounded-2xl font-semibold transition-all duration-300 shadow-lg disabled:opacity-50"
              >

                {loading
                  ? "Creating Account..."
                  : "Create Account"}

              </button>

              <div className="text-center text-gray-600 pt-5">

                Already have an account?{" "}

                <Link
                  to="/"
                  className="text-[#0d4039] font-bold hover:underline"
                >
                  Sign In
                </Link>

              </div>

            </form>

          </div>

        </div>

      </div>

    </div>
  );
};

export default Signup;
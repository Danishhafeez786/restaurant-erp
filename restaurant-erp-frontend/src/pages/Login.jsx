import { EyeIcon, EyeSlashIcon } from "@heroicons/react/24/outline";
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { loginUser } from "../services/auth/authService";


const Login = () => {

  const navigate = useNavigate();

  const [showPassword, setShowPassword] = useState(false);

  const [loading, setLoading] = useState(false);

  const [error, setError] = useState("");

  const [formData, setFormData] = useState({
    email: "",
    password: "",
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

    try {

      setLoading(true);

      const response = await loginUser(formData);

      console.log("LOGIN RESPONSE:", response);

      if (!response.success) {

        setError(
          response.message || "Login Failed"
        );

        return;
      }

      // SAVE TOKENS

      localStorage.setItem( "accessToken", response.data.accessToken );
      localStorage.setItem( "refreshToken", response.data.refreshToken );

      localStorage.setItem(
        "user", JSON.stringify({
          email: response.data.email,
          fullName: response.data.fullName,
          role: response.data.role,
          permissions : response.data.permissions
        })
      );
      // REDIRECT TO DASHBOARD

      navigate("/dashboard");

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

        {/* LEFT */}
        <div className="hidden lg:flex bg-gradient-to-br from-[#0b3b35] to-[#06211d] text-white p-14 flex-col justify-between">

          <div>

            <h1 className="text-4xl font-extrabold">
              DevMasters<span className="text-yellow-400">POS</span>
            </h1>

            <p className="text-gray-300 mt-3">
              Restaurant Management System
            </p>

          </div>

          <div>

            <h2 className="text-5xl font-bold leading-tight">
              Welcome Back!
            </h2>

            <p className="text-gray-300 mt-6 text-lg leading-relaxed">
              Manage orders, kitchen, delivery, inventory,
              reservations and reports from one platform.
            </p>

          </div>

          <div className="text-sm text-gray-400">
            © 2026 DevMasters - All rights reserved.
          </div>

        </div>

        {/* RIGHT */}
        <div className="bg-white flex items-center justify-center p-6 sm:p-10 lg:p-16">

          <div className="w-full max-w-md">

            <div className="mb-10">

              <h2 className="text-4xl font-bold text-gray-800">
                Login Account
              </h2>

              <p className="text-gray-500 mt-3">
                Continue managing your restaurant.
              </p>

            </div>

            <form
              onSubmit={handleSubmit}
              className="space-y-6"
            >

              {/* ERROR */}
              {error && (
                <div className="bg-red-100 text-red-600 px-4 py-3 rounded-xl text-sm">
                  {error}
                </div>
              )}

              {/* EMAIL */}
              <div>

                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Email Address
                </label>

                <input
                  type="email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  placeholder="Enter your email"
                  className="w-full border border-gray-300 rounded-2xl px-5 py-4 outline-none focus:ring-2 focus:ring-[#0d4039]"
                  required
                />

              </div>

              {/* PASSWORD */}
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
                    placeholder="Enter your password"
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

              {/* REMEMBER */}
              <div className="flex items-center justify-between">

                <label className="flex items-center gap-2 text-sm text-gray-600">

                  <input type="checkbox" />

                  Remember Me

                </label>

                <button
                  type="button"
                  className="text-[#0d4039] font-semibold hover:underline"
                >
                  Forgot Password?
                </button>

              </div>

              {/* BUTTON */}
              <button
                type="submit"
                disabled={loading}
                className="w-full bg-[#0d4039] hover:bg-[#0a2d28] text-white py-4 rounded-2xl font-semibold transition-all duration-300 shadow-lg disabled:opacity-50"
              >

                {loading ? "Please Wait..." : "Sign In"}

              </button>

              <p className="text-sm text-gray-600 text-center">
                Don't have an account?{" "}
                <Link
                  to="/signup"
                  className="text-[#0d4039] font-semibold hover:underline"
                >
                  Sign Up
                </Link>
              </p>
            </form>

          </div>

        </div>

      </div>

    </div>
  );
};

export default Login;
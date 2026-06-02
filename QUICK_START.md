# Quick Start Guide - Restaurant ERP Authentication

## Prerequisites Check
```bash
# Check Java version (need 25+)
java -version

# Check Node/npm (need Node 18+, npm 9+)
node --version
npm --version

# Check MongoDB running
mongosh  # or mongo
```

## Step 1: Start MongoDB
```bash
# If installed locally
mongod

# Or with Docker
docker run -d -p 27017:27017 --name mongodb mongo
```

## Step 2: Start Backend
```bash
cd restaurant-erp-backend
mvn spring-boot:run
# Backend runs on http://localhost:8080
```

## Step 3: Start Frontend
In a new terminal:
```bash
cd restaurant-erp-frontend
npm install
npm run dev
# Frontend runs on http://localhost:5173
```

## Step 4: Test the Application
1. Open browser to `http://localhost:5173`
2. Click "Sign up here" to create account
3. Fill in details:
   - Email: your@email.com
   - First/Last Name: Your Name
   - Restaurant: My Restaurant
   - Phone: +1234567890
   - Password: Test123!
4. Click "Create Account"
5. You'll be logged in and see the dashboard
6. Click "Logout" to test login page

## API Testing
Test the API is working:
```bash
curl http://localhost:8080/api/auth/test
# Should return: "Authentication API is working!"
```

## File Structure Created
```
Backend Files:
✓ User.java (Entity)
✓ UserRepository.java
✓ LoginRequest.java (DTO)
✓ SignupRequest.java (DTO)
✓ AuthResponse.java (DTO)
✓ JwtTokenProvider.java (Security)
✓ AuthService.java (Service)
✓ AuthController.java (REST API)
✓ SecurityConfig.java (Configuration)
✓ pom.xml (updated with JWT)
✓ application.properties (updated)

Frontend Files:
✓ AuthContext.jsx (State Management)
✓ authApi.js (API Service)
✓ Login.jsx (Login Page)
✓ Signup.jsx (Signup Page)
✓ Dashboard.jsx (Protected Page)
✓ ProtectedRoute.jsx (Route Guard)
✓ Auth.css (Styling)
✓ Dashboard.css (Styling)
✓ App.jsx (Routing)
✓ package.json (updated)
```

## Key Endpoints
| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | /api/auth/signup | Register new user |
| POST | /api/auth/login | Login user |
| GET | /api/auth/test | Test API |

## Database Collections
MongoDB creates:
- `users` collection with:
  - email (unique index)
  - password (encrypted)
  - firstName, lastName
  - restaurantName
  - phone
  - enabled, createdAt, updatedAt

## Troubleshooting

**Problem**: Port 8080 already in use
```bash
# Change backend port in application.properties
server.port=8081
```

**Problem**: MongoDB connection refused
```bash
# Start MongoDB service
# Windows: net start MongoDB
# Mac: brew services start mongodb-community
# Docker: docker start mongodb
```

**Problem**: CORS errors
- Backend CORS is configured for http://localhost:5173 and http://localhost:3000
- Make sure you're accessing from these URLs

## Development Tips
1. Keep browser DevTools open (F12) to see errors
2. Check browser console for frontend errors
3. Check terminal for backend errors
4. Use network tab to see API calls
5. MongoDB credentials stored in browser localStorage (dev only)

## Next Phase Features
- Implement password reset
- Add email verification
- Add role-based access
- Implement token refresh
- Add audit logging

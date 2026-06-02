# Restaurant ERP - Authentication System Setup

## Overview
Complete authentication system with Spring Boot backend and React frontend, including JWT token-based authentication, CORS configuration, and protected routes.

## Backend Setup

### Prerequisites
- Java 25 or higher
- Maven 3.6+
- MongoDB 4.0+ (running locally on port 27017)

### Backend Structure
```
restaurant-erp-backend/
├── src/main/java/com/devmasters/restaurant_erp/
│   ├── controller/
│   │   └── AuthController.java          # REST endpoints
│   ├── service/
│   │   └── AuthService.java             # Business logic
│   ├── repository/
│   │   └── UserRepository.java          # MongoDB access
│   ├── entity/
│   │   └── User.java                    # User model
│   ├── dto/
│   │   ├── LoginRequest.java
│   │   ├── SignupRequest.java
│   │   └── AuthResponse.java
│   ├── security/
│   │   └── JwtTokenProvider.java        # JWT token handling
│   ├── config/
│   │   └── SecurityConfig.java          # CORS & Security
│   └── RestaurantErpApplication.java
├── src/main/resources/
│   └── application.properties
└── pom.xml
```

### Backend API Endpoints

**POST /api/auth/signup**
- Register a new restaurant user
- Request body:
```json
{
  "email": "owner@restaurant.com",
  "firstName": "John",
  "lastName": "Doe",
  "restaurantName": "My Restaurant",
  "phone": "+1234567890",
  "password": "password123",
  "confirmPassword": "password123"
}
```
- Response:
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "email": "owner@restaurant.com",
  "firstName": "John",
  "lastName": "Doe",
  "restaurantName": "My Restaurant",
  "success": true,
  "message": "User registered successfully"
}
```

**POST /api/auth/login**
- Authenticate user and get JWT token
- Request body:
```json
{
  "email": "owner@restaurant.com",
  "password": "password123"
}
```
- Response: Same as signup

**GET /api/auth/test**
- Test endpoint to verify API is working

### Running the Backend

1. Ensure MongoDB is running:
```bash
# On Windows with MongoDB installed
mongod

# Or using Docker
docker run -d -p 27017:27017 --name mongodb mongo
```

2. Navigate to backend directory:
```bash
cd restaurant-erp-backend
```

3. Build and run:
```bash
mvn clean install
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

## Frontend Setup

### Prerequisites
- Node.js 18+ and npm 9+

### Frontend Structure
```
restaurant-erp-frontend/
├── src/
│   ├── components/
│   │   └── ProtectedRoute.jsx            # Route protection wrapper
│   ├── context/
│   │   └── AuthContext.jsx               # Auth state management
│   ├── pages/
│   │   ├── Login.jsx                     # Login page
│   │   ├── Signup.jsx                    # Sign up page
│   │   └── Dashboard.jsx                 # Protected dashboard
│   ├── services/
│   │   └── authApi.js                    # API calls
│   ├── styles/
│   │   ├── Auth.css                      # Auth pages styling
│   │   └── Dashboard.css                 # Dashboard styling
│   ├── App.jsx                           # Main app with routing
│   ├── main.jsx
│   └── index.css
├── package.json
└── vite.config.js
```

### Running the Frontend

1. Install dependencies:
```bash
cd restaurant-erp-frontend
npm install
```

2. Start development server:
```bash
npm run dev
```

The frontend will be available at `http://localhost:5173`

3. Build for production:
```bash
npm run build
```

## Features

### Authentication System
- **User Registration (Signup)**
  - Email, name, restaurant name, phone registration
  - Password confirmation validation
  - Duplicate email checking
  - Password encryption using BCrypt

- **User Login**
  - Email and password authentication
  - JWT token generation (24-hour expiry)
  - Token stored in localStorage

- **Protected Routes**
  - Automatic redirect to login if not authenticated
  - Route protection with ProtectedRoute component
  - Session persistence across page refreshes

### CORS Configuration
- Allows requests from `http://localhost:5173` and `http://localhost:3000`
- Supports credentials in requests
- Allows common HTTP methods (GET, POST, PUT, DELETE)

### Security Features
- JWT token-based authentication
- Password encryption with BCrypt
- Input validation on both frontend and backend
- CORS protection
- MongoDB unique index on email

## Authentication Flow

1. **User Registration**
   - User fills signup form
   - Frontend validates input locally
   - POST request to `/api/auth/signup`
   - Backend validates data, hashes password
   - JWT token generated
   - Token and user data stored in localStorage
   - User redirected to dashboard

2. **User Login**
   - User enters credentials
   - POST request to `/api/auth/login`
   - Backend verifies credentials
   - JWT token generated
   - Token and user data stored in localStorage
   - User redirected to dashboard

3. **Protected Navigation**
   - Every dashboard access checked via ProtectedRoute
   - If no token/user, redirect to login
   - Token persists across browser refreshes

4. **Logout**
   - Clear localStorage
   - Clear auth state
   - Redirect to login page

## Configuration

### Backend (application.properties)
```properties
# JWT Settings (in production, use environment variables)
app.jwtSecret=restaurant-erp-super-secret-key-change-this-in-production-use-environment-variable
app.jwtExpirationMs=86400000  # 24 hours

# Server
server.port=8080

# MongoDB
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=restaurant_db
```

### Frontend (API URLs)
Located in `src/services/authApi.js`:
```javascript
const API_BASE_URL = 'http://localhost:8080/api/auth';
```

## Testing the System

### Using Postman or cURL

1. **Test API Connection**
```bash
curl http://localhost:8080/api/auth/test
```

2. **Signup**
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@restaurant.com",
    "firstName": "Test",
    "lastName": "User",
    "restaurantName": "Test Restaurant",
    "phone": "+1234567890",
    "password": "Test123!",
    "confirmPassword": "Test123!"
  }'
```

3. **Login**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@restaurant.com",
    "password": "Test123!"
  }'
```

### Using the Frontend
1. Navigate to `http://localhost:5173`
2. Click "Sign up here" on login page
3. Fill signup form and create account
4. You'll be redirected to dashboard
5. Click "Logout" to logout

## Common Issues & Solutions

### Issue: CORS Error
**Solution**: Ensure backend is running and CORS configuration includes your frontend URL in `SecurityConfig.java`

### Issue: "Invalid email or password" on login
**Solution**: Verify MongoDB is running and user was created successfully. Check credentials match exactly.

### Issue: Token validation fails
**Solution**: Ensure `app.jwtSecret` is same in production. Token expires after 24 hours.

### Issue: Cannot connect to MongoDB
**Solution**: Start MongoDB service:
```bash
# Windows
net start MongoDB

# Mac
brew services start mongodb-community

# Docker
docker start mongodb
```

## Next Steps

1. **Add Password Reset**: Implement forgot password functionality
2. **Add Email Verification**: Send confirmation email on signup
3. **Add 2FA**: Implement two-factor authentication
4. **Role-Based Access**: Add admin and staff roles
5. **Token Refresh**: Implement refresh token mechanism
6. **Profile Management**: Allow users to update their profile
7. **Audit Logging**: Log all authentication events

## Environment Variables (Production)

For production deployment, move sensitive values to environment variables:

**Backend:**
- `JWT_SECRET` - Replace `app.jwtSecret`
- `JWT_EXPIRATION_MS` - Replace `app.jwtExpirationMs`
- `MONGODB_HOST` - MongoDB host
- `MONGODB_PORT` - MongoDB port
- `MONGODB_DATABASE` - Database name

**Frontend:**
- `VITE_API_BASE_URL` - Backend API URL

## License
MIT

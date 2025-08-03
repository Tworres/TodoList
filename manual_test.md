# Manual Testing for JWT Authentication

This document provides instructions for manually testing the JWT authentication system using curl commands.

## Prerequisites

- The TodoList application is running on `http://localhost:8080`
- curl is installed on your system

## Test Steps

### 1. Register a new user

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Test User\",\"email\":\"test@example.com\",\"password\":\"password123\"}"
```

Expected response (status 201 Created):
```json
{
  "id": 1,
  "name": "Test User",
  "email": "test@example.com"
}
```

### 2. Login with the registered user

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"test@example.com\",\"password\":\"password123\"}" \
  -v
```

Expected response (status 200 OK):
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "email": "test@example.com",
  "name": "Test User"
}
```

The `-v` flag will show the response headers, which should include the JWT cookie.

### 3. Access protected endpoint without authentication

```bash
curl -X GET http://localhost:8080/api/todos
```

Expected response (status 401 Unauthorized):
```json
{
  "error": "Unauthorized"
}
```

### 4. Access protected endpoint with token in Authorization header

Replace `YOUR_TOKEN` with the token received from the login response:

```bash
curl -X GET http://localhost:8080/api/todos \
  -H "Authorization: Bearer YOUR_TOKEN"
```

Expected response (status 200 OK):
```json
[]
```

### 5. Create a todo item (authenticated)

```bash
curl -X POST http://localhost:8080/api/todos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d "{\"title\":\"Test Todo\",\"description\":\"This is a test todo item\"}"
```

Expected response (status 201 Created):
```json
{
  "id": 1,
  "title": "Test Todo",
  "description": "This is a test todo item",
  "completed": false,
  "createdAt": "...",
  "updatedAt": null,
  "completedAt": null
}
```

### 6. Access protected endpoint with cookie authentication

If you have a browser or a tool that supports cookies, you can also test cookie-based authentication by:

1. Login using a browser or a tool that supports cookies
2. Access the protected endpoint without explicitly providing the Authorization header

The JWT cookie set during login should be automatically sent with the request, allowing access to the protected endpoint.

### 7. Try to register with the same email (should fail)

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Test User\",\"email\":\"test@example.com\",\"password\":\"password123\"}"
```

Expected response (status 400 Bad Request):
```json
{
  "error": "Email already in use: test@example.com"
}
```

### 8. Try to login with wrong password (should fail)

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"test@example.com\",\"password\":\"wrongpassword\"}"
```

Expected response (status 401 Unauthorized):
```json
{
  "error": "Invalid email or password"
}
```
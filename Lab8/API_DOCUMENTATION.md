# Customer API Documentation
Base URL
http://localhost:8080/api/customers

## 1. Endpoints
### 1.1 Get All Customers
Retrieve a paginated list of customers.

URL: /api/customers

Method: GET

Query Params:

page (int, optional, default=0): Page number.

size (int, optional, default=10): Items per page.

sortBy (string, optional, default="id"): Field to sort by.

sortDir (string, optional, default="asc"): "asc" or "desc".

Response: 200 OK

JSON

{
    "customers": [
        {
            "id": 1,
            "customerCode": "C001",
            "fullName": "John Doe",
            "email": "john.doe@example.com",
            "phone": "+1-555-0101",
            "address": "123 Main St, New York",
            "status": "ACTIVE",
            "createdAt": "2024-12-11T10:00:00"
        },
        {
            "id": 2,
            "customerCode": "C002",
            "fullName": "Jane Smith",
            "email": "jane.smith@example.com",
            "phone": "+1-555-0102",
            "address": "456 Oak Ave, Los Angeles",
            "status": "ACTIVE",
            "createdAt": "2024-12-11T10:05:00"
        }
    ],
    "currentPage": 0,
    "totalItems": 50,
    "totalPages": 25
}
### 1.2 Get Customer by ID
Retrieve details of a specific customer.

URL: /api/customers/{id}

Method: GET

Response: 200 OK

JSON

{
    "id": 1,
    "customerCode": "C001",
    "fullName": "John Doe",
    "email": "john.doe@example.com",
    "phone": "+1-555-0101",
    "address": "123 Main St, New York",
    "status": "ACTIVE",
    "createdAt": "2024-12-11T10:00:00"
}
### 1.3 Create Customer
Create a new customer profile.

URL: /api/customers

Method: POST

Body:

JSON

{
    "customerCode": "C005",
    "fullName": "Alice Wonderland",
    "email": "alice.w@example.com",
    "phone": "+1-555-9988",
    "address": "123 Rabbit Hole, Oxford",
    "status": "ACTIVE"
}
Response: 201 Created

JSON

{
    "id": 5,
    "customerCode": "C005",
    "fullName": "Alice Wonderland",
    "email": "alice.w@example.com",
    "phone": "+1-555-9988",
    "address": "123 Rabbit Hole, Oxford",
    "status": "ACTIVE",
    "createdAt": "2024-12-11T12:00:00"
}
### 1.4 Update Customer
Update an existing customer (Full Update).

URL: /api/customers/{id}

Method: PUT

Body: same as POST.

Response: 200 OK (Returns the updated customer object)

### 1.5 Partial Update (PATCH)
Update specific fields of a customer.

URL: /api/customers/{id}

Method: PATCH

Body:

JSON

{
    "address": "Updated Address Only"
}
Response: 200 OK (Returns the updated customer object with new address)

### 1.6 Delete Customer
Remove a customer from the system.

URL: /api/customers/{id}

Method: DELETE

Response: 200 OK

JSON

{
    "message": "Customer deleted successfully"
}
## 2. Error Responses
### 400 Bad Request (Validation Error)
Occurs when required fields are missing or format is incorrect (e.g., invalid email).

JSON

{
    "timestamp": "2024-12-11T12:05:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Validation Failed",
    "path": "/api/customers",
    "details": [
        "email: Invalid email format",
        "fullName: Full name is required"
    ]
}
### 404 Not Found
Occurs when requesting a resource ID that does not exist.

JSON

{
    "timestamp": "2024-12-11T12:06:00",
    "status": 404,
    "error": "Not Found",
    "message": "Customer not found with id: 999",
    "path": "/api/customers/999",
    "details": null
}
### 409 Conflict
Occurs when trying to create a resource with a unique field that already exists (e.g., email or customerCode).

JSON

{
    "timestamp": "2024-12-11T12:07:00",
    "status": 409,
    "error": "Conflict",
    "message": "Email already exists: john.doe@example.com",
    "path": "/api/customers",
    "details": null
}
### 500 Internal Server Error
Occurs when an unexpected system error happens (e.g., database connection failure).

JSON

{
    "timestamp": "2024-12-11T12:08:00",
    "status": 500,
    "error": "Internal Server Error",
    "message": "An unexpected error occurred",
    "path": "/api/customers",
    "details": null
}
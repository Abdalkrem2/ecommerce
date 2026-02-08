# E-Commerce Backend System

A scalable backend for an e-commerce platform built using Java and Spring Boot.

## Tech Stack
- Java
- Spring Boot
- Spring Data JPA
- MySQL
- Maven

## Features
- User authentication and authorization
- Product management
- Order management
- RESTful API design
- Layered architecture (Controller, Service, Repository)
- DTO mapping and validation

## Project Structure
- Controller layer: handles HTTP requests
- Service layer: business logic
- Repository layer: database operations

## How to Run
1. Clone the repository:
   git clone https://github.com/Abdalkrem2/ecommerce.git

2. Open the project in IntelliJ or Eclipse.

3. Configure MySQL in `application.properties`.

4. Run the main Spring Boot application.

## API Endpoints 

### Authentication
- POST /api/auth/signin → User login
- POST /api/auth/signup → User registration
- GET /api/auth/username → Get current username
- GET /api/auth/user → Get current user details
- POST /api/auth/signout → User logout

### Categories
- POST /api/admin/categories → Create category
- GET /api/public/categories → Get all categories
- PUT /api/admin/categories/{id} → Update category
- DELETE /api/admin/categories/{id} → Delete category

### Products
- POST /api/admin/categories/{categoryId}/products → Create product
- GET /api/public/products → Get all products
- GET /api/public/products/{id} → Get product by ID
- GET /api/public/categories/{categoryId}/products → Get products by category
- GET /api/public/products/search → Search products
- GET /api/public/categories/1/products/search → Search product by category
- PUT /api/admin/products/{id} → Update product
- DELETE /api/admin/products/{id} → Delete product

### Product Images
- POST /api/admin/products/{productId}/images → Upload image
- GET /api/admin/products/{productId}/images → Get product images
- PATCH /api/admin/products/{productId}/images/{imageId} → Update image
- PATCH /api/admin/products/{productId}/images/{imageId}/primary → Set primary image
- DELETE /api/admin/products/{productId}/images/{imageId} → Delete image

### Address
- POST /api/user/addresses → Create address
- GET /api/user/addresses/{addressId} → Get address by ID
- GET /api/user/addresses → Get user addresses
- PUT /api/user/addresses/{addressId} → Update address
- DELETE /api/user/addresses/{addressId} → Delete address

### Cart
- POST /api/cart/items → Add item to cart
- GET /api/cart/items → Get current user cart
- PUT /api/cart/items/{itemId} → Update item quantity
- DELETE /api/cart/items/{itemId} → Remove item from cart
- DELETE /api/cart → Clear cart

### Orders
- GET /api/orders → Get user orders
- GET /api/orders/{id} → Get order by ID
- POST /api/orders → Place new order

  




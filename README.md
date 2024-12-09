# Order management API

## Execution
Just type docker-compose up -d in a terminal where you have Docker and Docker Compose downloaded, this will create the app image using the Dockerfile (as it's not uploaded to any Docker repository).

## Structure
This project consist on a multi-module Maven project, there is a single Spring Boot application but uses many modules.
Every module except the application one is structured using a clean architecture, hexagonal architecture in this case.
In this way, we can change the different adapters like the database and the number of changes will be minor.
Apart from that, the understanding of the code is better, as it tells to the reader (developers) the actions.

### Application
Exposes all the endpoints, defines the application configuration (like security) and handles the different exceptions
It depends on the main modules orders and auth (bounded contexts).
The routes are exposed using openapi and generating the API interfaces from the yaml definitions.

### Modules

#### Shared
Contains common utilities for all modules

#### Auth
Contains all the logic related to authentication

####  Orders
Logic associated to the orders. Depends on the modules products and payments

####  Products
Logic of the products. Depends on the module categories

####  Categories
Logic associated to the categories

####  Payments
Logic associated to the payments (it can be useful in the future)

## Endpoints

### POST /api/v1/auth/register
It creates users and then generates the access token

### POST /api/v1/auth/login
It generates the access token from existing user credentials

### GET /api/v1/categories
It retrieves the different categories

### GET /api/v1/products
It retrieves the different products

### POST /api/v1/orders
It creates an order based on the products informed (if any) and the seat

### POST /api/v1/orders/{{orderId}}/products
It allows us to add products to an existing order

### POST /api/v1/orders/{{orderId}}/finish
It allows us to finish an order

### POST /api/v1/orders/{{orderId}}/cancel
It allows us to cancel an order

### DELETE /api/v1/orders/{{orderId}}/products/{{productId}}
It allows us to delete an existing product from an existing order

### PATCH /api/v1/orders/{{orderId}}/buyer-details
It allows us to update the buyer details of an order

## Persistence
The database used in this case is MongoDB, which is easy to configure and we reduce the time spent on the data structure.
This kind of no-relation database fits more with the agile methodology.
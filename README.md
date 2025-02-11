# Shop Product Management

A modular Java-based web application for managing products in an online store. The project includes three services:

- __Catalogue Service__: A REST API for managing the product catalog (the __catalogue-service__ module).
  
    Built with: Spring Boot, Spring Security, JPA, and PostgreSQL.
- __Manager App__: A web-based application for administrators to manage products (add, update, delete, attach photos) (the __manager-app__ module).

    Built with: Spring Boot, Spring Security, Thymeleaf, PostgreSQL, and a REST client.
- __Customer App__: A web-based application for customers to browse and view products with photos (the __customer-app__ module).

    Built with: Spring Boot, Thymeleaf, and a REST client.

## Features

REST API for product catalog management (CRUD operations).

Authentication and Authorization using Spring Security: 
- Interservice authorization to protect REST API;
- Authorization for managers to protect changing the product catalogue by unauthorized users.

Role-based access:
- Manager: Full product management (add, update, delete, view).
- Customer: View-only access to products.
- Modular design with independent services communicating via REST.

## Getting started

Follow the steps below to set up and run the project locally.

### Prerequisites

- Java 21 or higher;
- Apache Maven;
- PostgreSQL database
- An IDE (e.g., IntelliJ IDEA).

### Installation and Setup

1. Clone the repository to your local machine. Open the terminal, go to the folder you want to save this project in, and give the command:

```
https://github.com/korneevdi/Online-Shop.git
```

2. Set up the database for the product catalogue. This is the database for your products.
    - Create a PostgreSQL database named __catalogue__.
    - Update the database credentials in the _application.yaml_ file located in the _catalogue-service_ module:

```
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/catalogue
    username: your_db_username
    password: your_db_password
```

3. Set up the database for managers. This database stores the manager information: roles, usernames, and passwords.
    - Create a PostgreSQL database named __manager__.
    - Update the database credentials in the _application.yaml_ file located in the _manager-app_ module:

```
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/catalogue
    username: your_db_username
    password: your_db_password
```

### Build the project

Run command 

```
mvn clean package
```

or

```
mvn clean install
```

in the project root directory.

### Start the services

Each of the three services (catalogue-service, manager-app, and customer-app) must be run independently. You can run them by clicking the green button in the corresponding classes:

- __CatalogueServiceApplication__ for the _catalogue-service_ module;
- __ManagerApplication__ for the _manager-app_ module;
- __CustomerApplication__ for the _customer-app_ module.

Another option to run the services is to use Maven and run the command

```
mvn spring-boot:run
```

being located in the corresponding module directory. If necessary, you can edit configuration of the application services following _Run -> Edit configuration_.

By default, the services will run on the following ports:

- catalogue-service: http://localhost:8081
- manager-app: http://localhost:8080
- customer-app: http://localhost:8082

To see the databases in your IntelliJ IDEA, you can add data sources on the top right next to the maven icon. 

### Testing the application

1. As a manager
    - Go to http://localhost:8080.
    - Log in using the username and the password. You can set them in the _manager_ database.
    - Manage products (add, update, delete, view).

2. As a customer
    - Go to http://localhost:8082.
    - Browse and view the product catalog (read-only access).

### How to set username and password for manager

The __manager__ database contains 3 tables: _t_authority_, _t_user_, and _t_user_authority_.

The _t_authority_ table stores the manager authority, i.e. their role. You can set "ROLE_MANAGER" in the _c_authority_ column.

The _t_user_ table stores the manager's username and password. Here you can set them. It is better to use a [bcrypt generator](https://bcrypt-generator.com/) to encode passwords in the database. If you do it, just write

```
{bcrypt}<you password>
```

in the _c_password_ column.

The third table, _t_user_authority_, joins the first and the second tables using the manager id and the authority id.

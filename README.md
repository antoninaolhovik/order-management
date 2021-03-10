# Order Management
Spring-boot application with REST API.
Contains CRUD methods for Category, Product, Order and OrderItem. 
Integrated with ElasticSearch

- Spring-boot service application with REST CRUD methods for the following entities: Category, Product, Order and OrderItem. 
Category contains: name and a list of products. 
Product contains: price, sku and name.
Order Item contains: “quantity” value and has connection to Product.
Order contains: a list of order items and a total amount of the order.
- report controller, which returns date and sum of income for each day in JSON format (2016-08-22: 250.65, 2016-08.23: 571.12 ... etc).
- ElasticSearch integration and  “search” method, which returns the list of orders by part of the product name. 
Technology stack: Spring-boot, Hibernate, Liquibase, PostgreSQL.
Will be a plus: 
add corresponding tests to REST methods;
add integration with Swagger.


DROP TABLE IF EXISTS Order_Items;
DROP TABLE IF EXISTS Orders;
DROP TABLE IF EXISTS Products;
DROP TABLE IF EXISTS Customers;
DROP TABLE IF EXISTS Employees;
DROP TABLE IF EXISTS Category;
DROP TABLE IF EXISTS Suppliers;

CREATE TABLE Customers(
    customer_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    city VARCHAR(255),
    registration_date DATE
);

CREATE TABLE Category(
    category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(255)
);

CREATE TABLE Suppliers(
    supplier_id BIGINT AUTO_INCREMENT PRIMARY KEY ,
    supplier_name VARCHAR(255),
    country VARCHAR(255)
);

CREATE TABLE Products(
    product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id BIGINT,
    supplier_id BIGINT,
    product_name VARCHAR(255),
    price INT,
    stock INT,

    CONSTRAINT fk_products_category FOREIGN KEY (category_id) REFERENCES Category(category_id),
    CONSTRAINT fk_products_supplier FOREIGN KEY (supplier_id) REFERENCES Suppliers(supplier_id)
);

CREATE TABLE Employees(
    employee_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_name VARCHAR(255),
    department VARCHAR(255)
);

CREATE TABLE Orders(
    order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT,
    employee_id BIGINT,
    order_date DATE,
    status VARCHAR(255) NOT NULL,

    CONSTRAINT fk_orders_customer FOREIGN KEY (customer_id) REFERENCES Customers(customer_id),
    CONSTRAINT fk_order_employee FOREIGN KEY (employee_id) REFERENCES Employees(employee_id)
);

CREATE TABLE Order_Items(
    order_item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT,
    product_id BIGINT,
    quantity INT,
    discount INT,

    CONSTRAINT fk_order_item_orders FOREIGN KEY (order_id) REFERENCES Orders(order_id),
    CONSTRAINT fk_order_item_product FOREIGN KEY (product_id) REFERENCES Products(product_id)
);
INSERT INTO Customers(name, city, registration_date)
VALUES ('Alice Johnson', 'New York', '2024-01-15'),
       ('Bob Smith', 'Chicago', '2024-02-10'),
       ('Carol White', 'Boston', '2024-03-05'),
       ('Daniel Green', 'Seattle', '2024-04-12'),
       ('Eva Black', 'Miami', '2024-06-01'),
       ('Frank Stone', 'Dallas', '2024-07-20'),
       ('Grace Hill', 'Austin', '2024-08-11'),
       ('Henry Ford', 'Denver', '2024-09-05');

INSERT INTO Category(category_name)
VALUES ('Electronics'),
       ('Books'),
       ('Home'),
       ('Sports');

INSERT INTO Suppliers(supplier_name, country)
VALUES ('TechSupply', 'USA'),
       ('BookWorld', 'UK'),
       ('HomeGoods', 'Germany'),
       ('SportLine', 'Italy');

INSERT INTO Products(category_id, supplier_id, product_name, price, stock)
VALUES (1, 1, 'Laptop', 1200, 50),
       (1, 1, 'Wireless Mouse', 40, 500),
       (1, 1, 'Keyboard', 60, 400),

       (2, 2, 'DB Book', 70, 300),
       (2, 2, 'Algorithms Book', 80, 250),

       (3, 3, 'Desk Lamp', 35, 600),
       (3, 3, 'Office Chair', 120, 200),

       (4, 4, 'Football', 25, 800),
       (4, 4, 'Tennis Racket', 150, 300);

INSERT INTO Employees(employee_name, department)
VALUES ('Emma Brown', 'Sales'),
       ('David Miller', 'Support'),
       ('John Carter', 'Sales'),
       ('Anna Grey', 'Sales'),
       ('Luke White', 'Sales');

INSERT INTO Orders(customer_id, employee_id, order_date, status)
VALUES (1, 1, '2025-05-01', 'Processing'),
       (2, 2, '2025-05-02', 'Delivered'),
       (3, 3, '2025-05-03', 'Delivered'),
       (4, 1, '2025-05-04', 'Processing'),
       (5, 2, '2025-05-05', 'Delivered'),
       (6, 3, '2025-05-06', 'Delivered'),
       (7, 4, '2025-05-07', 'Processing'),
       (8, 5, '2025-05-08', 'Delivered'),
       (1, 2, '2025-05-09', 'Delivered'),
       (2, 1, '2025-05-10', 'Processing'),
       (3, 3, '2025-05-11', 'Delivered'),
       (4, 4, '2025-05-12', 'Delivered'),
       (5, 5, '2025-05-13', 'Processing'),
       (6, 1, '2025-05-14', 'Delivered'),
       (7, 2, '2025-05-15', 'Delivered'),
       (8, 3, '2025-05-16', 'Delivered');

INSERT INTO Order_Items(order_id, product_id, quantity, discount)
VALUES (1, 1, 30, 0),
       (2, 1, 40, 0),
       (3, 2, 60, 0),
       (4, 2, 70, 0),
       (5, 3, 80, 0),
       (6, 3, 90, 0),
       (7, 1, 50, 0),

       (8, 4, 20, 0),
       (9, 4, 30, 0),
       (10, 5, 25, 0),
       (11, 5, 35, 0),
       (12, 4, 40, 0),

       (13, 6, 50, 0),
       (14, 6, 60, 0),
       (15, 7, 70, 0),
       (16, 7, 80, 0),

       (1, 8, 90, 0),
       (2, 8, 100, 0),
       (3, 9, 110, 0),
       (4, 9, 120, 0),
       (5, 8, 60, 0);
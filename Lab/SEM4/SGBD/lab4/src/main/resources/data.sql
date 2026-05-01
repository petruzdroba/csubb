INSERT INTO departments (name)
VALUES ('HR'),
       ('IT'),
       ('Sales'),
       ('Marketing'),
       ('Accounting'),
       ('Legal'),
       ('Operations'),
       ('Design'),
       ('Product'),
       ('Security');

SET SESSION cte_max_recursion_depth = 10001;

INSERT INTO employees (name, email, salary, department_id)
WITH RECURSIVE seq AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1 FROM seq WHERE n < 10000
)
SELECT
    CONCAT('Employee_', n),
    CONCAT('employee', n, '@company.com'),
    ROUND(30000 + (RAND() * 70000), 2),
    (MOD(n, 10) + 1)
FROM seq;
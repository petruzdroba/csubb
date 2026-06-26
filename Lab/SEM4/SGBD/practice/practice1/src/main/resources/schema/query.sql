SELECT S.supplier_name,
       S.country,
       COUNT(P.product_id) AS quantity_sold,
       SUM(P.stock)        AS total_stock,
       AVG(P.price)        AS average_price
FROM Suppliers S
         JOIN Products P
              ON P.supplier_id = S.supplier_id
         JOIN Order_Items OI
              ON OI.product_id = P.product_id
GROUP BY S.supplier_id,
         S.supplier_name,
         S.country
HAVING SUM(OI.quantity) > 100
   AND COUNT(DISTINCT P.product_id) > 2
ORDER BY quantity_sold DESC;


SELECT C.city,
       COUNT(DISTINCT O.order_id) AS total_orders,
       SUM(P.price * OI.quantity) AS total_revenue
FROM Customers C
         JOIN Orders O
              ON O.customer_id = C.customer_id
        JOIN Order_Items OI
                    ON OI.order_id = O.order_id
        JOIN Products P
                    ON P.product_id = OI.product_id
GROUP BY C.city
HAVING total_revenue > 300;
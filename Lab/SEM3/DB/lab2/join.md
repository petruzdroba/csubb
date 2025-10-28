| Join Type   | Rows Returned                              | Missing Matches Become |
|--------------|---------------------------------------------|------------------------|
| **INNER JOIN** | Only matching rows in both tables           | Omitted entirely       |
| **LEFT JOIN**  | All rows from the left table + matches      | NULLs on the right     |
| **RIGHT JOIN** | All rows from the right table + matches     | NULLs on the left      |

SELECT c.nume_clasa,
       AVG(n.nota)               AS media_generala,
       COUNT(DISTINCT n.id_nota) AS nr_note,
       p.nume AS diriginte
FROM Clase c
         JOIN Profesori p
              ON p.id_profesor = c.id_diriginte
         JOIN Elevi e
              ON c.id_clasa = e.id_clasa
         JOIN Note n
              ON n.id_elev = e.id_elev
         JOIN Materii m
              ON m.id_materie = n.id_materie
WHERE m.denumire LIKE 'N%'
GROUP BY c.id_clasa, c.nume_clasa, p.id_profesor, p.nume
HAVING COUNT(n.id_nota) > 20;


EXPLAIN WITH Elevi_notati AS (
    SELECT e.id_elev,
           e.id_clasa,
           n.id_nota,
           n.id_materie,
           n.nota
    FROM Elevi e
             JOIN Note n ON n.id_elev = e.id_elev
)
SELECT c.nume_clasa,
       p.nume AS diriginte,
       COUNT(DISTINCT en.id_elev) AS elevi_notati,
       COUNT(DISTINCT en.id_materie) AS materii_notate,
       COUNT(DISTINCT en.id_nota) AS total_note,
       AVG(en.nota) AS media
FROM Clase c
         JOIN Profesori p ON p.id_profesor = c.id_diriginte
         JOIN Elevi_notati en ON en.id_clasa = c.id_clasa
GROUP BY c.id_clasa, c.nume_clasa, p.nume
HAVING COUNT(DISTINCT en.id_materie) > 3
   AND AVG(en.nota) > 5;
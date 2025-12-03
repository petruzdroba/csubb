SET enable_seqscan = OFF; -- so it forces it to use indexes
SET enable_seqscan = ON;

CREATE INDEX IX_Categorie_nume_asc_seniori_minori
ON CategoriiVizitatori (nume ASC)
WHERE nume IN ('Seniori', 'Minori');
EXPLAIN ANALYSE SELECT * FROM CategoriiVizitatori WHERE nume='Seniori' OR nume='Minori';

CREATE INDEX IX_Categorie_nume_ask ON CategoriiVizitatori (nume);
SELECT * FROM Sectiune WHERE nume LIKE 'C%';

SELECT * FROM Sectiune WHERE nume LIKE '%_%n';

CREATE INDEX IX_Nota_cod_v_null ON Nota (cod_v);
SELECT V.id, V.nume, V.email 
FROM Vizitatori V
LEFT JOIN Nota n ON V.id = n.cod_v 
WHERE n.cod_v IS NULL;

CREATE INDEX idx_nota_cod_v_inc
ON Nota(cod_v)
INCLUDE (nota, cod_a);

SELECT v.nume, n.nota, a.nume
FROM Vizitatori v
JOIN Nota n ON v.id = n.cod_v
JOIN Atractie a ON n.cod_a = a.id;

-- uses idx_nota_cod_v_inc and IX_Nota_cod_v_null

SELECT v.nume, COUNT(cod_v)
FROM Vizitatori v
LEFT JOIN Nota n ON n.cod_v = v.id
GROUP BY v.id, v.nume;


SELECT DISTINCT n.nota
FROM Nota n
WHERE nota IN (SELECT nota FROM Nota);

SELECT * FROM Persoane;
SELECT nume, prenume FROM Persoane WHERE localitate='Sibiu';
ALTER TABLE Persoane
ADD CONSTRAINT pk_Persoane PRIMARY KEY (cod_p);
SELECT * FROM Persoane;
SELECT * FROM Persoane WHERE cod_p=10;
SELECT * FROM Persoane WHERE localitate='Sibiu';
SELECT nume, prenume, localitate FROM Persoane WHERE localitate='Sibiu';
CREATE INDEX IX_Persoane_localitate_asc_nume_asc_prenume_asc ON Persoane
(localitate ASC, nume ASC, prenume ASC);
SELECT nume, prenume, localitate FROM Persoane WHERE localitate='Sibiu';
--se foloseste indexul pentru sortare
SELECT nume, prenume, localitate FROM Persoane ORDER BY localitate, nume;
--nu se foloseste indexul pentru sortare
SELECT nume, prenume, localitate FROM Persoane ORDER BY nume, localitate;
--index nonclustered unic
CREATE UNIQUE INDEX IX_Persoane_email_asc_uq ON Persoane (email ASC);
--index filtered
CREATE INDEX IX_Persoane_localitate_asc_email_asc_data_nasterii_asc ON Persoane
(localitate ASC, email ASC, data_nasterii ASC)
WHERE localitate='Sibiu';
--se foloseste indexul filtered
SELECT email, data_nasterii, localitate FROM Persoane WHERE localitate='Sibiu';
--nu se foloseste indexul filtered
SELECT email, data_nasterii, localitate FROM Persoane WHERE localitate='Tulcea';
--index nonclustered cu clauza INCLUDE
CREATE INDEX IX_Persoane_nume_asc_prenume_asc_data_nasterii_email ON Persoane
(nume ASC, prenume ASC) INCLUDE (data_nasterii, email);
SELECT nume, prenume, data_nasterii FROM Persoane WHERE nume='Pop';
--stergerea unui index
DROP INDEX IX_Persoane_nume_asc_prenume_asc_data_nasterii_email ON Persoane;
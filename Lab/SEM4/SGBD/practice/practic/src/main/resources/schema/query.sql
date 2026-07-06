explain
select COUNT(distinct p.id_proiectie) AS proiectii_c,
       COUNT(distinct b.id_bilete)    AS bielete_c,
       AVG(p.pret_bilet)              as pret_m
from sali s
         JOIN proiectii p on p.id_sala = s.id_sala
         JOIN bilete b on b.id_proiectie = p.id_proiectie
GROUP BY p.id_proiectie
HAVING COUNT(distinct b.id_bilete) > 100;


explain
select count(distinct p.id_proiectie) as proiectii_c,
       count(distinct f.titlu)        as filme_c,
       count(distinct b.id_bilete)    as bilete_c,
       sum(p.pret_bilet)              as venit_s,
       count(distinct b.id_client)    as clienti_c
from angajati a
         join programariAngajati pa on pa.id_angajat = a.id_angajat
         join proiectii p on p.id_proiectie = pa.id_proiectie
         join filme f on f.id_film = p.id_film
         join bilete b on b.id_proiectie = p.id_proiectie
         join clienti c on b.id_client = c.id_client
having count(distinct f.titlu) > 2
   and sum(p.pret_bilet) > 4000;
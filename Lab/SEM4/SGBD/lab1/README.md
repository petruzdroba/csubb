Cum gestionam link-ul la baza de date ?

Folosim o clasa de tipul DatabaseConnection -> singleton
attrb : -user:String, -password:String, -link:String metode: +getConnection() : Connection

Fiecare Obiect de tipul Respositori implementeaza o interfata de baza cu metodele si detine un DataBaseConnection -> shared amongst all of the repos
| 1:N | Park | Trails | A park has many trails |
| M:N | Trail | Tags | TrailTags |

create table clienti(
    id_client BIGINT auto_increment  primary key,
    nume varchar(255),
    prenume varchar(255),
    email varchar(255),
    telefon varchar(255)
);

create table filme(
    id_film bigint auto_increment primary key ,
    titlu varchar(255),
    gen varchar(255),
    durata  int,
    an_lansare int
);

create table sali(
    id_sala bigint auto_increment primary key,
    nume_sala varchar(255),
    capacitate int
);

create table proiectii(
    id_proiectie bigint primary key auto_increment,
    id_film bigint,
    id_sala bigint,
    data_ora datetime,
    pret_bilet int
);

create table bilete(
    id_bilete bigint primary key  auto_increment,
    id_proiectie bigint,
    id_client bigint,
    data_cumparare date
);

create table angajati(
    id_angajat bigint primary key auto_increment,
    nume varchar(255),
    functie varchar(255),
    salariu int
);

create table programariAngajati(
    id_angajat bigint,
    id_proiectie bigint)
#pragma once

#include "Medicament.h"

typedef struct
{
    Medicament *medicamente;
    int lungime;
    int capacitate;
} ListaMedicamente;

ListaMedicamente constructorListaMedicamente();
/*
    Functie care creeaza o Lista de Medicamente goala
    return : Lista de medicamente
*/

void eliminaListaMedicamente(ListaMedicamente *farmacie);
/*
    Functie care elimina fiecare medicament din lista
    return : void
*/

int adaugaMedicamentRepo(ListaMedicamente *farmacie, Medicament *medicament);
/*
    Functie care adauga un medicament validat in lista
        medicamnent : Medicament
    return:
        - 1, medicamentul a fost adaugat cu succes
        - 0, codul medicamentului exista deja, stocul a fost actualizat
        -2, capacitatea a fost intrecuta
*/

int modificareMedicamentRepo(ListaMedicamente *farmacie, Medicament *medicament);
/*
    Functie care modifica un medicament din lista
        medicament : Medicametn
        return:
        - 1, medicamentul a fost modificat cu succes
        - 0, codul medicamentului nu exista
*/

int stergereMedicamentRepo(ListaMedicamente *farmacie, int pozitie);
/*
    Functie care sterge un medicament din lista
        medicament : Medicametn
        return:
        - 1, medicamentul a fost sters cu succes
        - 0, codul medicamentului nu exista
*/

void redimensionareLista(ListaMedicamente* farmacie);

int compareName( const Medicament* a, const Medicament* b);

int compareCantitateCrescator(const Medicament* a, const Medicament* b);

int compareCantitateDescrescator(const Medicament* a, const Medicament* b);
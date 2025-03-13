#pragma once

typedef struct
{
    int cod;
    char *nume;
    int cantitate;
    int concentratie;
} Medicament;

Medicament *constructorMedicament(int cod, const char *nume, int cantitate, int concentratie);
/*
    Functie care creeeaza un medicament
        cod: int > 0
        nume : string != ""
        cantitate: int >0
        concentratie: int >0
    return: un struct de tipul medicament cu informatiile date
*/

int validareMedicament(int cod, const char *nume, int cantitate, int concentratie);
/*
    Functie care valideaza un medicament
        cod: int > 0
        nume : string != ""
        cantitate: int >0
        concentratie: int >0
    return: bool - 0 datele nu sunt valide , 1 - datele sunt valide
*/

void eliminaMedicament(Medicament *medicament);
/*
    Functie care elimina un medicament
        medicament: Medicament
*/
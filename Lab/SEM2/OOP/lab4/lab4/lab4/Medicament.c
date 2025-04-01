#include "Medicament.h"
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

Medicament *constructorMedicament(int cod, const char *nume, int cantitate, int concentratie)
{
    Medicament *medicament = (Medicament *)malloc(sizeof(Medicament));

    medicament->cod = cod;
    strcpy_s(medicament->nume,sizeof(medicament->nume), nume);
    medicament->cantitate = cantitate;
    medicament->concentratie = concentratie;

    return medicament;
}

int validareMedicament(int cod, const char *nume, int cantitate, int concentratie)
{
    if (cod < 0)
        return 0;
    if (strcmp(nume, "") == 0)
        return 0;

    if (cantitate < 0)
        return 0;

    if (concentratie < 0)
        return 0;

    return 1;
}

void eliminaMedicament(Medicament *medicament)
{
    if (medicament != NULL)
    {

        medicament->cod = 0;
        medicament->cantitate = 0;
        medicament->concentratie = 0;
        medicament->nume[0] = '\0';
    }
}
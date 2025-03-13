#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include "repo.h"
#include "Medicament.h"

ListaMedicamente constructorListaMedicamente()
{
    ListaMedicamente farmacie;
    farmacie.lungime = 0;
    farmacie.capacitate = 101;
    // farmacie.medicamente = [];

    return farmacie;
}

void eliminaListaMedicamente(ListaMedicamente *farmacie)
{
    if (farmacie)
        return;

    for (int index = 0; index < farmacie->lungime; ++index)
    {
        eliminaMedicament(&farmacie->medicamente[index]);
    }
    farmacie->lungime = 0;
}

int adaugaMedicamentRepo(ListaMedicamente *farmacie, Medicament *medicament)
{
    for (int index = 0; index < farmacie->lungime; ++index)
    {
        if (farmacie->medicamente[index].cod == medicament->cod)
        {
            farmacie->medicamente[index].cantitate = medicament->cantitate;
            return 0;
        }
    }

    if (farmacie->lungime + 1 > farmacie->capacitate)
        return 1;

    farmacie->medicamente[farmacie->lungime] = *medicament;
    farmacie->lungime += 1;
}

int modificareMedicamentRepo(ListaMedicamente *farmacie, Medicament *medicament)
{
    for (int index = 0; index < farmacie->lungime; ++index)
    {
        if (farmacie->medicamente[index].cod == medicament->cod)
        {
            farmacie->medicamente[index].nume = medicament->nume;
            farmacie->medicamente[index].cantitate = medicament->cantitate;
            farmacie->medicamente[index].concentratie = medicament->concentratie;
            return 1;
        }
    }
    return 0;
}

int stergereMedicamentRepo(ListaMedicamente *farmacie, int cod)
{
    for (int index = 0; index < farmacie->lungime; ++index)
    {
        if (farmacie->medicamente[index].cod == cod)
        {
            eliminaMedicament(&farmacie->medicamente[index]);
            return 1;
        }
    }
    return 0;
}
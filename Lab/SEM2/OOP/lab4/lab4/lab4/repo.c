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

    return farmacie;
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
        return 2;

    farmacie->medicamente[farmacie->lungime] = *medicament;
    farmacie->lungime += 1;
    return 1;
}

void eliminaListaMedicamente(ListaMedicamente* farmacie)
{
    for (int index = 0; index < farmacie->lungime; ++index)
    {
        if(&farmacie->medicamente[index])
            eliminaMedicament(&farmacie->medicamente[index]);
    }
    farmacie->lungime = 0;
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
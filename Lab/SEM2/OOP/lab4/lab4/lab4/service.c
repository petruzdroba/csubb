#include "repo.h"
#include "Medicament.h"
#include "service.h"
#include <string.h>
#include <stdlib.h>
#include <stdio.h>

int adaugareMedicament(ListaMedicamente *farmacie, int cod, const char *nume, int cantitate, int concentratie)
{
    if (validareMedicament(cod, nume, cantitate, concentratie) == 0)
        return 0;

    Medicament *medicament = constructorMedicament(cod, nume, cantitate, concentratie);

    if (adaugaMedicamentRepo(farmacie, medicament) == 0)
    {
        return 2;
    }
    return 1;
}

int modificareMedicament(ListaMedicamente *farmacie, int cod, const char *nume, int cantitate, int concentratie)
{
    if (validareMedicament(cod, nume, cantitate, concentratie) == 0)
        return 0;

    Medicament *medicament = constructorMedicament(cod, nume, cantitate, concentratie);

    return modificareMedicamentRepo(farmacie, medicament);
}

int stergereMedicament(ListaMedicamente *farmacie, int cod)
{
    if (cod < 0)
        return 0;

    int poz = -1;
    for (int index = 0; index < farmacie->lungime; ++index)
    {
        if (cod == farmacie->medicamente[index].cod)
        {
            poz = index;
            break;
        }
    }

    if (poz == -1)
        return 0;

    return stergereMedicamentRepo(farmacie, poz);
}

void copyList(ListaMedicamente *newList, ListaMedicamente *list)
{
    for (int index = 0; index < list->lungime; index++)
    {
        newList->medicamente[index] = list->medicamente[index];
    }
    newList->lungime = list->lungime;
    newList->capacitate = list->capacitate;
}


ListaMedicamente sortCrescatorNume(ListaMedicamente *farmacie, int(* compareName)(const void * a, const void * b))
{
    ListaMedicamente sortedFarmacie = constructorListaMedicamente();
    copyList(&sortedFarmacie, farmacie);

    qsort(sortedFarmacie.medicamente, sortedFarmacie.lungime, sizeof(Medicament), compareName);

    return sortedFarmacie;
}

ListaMedicamente sortCantitateCrescator(ListaMedicamente *farmacie, int(* compareCantitateCrescator)(const void * a, const void * b))
{
    ListaMedicamente sortedFarmacie = constructorListaMedicamente();
    copyList(&sortedFarmacie, farmacie);

    qsort(sortedFarmacie.medicamente, sortedFarmacie.lungime, sizeof(Medicament), compareCantitateCrescator);

    return sortedFarmacie;
}

ListaMedicamente sortCantitateDescrescator(ListaMedicamente *farmacie, int(* compareCantitateDescrescator)(const void * a, const void *b))
{
    ListaMedicamente sortedFarmacie = constructorListaMedicamente();
    copyList(&sortedFarmacie, farmacie);

    qsort(sortedFarmacie.medicamente, sortedFarmacie.lungime, sizeof(Medicament), compareCantitateDescrescator);

    return sortedFarmacie;
}

ListaMedicamente filtrareCantitate(ListaMedicamente *farmacie, int cantitate)
{
    ListaMedicamente filteredList = constructorListaMedicamente();

    for (int index = 0; index < farmacie->lungime; ++index)
    {
        if (farmacie->medicamente[index].cantitate <= cantitate)
        {
            adaugareMedicament(&filteredList, farmacie->medicamente[index].cod, farmacie->medicamente[index].nume, farmacie->medicamente[index].cantitate, farmacie->medicamente[index].concentratie);
        }
    }
    return filteredList;
}

ListaMedicamente filtrareAlpha(ListaMedicamente *farmacie, char litera)
{
    ListaMedicamente filteredList = constructorListaMedicamente();

    for (int index = 0; index < farmacie->lungime; ++index)
    {
        if (litera == farmacie->medicamente[index].nume[0])
        {
            adaugareMedicament(&filteredList, farmacie->medicamente[index].cod, farmacie->medicamente[index].nume, farmacie->medicamente[index].cantitate, farmacie->medicamente[index].concentratie);
        }
    }
    return filteredList;
}
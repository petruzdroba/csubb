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

    return stergereMedicamentRepo(farmacie, cod);
}

void copyList(ListaMedicamente *newList, ListaMedicamente *list)
{
    for (int index = 0; index < list->lungime; ++index)
    {
        newList->medicamente[index] = list->medicamente[index];
    }
    newList->lungime = list->lungime;
    newList->capacitate = list->capacitate;
}

int compareName(const void *a, const void *b)
{
    const Medicament *medi1 = (const Medicament *)a;
    const Medicament *medi2 = (const Medicament *)b;

    return strcmp(medi1->nume, medi2->nume);
}

int compareCantitateCrescator(const void *a, const void *b)
{
    const Medicament *medi1 = (const Medicament *)a;
    const Medicament *medi2 = (const Medicament *)b;

    return medi1->cantitate - medi2->cantitate;
}

int compareCantitateDescrescator(const void *a, const void *b)
{
    const Medicament *medi1 = (const Medicament *)a;
    const Medicament *medi2 = (const Medicament *)b;

    return medi2->cantitate - medi1->cantitate;
}

ListaMedicamente sortCrescatorNume(ListaMedicamente *farmacie)
{
    ListaMedicamente sortedFarmacie = constructorListaMedicamente();
    copyList(&sortedFarmacie, farmacie);

    qsort(&sortedFarmacie, sortedFarmacie.lungime, sizeof(Medicament), compareName);

    return sortedFarmacie;
}

ListaMedicamente sortCantitateCrescator(ListaMedicamente *farmacie)
{
    ListaMedicamente sortedFarmacie = constructorListaMedicamente();
    copyList(&sortedFarmacie, farmacie);

    qsort(&sortedFarmacie, sortedFarmacie.lungime, sizeof(Medicament), compareCantitateCrescator);

    return sortedFarmacie;
}

ListaMedicamente sortCantitateDescrescator(ListaMedicamente *farmacie)
{
    ListaMedicamente sortedFarmacie = constructorListaMedicamente();
    copyList(&sortedFarmacie, farmacie);

    qsort(&sortedFarmacie, sortedFarmacie.lungime, sizeof(Medicament), compareCantitateDescrescator);

    return sortedFarmacie;
}

ListaMedicamente filtrareCantitate(ListaMedicamente *farmacie, int cantitate)
{
    ListaMedicamente filteredList = constructorListaMedicamente();
    if (cantitate < 0)
    {
        return filteredList;
    }

    for (int index = 0; index < farmacie->lungime; ++index)
    {
        if (farmacie->medicamente[index].cantitate <= cantitate)
        {
            adaugaMedicamentRepo(&filteredList, &farmacie->medicamente[index]);
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
            adaugaMedicamentRepo(&filteredList, &farmacie->medicamente[index]);
        }
    }
    return filteredList;
}
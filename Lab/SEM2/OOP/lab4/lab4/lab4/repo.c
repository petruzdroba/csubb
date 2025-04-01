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

    farmacie.medicamente = (Medicament*)malloc(farmacie.capacitate * sizeof(Medicament));

    return farmacie;
}

void redimensionareLista(ListaMedicamente* farmacie)
{
    Medicament* medicamenteCopie = (Medicament*)malloc(2 * farmacie->capacitate * sizeof(Medicament));

    if (medicamenteCopie == NULL) return;

    for (int index = 0; index < farmacie->lungime; ++index)
    {
        medicamenteCopie[index] = farmacie->medicamente[index];

    }

    farmacie->capacitate = 2 * farmacie->capacitate;
    free(farmacie->medicamente);

    farmacie->medicamente = medicamenteCopie;
}

int adaugaMedicamentRepo(ListaMedicamente *farmacie, Medicament *medicament)
{
    if (farmacie->lungime >= farmacie->capacitate)
        redimensionareLista(farmacie);

    for (int index = 0; index < farmacie->lungime; ++index)
    {
        if (farmacie->medicamente[index].cod == medicament->cod)
        {
            farmacie->medicamente[index].cantitate = medicament->cantitate;
            free(medicament);
            return 0;
        }
    }

    farmacie->medicamente[farmacie->lungime] = *medicament;
    farmacie->lungime += 1;
    free(medicament);
    return 1;
}

void eliminaListaMedicamente(ListaMedicamente* farmacie)
{
    if (farmacie == NULL) return;

    for (int index = 0; index < farmacie->lungime; index++)
    {
            Medicament p = farmacie->medicamente[index];
            eliminaMedicament(&p);
    }
    farmacie->lungime = 0;
    free(farmacie->medicamente);
}

int modificareMedicamentRepo(ListaMedicamente *farmacie, Medicament *medicament)
{
    for (int index = 0; index < farmacie->lungime; ++index)
    {
        if (farmacie->medicamente[index].cod == medicament->cod)
        {
            strcpy_s(farmacie->medicamente[index].nume,sizeof(medicament->nume), medicament->nume);
            farmacie->medicamente[index].cantitate = medicament->cantitate;
            farmacie->medicamente[index].concentratie = medicament->concentratie;
            free(medicament);
            return 1;
        }
    }
    free(medicament);
    return 0;
}

int stergereMedicamentRepo(ListaMedicamente *farmacie, int pozitie)
{
    eliminaMedicament(&farmacie->medicamente[pozitie]);

    for (int index = pozitie + 1; index < farmacie->lungime; index++)
        farmacie->medicamente[index - 1] = farmacie->medicamente[index];

    farmacie->lungime -= 1;
    return 1;
}

int compareName(const Medicament* a, const Medicament* b)
{
    return strcmp(a->nume, b->nume);
}

int compareCantitateCrescator(const Medicament* a, const Medicament* b)
{
    return a->cantitate - b->cantitate;
}

int compareCantitateDescrescator(const Medicament* a, const Medicament* b)
{
    return b->cantitate - a
        ->cantitate;
}

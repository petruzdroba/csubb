#include <stdio.h>
#include <stdlib.h>
#include "ui.h"
#include "service.h"

void printMedicamente(ListaMedicamente sortedList)
{
    if (sortedList.lungime == 0)
    {
        printf("Lista este goala!\n\n");
        return;
    }

    printf("Nume");
    printf(" Cod");
    printf(" Cantitate");
    printf(" Concentratie\n");

    for (int index = 0; index < sortedList.lungime; ++index)
    {
        printf(sortedList.medicamente[index].nume);
        printf("    %d", sortedList.medicamente[index].cod);
        printf("    %d", sortedList.medicamente[index].cantitate);
        printf("    %d", sortedList.medicamente[index].concentratie);
        printf("\n");
    }
}

void run(ListaMedicamente farmacie)
{
    printf("0.Exit\n");
    printf("1.Adauga medicament\n");
    printf("2.Modificare medicament\n");
    printf("3.Sterge medicament\n");
    printf("4.Vizualizare sortat\n");
    printf("5.Filtrare criteriu\n");
    while (1)
    {
        int optiune;
        printf(">>>");
        scanf("%d", &optiune);

        if (optiune == 1)
        {
            int cod;
            printf("cod>>");
            scanf("%d", &cod);

            char *nume = (char *)malloc(30 * sizeof(char));
            printf("nume>>");
            scanf("%s", nume);

            int cantitate;
            printf("cantitate>>");
            scanf("%d", &cantitate);

            int concentratie;
            printf("concentratie>>");
            scanf("%d", &concentratie);

            short response = adaugareMedicament(&farmacie, cod, nume, cantitate, concentratie);

            if (response == 1)
                printf("\nMedicament adaugat!\n");
            else if (response == 2)
                printf("\nMedicament existent, cantitate actualizata!\n");
            else
                printf("\nEroare fatala!\n");
        }
        else if (optiune == 2)
        {
            int cod;
            printf("cod>>");
            scanf("%d", &cod);

            char *nume = (char *)malloc(30 * sizeof(char));
            printf("new_nume>>");
            scanf("%s", nume);

            int cantitate;
            printf("new_cantitate>>");
            scanf("%d", &cantitate);

            int concentratie;
            printf("concentratie>>");
            scanf("%d", &concentratie);

            short response = modificareMedicament(&farmacie, cod, nume, cantitate, concentratie);

            if (response == 1)
                printf("\nMedicament modificat!\n");
            else
                printf("\nMedicament inexistent!\n");
        }
        else if (optiune == 3)
        {
            int cod;
            printf("cod>>");
            scanf("%d", &cod);

            short response = stergereMedicament(&farmacie, cod);

            if (response == 1)
                printf("\nMedicament sters!\n");
            else
                printf("\nMedicament inexistent!\n");
        }
        else if (optiune == 4)
        {
            printf("1.Alfabetic dupa nume\n");
            printf("2.Cantitate crescator\n");
            printf("3.Cantitate descrescator\n");

            int choice;
            printf(">>");
            scanf("%d", &choice);

            if (choice == 1)
            {
                ListaMedicamente sortedList = sortCrescatorNume(&farmacie);
                printMedicamente(sortedList);
            }
            else if (choice == 2)
            {
                ListaMedicamente sortedList = sortCantitateCrescator(&farmacie);
                printMedicamente(sortedList);
            }
            else if (choice == 3)
            {
                ListaMedicamente sortedList = sortCantitateDescrescator(&farmacie);
                printMedicamente(sortedList);
            }
            else
            {
                continue;
            }
        }
        else if (optiune == 5)
        {
            printf("1.Mai mic ca si cantiatate\n");
            printf("2.Incepand cu litera\n");

            int choice;
            printf(">>");
            scanf("%d", &choice);

            if (choice == 1)
            {
                int cantitate;
                printf("cantitate>>");
                scanf("%d", &cantitate);

                ListaMedicamente filteredList = filtrareCantitate(&farmacie, cantitate);
                printMedicamente(filteredList);
            }
            else if (choice == 2)
            {
                char letter;
                printf("letter>>");
                scanf(" %c", &letter);

                ListaMedicamente filteredList = filtrareAlpha(&farmacie, letter);
                printMedicamente(filteredList);
            }
            else
            {
                continue;
            }
        }
        else if (optiune == 0)
        {
            break;
        }
        else
        {
            continue;
        }
    }
}
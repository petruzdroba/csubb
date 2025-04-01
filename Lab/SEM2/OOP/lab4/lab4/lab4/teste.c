#include "teste.h"
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <string.h>

#include "Medicament.h"
#include "repo.h"
#include "service.h"

void testConstructorMedicament()
{
    Medicament *medicament = constructorMedicament(1, "Test Medicament", 1, 1);

    assert(medicament->cod == 1);
    assert(strcmp(medicament->nume, "Test Medicament") == 0);
    assert(medicament->cantitate == 1);
    assert(medicament->concentratie == 1);

    assert(medicament->cod != 999);
    assert(strcmp(medicament->nume, "NU Test MEdicament") != 0);
    assert(medicament->cantitate != 999);
    assert(medicament->concentratie != 999);

    eliminaMedicament(medicament);
    free(medicament);
}

void testEliminareMedicament()
{
    /*Medicament* medicament = NULL;
    eliminaMedicament(medicament);

    assert(medicament->cantitate == 0);
    assert(medicament->cod == 0);
    assert(medicament->concentratie == 0);*/
}

void testValidareMedicament()
{
    assert(validareMedicament(1, "Medicament Bun", 10, 1) == 1);
    assert(validareMedicament(2, "Medicament Rau", -1, 1) == 0);
    assert(validareMedicament(2, "Medicament Rau", 1, -1) == 0);
    assert(validareMedicament(4, "", 1, 1) == 0);
}

void testConstructorListaMedicamente()
{
    ListaMedicamente testFarmacie = constructorListaMedicamente();
    assert(testFarmacie.lungime == 0);
    assert(testFarmacie.capacitate == 101);

    eliminaListaMedicamente(&testFarmacie);
}

void testAdaugaMedicament()
{
    ListaMedicamente farmacie = constructorListaMedicamente();

    assert(farmacie.lungime == 0);
    assert(adaugareMedicament(&farmacie, 1, "Medicament Bun", 10, 1) == 1);
    assert(farmacie.lungime == 1);

    assert(adaugareMedicament(&farmacie, 2, "Medicament Bun", 1, 1) == 1);
    assert(farmacie.lungime == 2);

    assert(adaugareMedicament(&farmacie, 2, "Medicament Bun", 12, 1) == 2);
    assert(farmacie.medicamente[1].cantitate == 12);
    assert(farmacie.lungime == 2); 

    assert(adaugareMedicament(&farmacie, -2, "Medicament Rau", 1, 1) == 0);
    assert(adaugareMedicament(&farmacie, 1, "Medicament Bun", -10, 1) == 0);
    assert(adaugareMedicament(&farmacie, 2, "Medicament Rau", 1, -1) == 0);
    assert(adaugareMedicament(&farmacie, 3, "", 1, 1) == 0);
    assert(farmacie.lungime == 2);

   eliminaListaMedicamente(&farmacie);
}

void testEliminaListaMedicamente()
{
    ListaMedicamente farmacie = constructorListaMedicamente();

    assert(farmacie.lungime == 0);
    assert(adaugareMedicament(&farmacie, 1, "1", 10, 1) == 1);
    assert(adaugareMedicament(&farmacie, 2, "1", 10, 1) == 1);
    assert(adaugareMedicament(&farmacie, 3, "1", 10, 1) == 1);
    assert(farmacie.lungime == 3);

    eliminaListaMedicamente(&farmacie);

    assert(farmacie.lungime == 0);
}

void testModificareMedicament()
{
    ListaMedicamente farmacie = constructorListaMedicamente();
    assert(adaugareMedicament(&farmacie, 1, "1", 10, 1) == 1);
    assert(adaugareMedicament(&farmacie, 2, "1", 10, 1) == 1);
    assert(adaugareMedicament(&farmacie, 3, "1", 10, 1) == 1);
    
    assert(modificareMedicament(&farmacie, 1, "2", 100, 100) == 1);
    assert(farmacie.medicamente[0].cantitate == 100);
    assert(farmacie.medicamente[0].concentratie == 100);
    assert(strcmp(farmacie.medicamente[0].nume, "2") == 0);

    assert(modificareMedicament(&farmacie, 2, "2", 100, 100) == 1);
    assert(modificareMedicament(&farmacie, 3, "2", 100, 100) == 1);
    assert(modificareMedicament(&farmacie, 3, "2", 100, -100) == 0);

    assert(modificareMedicament(&farmacie, 4, "2", 100, 100) == 0);

    eliminaListaMedicamente(&farmacie);
}

void testStergeremedicament()
{
    ListaMedicamente farmacie = constructorListaMedicamente();
    assert(adaugareMedicament(&farmacie, 1, "1", 10, 1) == 1);
    assert(adaugareMedicament(&farmacie, 2, "2", 11, 2) == 1);
    assert(adaugareMedicament(&farmacie, 3, "3", 12, 3) == 1);
    assert(farmacie.lungime == 3);

    assert(stergereMedicament(&farmacie, 1) == 1);
    assert(farmacie.medicamente[0].cod != 1);
    assert(farmacie.medicamente[0].cantitate != 10);
    assert(farmacie.medicamente[0].concentratie != 1);
    assert(strcmp(farmacie.medicamente[0].nume, "1") != 0);

    assert(stergereMedicament(&farmacie, -1) == 0);

    assert(stergereMedicament(&farmacie, 4) == 0);
    eliminaListaMedicamente(&farmacie);
}

void testSortCrescatorNume()
{
    ListaMedicamente farmacie = constructorListaMedicamente();
    assert(adaugareMedicament(&farmacie, 1, "C", 10, 1) == 1);
    assert(adaugareMedicament(&farmacie, 2, "B", 10, 1) == 1);
    assert(adaugareMedicament(&farmacie, 3, "A", 10, 1) == 1);
    assert(farmacie.lungime == 3);

    ListaMedicamente sorted = sortCrescatorNume(&farmacie, compareName);
    assert(strcmp(sorted.medicamente[0].nume, "A") == 0);
    assert(strcmp(sorted.medicamente[1].nume, "B") == 0);
    assert(strcmp(sorted.medicamente[2].nume, "C") == 0);

    eliminaListaMedicamente(&sorted);
    eliminaListaMedicamente(&farmacie);
}

void testSortCrescatorCantitate()
{
    ListaMedicamente farmacie = constructorListaMedicamente();
    assert(adaugareMedicament(&farmacie, 1, "C", 30, 1) == 1);
    assert(adaugareMedicament(&farmacie, 2, "B", 20, 1) == 1);
    assert(adaugareMedicament(&farmacie, 3, "A", 10, 1) == 1);
    assert(farmacie.lungime == 3);

    ListaMedicamente sorted = sortCantitateCrescator(&farmacie, compareCantitateCrescator);
    assert(strcmp(sorted.medicamente[0].nume, "A") == 0);
    assert(strcmp(sorted.medicamente[1].nume, "B") == 0);
    assert(strcmp(sorted.medicamente[2].nume, "C") == 0);

    eliminaListaMedicamente(&sorted);
    eliminaListaMedicamente(&farmacie);
}

void testSortDescrescatorCantitate()
{

    ListaMedicamente farmacie = constructorListaMedicamente();
    assert(adaugareMedicament(&farmacie, 1, "C", 10, 1) == 1);
    assert(adaugareMedicament(&farmacie, 2, "B", 20, 1) == 1);
    assert(adaugareMedicament(&farmacie, 3, "A", 30, 1) == 1);
    assert(farmacie.lungime == 3);

    ListaMedicamente sorted = sortCantitateDescrescator(&farmacie, compareCantitateDescrescator);
    assert(strcmp(sorted.medicamente[0].nume, "A") == 0);
    assert(strcmp(sorted.medicamente[1].nume, "B") == 0);
    assert(strcmp(sorted.medicamente[2].nume, "C") == 0);

    eliminaListaMedicamente(&sorted);
    eliminaListaMedicamente(&farmacie);
}

void testFiltrareCantitate()
{
    ListaMedicamente farmacie = constructorListaMedicamente();

    assert(adaugareMedicament(&farmacie, 1, "C", 10, 1) == 1);
    assert(adaugareMedicament(&farmacie, 2, "B", 20, 1) == 1);
    assert(adaugareMedicament(&farmacie, 3, "A", 30, 1) == 1);
    assert(farmacie.lungime == 3);

    ListaMedicamente filtered = filtrareCantitate(&farmacie, 20);
    assert(strcmp(filtered.medicamente[1].nume, "B") == 0);
    assert(strcmp(filtered.medicamente[0].nume, "C") == 0);

    eliminaListaMedicamente(&filtered);
    eliminaListaMedicamente(&farmacie);
}

void testFiltrareAlpha()
{
    ListaMedicamente farmacie = constructorListaMedicamente();
    assert(adaugareMedicament(&farmacie, 1, "C", 10, 1) == 1);
    assert(adaugareMedicament(&farmacie, 2, "B", 20, 1) == 1);
    assert(adaugareMedicament(&farmacie, 3, "A", 30, 1) == 1);
    assert(farmacie.lungime == 3);

    ListaMedicamente filteredA = filtrareAlpha(&farmacie, 'A');
    ListaMedicamente filteredC = filtrareAlpha(&farmacie, 'C');
    assert(strcmp(filteredA.medicamente[0].nume, "A") == 0);
    assert(strcmp(filteredC.medicamente[0].nume, "C") == 0);

    eliminaListaMedicamente(&filteredA);
    eliminaListaMedicamente(&filteredC);
    eliminaListaMedicamente(&farmacie);
}

void testRedimensionareLista()
{
    ListaMedicamente farmacie = constructorListaMedicamente();
    assert(adaugareMedicament(&farmacie, 1, "C", 10, 1) == 1);
    assert(adaugareMedicament(&farmacie, 2, "B", 20, 1) == 1);
    assert(adaugareMedicament(&farmacie, 3, "A", 30, 1) == 1);
    assert(farmacie.lungime == 3);
    farmacie.capacitate = 4;
    assert(adaugareMedicament(&farmacie, 4, "B", 20, 1) == 1);
    assert(adaugareMedicament(&farmacie, 5, "A", 30, 1) == 1);

    assert(farmacie.lungime == 5);
    assert(farmacie.capacitate >= 5);

    eliminaListaMedicamente(&farmacie);
}

void runAllTests()
{
    testConstructorMedicament();

    testValidareMedicament();
    testEliminareMedicament();
    testConstructorListaMedicamente();
    testAdaugaMedicament();
    testEliminaListaMedicamente();
    testModificareMedicament();
    testStergeremedicament();
    testSortCrescatorNume();
    testSortCrescatorCantitate();
    testSortDescrescatorCantitate();
    testFiltrareCantitate();
    testFiltrareAlpha();
    testRedimensionareLista();

    printf("Tests completed\n");
}
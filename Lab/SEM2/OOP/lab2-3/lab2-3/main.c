#define _CRTDBG_MAP_ALLOC
#include <stdlib.h>
#include <crtdbg.h>

#include <stdio.h>
#include "ui.h"
#include "repo.h"
#include "teste.h"

int main()
{
    runAllTests();
    ListaMedicamente farmacie = constructorListaMedicamente();
    run(farmacie);
    eliminaListaMedicamente(&farmacie);
    //_CrtDumpMemoryLeaks();
    return 0;
}
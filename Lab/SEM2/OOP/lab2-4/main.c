#include <stdio.h>

#include "ui.h"
#include "repo.h"
#include "teste.h"

int main()
{
    ListaMedicamente farmacie = constructorListaMedicamente();
    runAllTests();
    run(farmacie);
    return 0;
}
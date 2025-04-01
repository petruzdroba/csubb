#define _CRTDBG_MAP_ALLOC
#include <stdio.h>
#include <stdlib.h>
#include "ui.h"
#include "teste.h"


int main() {
    printf("Rularea testelor...\n");
    ruleaza_toate_testele();

     printf("Toate testele au fost rulate cu succes!\n");

     //ruleaza();
     _CrtDumpMemoryLeaks();
    return 0;
}

#define _CRTDBG_MAP_ALLOC
#include <stdlib.h>
#include <crtdbg.h>


#include <iostream>
#include "teste.h"
#include "ui.h"

using namespace std;

int main()
{
	testAll();
	run();
	_CrtDumpMemoryLeaks();
	return 0;
}
#pragma once
#include "Cheltuieli.h"

typedef struct {
	void** elemente;
	int lungime;
	int capacitate;
}MyList;

MyList* createEmpty();
/*
	-creeaza o lista de undo goala cu lungimea 0 si capacitatea initiala 5
	-aloca memorie pentru elemente
	-returneaza aceasta lista
*/

void redimensionare(MyList* undoList);
/*
	-redimensioneaza lista
	-dubleaza capacitatea listei
*/

int addElement(MyList* undolist, void* element, void* (*functie)(void* element));
/*
	-adauga un element in lista de undo, cu deepCopy
	-returneaza 1 daca e ok
	-returneaza 0 daca nu e ok
*/

int removeLastElement(MyList* undoList );
/*
	-elimina ultimul element din lsita de undo
	-micsoreaza lungimea cu 1 
*/

MyList* deepCopyMyList(MyList* sourceList);
/*
	-copiaza in alta lista deep, lista sourceList si returneaza aceasta lista
*/

MyList* undo(MyList* undoList, MyList* listaCheltuieli);
/*
	-elimina din undo list ultimul element, si il returneaza
*/

void destroyUndoList(MyList* undoList);
/*
	-distruge lista de undo
*/
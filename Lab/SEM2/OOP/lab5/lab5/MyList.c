#include "MyList.h"
#include "Cheltuieli.h"
#include "repo.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>//memcpy

MyList* createEmpty()
{
	MyList* undoList = (MyList*)malloc(sizeof(MyList));
	if (undoList != NULL)
	{
		undoList->capacitate = 5;
		undoList->lungime = 0;
		undoList->elemente = malloc(sizeof(void*) * undoList->capacitate);
	}

	return undoList;
}

MyList* deepCopyMyList(MyList* sourceList)
{
	MyList* copyList = createEmpty();

	for (int index = 0; index < sourceList->lungime; index++)
	{
		add(copyList, ((Cheltuiala*)sourceList->elemente[index])->zi, ((Cheltuiala*)sourceList->elemente[index])->suma, ((Cheltuiala*)sourceList->elemente[index])->tip);
	}

	return copyList;
}

void redimensionare(MyList* undoList)
{
	undoList->capacitate *= 2;

	MyList* newElems = realloc(undoList->elemente, undoList->capacitate * sizeof(MyList));

	undoList->elemente = (void*)newElems;
}

int addElement(MyList* undoList, void* element, void* (*functie)(void* element))
{
	if (undoList->lungime >= undoList->capacitate)
	{
		redimensionare(undoList);
	}

	undoList->elemente[undoList->lungime] = functie(element);
	undoList->lungime++;
	return 1;
}

int removeLastElement(MyList* undoList)
{
	if (undoList->lungime >= 1)
	{
		distruge_lista(undoList->elemente[undoList->lungime - 1]);//treaba incepe de la 0
		free(undoList->elemente[undoList->lungime - 1]);
		undoList->lungime--;
		return 1;
	}
	return 0;
}

MyList* undo(MyList* undoList, MyList* listaCheltuieli)
{
	distruge_lista(listaCheltuieli);//out with the old, in with the new
	free(listaCheltuieli);
	if (undoList->lungime > 0)
	{
		listaCheltuieli = deepCopyMyList(undoList->elemente[undoList->lungime - 1]);//treaba incepe de la 0
		removeLastElement(undoList);
		return listaCheltuieli;
	}
	return createEmpty();
}

void destroyUndoList(MyList* undoList)
{
	for (int index = 0; index < undoList->lungime; index++)
	{
		distruge_lista(undoList->elemente[index]);
		free(undoList->elemente[index]);
	}//may god have mercy on this for

	free(undoList->elemente);
	undoList->elemente = NULL;
	undoList->lungime = 0;
	undoList->capacitate = 0;
}
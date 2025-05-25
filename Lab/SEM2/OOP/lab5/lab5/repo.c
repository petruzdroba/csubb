#define _CRT_SECURE_NO_WARNINGS
#include <string.h>
#include "repo.h"
#include <stdlib.h>
#include "Cheltuieli.h"
#include "MyList.h"

void distruge_lista(MyList* lista) {
	/**
	 * Distruge lista de cheltuieli, eliberand memoria alocata pentru aceasta.
	 *
	 * @param lista Pointer catre lista de cheltuieli ce trebuie distrusa.
	 *
	 * Aceasta functie elibereaza memoria ocupata de lista si reseteaza campurile listei la valori implicite.
	 */
	for (int index = 0; index < lista->lungime; index++)
	{
		if (lista->elemente[index])
		{
			DistrugeCheltuiala(lista->elemente[index]);
		}
	}

	free(lista->elemente);
	lista->elemente = NULL;
	lista->lungime = 0;
	lista->capacitate = 0;
}

void redimensioneaza_lista(MyList* lista) {
	/**
	 * Redimensioneaza lista de cheltuieli, dubland capacitatea acesteia.
	 *
	 * @param lista Pointer catre lista de cheltuieli ce urmeaza a fi redimensionata.
	 *
	 * Aceasta functie dubleaza capacitatea listei, alocand o noua memorie de dimensiune mai mare.
	 */

	lista->capacitate *= 2;
	Cheltuiala* noua_cheltuiala = realloc(lista->elemente, lista->capacitate * sizeof(Cheltuiala));

	lista->elemente = (void *)noua_cheltuiala;
	
}

int add(MyList* lista, int zi, float suma, const char* tip) {
	/**
	 * Adauga o noua elemente in lista de cheltuieli.
	 *
	 * @param lista Pointer catre lista de cheltuieli.
	 * @param zi Ziua cheltuielii.
	 * @param suma Suma cheltuielii.
	 * @param tip Tipul cheltuielii.
	 *
	 * @return 0 daca adaugarea a fost cu succes,
	 *        -1 daca lista este plina.
	 */
	if (lista->lungime < MAX_CHELTUIELI) {
		if (lista->lungime == lista->capacitate) {
			redimensioneaza_lista(lista);
		}
		/*lista->elemente[lista->lungime].zi = zi;
		lista->elemente[lista->lungime].suma = suma;
		strcpy(lista->elemente[lista->lungime].tip, tip);*/
		lista->elemente[lista->lungime] = creeazaCheltuiala(zi, suma, tip);
		lista->lungime++;
		return 0;
	}
	return -1;
}

int modificare(MyList* lista, int index, int zi, float suma, const char* tip) {
	/**
	 * Modifica o elemente existenta din lista.
	 *
	 * @param lista Pointer catre lista de cheltuieli.
	 * @param index Indexul cheltuielii de modificat.
	 * @param zi Noua zi a cheltuielii.
	 * @param suma Noua suma a cheltuielii.
	 * @param tip Noul tip al cheltuielii.
	 *
	 * @return 0 daca modificarea a avut succes,
	 *         -2 daca indexul este invalid.
	 */
	if (index >= 0 && index < lista->lungime) {
		((Cheltuiala*)lista->elemente[index])->zi = zi;
		((Cheltuiala*)lista->elemente[index])->suma = suma;
		strncpy(((Cheltuiala*)lista->elemente[index])->tip, tip, 50);
		return 0;
	}
	return -2;
}

int stergere(MyList* lista, int index) {
	/**
	 * Sterge o elemente din lista.
	 *
	 * @param lista Pointer catre lista de cheltuieli.
	 * @param index Indexul cheltuielii de sters.
	 *
	 * @return 0 daca stergerea a avut succes,
	 *        -2 daca indexul este invalid.
	 */
	if (index < 0 || index >= lista->lungime) {
		return -2;
	}
	DistrugeCheltuiala(lista->elemente[index]);
	for (int i = index; i < lista->lungime - 1; i++) {
		lista->elemente[i] = lista->elemente[i + 1];
	}

	lista->lungime--;

	return 0;
}

MyList* get_all(MyList* lista) {
	/**
	 * Returneaza intreaga lista de cheltuieli.
	 *
	 * @param lista Pointer catre lista de cheltuieli.
	 *
	 * @return Pointer catre lista de cheltuieli.
	 */
	if (!lista) return NULL;
	MyList* copia = (MyList*)malloc(sizeof(MyList));

	if (copia != NULL)
	{
		copia->capacitate = lista->capacitate;
		copia->lungime = lista->lungime;
		copia->elemente = NULL;
		if (lista->lungime > 0) {
			copia->elemente = (void*)malloc(lista->lungime * sizeof(Cheltuiala));

			if(copia->elemente)
				memcpy(copia->elemente, lista->elemente, lista->lungime * sizeof(Cheltuiala));
		}
	}

	return copia;
}

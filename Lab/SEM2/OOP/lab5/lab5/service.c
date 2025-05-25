#include<stdio.h>
#include "service.h"
#include <stdlib.h>
#include <string.h>
#include "repo.h"
#include "validator.h"
#include <errno.h>

int add_cheltuiala(MyList* lista, int zi, float suma, const char* tip) {
	/**
	 * Adauga o elemente in lista, dupa ce aceasta este validata.
	 *
	 * @param lista Pointer catre lista de cheltuieli.
	 * @param zi Ziua cheltuielii.
	 * @param suma Suma cheltuielii.
	 * @param tip Tipul cheltuielii.
	 *
	 * @return 0 daca adaugarea a avut succes,
	 *         eroare daca este invalida (zi, suma, tip).
	 */

	int eroare = valideaza_zi(zi);
	if (eroare != 0) {
		return eroare;
	}

	eroare = valideaza_suma(suma);
	if (eroare != 0) {
		return eroare;
	}

	eroare = valideaza_tip(tip);
	if (eroare != 0) {
		return eroare;
	}


	return add(lista, zi, suma, tip);
}

int modifica_cheltuiala(MyList* lista, int index, int zi, float suma, const char* tip) {
	/**
	 * Modifica o elemente din lista, dupa ce aceasta este validata.
	 *
	 * @param lista Pointer catre lista de cheltuieli.
	 * @param index Indexul cheltuielii de modificat.
	 * @param zi Noua zi a cheltuielii.
	 * @param suma Noua suma a cheltuielii.
	 * @param tip Noul tip al cheltuielii.
	 *
	 * @return 0 daca modificarea a avut succes,
	 *         eroare daca este invalida (zi, suma, tip).
	 */
	int eroare = valideaza_zi(zi);
	if (eroare != 0) {
		return eroare;
	}

	eroare = valideaza_suma(suma);
	if (eroare != 0) {
		return eroare;
	}

	eroare = valideaza_tip(tip);
	if (eroare != 0) {
		return eroare;
	}

	return modificare(lista, index, zi, suma, tip);
}

int sterge_cheltuiala(MyList* lista, int index) {
	/**
	 * Sterge o elemente din lista.
	 *
	 * @param lista Pointer catre lista de cheltuieli.
	 * @param index Indexul cheltuielii de sters.
	 *
	 * @return 0 daca stergerea a avut succes.
	 */
	return stergere(lista, index);
}
MyList* vizualizare(MyList* lista, const char* criteriu, const char* valoare) {
	/**
	 * Vizualizeaza cheltuieli pe baza unui criteriu (zi, suma, tip).
	 *
	 * @param lista Pointer catre lista de cheltuieli.
	* @param criteriu Criteriul dupa care se face cautarea (zi, suma, tip).
	 * @param valoare Valoarea asociata criteriului.
	 *
	 * @return 0 daca au fost gasite cheltuieli,
	 *        -1 daca nu au fost gasite.
	 */
	MyList* lista_filtrata = (MyList*)malloc(sizeof(MyList));
	if (!lista_filtrata) return NULL;

	lista_filtrata->capacitate = lista->capacitate;
	lista_filtrata->lungime = 0;
	lista_filtrata->elemente = (void*)malloc(lista->capacitate * sizeof(Cheltuiala));

	for (int i = 0; i < lista->lungime; i++) {
		if (strcmp(criteriu, "zi") == 0) {
			int zi_val = atoi(valoare);
			if (((Cheltuiala*)lista->elemente[i])->zi == zi_val) {
				lista_filtrata->elemente[lista_filtrata->lungime] = lista->elemente[i];
				lista_filtrata->lungime++;
			}
		}
		else if (strcmp(criteriu, "suma") == 0) {
			double suma_val = atof(valoare);
			if (((Cheltuiala*)lista->elemente[i])->suma == suma_val) {
				lista_filtrata->elemente[lista_filtrata->lungime] = lista->elemente[i];
				lista_filtrata->lungime++;
			}
		}
		else if (strcmp(criteriu, "tip") == 0) {
			if (strcmp(((Cheltuiala*)lista->elemente[i])->tip, valoare) == 0) {
				lista_filtrata->elemente[lista_filtrata->lungime] = lista->elemente[i];
				lista_filtrata->lungime++;
			}
		}
	}

	if (lista_filtrata->lungime == 0) {
		free(lista_filtrata->elemente);
		free(lista_filtrata);
		return NULL;
	}
	free(lista_filtrata->elemente);
	return lista_filtrata;
}
MyList* filtrareMaiMare(MyList* lista, float suma)
{
	MyList* lista_filtrata = (MyList*)malloc(sizeof(MyList));
	if (lista_filtrata != NULL)
	{
		lista_filtrata->capacitate = lista->capacitate;
		lista_filtrata->lungime = 0;
		lista_filtrata->elemente = (void*)malloc(lista->capacitate * sizeof(Cheltuiala));

		for (int i = 0; i < lista->lungime; i++) {
			if (((Cheltuiala*)lista->elemente[i])->suma >= suma) {
				if (lista_filtrata->elemente != NULL)
				{
					lista_filtrata->elemente[lista_filtrata->lungime] = lista->elemente[i];
					lista_filtrata->lungime++;
				}

			}
		}
		if (lista_filtrata->lungime == 0) {
			free(lista_filtrata->elemente);
			free(lista_filtrata);
			return NULL;
		}
	}


	//free(lista_filtrata->elemente);
	return lista_filtrata;
}

MyList* filtrareMaiMic(MyList* lista, float suma)
{
	MyList* lista_filtrata = (MyList*)malloc(sizeof(MyList));
	if (lista_filtrata != NULL)
	{
		lista_filtrata->capacitate = lista->capacitate;
		lista_filtrata->lungime = 0;
		lista_filtrata->elemente = (void*)malloc(lista->capacitate * sizeof(Cheltuiala));

		for (int i = 0; i < lista->lungime; i++) {
			if (((Cheltuiala*)lista->elemente[i])->suma <= suma) {
				if (lista_filtrata->elemente != NULL)
				{
					lista_filtrata->elemente[lista_filtrata->lungime] = lista->elemente[i];
					lista_filtrata->lungime++;
				}
			}
		}

		if (lista_filtrata->lungime == 0) {
			free(lista_filtrata->elemente);
			free(lista_filtrata);
			return NULL;
		}

	}
	//free(lista_filtrata->elemente);
	return lista_filtrata;
}

int compara_suma(const void* a, const void* b) {
	/**
	 * Compara doua cheltuieli pe baza sumei pentru ordonare.
	 *
	 * @param a Primul element.
	 * @param b Al doilea element.
	 *
	 * @return Diferenta intre sumele celor doua cheltuieli.
	 */
	 // Cast a and b to Cheltuiala** (pointer to pointer to Cheltuiala)
	Cheltuiala** cheltuiala_a = (Cheltuiala**)a;
	Cheltuiala** cheltuiala_b = (Cheltuiala**)b;

	if ((*cheltuiala_a)->suma < (*cheltuiala_b)->suma) return -1;
	if ((*cheltuiala_a)->suma > (*cheltuiala_b)->suma) return 1;
	return 0;
}

int compara_suma_desc(const void* a, const void* b) {
	/**
	 * Compara doua cheltuieli pe baza sumei pentru ordonare.
	 *
	 * @param a Primul element.
	 * @param b Al doilea element.
	 *
	 * @return Diferenta intre sumele celor doua cheltuieli.
	 */
	Cheltuiala** cheltuiala_a = (Cheltuiala**)a;
	Cheltuiala** cheltuiala_b = (Cheltuiala**)b;

	if ((*cheltuiala_a)->suma < (*cheltuiala_b)->suma) return 1;  // Flip the comparison
	if ((*cheltuiala_a)->suma > (*cheltuiala_b)->suma) return -1; // Flip the comparison
	return 0;
}

int compara_tip(const void* a, const void* b) {
	/**
	 * Compara doua cheltuieli pe baza tipului pentru ordonare.
	 *
	 * @param a Primul element.
	 * @param b Al doilea element.
	 *
	* @return - < 0 daca tipul cheltuielii din a este mai mic  decat tipul cheltuielii din b.
	 *          - 0 daca tipurile celor doua cheltuieli sunt egale.
	 *          - > 0 daca tipul cheltuielii din a este mai mare decat tipul cheltuielii din b.
	 */
	Cheltuiala** cheltuiala_a = (Cheltuiala**)a;
	Cheltuiala** cheltuiala_b = (Cheltuiala**)b;

	return strcmp((*cheltuiala_a)->tip, (*cheltuiala_b)->tip);
}

int compara_tip_desc(const void* a, const void* b) {
	/**
	 * Compara doua cheltuieli pe baza tipului pentru ordonare.
	 *
	 * @param a Primul element.
	 * @param b Al doilea element.
	 * @return  - < 0 daca tipul cheltuielii din b este mai mic  decat tipul cheltuielii din a.
	 *          - 0 daca tipurile celor doua cheltuieli sunt egale.
	 *          - > 0 daca tipul cheltuielii din b este mai mare decat tipul cheltuielii din a.
	 */
	Cheltuiala** cheltuiala_a = (Cheltuiala**)a;
	Cheltuiala** cheltuiala_b = (Cheltuiala**)b;

	return -strcmp((*cheltuiala_a)->tip, (*cheltuiala_b)->tip);
}

void ordonare(MyList* lista, const char* criteriu, int(*compara)(const void* a, const void* b)) {
	/**
	 * Ordoneaza lista de cheltuieli crescator dupa suma acestora sau dupa tip.
	 *
	 * @param lista Pointer catre lista de cheltuieli.
	 */
	if (strcmp(criteriu, "suma") == 0) {
		qsort(lista->elemente, lista->lungime, sizeof(Cheltuiala*), compara_suma);
	}
	else if (strcmp(criteriu, "tip") == 0) {
		qsort(lista->elemente, lista->lungime, sizeof(Cheltuiala*), compara);
	}
}


void ordonare_desc(MyList* lista, const char* criteriu, int(*compara)(const void* a, const void* b)) {
	/**
	 * Ordoneaza lista de cheltuieli descrescator dupa suma acestora sau dupa tip.
	 *
	 * @param lista Pointer catre lista de cheltuieli.
	 */
	if (lista && lista->elemente) {
		if (strcmp(criteriu, "suma") == 0) {
			qsort(lista->elemente, lista->lungime, sizeof(Cheltuiala*), compara_suma_desc);
		}
		else if (strcmp(criteriu, "tip") == 0) {
			qsort(lista->elemente, lista->lungime, sizeof(Cheltuiala*), compara);
		}
	}
}

MyList* get_all_(MyList* lista) {
	/**
	 * Obtine lista completa de cheltuieli.
	 *
	 * @param lista Pointer catre lista de cheltuieli.
	 *
	 * @return Pointer catre lista de cheltuieli.
	 */
	return get_all(lista);
}

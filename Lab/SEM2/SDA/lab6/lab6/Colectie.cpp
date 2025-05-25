#include "Colectie.h"
#include "IteratorColectie.h"
//#include <exception>
#include <iostream>
#include <math.h>

using namespace std;


void Colectie::actPrimLiber()
{
	primLiber++;
	while (primLiber < cp && elemente[primLiber] != NULL_TELEM)
		primLiber++;
}//O(cp)

int Colectie::d(TElem e) const
{
	return abs(e) % cp;
}//O(1)

void Colectie::resize() {
	int oldCp = cp;
	cp = cp * 2;

	TElem* oldElems = elemente;
	int* oldUrm = urmator;

	elemente = new TElem[cp];
	urmator = new int[cp];

	for (int i = 0; i < cp; ++i) {
		elemente[i] = NULL_TELEM;
		urmator[i] = NULL_TELEM;
	}

	primLiber = 0;
	for (int i = 0; i < oldCp; ++i) {
		if (oldElems[i] != NULL_TELEM) {

			/*int poz = d(oldElems[i]);
			if (elemente[poz] == NULL_TELEM) {
				elemente[poz] = oldElems[i];
			}
			else {
				int pozitierent = poz;
				while (urmator[current] != NULL_TELEM) {
					current = urmator[current];
				}
				while (primLiber < cp && elemente[primLiber] != NULL_TELEM)
					primLiber++;
				elemente[primLiber] = oldElems[i];
				urmator[current] = primLiber;
			}*/
			adauga(oldElems[i]);
		}
	}

	delete[] oldElems;
	delete[] oldUrm;

	actPrimLiber();
}//O(cp)


Colectie::Colectie() {
	cp = 10;

	elemente = new TElem[cp];
	urmator = new int[cp];

	for (int i = 0; i < cp; ++i)
	{
		elemente[i] = NULL_TELEM;
		urmator[i] = NULL_TELEM;
	}

	primLiber = 0;
}//O(cp)


void Colectie::adauga(TElem elem) {
	if (primLiber >= cp)
		resize();

	int pozitie = d(elem);
	if (elemente[pozitie] == NULL_TELEM)
	{//e libera
		elemente[pozitie] = elem;

		if (pozitie == primLiber)
			actPrimLiber();
		return;
	}

	int prev = NULL_TELEM;
	while (pozitie != NULL_TELEM)
	{
		prev = pozitie;
		pozitie = urmator[pozitie];
	}

	elemente[primLiber] = elem;
	urmator[prev] = primLiber;
	actPrimLiber();
}//O(1)

//bool Colectie::sterge(TElem elem) {
//	int pozitie = d(elem);
//	int prev = NULL_TELEM;
//
//	while (pozitie != NULL_TELEM && elemente[pozitie] != elem) {
//		prev = pozitie;
//		pozitie = urmator[pozitie];
//	}
//
//	if (pozitie == NULL_TELEM)
//		return false; 
//	if (prev == NULL_TELEM)
//	{
//		// elementul e primul din lant
//		int urm = urmator[pozitie];
//		if (urm != NULL_TELEM) {
//			elemente[pozitie] = elemente[urm];
//			urmator[pozitie] = urmator[urm];
//			elemente[urm] = NULL_TELEM;
//			urmator[urm] = NULL_TELEM;
//			if (urm < primLiber)
//				primLiber = urm;
//		}
//		else {
//			elemente[pozitie] = NULL_TELEM;
//			urmator[pozitie] = NULL_TELEM;
//			if (pozitie < primLiber)
//				 primLiber = pozitie;
//		}
//	}
//	else {
//		urmator[prev] = urmator[pozitie];
//		elemente[pozitie] = NULL_TELEM;
//		urmator[pozitie] = NULL_TELEM;
//		if (pozitie < primLiber)
//			primLiber = pozitie;
//	}
//	return true;
//}

bool Colectie::sterge(TElem elem) {
    int pozitie = d(elem), prev = NULL_TELEM;

    while (pozitie != NULL_TELEM && elemente[pozitie] != elem) {
        prev = pozitie;
        pozitie = urmator[pozitie];
    }
    if (pozitie == NULL_TELEM)
		return false;

	TElem* temp = new TElem[cp];
	int cnt = 0, j = urmator[pozitie];
	while (j != NULL_TELEM) {
		if (cnt >= cp)
			break;
		temp[cnt++] = elemente[j];  
		j = urmator[j];
	}

	int sters = pozitie;
	while (sters != NULL_TELEM) {
		int next = urmator[sters];
		elemente[sters] = NULL_TELEM;
		urmator[sters] = NULL_TELEM;
		if (sters < primLiber) primLiber = sters;
		sters = next;
	}

	if (prev != NULL_TELEM)
		urmator[prev] = NULL_TELEM;

	for (int k = 0; k < cnt; ++k)
		adauga(temp[k]);

	delete[] temp;
	return true;
}//O(n)


bool Colectie::cauta(TElem elem) const {
	int pozitie = d(elem);

	while (pozitie != NULL_TELEM)
	{
		if (elemente[pozitie] == elem)
			return true;
		pozitie = urmator[pozitie];
	}
	return false;
}//O(1)

int Colectie::nrAparitii(TElem elem) const {
	int ap = 0;
	int pozitie = d(elem);

	while (pozitie != NULL_TELEM)
	{
		if (elemente[pozitie] == elem)
			ap++;
		pozitie = urmator[pozitie];
	}
	return ap;
}//O(n)


int Colectie::dim() const {
	int len = 0;

	for (int i = 0; i < cp; ++i)
	{
		if (elemente[i] != NULL_TELEM)
			len++;
	}
	return len;
}//O(cp)


bool Colectie::vida() const {
	return primLiber == 0;
}//O(1)

IteratorColectie Colectie::iterator() const {
	return  IteratorColectie(*this);
}

/*
	pre: c este o colectie
	post: se reeturneaza un numar care este max - min

	subalgoritm diferenta()
		daca vida(c) == 1 atunci
			diferenta <- -1

		pozitie <- 0
		min <- element(c,it,0), max <- elemente(c, it,0)

		cat timp pozitie <= dim(c) executa
			daca(element(c, it,pozitie) < min) atunci
				min = element(c, it, pozitie)

			daca(element(c, it,pozitie) >max) atunci
				max = element(c, it, pozitie)

			pozitie <- pozitie + 1
		sf cat timp
		diferenta <- max - min
	sf subalgoritm
*/
/*
	Complexitate : Teta(n^2) - se trece prin toate elementele => complexitate si se compara de fiecare data cu dimensiunea
					dim() - Teta(n)

					Complexitate Generala - Teta(n^2)
*/

int Colectie::diferenta() const
{
	if (vida())
		return -1;

	int pozitie = 0;
	int min = elemente[0], max = elemente[0];

	while (pozitie < dim())
	{
		if (elemente[pozitie] < min)
			min = elemente[pozitie];
		if (elemente[pozitie] > max)
			max = elemente[pozitie];

		pozitie ++;
	}

	return max - min;
}


Colectie::~Colectie() {
	delete[] elemente;
	delete[] urmator;
}



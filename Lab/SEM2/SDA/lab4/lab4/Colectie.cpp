#include "Colectie.h"
#include "IteratorColectie.h"
#include <exception>
#include <iostream>
#include <utility>

using namespace std;


int Colectie::aloca()
{
	//se sterge primul element din lista spatiului liber
	int i = primLiber;
	primLiber = urm[primLiber];
	return i;
}//O(1)

void Colectie::dealoca(int i)
{
	//se trece pozitia i in lista spatiului liber
	urm[i] = primLiber;
	primLiber = i;
}//O(1)

int Colectie::creeazaNod(TElem e)
{
	if (primLiber == -1)
		resize();

	int i = aloca();
	if (i != -1)
	{
		this->e[i] = make_pair(e, 1);
		urm[i] = -1;
	}
	return i;
}//O(1) amortizat

void Colectie::resize() {
	int oldCp = cp;
	cp = cp * 2;

	pair<TElem, int>* newE = new pair<TElem, int>[cp];
	int* newUrm = new int[cp];

	for (int i = 0; i < oldCp; i++) {
		newE[i] = e[i];
		newUrm[i] = urm[i];
	}

	for (int i = oldCp; i < cp - 1; i++) {
		newUrm[i] = i + 1;
	}
	newUrm[cp - 1] = -1;

	delete[] e;
	delete[] urm;

	e = newE;
	urm = newUrm;
	primLiber = oldCp;
}//O(n)


Colectie::Colectie() {
	/* de adaugat */
	prim = -1;

	e = new pair<TElem, int>[cp];
	urm = new int[cp];

	//toate pozitiile din vecto sunt marcate ca fiind libere
	for (int i = 0; i < cp - 1; i++)
		urm[i] = i + 1;
	urm[cp - 1] = -1;

	primLiber = 0;
}//O(capacitate)


void Colectie::adauga(TElem elem) {
	/* de adaugat */
	int curent = prim;

	while (curent != -1)
	{
		if (e[curent].first == elem)
		{
			e[curent].second++;
			return;
		}
		curent = urm[curent];
	}

	//elementul este unic
	int i = creeazaNod(elem);
	if (i != -1) {
		urm[i] = prim;
		prim = i;
	}
}//O(n)


bool Colectie::sterge(TElem elem) {
	/* de adaugat */
	int curent = prim;
	int prev = -1;

	while (curent != -1)
	{
		if (e[curent].first == elem)
		{
			if (e[curent].second > 1)
			{
				e[curent].second--;
				return true;
			}
			else
			{
				if (curent == prim) {
					prim = urm[curent];
				}
				else {
					urm[prev] = urm[curent];
				}

				dealoca(curent);
				return true;
			}
		}
		prev = curent;
		curent = urm[curent];
	}
	return false;
}//O(n)



bool Colectie::cauta(TElem elem) const {
	/* de adaugat */
	int curent = prim;

	while (curent != -1)
	{
		if (e[curent].first == elem)
		{
			return true;
		}
		curent = urm[curent];
	}
	return false;
}//O(n)

int Colectie::nrAparitii(TElem elem) const {
	/* de adaugat */
	int curent = prim;
	while (curent != -1)
	{
		if (e[curent].first == elem)
		{
			return e[curent].second;
		}
		curent = urm[curent];
	}
	return 0;
}//O(n)


int Colectie::dim() const {
	/* de adaugat */
	int len = 0;
	int curent = prim;

	while (curent != -1)
	{
		len += e[curent].second;
		curent = urm[curent];
	}
	return len;
}//O(n)


bool Colectie::vida() const {
	/* de adaugat */
	return prim == -1;
}//O(1)

IteratorColectie Colectie::iterator() const {
	return  IteratorColectie(*this);
}
/*
pre:
	c e colectie
post:
	se returneaza un numar, care reprezinta numarul de elemente unice din colectie

	subalgoritm numarElementeUnice()
		nr <- 0
		curent <- prim(c)

		cat timp curent != -1 exec
			nr <- nr + 1
			curent <- urmator(c,curent)
		sf cat timp

		numarElementeUnice() <- nr
	sf subalgoritm
*/

/*
	Complexitate
		Best case: toate elementele sunt la fel => Teta(1) -> intra in while o singura data pentru orice colectie cu n elemente la fel
		Worst case: toate elementele sunt unice => Teta(n) -> se parcurge toata colectia
		Middle case: functia trece prin 1,2...n elemente
					pasii medii => (1+2+...+n)/n = n(n+1)/2*n = (n+1)/2 => Teta(n)

		Complexitate generala O(n)
*/
int Colectie::numaraElementeUnice() const
{
	int nr = 0;
	int curent = prim;

	while (curent != -1)
	{
		nr++;
		curent = urm[curent];
	}

	return nr;
}
/*
	pre: c este Colectie
	post: se returneaza diferenta dinte elementul maxim si elementul minim

	subalgoritm diferenta()
		daca dim(c) == 0 atunci
			diferenta <- -1
  		min <- prim(c), max <- prim(c)
		curent <- prim(c)

		cat timp curent != -1 exec
			daca element(c,it) < min atunci
				min <- element(c)

			daca element(c,it) > max atunci
				max <- element(c)

			curent <- urmator(c,curent)
		sf cat timp
		diferenta <- max - min
	sf subalgortim
*/

/*
	Complexitate
		Best case : Teta(1) - cand toate elementele sunt acelasi numar -> se parcurge doar o data , <el, aparitii>
		Middle case: functia trece prin 1,2...n elemente
					pasii medii => (1+2+...+n)/n = n(n+1)/2*n = (n+1)/2 => Teta(n)
		Worst case : toate elementele sunt unice - se trece prin toate => Teta(n)

		Complexitate generala O(n)
*/

int Colectie::diferenta() const
{
	if (dim() == 0)
		return -1;

	int curent = prim;
	int min = e[prim].first, max=e[prim].first;

	while (curent != -1)
	{
		if (e[curent].first < min)
			min = e[curent].first;
		if (e[curent].first > max)
			max = e[curent].first;

		curent = urm[curent];
	}
	return max - min;
}


Colectie::~Colectie() {
	/* de adaugat */
	delete[] e;
	delete[] urm;
}//O(1)



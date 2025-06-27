#include "IteratorMDO.h"
#include "MDO.h"
#include <iostream>
#include <vector>
#include <algorithm>
#include <exception>

using namespace std;

MDO::MDO(Relatie r) :comp{r} {
	/* de adaugat */
	prim = -1;
	cp = 320;
	len = 0;

	elemente = new Node[cp];
	for (int i = 0; i < cp - 1; ++i)
		elemente[i].nextFree = i+1;
	elemente[cp - 1].nextFree = -1;
	primLiber = 0;
}//O(cp)

int MDO::aloca() {
	/*if (primLiber == -1)
		redim();*/

	//pregatim locatia unde va fi adaugat un element nou
	int index = primLiber;
	primLiber = elemente[index].nextFree;
	elemente[index].left = -1;
	elemente[index].right = -1;
	elemente[index].elems.clear();
	elemente[index].nextFree = -1;
	return index;
}//O(1)

int MDO::tree_min(int node) const {
	//gasim cel mai din din stanga element care iese din nodul nostru
	if (node == -1) return -1;
	while (elemente[node].left != -1) {
		node = elemente[node].left;
	}
	return node;
}//O(log n) - inaltimea arbore

int MDO::succesor(int index)const {
	//gaseste cheia urmatoare
	if (index == -1) return -1;

	if (elemente[index].right != -1) {
		return tree_min(elemente[index].right);
	}

	int parent = elemente[index].parinte;
	int current = index;
	while (parent != -1 && elemente[parent].right == current) {
		current = parent;
		parent = elemente[parent].parinte;
	}
	return parent;
}//O(log n)


void MDO::adauga(TCheie c, TValoare v) {
	if (prim == -1)
	{//nu exista root inca
		int index = aloca();
		elemente[index].cheie = c;
		elemente[index].elems.push_back(v);
		len += 1;
		elemente[index].parinte = -1;
		prim = index;
		return;
	}

	int e = prim;
	int parent = -1;
	while (e != -1)
	{
		parent = e;
		if (comp(c, elemente[e].cheie) && comp(elemente[e].cheie, c)) {//ambele returneaza true => sunt egale
			elemente[e].elems.push_back(v);
			len += 1;
			return;
		}
		else if (comp(c, elemente[e].cheie) && !comp(elemente[e].cheie, c)) {//cheia e mai mica, cautam in stanga
			e = elemente[e].left;
		}
		else {
			e = elemente[e].right;
		}
	}

	//e == -1, nu am gasit cheia
	int index = aloca();
	elemente[index].cheie = c;
	elemente[index].elems.push_back(v);
	len += 1;
	elemente[index].parinte = parent;
	elemente[index].left = -1;
	elemente[index].right = -1;

	if (comp(c, elemente[parent].cheie) && !comp(elemente[parent].cheie, c)) {//punem copilul in ABC, link parent
		elemente[parent].left = index;
	}
	else {
		elemente[parent].right = index;
	}
}//O(n)

vector<TValoare> MDO::cauta(TCheie c) const {
	int curent = prim;
	while (curent != -1)
	{//gasim cheia
		if (comp(c, elemente[curent].cheie) && comp(elemente[curent].cheie, c)) {
			return elemente[curent].elems;
		}
		else if (comp(c, elemente[curent].cheie) && !comp(elemente[curent].cheie, c)) {
			curent = elemente[curent].left;
		}
		else {
			curent = elemente[curent].right;
		}
	}

	return vector<TValoare>();
}//O(n)

bool MDO::sterge(TCheie c, TValoare v) {
	int curent = prim;
	while (curent != -1)
	{
		if (comp(c, elemente[curent].cheie) && comp(elemente[curent].cheie, c)) {
			//cheia e gasita
			auto index = find(elemente[curent].elems.begin(), elemente[curent].elems.end(), v);
			if (index == elemente[curent].elems.end())
				return false;
			elemente[curent].elems.erase(index);
			len -= 1;
			return true;
		}
		else if (comp(c, elemente[curent].cheie) && !comp(elemente[curent].cheie, c)) {
			curent = elemente[curent].left;
		}
		else {
			curent = elemente[curent].right;
		}
	}
	return false;
}//O(n)

int MDO::dim() const {
	return len;
}//O(1)

bool MDO::vid() const {
	return len == 0;
}//O(1)

IteratorMDO MDO::iterator() const {
	return IteratorMDO(*this);
}

MDO::~MDO() {
	delete[] elemente;
}
/*
	preconditie: d este un Multidictionar Ordonat valid
	postconditie: elementele care nu indeplinesc conditia cond sunt eliminate

	subalgoritm filtreaza(d, cond)
		it = iterator(d)
		cat timp valid(it)
			daca cond(valoare(element(it)))
				sterge(d, cheie(element(it)), valoare(element(it)))
			altfel
				urmator(it)
		sf cat timp
	sf subalgoritm
*/

/*
	Complexitate:
		-worst case = best case : se parcurg toate elementele din multidictionar, orice ar fi
		Teta(n) - se verifica conditia, daca nu o indeplineste elementul este eliminat
	Complexitate generala Teta(n)
*/
void MDO::filtreaza(Conditie cond)
{
	IteratorMDO it = iterator();

	while (it.valid())
	{
		if (!cond(it.element().second))
		{
			sterge(it.element().first, it.element().second);
		}
		else
			it.urmator();
	}

}

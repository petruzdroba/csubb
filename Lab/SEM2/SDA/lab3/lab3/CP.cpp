#include "CP.h"
#include <exception>
#include <iostream>
#include <stdio.h>
#include <stdexcept>

using namespace std;


CP::CP(Relatie r) {
	/* de adaugat */
	relatie = r;
	cp = LDI{ r };
}//O(1)


void CP::adauga(TElem e, TPrioritate p) {
	/* de adaugat */
	this->cp.add(make_pair(e, p));
}//O(n)

//arunca exceptie daca coada e vida
Element CP::element() const {
	/* de adaugat */
	if (cp.size() == 0)
		throw invalid_argument("element->empty");

	Node* current = cp.begin();
	Element highestPriorityElement = current->data;

	return highestPriorityElement;
}//O(1)

Element CP::sterge() {
	/* de adaugat */
	if (this->cp.size() == 0)
		throw invalid_argument("sterge->empty");

	Element highestPriorityElement = element();

	Node* current = cp.begin();
	cp.erase(current);

	return highestPriorityElement;
}//O(1)

bool CP::vida() const {
	/* de adaugat */
	return cp.size() <= 0;
}//O(1)


CP::~CP() {
	while (!vida())
	{
		sterge();
	}
};// O(n)


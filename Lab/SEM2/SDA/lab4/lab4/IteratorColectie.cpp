#include "IteratorColectie.h"
#include "Colectie.h"
#include<stdexcept>

IteratorColectie::IteratorColectie(const Colectie& c): col(c) {
	/* de adaugat */
	curent = col.prim;
	pozInNod = 1;
}//O(1)

void IteratorColectie::prim() {
	/* de adaugat */
	curent = col.prim;
	pozInNod = 1;
}//O(1)


void IteratorColectie::urmator() {
	/* de adaugat */
	if (!valid())
		throw std::invalid_argument("Iteratorul nu este valid\n");
	if (pozInNod == col.e[curent].second)
	{
		curent = col.urm[curent];
		pozInNod = 1;
	}
	else {
		pozInNod++;
	}
}//O(1)


bool IteratorColectie::valid() const {
	/* de adaugat */
	if (pozInNod <= col.e[curent].second && curent > -1)
		return true;
	return false;
}//O(1)



TElem IteratorColectie::element() const {
	/* de adaugat */
	if (!valid())
		throw std::invalid_argument("Iteratorul nu este valid\n");
	return col.e[curent].first;
}//O(1)

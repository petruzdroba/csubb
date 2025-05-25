#include "IteratorColectie.h"
#include "Colectie.h"
#include<stdexcept>

IteratorColectie::IteratorColectie(const Colectie& c): col(c) {
	curent = 0;
}//O(1)

void IteratorColectie::prim() {
	curent = 0;
}//O(1)


void IteratorColectie::urmator() {
	if (!valid())
		throw std::invalid_argument("Iteratorul nu este valid\n");	
	curent++;
	while (curent < col.cp && col.elemente[curent] == NULL_TELEM) {
		curent++;
	}
}//O(n)


bool IteratorColectie::valid() const {
	return  curent < col.cp && col.elemente[curent] != NULL_TELEM;
}//O(1)



TElem IteratorColectie::element() const {
	if (!valid())
		throw std::invalid_argument("Iteratorul nu este valid\n");
	return col.elemente[curent];
}//O(1)

#include "IteratorMDO.h"
#include "MDO.h"
#include <stdexcept>

IteratorMDO::IteratorMDO(const MDO& d) : dict(d) {
	prim();
}//O(1)

void IteratorMDO::prim() {
	//punem in curent indexul cheii cu cea mai mica val
	curent = dict.tree_min(dict.prim);
	pozInVect = 0;
}//O(1)

void IteratorMDO::urmator() {
	if (!valid())
		throw std::invalid_argument("Argument faulty!\n");

	if (pozInVect + 1 < (int)dict.elemente[curent].elems.size()) {
		pozInVect++;//daca inca nu am parcurs toate elem din vect, continuam
	}
	else {//altfel gasim urmatoarea cheie, si resetam
		curent = dict.succesor(curent);
		pozInVect = 0;
	}
}//O(log n)

bool IteratorMDO::valid() const {
	return curent != -1 && pozInVect< (int)dict.elemente[curent].elems.size();
}//O(1)

TElem IteratorMDO::element() const {
	if (!valid())
		throw std::invalid_argument("Iterator faulty!\n");

	return std::make_pair(
		dict.elemente[curent].cheie,
		dict.elemente[curent].elems[pozInVect]
	);
}//O(1)



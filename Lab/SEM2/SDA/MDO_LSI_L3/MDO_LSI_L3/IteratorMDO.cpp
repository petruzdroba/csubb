#include "IteratorMDO.h"
#include "MDO.h"

IteratorMDO::IteratorMDO(const MDO& d) : dict(d){
	prim();
}

void IteratorMDO::prim(){
	curent = dict.prim;
}

void IteratorMDO::urmator(){
	if (!valid())
		throw std::exception("Iterator invalid urmator");
	curent = curent->next;
}

bool IteratorMDO::valid() const{
	return curent != nullptr;
}

TElem IteratorMDO::element() const{
	if (!valid())
		throw std::exception("Iterator invalid element");
	return curent->e;
}



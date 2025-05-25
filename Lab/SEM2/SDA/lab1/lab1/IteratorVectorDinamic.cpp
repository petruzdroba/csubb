#include "IteratorVectorDinamic.h"
#include "VectorDinamic.h"
#include <exception>
#include <iostream>

using namespace std;

IteratorVectorDinamic::IteratorVectorDinamic(const VectorDinamic& _v)
	: v(_v){
	curent = 0;
}//Complexitate O(1)




void IteratorVectorDinamic::prim() {
	curent = 0;
}//Complexitate O(1)



bool IteratorVectorDinamic::valid() const{
	if (curent >= v.dim())
		return false;
	return true;
}//Complexitate O(1)



TElem IteratorVectorDinamic::element() const{
	try {
		if (!this->valid())
			throw invalid_argument("Iterator invalid");
		return v.element(curent);
	}
	catch (const invalid_argument& exception) {
		cout << exception.what() << endl;
		return -1;
	}//Complexitate O(1)
}



void IteratorVectorDinamic::urmator() {
	try {
		if (!this->valid())
			throw invalid_argument("Iterator invalid");
		if (curent < v.dim())
			curent++;
	}
	catch (const invalid_argument& exception) {
		cout << exception.what() << endl;
	}
}//Complexitate O(1)


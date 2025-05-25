#pragma once
#include "VectorDinamic.h"

class IteratorVectorDinamic {

	friend class VectorDinamic;
private:

	IteratorVectorDinamic(const VectorDinamic& );
	//constructorul primeste o referinta catre Container
	//iteratorul va referi primul element din container

	const VectorDinamic& v;
	//contine o referinta catre containerul pe care il itereaza
	int curent;

	/* aici e reprezentarea specifica a iteratorului */

public:

		void prim();
		//reseteaza pozitia iteratorului la inceputul containerului

		void urmator();
		//muta iteratorul in container
		// arunca exceptie daca iteratorul nu e valid

		bool valid() const;
		//verifica daca iteratorul e valid (indica un element al containerului)

		TElem element() const;
		//returneaza valoarea elementului din container referit de iterator
		//arunca exceptie daca iteratorul nu e valid

};
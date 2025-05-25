#pragma once
// tip de data generic (pentru moment este intreg)

typedef int TElem;

class IteratorVectorDinamic;

class VectorDinamic
{

private:
	/* aici e reprezentarea */
	int capacitate;
	int dimensiune;
	TElem *elemente;


	void redim();
	// redimensionare

public:
	VectorDinamic(int capacitate);
	// constructor
	// arunca exceptie in cazul in care capacitate e <=0

	int dim() const;
	// returnare dimensiune

	TElem element(int i) const;
	// returnare element
	// arunca exceptie daca i nu e valid
	// indicii ii consideram de la 0

	TElem modifica(int i, TElem e);
	// modifica element de pe pozitia i si returneaza vechea valoare
	// arunca exceptie daca i nu e valid

	void adaugaSfarsit(TElem e);
	// adaugare element la sfarsit

	void adauga(int i, TElem e);
	// adaugare element pe o pozitie i
	// arunca exceptie daca i nu e valid

	TElem sterge(int i);
	// sterge element de pe o pozitie i si returneaza elementul sters
	// arunca exceptie daca i nu e valid

	IteratorVectorDinamic iterator();
	// returnare iterator

	bool suntUnice() const;
	//returneaza true daca elementele vectorului sunt unice
	//false in caz contrar

	~VectorDinamic();
	// destructor
};

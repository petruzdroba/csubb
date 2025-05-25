#include "VectorDinamic.h"
#include "IteratorVectorDinamic.h"
#include <exception>
#include <iostream>

using namespace std;

void VectorDinamic::redim()
{
	TElem* doubleElemente = new TElem[capacitate * 2];
	for (int index = 0; index < dimensiune; ++index)
		doubleElemente[index] = elemente[index];

	capacitate *= 2;

	delete[] elemente;

	elemente = doubleElemente;
}//Complexitate O(n)

VectorDinamic::VectorDinamic(int cp)
{
	try
	{
		if (cp <= 0)
			throw invalid_argument("Capacitatea nu e mai mare ca 0");

		capacitate = cp;
		dimensiune = 0;
		elemente = new TElem[cp];
	}
	catch (const invalid_argument& exception)
	{
		cout <<exception.what()<<endl;
	}
}//Complexitate O(1)

VectorDinamic::~VectorDinamic()
{
	delete[] elemente;
}

int VectorDinamic::dim() const
{
	return dimensiune;
}//Complexitate O(1) constanta

TElem VectorDinamic::element(int i) const
{
	try
	{
		if (i < 0 || i > dimensiune)
			throw invalid_argument("Pozitia nu e valida:element");
		return elemente[i];//changed from -1 , to 0
	}
	catch(const invalid_argument& exception)
	{
		cout << exception.what() << endl;
		return 0;
	}//Complexitate O(1) constanta
}

void VectorDinamic::adaugaSfarsit(TElem e)
{
	if (dimensiune >= capacitate)
		redim();

	elemente[dimensiune] = e;
	dimensiune++;//increase after
}//Complexitate O(1)

void VectorDinamic::adauga(int i, TElem e)
{
	try
	{
		if (i < 0)
			throw invalid_argument("Pozitia nu e valida:adauga");

		if (dimensiune >= capacitate) 
			redim();

		for (int index = dimensiune - 1; index >= i; --index)//redim, delete[] heap corruption fix
		{
			elemente[index + 1] = elemente[index];
		}
		elemente[i] = e;
		dimensiune++;//increase after
	}
	catch (const invalid_argument& exception)
	{
		cout << exception.what() << endl;
	}
}//Complexitate O(n)

TElem VectorDinamic::modifica(int i, TElem e)
{
	try
	{
		if (i < 0 || i > dimensiune)//>=
			throw invalid_argument("Pozitia nu este valida:modifica");

		TElem modifiedElement = elemente[i];
		elemente[i] = e;
		return modifiedElement;
	}
	catch (const invalid_argument& exception)
	{
		cout << exception.what() << endl;
		return 0;
	}
}//Complexitate(1)

TElem VectorDinamic::sterge(int i)
{
	try
	{
		if (i < 0 || i > dimensiune)
			throw invalid_argument("Pozitia nu este valida:sterge");

		TElem deletedElement = elemente[i];

		for (int index = i; index < dimensiune - 1; index++)
		{
			elemente[index] = elemente[index + 1];
		}
		dimensiune--;

		return deletedElement;
	}
	catch (const invalid_argument& exception)
	{
		//cout << exception.what() << endl;
		return 0;
	}
}//Complexitate O(n)

bool VectorDinamic::suntUnice() const
{
	/*
	Pseudocod
	SubAlgoritm suntUnice(v)
		pre: v apartine VectorDinamic, v.n > 0
		post: v.suntUnice() apartine {0,1}, 0 daca nu sunt unice, 1 daca sunt
		@arunca exceptie daca vectorul este gol

	Metoda:VectorDinamic suntUnice(v)
		daca v.n <= 0 atunci
			@arunca exceptie
		sfdaca

		pentru index = 0, v.n - 1,1 executa
			pentru jndex = index + 1, v.n, 1 executa
				daca v.elemente[index] = v.elemente[jndex] atunci
					returneaza 0
				sfdaca
			sfpentru
		sfpentru
		returneaza 1
	*/
	/*
	Analiza complexitate
		Caz favorabil: primele doua elemente ale vectorului sunt egale T(n) = 2 apartine Teta(1)
		Caz nefavorabil : toate elementele sunt unice Suma de la 0 la n-1 din Suma de la j = i+ 1 la n din 1
							= Suma de la 0 la n-1  din (n - i - 1 + 1) = Suma de la 0 la n-1 din (n-i) = n + (n -1) + ... + (n-n+1)+0 = n(n-1)/2
							=T(n) apartine Teta(n^2)
		Caz mediu : pentru un i fixat al doile for poate fi executat de 1,2,3...n-i ori pasi
					pasi medii = (1+2+3+...+n-i)/(n-i) = (n-i)(n-i-1)/2(n-i) = (n-i-1)/2
					acesti pasi medii se inampla de n ori = n(n-i-1)/2 = T(n) apartine Teta(n^2)
	*/
	try
	{
		if (dimensiune <= 0)
			throw invalid_argument("Vectorul nu are elemente: suntUnice");

		for (int index = 0; index < dimensiune - 1; ++index)
		{
			for (int jndex = index + 1; jndex < dimensiune; ++jndex)
			{
				if (element(index) == element(jndex))
					return false;
			}
		}
		return true;
	}
	catch (const invalid_argument& exception)
	{
		//cout << exception.what() << endl;
		return false;
	}
}//Complexitate generala O(n^2)

IteratorVectorDinamic VectorDinamic::iterator()
{
	return IteratorVectorDinamic(*this);
}//Complexitate O(1)

#include <iostream>
#include "CP.h"
#include "TestExtins.h"
#include "TestScurt.h"
#include "testLDI.h"
#include "assert.h"

using namespace std;

bool relatie (TPrioritate p1, TPrioritate p2) {
	if (p1 <= p2) {
		return true;
	}
	else {
		return false;
	}
}
/*
pre:
	c apartine CP
post
	c are exact aceleasi elemente
subalgortim tiparesteCoada(CoadaPrioritati c)
	{se creeaza o coada auxiliara vida} copy
	cat timp not vida(c) executa
		sterge(c,el)
		@afisam el (numai data)
		adauga(copy, el)
	sf cat timp

	cat timp not vida(copy) executa
		sterge(copy, el)
		adauga(c,el);
	sf cat timp
	
*/
void tiparesteCoada(CP& c)
{
	CP copy{ relatie };
	while (!c.vida())
	{
		Element el = c.sterge();
		cout << el.first << " ";
		copy.adauga(el.first, el.second);
	}

	while (!copy.vida())
	{
		Element el = copy.sterge();

		c.adauga(el.first, el.second);
	}
}//TetaO(n)

int main() {
	testLDI();
	testAll();
	testAllExtins();
 	cout<<"End"<<endl;

	CP c{ relatie };
	//adaugam 5, 3, 10, 2, 12 -> rezultat: 2, 3, 5, 10, 12
	c.adauga(5, 5);
	c.adauga(3,3);
	c.adauga(10,10);
	c.adauga(2,2);
	c.adauga(12,12);
	tiparesteCoada(c);
	assert(c.vida() == false);
	assert(c.element().first == 2);
}

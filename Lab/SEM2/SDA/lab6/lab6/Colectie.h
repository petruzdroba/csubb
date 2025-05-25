#pragma once

#define NULL_TELEM 2147483647
typedef int TElem;

class IteratorColectie;

class Colectie
{
	friend class IteratorColectie;

private:
	/* aici e reprezentarea */
	int cp;
	TElem *elemente;
	int* urmator;
	int primLiber;//pozitie unde se poate adauga un element, daca exista coliziune

	void actPrimLiber();

	int d(TElem e)const ;//functie dispersie

	void resize();
public:
		//constructorul implicit
		Colectie();

		//adauga un element in colectie
		void adauga(TElem e);

		//sterge o aparitie a unui element din colectie
		//returneaza adevarat daca s-a putut sterge
		bool sterge(TElem e);

		//verifica daca un element se afla in colectie
		bool cauta(TElem elem) const;

		//returneaza numar de aparitii ale unui element in colectie
		int nrAparitii(TElem elem) const;


		//intoarce numarul de elemente din colectie;
		int dim() const;

		//verifica daca colectia e vida;
		bool vida() const;

		//returneaza un iterator pe colectie
		IteratorColectie iterator() const;

		int diferenta() const;

		// destructorul colectiei
		~Colectie();

};


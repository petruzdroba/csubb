#pragma once
#include <vector>
#include "Disciplina.h"
#include "vector.h"

using namespace std;

class Repository {
private:
	VectorDinamic<Disciplina> discipline;
public:

	Repository() noexcept = default;
	/*
		constructor repository
	*/

	int getLungime()const noexcept;
	/*
		functie care returneaza lungimea listei de obiecte
		output:
			un numar reprezentand numarul de obiecte din lista
	*/

	const VectorDinamic<Disciplina>& getAll() const noexcept {
		return discipline.getAll();  // Const version
	}
	/*
		functie care returneaza toate obiectele din lista
		output:
			vector de Disciplina
	*/

	void adauga(const Disciplina& disciplina);
	/*
		functie care adauga un obiect disciplina in repository
		input:
			disciplina: Disciplina
				denumire : string != ''
				ore : int > 0
				tip : string != ''
				profesor: string != ''
	*/

	void sterge(const int pozitie);
	/*
		functie care sterge un obiect de la pozitia pozitie
		input:
			pozitie : int , pozitie < repo.getLungime()
		output:
			un obiect de tipul Disciplina care se afla la pozitia pozitie
			un obiect gol daca pozitia nu este consisitenta
	*/

	void modifica(const int pozitie, const Disciplina& newDisciplina);
	/*
		functie care inlocuieste obiectul de la pozitia pozitie cu newDisciplina
		input:
			pozitie : int , pozitie < repo.getLungime()
			disciplina: Disciplina
				denumire : string != ''
				ore : int > 0
				tip : string != ''
				profesor: string != ''
	*/
};
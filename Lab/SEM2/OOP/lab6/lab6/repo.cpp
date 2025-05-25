#include "repo.h"

using namespace std;

void Repository::adauga(const Disciplina& disciplina)
{
	discipline.push_back(disciplina);
}

void Repository::sterge(const int pozitie)
{
	//Disciplina disciplinaStearsa = getAll()[pozitie];
	//Disciplina disciplinaStearsa = discipline.all()[pozitie];
	//discipline.erase(discipline.begin() + pozitie);
	discipline.erase(pozitie);
	//return disciplinaStearsa;
}

void Repository::modifica(const int pozitie, const Disciplina& newDisciplina)
{
	//discipline[pozitie] = newDisciplina;
	discipline.set(pozitie, newDisciplina);
}


int Repository::getLungime() const noexcept
{
	return discipline.size(); //type size_t
}

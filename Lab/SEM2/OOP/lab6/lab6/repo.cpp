#include "repo.h"

using namespace std;

vector<Disciplina> Repository::getAll() const
{
	return discipline;
}

void Repository::adauga(const Disciplina& disciplina)
{
	discipline.push_back(disciplina);
}

Disciplina Repository::sterge(const int pozitie)
{
	Disciplina disciplinaStearsa = getAll()[pozitie];
	discipline.erase(discipline.begin() + pozitie);
	return disciplinaStearsa;
}

void Repository::modifica(const int pozitie, const Disciplina& newDisciplina)
{
	discipline[pozitie] = newDisciplina;
}


int Repository::getLungime() const
{
	return (int)discipline.size(); //type size_t
}

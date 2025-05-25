#include "repo.h"
#include "exception.h"

using namespace std;

void Repository::adauga(const Disciplina& disciplina)
{
	for (const Disciplina& d : discipline)
	{
		if (d.getDenumire() == disciplina.getDenumire())
			throw MyException("Repository: Disciplina existenta");
	}
	discipline.push_back(disciplina);
}

void Repository::sterge(const int pozitie)
{
	discipline.erase(discipline.begin() + pozitie);
}

void Repository::modifica(const int pozitie, const Disciplina& newDisciplina)
{
	//discipline.set(pozitie, newDisciplina);
	discipline[pozitie] = newDisciplina;
}


int Repository::getLungime() const noexcept
{
	return (int)discipline.size(); //type size_t
}

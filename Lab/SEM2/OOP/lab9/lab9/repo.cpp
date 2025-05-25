#include "repo.h"
#include "exception.h"
#include <fstream>

using namespace std;

void Repository::adauga(const Disciplina& disciplina)
{
	for (const Disciplina& d : discipline)
	{
		if (d.getDenumire() == disciplina.getDenumire())
			throw MyException("Repository: Disciplina existenta");
	}
	discipline.push_back(disciplina);
	writeToFile(disciplina);
}

void Repository::sterge(const int pozitie)
{
	discipline.erase(discipline.begin() + pozitie);
	overwriteToFile();
}

void Repository::modifica(const int pozitie, const Disciplina& newDisciplina)
{
	discipline[pozitie] = newDisciplina;
	overwriteToFile();
}



int Repository::getLungime() const noexcept
{
	return (int)discipline.size(); //type size_t
}

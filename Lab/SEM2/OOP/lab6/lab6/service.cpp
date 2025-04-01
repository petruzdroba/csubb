#include "service.h"
#include "validator.h"
#include "Disciplina.h"

bool Service::adaugaService(const string& denumire, const int& ore, const string& tip, const string& profesor)
{
	if (!validatorDisciplina(denumire, ore, tip, profesor))
		return false;

	Disciplina disciplina{ denumire, ore, tip, profesor };
	repo.adauga(disciplina);

	return true;
}

Disciplina Service::stergeService(const int pozitie)
{
	if (pozitie < 0 || pozitie > repo.getLungime())
		return Disciplina{ "", 0,"","" };

	return repo.sterge(pozitie);
}

bool Service::modificaService(const int pozitie, const string& denumire, const int& ore, const string& tip, const string& profesor)
{
	if (pozitie < 0 || pozitie > repo.getLungime())
		return false;

	if (!validatorDisciplina(denumire, ore, tip, profesor))
		return false;

	Disciplina disciplina{ denumire, ore, tip, profesor };
	repo.modifica(pozitie,disciplina);

	return true;
}

int Service::getLungimeService() const
{
	return repo.getLungime();
}

vector<Disciplina> Service::getAllService() const
{
	return repo.getAll();
}

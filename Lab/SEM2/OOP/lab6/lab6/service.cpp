#include "service.h"
#include "validator.h"
#include "Disciplina.h"
#include <algorithm>

bool Service::adaugaService(const string& denumire, const int& ore, const string& tip, const string& profesor)
{
	validatorDisciplina(denumire, ore, tip, profesor);

	Disciplina disciplina{ denumire, ore, tip, profesor };
	repo.adauga(disciplina);

	return true;
}

void Service::stergeService(const int pozitie)
{
	if (pozitie < 0 || pozitie >= repo.getLungime())
		throw invalid_argument("Service : pozitie invalida");
	/*if (pozitie < 0 || pozitie >= repo.getLungime())
		return Disciplina{ "", 0,"","" };*/

	repo.sterge(pozitie);
}

bool Service::modificaService(const int pozitie, const string& denumire, const int& ore, const string& tip, const string& profesor)
{
	if (pozitie < 0 || pozitie >= repo.getLungime())
		throw invalid_argument("Service : pozitie invalida");

	validatorDisciplina(denumire, ore, tip, profesor);

	Disciplina disciplina{ denumire, ore, tip, profesor };
	repo.modifica(pozitie,disciplina);

	return true;
}

int Service::getLungimeService() const noexcept
{
	return repo.getLungime();
}

const VectorDinamic<Disciplina>& Service::getAllService() noexcept
{
	return repo.getAll();
}

const VectorDinamic<Disciplina> Service::filtrareOre(const int nrOre) 
{
	VectorDinamic<Disciplina> filtrate;

	for (Disciplina d : repo.getAll())
	{
		if (d.getOre() >= nrOre)
			filtrate.push_back(d);
	}

	return filtrate;
}

const VectorDinamic<Disciplina> Service::filtrareProfesor(const string numeProfesor)
{
	VectorDinamic<Disciplina> filtrate;


	for (Disciplina d : repo.getAll())
	{
		if (d.getProfesor() == numeProfesor)
			filtrate.push_back(d);
	}

	return filtrate;
}

void sortare(VectorDinamic<Disciplina>& vector, bool(*cmp)(const Disciplina& a, const Disciplina& b)) noexcept
{
	sort(vector.begin(), vector.end(), cmp);
}

bool cmpDenumire(const Disciplina& a, const Disciplina& b) {
	return a.getDenumire() < b.getDenumire();
}

const VectorDinamic<Disciplina> Service::sortareDenumire() 
{
	VectorDinamic<Disciplina> sortate = repo.getAll();
	sortare(sortate, cmpDenumire);
	return sortate;
}

bool cmpTip(const Disciplina& a, const Disciplina& b) {
	return a.getTip() < b.getTip();
}

const VectorDinamic<Disciplina> Service::sortareTip()
{ 
	VectorDinamic<Disciplina> sortate = repo.getAll();
	sortare(sortate, cmpTip);
	return sortate;
}

bool cmpOre(const Disciplina& a, const Disciplina& b) {
	return a.getOre() < b.getOre();
}

const VectorDinamic<Disciplina> Service::sortareOre()
{
	VectorDinamic<Disciplina> sortate = repo.getAll();
	sortare(sortate, cmpOre);
	return sortate;
}

bool cmpProfesor(const Disciplina& a, const Disciplina& b) {
	return a.getProfesor() < b.getProfesor();
}

const VectorDinamic<Disciplina> Service::sortareProfesor()
{
	VectorDinamic<Disciplina> sortate = repo.getAll();
	sortare(sortate, cmpProfesor);
	return sortate;
}

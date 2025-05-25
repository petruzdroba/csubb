#include <algorithm>
#include <iterator>
#include <string.h>
#include <random>
#include <fstream>

#include "service.h"
#include "validator.h"
#include "exception.h"
#include "Disciplina.h"

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
		throw MyException("Service : pozitie invalida");
	/*if (pozitie < 0 || pozitie >= repo.getLungime())
		return Disciplina{ "", 0,"","" };*/

	repo.sterge(pozitie);
}

bool Service::modificaService(const int pozitie, const string& denumire, const int& ore, const string& tip, const string& profesor)
{
	if (pozitie < 0 || pozitie >= repo.getLungime())
		throw MyException("Service : pozitie invalida");

	validatorDisciplina(denumire, ore, tip, profesor);

	Disciplina disciplina{ denumire, ore, tip, profesor };
	repo.modifica(pozitie, disciplina);

	return true;
}

int Service::getLungimeService() const noexcept
{
	return repo.getLungime();
}

const vector<Disciplina>& Service::getAllService() noexcept
{
	return repo.getAll();
}

const vector<Disciplina> Service::filtrareOre(const int nrOre)
{
	vector<Disciplina> filtrate;

	copy_if(repo.getAll().begin(), repo.getAll().end(),
		back_inserter(filtrate),
		[nrOre](const Disciplina& d) {
			return d.getOre() >= nrOre;
		});

	return filtrate;
}

const vector<Disciplina> Service::filtrareProfesor(const string numeProfesor)
{
	vector<Disciplina> filtrate;

	copy_if(repo.getAll().begin(), repo.getAll().end(),
		back_inserter(filtrate),
		[numeProfesor](const Disciplina& d) {
			return d.getProfesor() == numeProfesor;
		});

	return filtrate;
}

void sortare(vector<Disciplina>& vector, bool(*cmp)(const Disciplina& a, const Disciplina& b)) noexcept
{
	sort(vector.begin(), vector.end(), cmp);
}

bool cmpDenumire(const Disciplina& a, const Disciplina& b) {
	return a.getDenumire() < b.getDenumire();
}

const vector<Disciplina> Service::sortareDenumire()
{
	vector<Disciplina> sortate = repo.getAll();
	sortare(sortate, cmpDenumire);
	return sortate;
}

bool cmpTip(const Disciplina& a, const Disciplina& b) {
	return a.getTip() < b.getTip();
}

const vector<Disciplina> Service::sortareTip()
{
	vector<Disciplina> sortate = repo.getAll();
	sortare(sortate, cmpTip);
	return sortate;
}

bool cmpOre(const Disciplina& a, const Disciplina& b) {
	return a.getOre() < b.getOre();
}

const vector<Disciplina> Service::sortareOre()
{
	vector<Disciplina> sortate = repo.getAll();
	sortare(sortate, cmpOre);
	return sortate;
}

bool cmpProfesor(const Disciplina& a, const Disciplina& b) {
	return a.getProfesor() < b.getProfesor();
}

const vector<Disciplina> Service::sortareProfesor()
{
	vector<Disciplina> sortate = repo.getAll();
	sortare(sortate, cmpProfesor);
	return sortate;
}

void Service::adaugaContractService(const string denumire)
{
	auto found = find_if(repo.getAll().begin(), repo.getAll().end(),
		[&denumire](const Disciplina& d) {
			return d.getDenumire() == denumire;
		});

	if (found != repo.getAll().end())
	{
		//adaug in contract
		Disciplina newDisciplina = *found;
		contract.adaugaContract(newDisciplina);
	}
	else {
		throw MyException("Service: Denumire inexistenta");
	}
}

int Service::getContractLungime() const
{
	return contract.getLungime();
}

void Service::deleteAllContract()
{
	contract.deleteAll();
}

void Service::randomPopulateContract(const int nrDiscipline)
{
	if (nrDiscipline <= 0)
		throw MyException("Service: Nr Discipline imposibil");

	if (repo.getLungime() <= 0)
		throw MyException("Service: Nu exista discipline");

	mt19937 mt{ random_device{}() };
	uniform_int_distribution<> dist(0, repo.getLungime() - 1);

	for (int index = 1; index <= nrDiscipline; index++)
	{
		int rndNr = dist(mt);
		contract.adaugaContract(repo.getAll()[rndNr]);
	}
}

void Service::exportContract(const string path) const
{
	ofstream file(path);
	for (int index = 0; index < contract.getLungime(); ++index)
	{
		file << contract.getAll()[index].getDenumire() << "," << contract.getAll()[index].getOre() << "," << contract.getAll()[index].getTip() << "," << contract.getAll()[index].getProfesor() << "\n";
	}
	file.close();
}

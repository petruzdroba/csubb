#include "teste.h"
#include <iostream>
#include <cassert>

#include "Disciplina.h"
#include "repo.h"
#include "service.h"

void testDisciplinaConstructor() {
	Disciplina disciplina{ "Disciplina test 1", 1, "Tip test 1", "Profesor test 1" };

	assert(disciplina.getDenumire() == "Disciplina test 1");
	assert(disciplina.getOre() == 1);
	assert(disciplina.getTip() == "Tip test 1");
	assert(disciplina.getProfesor() == "Profesor test 1");

	string newDenumire = "Disciplina test 2", newTip = "Tip test 2", newProfesor = "Profesor test 2";
	int newOre = 2;

	disciplina.setDenumire(newDenumire);
	disciplina.setOre(newOre);
	disciplina.setTip(newTip);
	disciplina.setProfesor(newProfesor);

	assert(disciplina.getDenumire() == "Disciplina test 2");
	assert(disciplina.getOre() == 2);
	assert(disciplina.getTip() == "Tip test 2");
	assert(disciplina.getProfesor() == "Profesor test 2");

	Disciplina disciplina2 = disciplina;
	assert(disciplina2.getDenumire() == "Disciplina test 2");
	assert(disciplina2.getOre() == 2);
	assert(disciplina2.getTip() == "Tip test 2");
	assert(disciplina2.getProfesor() == "Profesor test 2");
}

void testRepositoryAdauga()
{
	Repository repo;
	assert(repo.getLungime() == 0);

	Disciplina disciplina{ "Disciplina test 1", 1, "Tip test 1", "Profesor test 1" };
	repo.adauga(disciplina);
	assert(repo.getLungime() == 1);

	for (int index = 2; index <= 100; ++index) {
		repo.adauga(disciplina);
		assert(repo.getLungime() == index );
	}

	for (Disciplina d : repo.getAll())
	{
		assert(d.getDenumire() == "Disciplina test 1");
		assert(d.getOre() == 1);
		assert(d.getTip() == "Tip test 1");
		assert(d.getProfesor() == "Profesor test 1");
	}
}

void testRepositorySterge()
{
	Repository repo;

	Disciplina disciplina1{ "Disciplina test 1", 1, "Tip test 1", "Profesor test 1" };
	repo.adauga(disciplina1);
	Disciplina disciplina2{ "Disciplina test 2", 2, "Tip test 2", "Profesor test 2" };
	repo.adauga(disciplina2);

	assert(repo.sterge(1) == disciplina2);
}

void testRepositoryModifica()
{
	Repository repo;

	Disciplina disciplina1{ "Disciplina test 1", 1, "Tip test 1", "Profesor test 1" };
	repo.adauga(disciplina1);
	Disciplina disciplina2{ "Disciplina test 2", 2, "Tip test 2", "Profesor test 2" };
	repo.adauga(disciplina2);

	Disciplina disciplina3{ "Disciplina test 3", 3, "Tip test 3", "Profesor test 3" };
	repo.modifica(0, disciplina3);

	assert(repo.getAll()[0] == disciplina3);
}

void testAdaugaService()
{
	Repository repo;
	Service service{ repo };

	string newDenumire = "Disciplina test 1", newTip = "Tip test 1", newProfesor = "Profesor test 1";
	int newOre = 1;

	assert(service.adaugaService(newDenumire, newOre, newTip, newProfesor));
	assert(service.adaugaService(newDenumire, newOre, newTip, newProfesor));
	assert(service.adaugaService(newDenumire, newOre, newTip, newProfesor));

	assert(service.getLungimeService() == 3);
	Disciplina disciplina{ "Disciplina test 1", 1, "Tip test 1", "Profesor test 1" };
	for (Disciplina d : service.getAllService()) {
		assert(d == disciplina);
	}

	assert(!service.adaugaService("", newOre, newTip, newProfesor));
	assert(!service.adaugaService(newDenumire, 0, newTip, newProfesor));
	assert(!service.adaugaService(newDenumire, newOre, "", newProfesor));
	assert(!service.adaugaService(newDenumire, newOre, "", ""));
}

void testSergeService()
{
	Repository repo;
	Service service{ repo };

	string newDenumire = "Disciplina test 1", newTip = "Tip test 1", newProfesor = "Profesor test 1";
	int newOre = 1;

	assert(service.adaugaService(newDenumire, newOre, newTip, newProfesor));
	assert(service.adaugaService(newDenumire, newOre, newTip, newProfesor));
	assert(service.adaugaService(newDenumire, newOre, newTip, newProfesor));

	assert(service.getLungimeService() == 3);
	Disciplina disciplina{ "Disciplina test 1", 1, "Tip test 1", "Profesor test 1" };
	
	assert(service.stergeService(2) == disciplina);
	assert(service.stergeService(1) == disciplina);
	assert(service.stergeService(0) == disciplina);

	Disciplina disciplinaDef{ "", 0,"","" };
	assert(service.stergeService(-1) == disciplinaDef);
	assert(service.stergeService(1) == disciplinaDef);
}

void testModificaService()
{
	Repository repo;
	Service service{ repo };

	string denumire = "Disciplina test 1", tip = "Tip test 1", profesor = "Profesor test 1";
	int ore = 1;

	service.adaugaService(denumire, ore, tip, profesor);
	service.adaugaService(denumire, ore, tip, profesor);
	service.adaugaService(denumire, ore, tip, profesor);

	string newDenumire = "Disciplina test 2", newTip = "Tip test 2", newProfesor = "Profesor test 2";
	int newOre = 2;

	assert(service.modificaService(0, newDenumire, newOre, newTip, newProfesor));
	Disciplina disciplinaModif{ newDenumire, newOre, newTip, newProfesor};

	assert(service.getAllService()[0] == disciplinaModif);

	assert(!service.modificaService(-1, newDenumire, newOre, newTip, newProfesor));
	assert(!service.modificaService(5, newDenumire, newOre, newTip, newProfesor));

	assert(!service.modificaService(0, "", newOre, newTip, newProfesor));
	assert(!service.modificaService(0, newDenumire, -1, newTip, newProfesor));
	assert(!service.modificaService(0, newDenumire, newOre, "", newProfesor));
	assert(!service.modificaService(0, newDenumire, newOre, newTip, ""));
}

void testAll(){
	testDisciplinaConstructor();
	testRepositoryAdauga();
	testRepositorySterge();
	testRepositoryModifica();

	testAdaugaService();
	testSergeService();
	testModificaService();
	cout << "Teste rulate\n";
}
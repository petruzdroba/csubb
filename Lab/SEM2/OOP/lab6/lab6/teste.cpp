#include "teste.h"
#include <iostream>
#include <cassert>

#include "Disciplina.h"
#include "repo.h"
#include "service.h"
#include "vector.h"

static void testDisciplinaConstructor() {
	Disciplina disciplina{ "Disciplina test 1", 1, "Tip test 1", "Profesor test 1" };

	assert(disciplina.getDenumire() == "Disciplina test 1");
	assert(disciplina.getOre() == 1);
	assert(disciplina.getTip() == "Tip test 1");
	assert(disciplina.getProfesor() == "Profesor test 1");

	string newDenumire = "Disciplina test 2", newTip = "Tip test 2", newProfesor = "Profesor test 2";
	constexpr int newOre = 2;

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

static void testRepositoryAdauga()
{
	Repository repo;
	assert(repo.getLungime() == 0);

	Disciplina disciplina{ "Disciplina test 1", 1, "Tip test 1", "Profesor test 1" };
	repo.adauga(disciplina);
	assert(repo.getLungime() == 1);

	for (int index = 2; index <= 100; ++index) {
		repo.adauga(disciplina);
		assert(repo.getLungime() == index);
	}

	for (Disciplina d : repo.getAll())
	{
		assert(d.getDenumire() == "Disciplina test 1");
		assert(d.getOre() == 1);
		assert(d.getTip() == "Tip test 1");
		assert(d.getProfesor() == "Profesor test 1");
	}
}

static void testRepositorySterge()
{
	Repository repo;

	Disciplina disciplina1{ "Disciplina test 1", 1, "Tip test 1", "Profesor test 1" };
	repo.adauga(disciplina1);
	Disciplina disciplina2{ "Disciplina test 2", 2, "Tip test 2", "Profesor test 2" };
	repo.adauga(disciplina2);
	assert(repo.getLungime() == 2);

	repo.sterge(1);
	assert(repo.getLungime() == 1);
	//assert(repo.sterge(1) == disciplina2);
}

static void testRepositoryModifica()
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

static void testAdaugaService()
{
	Repository repo;
	Service service{ repo };

	string newDenumire = "Disciplina test 1", newTip = "Tip test 1", newProfesor = "Profesor test 1";
	constexpr int newOre = 1;

	assert(service.adaugaService(newDenumire, newOre, newTip, newProfesor));
	assert(service.adaugaService(newDenumire, newOre, newTip, newProfesor));
	assert(service.adaugaService(newDenumire, newOre, newTip, newProfesor));

	assert(service.getLungimeService() == 3);
	Disciplina disciplina{ "Disciplina test 1", 1, "Tip test 1", "Profesor test 1" };
	for (Disciplina d : service.getAllService()) {
		assert(d == disciplina);
	}

	try {
		service.adaugaService("", newOre, newTip, newProfesor);  // Invalid denumire (empty string)
		assert(false);  // If no exception is thrown, the test fails
	}
	catch (const invalid_argument& e) {
		assert(string(e.what()) == "Validator: denumire nu poate fi un sir gol");
	}

	try {
		service.adaugaService(newDenumire, 0, newTip, newProfesor);  // Invalid ore (0)
		assert(false);  // If no exception is thrown, the test fails
	}
	catch (const invalid_argument& e) {
		assert(string(e.what()) == "Validator: valoare ore negativa");
	}

	try {
		service.adaugaService(newDenumire, newOre, "", newProfesor);  // Invalid tip (empty string)
		assert(false);  // If no exception is thrown, the test fails
	}
	catch (const invalid_argument& e) {
		assert(string(e.what()) == "Validator: tip nu poate fi un sir gol");
	}

	try {
		service.adaugaService(newDenumire, newOre, "", "");  // Invalid tip and profesor (both empty)
		assert(false);  // If no exception is thrown, the test fails
	}
	catch (const invalid_argument& e) {
		assert(string(e.what()) == "Validator: tip nu poate fi un sir gol");
	}
}

static void testSergeService()
{
	Repository repo;
	Service service{ repo };

	string newDenumire = "Disciplina test 1", newTip = "Tip test 1", newProfesor = "Profesor test 1";
	constexpr int newOre = 1;

	assert(service.adaugaService(newDenumire, newOre, newTip, newProfesor));
	assert(service.adaugaService(newDenumire, newOre, newTip, newProfesor));
	assert(service.adaugaService(newDenumire, newOre, newTip, newProfesor));

	assert(service.getLungimeService() == 3);
	Disciplina disciplina{ "Disciplina test 1", 1, "Tip test 1", "Profesor test 1" };

	service.stergeService(2);
	service.stergeService(1);
	service.stergeService(0);
	assert(service.getLungimeService() == 0);

	Disciplina disciplinaDef{ "", 0,"","" };
	try {
		service.stergeService(-1);
		service.stergeService(1);

	}
	catch (const invalid_argument& e) {
		assert(string(e.what()) == "Service : pozitie invalida");
	}
	assert(service.getLungimeService() == 0);
}

static void testModificaService()
{
	Repository repo;
	Service service{ repo };

	string denumire = "Disciplina test 1", tip = "Tip test 1", profesor = "Profesor test 1";
	constexpr int ore = 1;

	service.adaugaService(denumire, ore, tip, profesor);
	service.adaugaService(denumire, ore, tip, profesor);
	service.adaugaService(denumire, ore, tip, profesor);

	string newDenumire = "Disciplina test 2", newTip = "Tip test 2", newProfesor = "Profesor test 2";
	constexpr int newOre = 2;

	assert(service.modificaService(0, newDenumire, newOre, newTip, newProfesor));
	Disciplina disciplinaModif{ newDenumire, newOre, newTip, newProfesor };

	assert(service.getAllService()[0] == disciplinaModif);

	try {
		assert(service.modificaService(-1, newDenumire, newOre, newTip, newProfesor));
	}
	catch (const invalid_argument& e) {
		assert(string(e.what()) == "Service : pozitie invalida");
	}
	try {
		assert(service.modificaService(5, newDenumire, newOre, newTip, newProfesor));

	}
	catch (const invalid_argument& e) {
		assert(string(e.what()) == "Service : pozitie invalida");
	}

	try {
		assert(!service.modificaService(0, "", newOre, newTip, newProfesor));
	}
	catch (const invalid_argument& e) {
		assert(string(e.what()) == "Validator: denumire nu poate fi un sir gol");
	}

	try {
		assert(!service.modificaService(0, newDenumire, -1, newTip, newProfesor));
	}
	catch (const invalid_argument& e) {
		assert(string(e.what()) == "Validator: valoare ore negativa");
	}

	try {
		assert(!service.modificaService(0, newDenumire, newOre, "", newProfesor));
	}
	catch (const invalid_argument& e) {
		assert(string(e.what()) == "Validator: tip nu poate fi un sir gol");
	}

	try {
		assert(!service.modificaService(0, newDenumire, newOre, newTip, ""));
	}
	catch (const invalid_argument& e) {
		assert(string(e.what()) == "Validator: profesor nu poate fi un sir gol");
	}
}

void testFiltrareOre()
{
	Repository repo;
	Service service{ repo };

	assert(service.adaugaService("1", 10, "1", "1") == 1);
	assert(service.adaugaService("1", 5, "1", "1") == 1);
	assert(service.adaugaService("1", 5, "1", "1") == 1);
	assert(service.adaugaService("1", 5, "1", "1") == 1);
	assert(service.adaugaService("1", 5, "1", "1") == 1);
	assert(service.getLungimeService() == 5);

	VectorDinamic<Disciplina> filtrate = service.filtrareOre(6);
	assert(filtrate.getLungime() == 1);
	assert(filtrate[0].getOre() == 10);
}

void testFiltrareProfesor()
{
	Repository repo;
	Service service{ repo };
	string numeProfesor = "Test 1";

	assert(service.adaugaService("1", 10, "1", numeProfesor) == 1);
	assert(service.adaugaService("1", 10, "1", numeProfesor) == 1);
	assert(service.adaugaService("1", 10, "1", numeProfesor) == 1);
	assert(service.adaugaService("1", 10, "1", "Test 2") == 1);
	assert(service.adaugaService("1", 10, "1", "Test 2") == 1);
	assert(service.getLungimeService() == 5);

	VectorDinamic<Disciplina> filtrate = service.filtrareProfesor(numeProfesor);
	assert(filtrate.getLungime() == 3);
	assert(filtrate[0].getProfesor() == numeProfesor);
}

void testSortare()
{
	Repository repo;
	Service service{ repo };

	assert(service.adaugaService("1", 33, "z", "m") == 1);
	assert(service.adaugaService("2", 11, "x", "d") == 1);
	assert(service.adaugaService("3", 44, "a", "b") == 1);
	assert(service.adaugaService("4", 22, "y", "a") == 1);
	assert(service.adaugaService("5", 66, "b", "f") == 1);
	assert(service.adaugaService("6", 55, "c", "z") == 1);
	assert(service.getLungimeService() == 6);

	VectorDinamic<Disciplina> sortateDenumire = service.sortareDenumire();
	assert(sortateDenumire.getAll()[0].getDenumire() == "1");
	assert(sortateDenumire.getAll()[5].getDenumire() == "6");
	assert(sortateDenumire.getLungime() == 6);

	VectorDinamic<Disciplina> sortateTip = service.sortareTip();
	assert(sortateTip.getAll()[0].getTip() == "a");
	assert(sortateTip.getAll()[5].getTip() == "z");
	assert(sortateTip.getLungime() == 6);

	VectorDinamic<Disciplina> sortateOre = service.sortareOre();
	assert(sortateOre.getAll()[0].getOre() == 11);
	assert(sortateOre.getAll()[5].getOre() == 66);
	assert(sortateOre.getLungime() == 6);

	VectorDinamic<Disciplina> sortateProfesor = service.sortareProfesor();
	assert(sortateProfesor.getAll()[0].getProfesor() == "a");
	assert(sortateProfesor.getAll()[5].getProfesor() == "z");
	assert(sortateProfesor.getLungime() == 6);
}

/*
MyVector testCopyIterate(MyVector v) {
	double totalPrice = 0;
	for (auto el : v) {
		totalPrice += el.getPrice();
	}
	Pet p{ "total","tt",totalPrice };
	v.add(p);
	return v;
}
*/

VectorDinamic<Disciplina> testCopyIterate(VectorDinamic<Disciplina> v)
{
	int totalHours = 0;
	for (auto el : v)
	{
		totalHours += el.getOre();
	}
	Disciplina p{ "total", totalHours, "tt", "tt" };
	v.push_back(p);
	return v;
}

void testCreateCopyAssign()
{
	VectorDinamic<Disciplina> v;

	for (int index = 0; index < 100; ++index)
	{
		Disciplina p{ to_string(index) + "_denumire", index + 10, to_string(index) + "_type" ,to_string(index) + "_profesor" };
		v.push_back(p);
	}
	assert(v.size() == 100);
	assert(v[50].getTip() == "50_type");

	VectorDinamic<Disciplina> v2 = { v };
	assert(v2.size() == 100);
	assert(v2[50].getTip() == "50_type");

	VectorDinamic<Disciplina> v3;
	v3 = v;
	assert(v3.size() == 100);
	assert(v3[50].getTip() == "50_type");

	auto v4 = testCopyIterate(v3);
	assert(v4.size() == 101);
	assert(v4[50].getTip() == "50_type");

}

/*
void testMoveConstrAssgnment() {
	std::vector<MyVector> v;
	//adaugam un vector care este o variabila temporara
	//se v-a apela move constructor
	v.push_back(MyVector{});

	//inseram, la fel se apeleaza move costructor de la vectorul nostru
	v.insert(v.begin(),MyVector{});

	assert(v.size() == 2);

	MyVector v2;
	addPets(v2, 50);

	v2 = MyVector{};//move assignment

	assert(v2.size() == 0);

}*/

void testMoveCA()
{
	vector<VectorDinamic<Disciplina>> v;
	v.push_back(VectorDinamic<Disciplina>{});

	v.insert(v.begin(), VectorDinamic<Disciplina>{});
	assert(v.size() == 2);

	VectorDinamic<Disciplina> v2;
	for (int index = 0; index < 50; ++index)
	{
		Disciplina p{ to_string(index) + "_denumire", index + 10, to_string(index) + "_type" ,to_string(index) + "_profesor" };
		v2.push_back(p);
	}

	v2 = VectorDinamic<Disciplina>{};
	assert(v2.size() == 0);
}

void testAll() {
	testDisciplinaConstructor();
	testRepositoryAdauga();
	testRepositorySterge();
	testRepositoryModifica();

	testAdaugaService();
	testSergeService();
	testModificaService();

	testFiltrareOre();
	testFiltrareProfesor();
	testSortare();

	testCreateCopyAssign();
	testMoveCA();
	cout << "Teste rulate\n";
}
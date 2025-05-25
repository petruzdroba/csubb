#include "teste.h"
#include <iostream>
#include <cassert>
#include <vector>
#include <fstream>
#include <sstream>
#include <string>

#include "Disciplina.h"
#include "repo.h"
#include "service.h"
#include "exception.h"

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

	Disciplina disciplina{ "1", 1, "1", "1" };
	repo.adauga(disciplina);
	assert(repo.getLungime() == 1);

	for (int index = 2; index <= 100; ++index) {
		repo.adauga(Disciplina{ to_string(index) ,index,to_string(index) ,to_string(index) });
		assert(repo.getLungime() == index);
	}

	int index2 = 1;
	for (Disciplina d : repo.getAll())
	{
		assert(d.getDenumire() == to_string(index2));
		assert(d.getOre() == index2);
		assert(d.getTip() == to_string(index2));
		assert(d.getProfesor() == to_string(index2));
		index2++;
	}

	try
	{
		repo.adauga(disciplina);
	}
	catch (const MyException& e) {
		assert(string(e.getMessage()) == "Repository: Disciplina existenta");
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
	Contract contract;
	Service service{ repo, contract };

	string newDenumire = "Disciplina test 1", newTip = "Tip test 1", newProfesor = "Profesor test 1";
	constexpr int newOre = 1;

	assert(service.adaugaService(newDenumire, newOre, newTip, newProfesor));

	assert(service.getLungimeService() == 1);
	Disciplina disciplina{ "Disciplina test 1", 1, "Tip test 1", "Profesor test 1" };

	try {
		service.adaugaService("", newOre, newTip, newProfesor);  // Invalid denumire (empty string)
		assert(false);
	}
	catch (const MyException& e) {
		assert(string(e.getMessage()) == "Validator: denumire nu poate fi un sir gol");
	}

	try {
		service.adaugaService(newDenumire, 0, newTip, newProfesor);  // Invalid ore (0)
		assert(false);
	}
	catch (const MyException& e) {
		assert(string(e.getMessage()) == "Validator: valoare ore negativa");
	}

	try {
		service.adaugaService(newDenumire, newOre, "", newProfesor);  // Invalid tip (empty string)
		assert(false);
	}
	catch (const MyException& e) {
		assert(string(e.getMessage()) == "Validator: tip nu poate fi un sir gol");
	}

	try {
		service.adaugaService(newDenumire, newOre, "", "");  // Invalid tip and profesor (both empty)
		assert(false);
	}
	catch (const MyException& e) {
		assert(string(e.getMessage()) == "Validator: tip nu poate fi un sir gol");
	}
}

static void testSergeService()
{
	Repository repo;
	Contract contract;
	Service service{ repo, contract };

	assert(service.adaugaService("1", 33, "z", "m") == 1);
	assert(service.adaugaService("2", 11, "x", "d") == 1);
	assert(service.adaugaService("3", 44, "a", "b") == 1);

	assert(service.getLungimeService() == 3);

	service.stergeService(2);
	service.stergeService(1);
	service.stergeService(0);
	assert(service.getLungimeService() == 0);

	Disciplina disciplinaDef{ "", 0,"","" };
	try {
		service.stergeService(-1);
		service.stergeService(1);

	}
	catch (const MyException& e) {
		assert(string(e.getMessage()) == "Service : pozitie invalida");
	}
	assert(service.getLungimeService() == 0);
}

static void testModificaService()
{
	Repository repo;
	Contract contract;
	Service service{ repo, contract };

	assert(service.adaugaService("1", 33, "z", "m") == 1);
	assert(service.adaugaService("2", 11, "x", "d") == 1);
	assert(service.adaugaService("3", 44, "a", "b") == 1);

	string newDenumire = "Disciplina test 2", newTip = "Tip test 2", newProfesor = "Profesor test 2";
	constexpr int newOre = 2;

	assert(service.modificaService(0, newDenumire, newOre, newTip, newProfesor));
	Disciplina disciplinaModif{ newDenumire, newOre, newTip, newProfesor };

	assert(service.getAllService()[0] == disciplinaModif);

	try {
		assert(service.modificaService(-1, newDenumire, newOre, newTip, newProfesor));
	}
	catch (const MyException& e) {
		assert(string(e.getMessage()) == "Service : pozitie invalida");
	}
	try {
		assert(service.modificaService(5, newDenumire, newOre, newTip, newProfesor));

	}
	catch (const MyException& e) {
		assert(string(e.getMessage()) == "Service : pozitie invalida");
	}

	try {
		assert(!service.modificaService(0, "", newOre, newTip, newProfesor));
	}
	catch (const MyException& e) {
		assert(string(e.getMessage()) == "Validator: denumire nu poate fi un sir gol");
	}

	try {
		assert(!service.modificaService(0, newDenumire, -1, newTip, newProfesor));
	}
	catch (const MyException& e) {
		assert(string(e.getMessage()) == "Validator: valoare ore negativa");
	}

	try {
		assert(!service.modificaService(0, newDenumire, newOre, "", newProfesor));
	}
	catch (const MyException& e) {
		assert(string(e.getMessage()) == "Validator: tip nu poate fi un sir gol");
	}

	try {
		assert(!service.modificaService(0, newDenumire, newOre, newTip, ""));
	}
	catch (const MyException& e) {
		assert(string(e.getMessage()) == "Validator: profesor nu poate fi un sir gol");
	}
}

void testFiltrareOre()
{
	Repository repo;
	Contract contract;
	Service service{ repo, contract };

	assert(service.adaugaService("1", 10, "1", "1") == 1);
	assert(service.adaugaService("2", 5, "1", "1") == 1);
	assert(service.adaugaService("3", 5, "1", "1") == 1);
	assert(service.adaugaService("4", 5, "1", "1") == 1);
	assert(service.adaugaService("5", 5, "1", "1") == 1);
	assert(service.getLungimeService() == 5);

	vector<Disciplina> filtrate = service.filtrareOre(6);
	assert(filtrate.size() == 1);
	assert(filtrate[0].getOre() == 10);
}

void testFiltrareProfesor()
{
	Repository repo;
	Contract contract;
	Service service{ repo, contract };
	string numeProfesor = "Test 1";

	assert(service.adaugaService("1", 10, "1", numeProfesor) == 1);
	assert(service.adaugaService("2", 10, "1", numeProfesor) == 1);
	assert(service.adaugaService("3", 10, "1", numeProfesor) == 1);
	assert(service.adaugaService("4", 10, "1", "Test 2") == 1);
	assert(service.adaugaService("5", 10, "1", "Test 2") == 1);
	assert(service.getLungimeService() == 5);

	vector<Disciplina> filtrate = service.filtrareProfesor(numeProfesor);
	assert(filtrate.size() == 3);
	assert(filtrate[0].getProfesor() == numeProfesor);
}

void testSortare()
{
	Repository repo;
	Contract contract;
	Service service{ repo, contract };

	assert(service.adaugaService("1", 33, "z", "m") == 1);
	assert(service.adaugaService("2", 11, "x", "d") == 1);
	assert(service.adaugaService("3", 44, "a", "b") == 1);
	assert(service.adaugaService("4", 22, "y", "a") == 1);
	assert(service.adaugaService("5", 66, "b", "f") == 1);
	assert(service.adaugaService("6", 55, "c", "z") == 1);
	assert(service.getLungimeService() == 6);

	vector<Disciplina> sortateDenumire = service.sortareDenumire();
	assert(sortateDenumire[0].getDenumire() == "1");
	assert(sortateDenumire[5].getDenumire() == "6");
	assert(sortateDenumire.size() == 6);

	vector<Disciplina> sortateTip = service.sortareTip();
	assert(sortateTip[0].getTip() == "a");
	assert(sortateTip[5].getTip() == "z");
	assert(sortateTip.size() == 6);

	vector<Disciplina> sortateOre = service.sortareOre();
	assert(sortateOre[0].getOre() == 11);
	assert(sortateOre[5].getOre() == 66);
	assert(sortateOre.size() == 6);

	vector<Disciplina> sortateProfesor = service.sortareProfesor();
	assert(sortateProfesor[0].getProfesor() == "a");
	assert(sortateProfesor[5].getProfesor() == "z");
	assert(sortateProfesor.size() == 6);
}

static void testAdaugaContractService()
{
	Repository repo;
	Contract contract;
	Service service{ repo, contract };

	assert(service.adaugaService("1", 10, "1", "1") == 1);
	assert(service.adaugaService("6", 10, "1", "1") == 1);
	assert(service.adaugaService("1111", 10, "1", "1") == 1);
	assert(service.adaugaService("3", 10, "1", "1") == 1);
	assert(service.adaugaService("4", 10, "1", "1") == 1);
	assert(service.adaugaService("5", 10, "1", "1") == 1);
	assert(service.getLungimeService() == 6);

	service.adaugaContractService("1111");
	assert(service.getContractLungime() == 1);
	assert(service.getAllContract()[0].getDenumire() == "1111");

	try {
		service.adaugaContractService("2");
		assert(false);
	}
	catch (const MyException& e) {
		assert(string(e.getMessage()) == "Service: Denumire inexistenta");
	}
}

static void testDeleteAllContract()
{
	Repository repo;
	Contract contract;
	Service service{ repo, contract };

	assert(service.adaugaService("1", 10, "1", "1") == 1);
	for (int index = 1; index <= 5; ++index)
		service.adaugaContractService("1");
	assert(service.getContractLungime() == 1);

	service.deleteAllContract();
	assert(service.getContractLungime() == 0);
}

static void testRandomPopulate()
{
	Repository repo;
	Contract contract;
	Service service{ repo, contract };

	try
	{
		service.randomPopulateContract(10);
		assert(false);
	}
	catch (const MyException& e)
	{
		assert(string(e.getMessage()) == "Service: Nu exista discipline");
	}

	try
	{
		service.randomPopulateContract(0);
		assert(false);
	}
	catch (const MyException& e)
	{
		assert(string(e.getMessage()) == "Service: Nr Discipline imposibil");
	}

	try
	{
		service.randomPopulateContract(1);
		assert(false);
	}
	catch (const MyException& e)
	{
		assert(string(e.getMessage()) == "Service: Nu exista discipline");
	}

	assert(service.adaugaService("1", 33, "z", "m") == 1);
	assert(service.adaugaService("2", 11, "x", "d") == 1);
	assert(service.adaugaService("3", 44, "a", "b") == 1);
	assert(service.adaugaService("4", 22, "y", "a") == 1);
	assert(service.adaugaService("5", 66, "b", "f") == 1);
	assert(service.adaugaService("6", 55, "c", "z") == 1);

	try
	{
		service.randomPopulateContract(10);
		assert(false);
	}
	catch (const MyException& e)
	{
		assert(string(e.getMessage()) == "Service: Nu exista discipline");
	}
	assert(service.getLungimeService() == 6);

	service.randomPopulateContract(6);
	assert(service.getContractLungime() == 6);
}

static void testGenerateContract()
{
	Repository repo;
	Contract contract;
	Service service{ repo, contract };

	assert(service.adaugaService("1", 33, "z", "m") == 1);
	assert(service.adaugaService("2", 11, "x", "d") == 1);
	assert(service.adaugaService("3", 44, "a", "b") == 1);
	assert(service.adaugaService("4", 22, "y", "a") == 1);
	assert(service.adaugaService("5", 66, "b", "f") == 1);
	assert(service.adaugaService("6", 55, "c", "z") == 1);
	assert(service.getLungimeService() == 6);

	service.adaugaContractService("1");
	service.adaugaContractService("2");
	service.adaugaContractService("3");
	service.exportContract("test.txt");

	string line;
	ifstream file("test.txt");

	for (int index = 1; index <= 3; ++index)
	{
		getline(file, line);
		stringstream ss(line);
		string firstField;
		getline(ss, firstField, ',');

		assert(firstField == to_string(index));
	}

	file.close();
}

void clearFile(const string path)
{
	if (path != "")
	{
		ofstream file(path, ios::trunc);
		file.close();
	}
}

static void testWriteToFile()
{
	clearFile("test.txt");
	ifstream test_empty("test.txt", ios::binary);
	assert(test_empty.peek() == EOF);
	test_empty.close();

	Repository repo("test.txt");
	Contract contract;
	Service service{ repo, contract };

	assert(service.adaugaService("1", 33, "z", "m") == 1);
	assert(service.adaugaService("2", 11, "x", "d") == 1);
	assert(service.adaugaService("3", 44, "a", "b") == 1);
	assert(service.getLungimeService() == 3);

	string line;
	ifstream file("test.txt");

	for (int index = 1; index <= 3; ++index)
	{
		getline(file, line);
		stringstream ss(line);
		string firstField;
		getline(ss, firstField, ',');

		assert(firstField == to_string(index));
	}

	file.close();
}

static void testOverwriteToFile()
{
	clearFile("test.txt");
	Repository repo("test.txt");
	Contract contract;
	Service service{ repo, contract };

	assert(service.adaugaService("1", 33, "z", "m") == 1);
	assert(service.adaugaService("2", 11, "x", "d") == 1);
	assert(service.adaugaService("3", 44, "a", "b") == 1);
	assert(service.getLungimeService() == 3);

	string line;//adaug obiecte, dupa le sterg, si vad daca se modifica fisierul complet
	ifstream file("test.txt");

	for (int index = 1; index <= 3; ++index)
	{
		getline(file, line);
		stringstream ss(line);
		string firstField;
		getline(ss, firstField, ',');

		assert(firstField == to_string(index));
	}

	file.close();

	service.stergeService(0);
	service.stergeService(1); // ramane 1

	ifstream file2("test.txt");

	string line2;
	getline(file2, line2);
	assert(line2 == "2,11,x,d");

	file2.close();
}

static void testUndoAdd()
{
	Repository repo;
	Contract contract;
	Service service{ repo, contract };

	assert(service.adaugaService("1", 33, "z", "m") == 1);
	assert(service.adaugaService("2", 11, "x", "d") == 1);
	assert(service.adaugaService("3", 44, "a", "b") == 1);
	assert(service.getLungimeService() == 3);

	service.undo();
	assert(service.getLungimeService() == 2);
	service.undo();
	assert(service.getLungimeService() == 1);
	service.undo();
	assert(service.getLungimeService() == 0);

	try
	{
		service.undo();
		assert(false);
	}
	catch (const MyException& e)
	{
		assert(string(e.getMessage()) == "Service: Undo list empty");
	}
}

static void testUndoSterge()
{
	Repository repo;
	Contract contract;
	Service service{ repo, contract };

	assert(service.adaugaService("1", 33, "z", "m") == 1);
	assert(service.adaugaService("2", 11, "x", "d") == 1);
	assert(service.adaugaService("3", 44, "a", "b") == 1);

	service.stergeService(0);
	service.stergeService(0);
	service.stergeService(0);
	assert(service.getLungimeService() == 0);

	service.undo();
	assert(service.getLungimeService() == 1);
	service.undo();
	assert(service.getLungimeService() == 2);
	service.undo();
	assert(service.getLungimeService() == 3);
}

static void testUndoModifica()
{
	Repository repo;
	Contract contract;
	Service service{ repo, contract };

	assert(service.adaugaService("1", 33, "z", "m") == 1);
	assert(service.adaugaService("2", 11, "x", "d") == 1);
	assert(service.adaugaService("3", 44, "a", "b") == 1);

	service.modificaService(0, "9", 9, "9", "9");
	assert(service.getAllService()[0].getDenumire() == "9");

	service.undo();
	assert(service.getAllService()[0].getDenumire() == "1");
}

static void testGetTypes()
{
	Repository repo;
	Contract contract;
	Service service{ repo, contract };

	assert(service.adaugaService("1", 33, "z", "m") == 1);
	assert(service.adaugaService("2", 11, "x", "d") == 1);
	assert(service.adaugaService("3", 44, "a", "b") == 1);

	assert(service.getTypes().size() == 3);

	assert(service.getTypes().front().second == 1);
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

	testAdaugaContractService();
	testDeleteAllContract();
	testRandomPopulate();
	testGenerateContract();

	testWriteToFile();
	testOverwriteToFile();

	testUndoAdd();
	testUndoSterge();
	testUndoModifica();

	testGetTypes();
	cout << "Teste rulate\n";
}
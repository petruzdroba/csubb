#include "teste.h"
#include "repo.h"
#include "service.h"

#include "Parcare.h"
#include <fstream>
#include <assert.h>


using namespace std;

static void resetFile() {
	ifstream in("teste.txt", ios::trunc);
	in.close();

	ofstream out("teste.txt");
	out << "1" << "," << 1 << "," << 1 << "," << "X" << endl;
	out.close();
}

static void testRepo() {
	Repo r{ "teste.txt" };

	assert(r.getAll()[0] == Parcare("1", 1, 1, "X"));
}

static void testService() {
	ofstream out("teste.txt");
	out << "1" << "," << 1 << "," << 1 << "," << "X" << endl;
	out << "2" << "," << 3 << "," << 4 << "," << "X" << endl;
	out.close();

	Repo r{ "teste.txt" };
	Service s{ r };

	assert(s.getAllS()[0] == Parcare("2", 3, 4, "X"));

}

static void testAdauga() {
	Repo r{ "teste.txt" };
	Service s{ r };

	try { s.adaugaS("", 1, 1, "X"); assert(false); }
	catch (invalid_argument e) { assert(true); }

	try { s.adaugaS("1", -1, 1, "X"); assert(false); }
	catch (invalid_argument e) { assert(true); }

	try { s.adaugaS("1", 1, -1, "X"); assert(false); }
	catch (invalid_argument e) { assert(true); }

	try { s.adaugaS("1", 1, 1, "XXX"); assert(false); }
	catch (invalid_argument e) { assert(true); }

	s.adaugaS("2", 1, 1, "X");
	assert(s.getAllS().size() == 2);

	try { s.adaugaS("2", 1, 1, "X"); assert(false); }
	catch (invalid_argument e) { assert(true); }
}

static void testModifica() {
	Repo r{ "teste.txt" };
	Service s{ r };
	s.adaugaS("2", 1, 1, "X");
	s.adaugaS("3", 1, 1, "X");
	s.adaugaS("4", 1, 1, "X");


	try { s.modificaS( "", 1, 1, "X"); assert(false); }
	catch (invalid_argument e) { assert(true); }

	try { s.modificaS( "2", -1, 1, "X"); assert(false); }
	catch (invalid_argument e) { assert(true); }

	try { s.modificaS( "2", 1, 6, "X"); assert(false); }
	catch (invalid_argument e) { assert(true); }

	try { s.modificaS( "2", 1, 1, "XXX"); assert(false); }
	catch (invalid_argument e) { assert(true); }

	assert(s.getAllS()[0].getAdresa() == "1");
	s.modificaS( "1", 2, 2, "XXXX");
	assert(s.getAllS()[0].getLinii() == 2);
	assert(s.getAllS()[0].getColoanre() == 2);
	assert(s.getAllS()[0].getStare() == "XXXX");
}

void runTests()
{
	resetFile();
	testRepo();
	testService();
	resetFile();

	testAdauga();
	resetFile();

	testModifica();
	resetFile();
}

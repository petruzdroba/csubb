#include "teste.h"
#include "repo.h"
#include "Student.h"
#include "service.h"

#include <assert.h>

static void resetTestFile()
{
	ifstream in("teste.txt", ios::trunc);
	in.close();

	ofstream out("teste.txt");
	out << 1 << "," << "Stud1" << "," << 19 << "," << "ai" << endl;
}

static void testRepo()
{
	resetTestFile();
	Repo repo{ "teste.txt" };

	assert(repo.getAll()[0] == Student(1, "Stud1", 19, "ai"));
}

static void testService()
{
	ofstream out("teste.txt");
	out << 2 << "," << "Stud2" << "," << 19 << "," << "ai" << endl;
	out << 3 << "," << "Stud3" << "," << 19 << "," << "ai" << endl;
	out << 4 << "," << "Stud4" << "," << 19 << "," << "ai" << endl;
	out.close();

	Repo repo{ "teste.txt" };
	Service s{ repo };

	assert(s.getAllS().size() == 3);

	s.intinerire();
	assert(s.getAllS()[2].getVarsta() == 18);

	s.imbatranire();
	assert(s.getAllS()[2].getVarsta() == 19);

	s.undoThis();
	assert(s.getAllS()[2].getVarsta() == 18);

	s.intinerire();
	assert(s.getAllS()[2].getVarsta() == 17);

	s.undoThis();
	assert(s.getAllS()[2].getVarsta() == 18);

	vector<int> ids{ 2,3 };
	s.sterge(ids);
	assert(s.getAllS().size() == 1);

	s.undoThis();
	assert(s.getAllS().size() == 3);

	s.redoThis();
	assert(s.getAllS().size() == 1);

	resetTestFile();
}


void runTests()
{
	resetTestFile();
	testRepo();
	testService();
}

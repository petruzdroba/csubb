#include "teste.h"
#include "repo.h"
#include "Produs.h"
#include "service.h"
#include <fstream>
#include <assert.h>


using namespace std;

static void resetFile(string path)
{
	ifstream in(path, ios::trunc);
	in.close();

	ofstream out(path);
	out << 1 << "@" << 1 << "@" << 1 << "@" << 1 << endl;
}

static void testRepo()
{
	Repository repo{ "teste.txt" };
	assert(repo.getAll()[0] == Produs(1, "1", "1", 1));
}

static void testService()
{
	Repository repo{ "teste.txt" };
	assert(repo.getAll()[0] == Produs(1, "1", "1", 1));
	Service serv{ repo };
	assert(serv.getAllS()[0] == Produs(1, "1", "1", 1));
}

static void testAdd() {
	Repository repo{ "teste.txt" };
	Service serv{ repo };

	serv.add(2, "2", "2", 2);
	assert(serv.getAllS()[1] == Produs(2, "2", "2", 2));
	resetFile("teste.txt");

	try {
		serv.add(2, "", "2", 2);
		assert(false);
	}
	catch (invalid_argument e)
	{
		assert(true);
	}

	try {
		serv.add(2, "2", "2", 0);
		assert(false);
	}
	catch (invalid_argument e)
	{
		assert(true);
	}

	try {
		serv.add(2, "2", "2", 2);
		assert(false);
	}
	catch (invalid_argument e)
	{
		assert(true);
	}
}

static void testTypes() {
	Repository repo{ "teste.txt" };
	Service serv{ repo };

	serv.add(2, "2", "2", 2);
	serv.add(3, "2", "2", 2);

	assert(serv.getTypes()[0].second == 1);
	assert(serv.getTypes()[1].second == 2);

	resetFile("teste.txt");
}

void runTests()
{
	resetFile("teste.txt");
	testRepo();
	testService();
	testAdd();
	testTypes();
}

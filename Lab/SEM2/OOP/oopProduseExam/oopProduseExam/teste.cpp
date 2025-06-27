#include "teste.h"
#include "repo.h"
#include "Produs.h"
#include "service.h"
#include <fstream>
#include <assert.h>


using namespace std;

static void clearFile(string path)
{
	ifstream in(path, ios::trunc);
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

void runTests()
{
	testRepo();
	testService();
}

#include "teste.h"
#include "Rochie.h"
#include "repo.h"


#include <cassert>
#include <fstream>


static void testRochie()
{
	Rochie r(1,"1","1",1, 1);
	assert(r.getCod() == 1);
	assert(r.getDenumire() == "1");
	assert(r.getMarime() == "1");
	assert(r.getPret() == 1);
	assert(r.getDispoibilitate() == true);
}

static void clearFile(string path)
{
	ofstream file1(path, ios::trunc); //clear file
	file1.close();
}

static void testRepoFileRead()
{
	clearFile("test.txt");

	ofstream file2("test.txt");
	file2 << 2 << "," << 2 << "," << 2 << "," << 2 << "," << 1 << "\n";
	file2 << 2 << "," << 2 << "," << 2 << "," << 2 << "," << 0 << "\n";
	file2 << 2 << "," << 2 << "," << 4 << "," << 4 << "," << 1 << "\n";

	file2.close();

	Repo repo("test.txt");
	assert(repo.getAll()[0].getCod() == 2);
	assert(repo.getAll()[1].getDispoibilitate() == false);
	assert(repo.getAll()[2].getMarime() == "4");

	file2.close();
}

//static void testRepoCRUD()
//{
//	clearFile("test.txt");
//	Repo repo("test.txt");
//
//	Rochie r1(1, "1", "1", 1, 1);
//	Rochie r2(2, "2", "2", 2, 2);
//	Rochie r3(3, "3", "3", 3, 3);
//	Rochie r4(4, "4", "4", 4, 4);
//
//	repo.adauga(r1);
//	repo.adauga(r2);
//	repo.adauga(r3);
//	assert(repo.getAll()[0] == r1);
//	assert(repo.getAll()[1] == r2);
//	assert(repo.getAll()[2] == r3);
//	assert(repo.len() == 3);
//
//	repo.sterge(1);
//	assert(repo.getAll()[0] == r1);
//	assert(repo.getAll()[1] == r3);
//	assert(repo.len() == 2);
//	
//	repo.modifica(0,r4);
//	assert(repo.getAll()[0] == r4);
//}

void runAll()
{
	testRochie();
	testRepoFileRead();
	//testRepoCRUD();
}

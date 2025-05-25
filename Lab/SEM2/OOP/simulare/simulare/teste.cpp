#include "teste.h"
#include "Repo.h"
#include "Jucator.h"
#include "service.h"

#include <cassert>
#include <stdexcept>


void testJucator()
{
	Jucator j("1", "1", 1, 1, 1, 1);
	assert(j.getNume() == "1");
	assert(j.getTara() == "1");
	assert(j.getMeciuri() == 1);
	assert(j.getPuncte() == 1);
	assert(j.getRebounds() == 1);
	assert(j.getAssisturi() == 1);
}

void testRepo()
{
	Repo r;
	Jucator j("1", "1", 1, 1, 1, 1);

	assert(r.getLen() == 0);
	r.adauga(j);
	assert(r.getLen() == 1);
	assert(r.getAll()[0].getNume() == "1");
}

void testService()
{
	Repo r;
	Service s(r);

	s.adauga("1 1", "1", 1, 1, 1, 1);
	assert(s.all()[0].getNume() == "1 1");
	assert(s.len() == 1);
	try
	{
		s.adauga("1", "1", 1, 1, 1, 1);
	}
	catch(const invalid_argument & e) {
		assert((string)e.what() == "Numele nu e valid");
	}

	try
	{
		s.adauga("1 1", "", 1, 1, 1, 1);
	}
	catch (const invalid_argument& e) {
		assert((string)e.what() == "Tara nu e valid");
	}
	Jucator j("1 1", "1", 1, 1, 1, 1);
	assert(s.westbrook(j) == false);

	Jucator j2("1 1", "1", 1, 10, 10, 10);
	assert(s.westbrook(j2) == true);

	/*Jucator j1("1 1", "1", 1, 1, 2, 3);
	Jucator j2("2 2", "1", 1, 2, 1, 3);
	Jucator j3("3 3", "1", 1, 3, 2, 1);*/
	Repo r2;
	Service s2(r2);

	s2.adauga("1 1", "1", 1, 1, 2, 30);
	s2.adauga("2 2", "1", 1, 2, 30, 1);
	s2.adauga("3 3", "1", 1, 30, 2, 1);


	vector<Jucator> sort = s2.all();
	s2.sortOpt(sort, "Puncte");
	assert(sort[0].getNume() == "3 3");
	s2.sortOpt(sort, "Rebounds");
	assert(sort[0].getNume() == "2 2");
	s2.sortOpt(sort, "Assist");
	assert(sort[0].getNume() == "1 1");
}

void runAll() 
{
	testJucator();
	testRepo();
	testService();
}
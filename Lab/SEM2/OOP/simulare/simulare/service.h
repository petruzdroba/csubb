#pragma once

#include "Repo.h"
#include "Jucator.h"

using namespace std;

class Service
{
private:
	Repo& repo;

public:
	Service(Repo& r) : repo(r) {};


	vector<Jucator>& all();

	int len();

	bool westbrook(Jucator& j);

	void adauga(const string& nume, const string& tara, int meciuri, int puncte, int rebounds, int assist);

	void exportProcentage(int proc, string option, string path);

	void sortOpt(vector<Jucator>& j, string option);
};
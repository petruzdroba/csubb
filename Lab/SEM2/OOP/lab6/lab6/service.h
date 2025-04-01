#pragma once
#include "repo.h"

class Service {
private:
	Repository repo;
public:
	Service(Repository& repo) : repo(repo) {};

	bool adaugaService(const string& denumire, const int& ore, const string& tip, const string& profesor);

	Disciplina stergeService(const int pozitie);

	bool modificaService(const int pozitie, const string& denumire, const int& ore, const string& tip, const string& profesor);

	vector<Disciplina> getAllService() const;

	int getLungimeService() const;
};
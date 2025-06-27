#pragma once

#include "repo.h"
#include "Produs.h"

class Service {
private:
	Repository& repo;
public:
	Service(Repository& r) : repo(r) {};

	vector<Produs>& getAllS() { return repo.getAll(); }
};
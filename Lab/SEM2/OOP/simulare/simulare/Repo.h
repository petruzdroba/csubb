#pragma once

#include <vector>
#include "Jucator.h"

using namespace std;

class Repo {
private:
	vector<Jucator> jucatori;

public:
	Repo() {};

	vector<Jucator>& getAll();

	int getLen();

	void adauga(const Jucator& j);
};
#pragma once
#include <vector>
#include "Disciplina.h"

using namespace std;

class Repository {

private:
	vector<Disciplina> discipline;
public:

	Repository() = default;

	int getLungime()const;

	vector<Disciplina> getAll()const;

	void adauga(const Disciplina& disciplina);

	Disciplina sterge(const int pozitie);

	void modifica(const int pozitie, const Disciplina& newDisciplina);
};
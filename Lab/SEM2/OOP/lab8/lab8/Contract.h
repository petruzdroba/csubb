#pragma once
#include <vector>

#include "Disciplina.h"

class Contract
{
private:
	vector<Disciplina> contracte;
public:
	vector<Disciplina> getAll() const
	{
		return contracte;
	}

	void adaugaContract(Disciplina const newDisciplina);

	void deleteAll()
	{
		contracte.clear();
		contracte.shrink_to_fit();
	}

	int getLungime()const
	{
		return (int)contracte.size();
	}
};


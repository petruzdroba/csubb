#pragma once

#include "repo.h"
#include "Produs.h"
#include "Observator.h"

#include <algorithm>


class Service: public Observable {
private:
	Repository& repo;
public:
	Service(Repository& r) : repo(r) {};

	vector<Produs>& getAllS() {
		vector<Produs>& s = repo.getAll();

		sort(s.begin(), s.end(), [](const Produs& a, const Produs& b) {return a.getPret() < b.getPret(); });

		return s;
	}

	int nrVocale(const Produs p) {
		int count = 0;
		for (char a : p.getNume())
		{
			if (a == 'a' || a == 'e' || a == 'i' || a == 'o' || a == 'u' || a == 'A' || a == 'E' || a == 'I' || a == 'O' || a == 'U')
				count++;
		}

		return count;
	}

	void add(int id, string nume, string tip, double pret)
	{
		if (nume == "")
			throw invalid_argument("Nume vid");

		if (pret < 1.0 || pret > 100.0)
			throw invalid_argument("Pret invalid");

		Produs newP(id, nume, tip, pret);

		repo.add(newP);
		notify();
	}

	vector<pair<string, int>> getTypes() {
		vector<Produs>& s = repo.getAll();
		vector<pair<string, int>> rez;

		for (auto p : s)
		{
			bool found = false;
			for (auto& all : rez)
			{
				if (all.first == p.getTip())
				{
					found = true;
					all.second += 1;
				}
			}

			if (!found)
				rez.push_back(make_pair(p.getTip(), 1));
		}

		return rez;
	}
};
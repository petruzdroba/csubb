#pragma once

#include "repo.h"
#include <vector>
#include <algorithm>
#include <iterator>

using namespace std;

class Service
{
private:
	Repo repo;

public:
	Service(const Repo& repo)
		: repo(repo)
	{
	}

	const vector<Rochie>& all() const
	{
		return repo.getAll();
	}

	bool inchiriaza(Rochie r) {
		if (r.getDispoibilitate() == false)
			return false;
		else
		{
			repo.inchiriazaR(r);
			return true;
		}
	}

	int lungime()
	{
		return repo.len();
	};

	static bool cmpSize(const Rochie& a, const Rochie& b)
	{
		int A= 0, B=0;
		if (a.getMarime() == "s")
			A = 1;
		if (a.getMarime() == "m")
			A = 2;
		if (a.getMarime() == "l")
			A = 3;

		if (b.getMarime() == "s")
			B = 1;
		if (b.getMarime() == "m")
			B = 2;
		if (b.getMarime() == "l")
			B = 3;

		return A < B;
	}

	vector<Rochie> sortareMarime()
	{
		vector<Rochie> sortat = all();

		sort(sortat.begin(), sortat.end(), cmpSize);

		return sortat;
	}

	static bool cmpPret(const Rochie& a, const Rochie& b)
	{
		return a.getPret() < b.getPret(); ;
	}

	vector<Rochie> sortarePret()
	{
		vector<Rochie> sortat = all();

		sort(sortat.begin(), sortat.end(), cmpPret);

		return sortat;
	}
};
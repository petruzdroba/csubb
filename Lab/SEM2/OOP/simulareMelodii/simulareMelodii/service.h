#pragma once

#include "repo.h"
#include "Melodie.h"


class Service
{
private:
	Repo& repo;
public:
	Service(Repo& r) : repo(r) {};

	vector<Melodie>& all() {
		return repo.getAll();
	}

	int len() {
		return (int)all().size();
	}

	int getSameRank(int rank)
	{
		int count = 0;

		for (Melodie& m : all())
		{
			count += (m.getRank() == rank);
		}

		return count;
	}

	int getByArtist(string a)
	{
		int count = 0;

		for (Melodie& m : all())
		{
			count += (m.getArtist() == a);
		}

		return count;
	}

	void modifica(int poz, int newRank, string newName)
	{
		repo.modifica(poz, newRank, newName);
	}

	void sterge(int poz)
	{
		repo.sterge(poz);
	}
};
#pragma once

#include <fstream>
#include <vector>
#include <sstream>
#include <algorithm>

#include "Melodie.h"

using namespace std;

class Repo
{
private:
	string path;
	vector<Melodie> melodii;

	void sorter()
	{
		sort(melodii.begin(), melodii.end(), [](const Melodie& a, const Melodie& b) {return a.getRank() < b.getRank(); });
	}

	void writeToFile()
	{
		ofstream file(path);

		for (Melodie& m : melodii)
		{
			file << m.getId() << "," << m.getTitlu() << "," << m.getArtist() << "," << m.getRank() << "\n";
		}
		file.close();
	}
public:

	Repo(const string& path)
		: path(path)
	{
		this->path = path;
		ifstream file(path);

		string line;
		while (getline(file, line))
		{
			stringstream ss(line);
			string id, titlu, artist, rank;
			getline(ss, id, ',');
			getline(ss, titlu, ',');
			getline(ss, artist, ',');
			getline(ss, rank, ',');
			melodii.push_back(Melodie(stoi(id), titlu, artist, stoi(rank)));
		}
		sorter();
		file.close();
	}

	vector<Melodie>& getAll()
	{
		return melodii;
	}

	void modifica(int poz, int newRank, string newName)
	{
		melodii[poz].setRank(newRank);
		melodii[poz].setName(newName);
		writeToFile();
		sorter();
	}

	void sterge(int poz)
	{
		melodii.erase(melodii.begin() + poz);
		writeToFile();
		sorter();
	}
};
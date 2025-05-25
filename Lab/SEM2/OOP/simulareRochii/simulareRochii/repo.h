#pragma once

#include "Rochie.h"
#include <vector>
#include <fstream>
#include <sstream>

class Repo {
private:
	vector<Rochie> rochii;
	string path;

	void toFile()
	{
		ofstream file(path);
		for (const Rochie& r : rochii)
		{
			file << r.getCod() << "," << r.getDenumire() << "," << r.getMarime() << "," << r.getPret() << "," << (int)r.getCod() << "\n";
		}
		file.close();
	}

public:
	Repo(const string& path)
	{
		this->path = path;
		ifstream file(path);

		string line;
		while (getline(file, line))
		{
			stringstream ss(line);
			
			string cod, nume, marime, pret, disp;

			getline(ss,cod , ',');
			getline(ss, nume, ',');
			getline(ss, marime, ',');
			getline(ss, pret, ',');
			getline(ss, disp, ',');
			rochii.push_back( Rochie(stoi(cod), nume, marime, stoi(pret), (bool)(stoi(disp)) ));
		}
		file.close();
	}

	const vector<Rochie>& getAll() const
	{
		return rochii;
	}

	const int len()const
	{
		return (int)rochii.size();
	}

	void inchiriazaR(Rochie r)
	{
		for (Rochie& rr : rochii)
		{
			if (r == rr)
			{
				rr.setDisponiobilitate(false);
				break;
			}
		}
	}

	/*void adauga(const Rochie& r);

	void sterge(const int pozitie);

	void modifica(const int pozitie, const Rochie& r);*/

};
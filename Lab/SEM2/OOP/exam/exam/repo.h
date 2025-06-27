#pragma once
#include "Parcare.h"
#include <vector>
#include <sstream>
#include <string>

#include <fstream>

using namespace std;

class Repo {
private:
	string path;
	vector<Parcare> parcari;

	void writeToFile() {
		/*
			Functia rescrie tot vectorul de Parcare in path
			pre:
				path non empty
			post:
				este updatat fisierul
		*/
		ofstream file(path);

		for (auto& a : parcari)
			file << a.getAdresa() << "," << a.getLinii() << "," << a.getColoanre() << "," << a.getStare() << "\n";

		file.close();
	}
public:
	Repo(string p) :path(p) {
		/*
			Creeaza un obiect de tipul repository
			pre:
				path este un string valid reprezentand numele unui fisier
			post:
				se creeaza un repo cu path path
		*/
		ifstream file(path);

		string line;
		while (getline(file, line))
		{
			stringstream ss(line);

			string adresa, lin, col, stare;

			getline(ss, adresa, ',');
			getline(ss, lin, ',');
			getline(ss, col, ',');
			getline(ss, stare, ',');

			parcari.push_back(Parcare(adresa, stoi(lin), stoi(col), stare));
		}

		file.close();
	}

	vector<Parcare>& getAll() { return parcari; }/*Returneaza toate parcarile*/

	void adauga(const Parcare& p)
	{
		/*
			Adauga o parcare noua, si o salveaza in fisier
			pre:
				Parcare p valida, cu adresa unica, altfel arunca exceptie
			post:
				Se adauga o parcare p, care se salveaza in fisier
		*/
		for (auto& a : parcari)
			if (a.getAdresa() == p.getAdresa())
				throw invalid_argument("Adresa invalida");

		parcari.push_back(p);
		writeToFile();
	}

	void modifica(const Parcare& p) {
		/*
			Modifica parcarea cu adresa p.getAdresa()
			pre:
				p o parcare valida
			post:
				se modifica parcarea cu adresa p.getAdresa(), si se salveaza in fisiser
		*/
		for (auto& a : parcari)
			if (a.getAdresa() == p.getAdresa())
			{
				a = p;
				break;
			}

		writeToFile();
	}

};
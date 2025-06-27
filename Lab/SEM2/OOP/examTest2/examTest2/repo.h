#pragma once
#include "Produs.h"
#include <vector>
#include <string>
#include <sstream>
#include <fstream>

using namespace std;

class Repository {
private:
	string path;
	vector<Produs> produse;
public:
	Repository(string p) : path(p) {
		ifstream file(path);

		string line;
		while (getline(file, line)) {
			stringstream ss(line);

			string id, nume, tip, pret;
			getline(ss, id, '@');
			getline(ss, nume, '@');
			getline(ss, tip, '@');
			getline(ss, pret, '@');

			produse.push_back(Produs(stoi(id), nume, tip, stod(pret)));
		}
		file.close();
	}

	vector<Produs>& getAll() { return produse; }

	void writeToFile() {
		ofstream file(path);

		for (auto& prod : produse)
		{
			file << prod.getId() << "@" << prod.getNume() << "@" << prod.getTip() << "@" << prod.getPret() << endl;
		}

		file.close();
	}

	void add(const Produs p) {
		for (auto& prod : produse)
			if (prod.getId() == p.getId())
				throw invalid_argument("Id existent");
		produse.push_back(p);
		writeToFile();
	}
};
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
	Repository(const string p) : path(p) {
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
	}

	vector<Produs>& getAll() { return produse; }
};
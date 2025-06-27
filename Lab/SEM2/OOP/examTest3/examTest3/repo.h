#pragma once

#include <string>
#include <vector>
#include <fstream>
#include <sstream>

#include "Student.h"

using namespace std;

class Repo {
private:
	string path;
	vector<Student> studenti;

	void writeToFile() {
		ofstream file(path);
		for (auto& el : studenti)
			file << el.getNr() << "," << el.getNume() << "," << el.getVarsta() << "," << el.getFacultate() << "\n";

		file.close();
	}
public:
	Repo(string p) : path(p) {
		ifstream file(path);

		string line;
		while (getline(file, line))
		{
			stringstream ss(line);

			string id, nume, varsta, faculta;

			getline(ss, id, ',');
			getline(ss, nume, ',');
			getline(ss, varsta, ',');
			getline(ss, faculta, ',');

			studenti.push_back(Student(stoi(id), nume, stoi(varsta), faculta));
		}

		file.close();
	}

	vector<Student>& getAll() { return studenti;  }

	void intinerire() {
		for (auto& el : studenti)
			el.setVarsta(el.getVarsta() - 1);

		writeToFile();
	}

	void imbatranire() {
		for (auto& el : studenti)
			el.setVarsta(el.getVarsta() + 1);
		writeToFile();

	}

	void stergeR(int id) {
		for (auto it = studenti.begin(); it <= studenti.end(); it++)
			if (it->getNr() == id)
			{
				studenti.erase(it);
				break;
			}

		writeToFile();
	}

	void adauga(const Student& stud)
	{
		studenti.push_back(stud);
		writeToFile();
	}

	Student getStudentById(int id)
	{
		for (auto& el : studenti)
			if (el.getNr() == id)
				return el;
		return studenti[0];
	}
};
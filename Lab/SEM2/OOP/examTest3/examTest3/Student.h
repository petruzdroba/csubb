#pragma once
#include <string>

using namespace std;

class Student {
private:
	int nrMatricol;
	string nume;
	int varsta;
	string facultate;
public:


	Student(int nrMatricol, const string& nume, int varsta, const string& facultate)
		: nrMatricol(nrMatricol), nume(nume), varsta(varsta), facultate(facultate)
	{
	}

	bool operator==(const Student& other) const
	{
		return nrMatricol == other.nrMatricol && nume == other.nume && varsta == other.varsta && facultate == other.facultate;
	}

	int getNr() const { return nrMatricol; }
	string getNume() const { return nume; }
	string getFacultate() const { return facultate; }
	int getVarsta() const { return varsta; }

	void setVarsta(int n) { varsta = n; }
};
#pragma once
#include <iostream>

using namespace std;

class Produs {
private:
	int id;
	string nume, tip;
	double pret;
public:


	Produs(int id, const string& nume, const string& tip, double pret)
		: id(id), nume(nume), tip(tip), pret(pret)
	{
	}

	bool operator==(const Produs& other) const
	{
		return id == other.id && nume == other.nume && tip == other.tip && pret == other.pret;
	}

	int getId() const { return id; }
	string getNume() const { return nume; }
	string getTip() const { return tip; }
	double getPret() const { return pret; }
};
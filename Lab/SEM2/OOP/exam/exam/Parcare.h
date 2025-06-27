#pragma once

#include <string>

using namespace std;

class Parcare {
private:
	string adresa;
	int linii, coloane;
	string stare;
public:


	Parcare(const string& adresa, int linii, int coloane, const string& stare)
		: adresa(adresa), linii(linii), coloane(coloane), stare(stare)
	{
	}

	bool operator==(const Parcare& other) const
	{
		return adresa == other.adresa && linii == other.linii && coloane == other.coloane && stare == other.stare;
	}

	int getTotalLocuri() const { return linii * coloane; }

	string getAdresa() const { return adresa; }

	int getLinii() const { return linii; }
	int getColoanre() const { return coloane; }
	string getStare() const { return stare; }
};
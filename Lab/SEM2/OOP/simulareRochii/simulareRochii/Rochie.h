#pragma once
#include <string>

using namespace std;

class Rochie
{
private:
	int cod;
	string denumire;
	string marime;
	int pret;
	float disponibilitate;

public:
	

	Rochie(int cod, const string& denumire, const string& marime, int pret, float disponibilitate)
		: cod(cod), denumire(denumire), marime(marime), pret(pret), disponibilitate(disponibilitate)
	{
	}

	Rochie() = default;

	int getCod()const {
		return cod;
	}

	string getDenumire() const{
		return denumire;
	}

	string getMarime() const {
		return marime;
	}

	int getPret() const {
		return pret;
	}

	bool getDispoibilitate()const {
		return disponibilitate;
	}

	void setDisponiobilitate(bool val) {
		this->disponibilitate = val;
	}

	bool operator==(const Rochie& other) const
	{
		return cod == other.cod && denumire == other.denumire && marime == other.marime && pret == other.pret && disponibilitate == other.disponibilitate;
	}
};
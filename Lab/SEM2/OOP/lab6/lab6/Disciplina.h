#pragma once
#include <string>

using namespace std;

class Disciplina {

private:
	string denumire;
	int ore;
	string tip;
	string profesor;

public:
	Disciplina(string denumire, int ore, string tip, string profesor) {
		this->denumire = denumire;
		this->ore = ore;
		this->tip = tip;
		this->profesor = profesor;
	}

	/*constructor de copiere*/
	Disciplina(const Disciplina& other) {
		denumire = other.denumire;
		ore = other.ore;
		tip = other.tip;
		profesor = other.profesor;
	}

	bool operator==(const Disciplina& other) const {
		return (denumire == other.getDenumire() && ore == other.getOre() && tip == other.getTip() && profesor == other.getProfesor());
	}

	string getDenumire() const;

	int getOre() const;

	string getTip() const;

	string getProfesor() const;

	void setDenumire(const string& newDenumire);

	void setOre(const int& newOre);

	void setTip(const string& newTip);

	void setProfesor(const string& newProfesor);
};
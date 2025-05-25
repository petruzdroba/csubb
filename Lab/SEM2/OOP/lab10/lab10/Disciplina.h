#pragma once
#include <string>
#include <iostream>

using namespace std;

class Disciplina {

private:
	string denumire;
	int ore;
	string tip;
	string profesor;

public:
	/*Disciplina() 
		: denumire(""), ore(0), tip(""), profesor("") {}
	;*/

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
		cout << "Obiect copiat\n";
	}

	bool operator==(Disciplina& other) const {
		return (denumire == other.getDenumire() && ore == other.getOre() && tip == other.getTip() && profesor == other.getProfesor());
	}

	Disciplina& operator=(const Disciplina& other) {
		if (this == &other) return *this;  // Self-assignment check

		// For classes with only primitive data members, just copy them directly
		this->denumire = other.denumire;  // Example: assuming Disciplina has a `name` member variable
		this->ore = other.ore;
		this->profesor = other.profesor;
		this->tip = other.tip;
		return *this;
	}

	~Disciplina() {
		
	}

	string getDenumire() const;

	int getOre() const noexcept;

	string getTip() const;

	string getProfesor() const;

	void setDenumire(const string& newDenumire);

	void setOre(const int& newOre) noexcept;

	void setTip(const string& newTip);

	void setProfesor(const string& newProfesor);
};
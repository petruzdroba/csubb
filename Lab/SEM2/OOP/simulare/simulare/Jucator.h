#pragma once

#include <string>

using namespace std;

class Jucator {
private:
	string nume;
	string tara;
	int meciuri;
	int puncte;
	int rebounds;
	int assist;

public:


	Jucator(const string& nume, const string& tara, int meciuri, int puncte, int rebounds, int assist)
		: nume(nume), tara(tara), meciuri(meciuri), puncte(puncte), rebounds(rebounds), assist(assist)
	{
	}

	string getNume()
	{
		return nume;
	}

	string getTara() {
		return tara;
	}

	int getMeciuri() {
		return meciuri;
	}

	int getPuncte() {
		return puncte;
	}

	int getRebounds() {
		return rebounds;
	}

	int getAssisturi() {
		return assist;
	}

	const int getPuncte() const {
		return puncte;
	}

	const int getRebounds() const {
		return rebounds;
	}

	const int getAssisturi() const {
		return assist;
	}
};
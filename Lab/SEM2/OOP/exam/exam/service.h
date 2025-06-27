#pragma once

#include "repo.h"
#include "Parcare.h"
#include <ob.h>

#include <vector>
#include <algorithm>

using namespace std;

class Service: public Observator {
private:
	Repo& r;
public:
	Service(Repo& r) :r(r) {};

	vector<Parcare>& getAllS() {//descrescator
		/*
			Sorteaza descrescator dupa nr total de locuri si returneaza parcarile
			pre:
				r este un repo valid
			post:
				se returneaza lista de parcari sortata
		*/
		auto& p = r.getAll();

		sort(p.begin(), p.end(), [](const Parcare& a, const Parcare& b) {return a.getTotalLocuri() > b.getTotalLocuri(); });

		return p;
	}
	/// <summary>
	/// /
	/// </summary>
	/// <param name="adresa"></param>
	/// <param name="linii"></param>
	/// <param name="coloane"></param>
	/// <param name="stares"></param>
	void adaugaS(string adresa, int linii, int coloane, string stares)
		/*
			Creeaza si adauga o parcare noua
			pre:
				adresa: string, non-empty altdfel arunca
				linii, coloane: int, numere pozitive, altfel aruna 
				stare: string, sa aiba linii*coloane ecaractere
			post:
				Se creeaza o parcare de tip parcare, si se adauga in Repo
		*/
	{
		if (adresa == "")
			throw invalid_argument("Adresa invalida");

		if(linii < 1 || coloane < 1)
			throw invalid_argument("Size invalid");

		if(linii*coloane != (int)stares.size())
			throw invalid_argument("Stare invalid");

		r.adauga(Parcare(adresa, linii, coloane, stares));
		notify();
	}

	void modificaS(string adresa, int linii, int coloane, string stares)
	{
		/*
			Creeaza si modifica Paracarea cu adresa adresa
			pre:
				adresa: string , non_empty altfel arunca
				linii, coloane: int, apartin [1,5] altfel arunca
				stare: string, sa aiba linii*coloane ecaractere
			post:
				se modifica parcarea cu adresa adresa
		*/
		if (adresa == "")
			throw invalid_argument("Adresa invalida");

		if (linii < 1 || linii >5)
			throw invalid_argument("Size invalid");

		if (coloane < 1 || coloane >5)
			throw invalid_argument("Size invalid");

		if (linii * coloane != (int)stares.size())
			throw invalid_argument("Stare invalid");

		r.modifica(Parcare(adresa, linii, coloane, stares));
		notify();

	}
};
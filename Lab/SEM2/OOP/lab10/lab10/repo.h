#pragma once
#include <vector>
#include <string>
#include <sstream>
#include <fstream>
#include "Disciplina.h"

using namespace std;

class Repository {
private:
	string path;
	vector<Disciplina> discipline;

	void writeToFile(const Disciplina& d)const
	{
		if (path != "")
		{
			ofstream file(this->path, ios::app);
			file << d.getDenumire() << "," << d.getOre() << "," << d.getTip() << "," << d.getProfesor() << "\n";
			file.close();
		}
	}

	void overwriteToFile()const
	{
		if (path != "")
		{
			ofstream file(this->path);
			for (int index = 0; index < (int)discipline.size(); ++index)
			{
				file << discipline[index].getDenumire() << "," << discipline[index].getOre() << "," << discipline[index].getTip() << "," << discipline[index].getProfesor() << "\n";
			}
			file.close();
		}
	}

public:

	Repository() noexcept : path("") {}
	/*
		constructor repository
	*/

	Repository(const string& path)
	{
		ifstream file(path);
		this->path = path;

		string line;
		while (getline(file, line))
		{
			stringstream ss(line);
			string denumire, tip, profesor, ore;

			getline(ss, denumire, ',');
			getline(ss, ore, ',');
			getline(ss, tip, ',');
			getline(ss, profesor, ',');

			discipline.push_back(Disciplina(denumire, stoi(ore), tip, profesor));
		}
	}

	int getLungime()const noexcept;
	/*
		functie care returneaza lungimea listei de obiecte
		output:
			un numar reprezentand numarul de obiecte din lista
	*/

	const vector<Disciplina>& getAll() const noexcept {
		return discipline;  // Const version
	}
	/*
		functie care returneaza toate obiectele din lista
		output:
			vector de Disciplina
	*/

	void adauga(const Disciplina& disciplina);
	/*
		functie care adauga un obiect disciplina in repository
		input:
			disciplina: Disciplina
				denumire : string != ''
				ore : int > 0
				tip : string != ''
				profesor: string != ''
	*/

	void sterge(const int pozitie);
	/*
		functie care sterge un obiect de la pozitia pozitie
		input:
			pozitie : int , pozitie < repo.getLungime()
		output:
			un obiect de tipul Disciplina care se afla la pozitia pozitie
			un obiect gol daca pozitia nu este consisitenta
	*/

	void modifica(const int pozitie, const Disciplina& newDisciplina);
	/*
		functie care inlocuieste obiectul de la pozitia pozitie cu newDisciplina
		input:
			pozitie : int , pozitie < repo.getLungime()
			disciplina: Disciplina
				denumire : string != ''
				ore : int > 0
				tip : string != ''
				profesor: string != ''
	*/

};
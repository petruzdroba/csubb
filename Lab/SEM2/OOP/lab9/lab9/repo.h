#pragma once
#include <vector>
#include <map>
#include <string>
#include <sstream>
#include <fstream>
#include "exception.h"
#include "Disciplina.h"
#include <random>

using namespace std;

class RepositoryAbs
{
protected:
	virtual void writeToFile(const Disciplina& disciplina)const = 0;

	virtual void overwriteToFile()const = 0;

public:
	virtual void adauga(const Disciplina& disciplina) = 0;

	virtual void sterge(const int pozitie) = 0;

	virtual void modifica(int pozitie, const Disciplina& newDisciplina) = 0;

	const virtual vector<Disciplina>& getAll() noexcept = 0;

	virtual int getLungime() const = 0;

	virtual ~RepositoryAbs() {};
};

class Repository : public RepositoryAbs {
private:
	string path;
	vector<Disciplina> discipline;

protected:
	void writeToFile(const Disciplina& d)const override
	{
		if (path != "")
		{
			ofstream file(this->path, ios::app);
			file << d.getDenumire() << "," << d.getOre() << "," << d.getTip() << "," << d.getProfesor() << "\n";
			file.close();
		}
	}

	void overwriteToFile()const override
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

	~Repository() override {};

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

	int getLungime()const noexcept override;
	/*
		functie care returneaza lungimea listei de obiecte
		output:
			un numar reprezentand numarul de obiecte din lista
	*/

	const vector<Disciplina>& getAll() noexcept override {
		return discipline;  // Const version
	}
	/*
		functie care returneaza toate obiectele din lista
		output:
			vector de Disciplina
	*/

	void adauga(const Disciplina& disciplina) override;
	/*
		functie care adauga un obiect disciplina in repository
		input:
			disciplina: Disciplina
				denumire : string != ''
				ore : int > 0
				tip : string != ''
				profesor: string != ''
	*/

	void sterge(const int pozitie) override;
	/*
		functie care sterge un obiect de la pozitia pozitie
		input:
			pozitie : int , pozitie < repo.getLungime()
		output:
			un obiect de tipul Disciplina care se afla la pozitia pozitie
			un obiect gol daca pozitia nu este consisitenta
	*/

	void modifica(const int pozitie, const Disciplina& newDisciplina) override;
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

class RepositoryDictionar : public RepositoryAbs
{
private:
	float probabilitate;
	string path;
	map<int, Disciplina> discipline;
	vector<Disciplina> values;

protected:

	void writeToFile(const Disciplina& d)const override
	{
		if (path != "")
		{
			ofstream file(this->path, ios::app);
			file << d.getDenumire() << "," << d.getOre() << "," << d.getTip() << "," << d.getProfesor() << "\n";
			file.close();
		}
	}

	void overwriteToFile()const override
	{
		if (path != "")
		{
			ofstream file(this->path);

			for (const auto& pair : discipline)
			{
				file << pair.second.getDenumire() << "," << pair.second.getOre() << "," << pair.second.getTip() << "," << pair.second.getProfesor() << "\n";

			}

			file.close();
		}
	}


public:
	RepositoryDictionar(const float& p) noexcept : probabilitate(p), path("") {};


	~RepositoryDictionar() override {};

	RepositoryDictionar(const float& p, const string& path)
	{
		this->probabilitate = p;

		ifstream file(path);
		this->path = path;
		int index = 1;

		string line;
		while (getline(file, line))
		{
			stringstream ss(line);
			string denumire, tip, profesor, ore;

			getline(ss, denumire, ',');
			getline(ss, ore, ',');
			getline(ss, tip, ',');
			getline(ss, profesor, ',');

			discipline[index-1] = Disciplina(denumire, stoi(ore), tip, profesor);
		}
	}

	int getLungime()const noexcept override
	{
		return (int)discipline.size();
	}


	const vector<Disciplina>& getAll()  noexcept override {
		values.clear();

		for (const auto& pair : discipline)
		{
			values.push_back(pair.second);
		}

		return values;
	}

	void adauga(const Disciplina& disciplina) override
	{
		mt19937 mt{ random_device{}() };
		uniform_real_distribution<> dist(0, 1);

		if (dist(mt) <= probabilitate)
			throw MyException("adauga: unlucky repo");

		if (discipline.empty())
		{
			discipline[1] = disciplina;
		}
		else
		{
			int lastKey = discipline.rbegin()->first;
			discipline[lastKey + 1] = disciplina;
		}
		writeToFile(disciplina);
	}


	void sterge(const int pozitie) override
	{
		mt19937 mt{ random_device{}() };
		uniform_real_distribution<> dist(0, 1);

		if (dist(mt) <= probabilitate)
			throw MyException("sterge: unlucky repo");

		discipline.erase(pozitie);
		overwriteToFile();
	}


	void modifica(const int pozitie, const Disciplina& newDisciplina) override
	{
		mt19937 mt{ random_device{}() };
		uniform_real_distribution<> dist(0, 1);

		if (dist(mt) <= probabilitate)
			throw MyException("modifica: unlucky repo");

		discipline[pozitie] = newDisciplina;
		overwriteToFile();
	}
};
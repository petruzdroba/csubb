#pragma once
#include "repo.h"
#include "Contract.h"
#include "undo.h"

class Service {
private:
	private:
		RepositoryAbs& repo; 
		Contract contract;
		vector<unique_ptr<UndoAction>> undoList;

	public:
		Service(RepositoryAbs& repo, Contract& contract) : repo(repo), contract(contract) {}

	/*
		constructor service
		input:
			repo: Repository
		output:
			service: Service cu un repo: Repository
	*/

	bool adaugaService(const string& denumire, const int& ore, const string& tip, const string& profesor);
	/*
		functie care adauga in repo, prin service, validand obiectul
		input:
			denumire : string != ''
			ore : int > 0
			tip : string != ''
			profesor: string != ''
		output:
			true/1 - daca obiectul a fost creat si adaugat cu success
			false/0 - daca validarea a esuat
	*/

	void stergeService(const int pozitie);
	/*
		functie care sterge obiectul de la pozitia pozitie din repo prin service
		input:
			pozitie : int pozitie < service.getLungime() si pozitie > 0
		output:
			obiect de tipul Disciplina care a fost eliminat
	*/

	bool modificaService(const int pozitie, const string& denumire, const int& ore, const string& tip, const string& profesor);
	/*
		functie care modifica atributele unuei Disciplina de la pozitia pozitie
		input:
			pozitie : int pozitie < service.getLungime() si pozitie > 0
			denumire : string != ''
			ore : int > 0
			tip : string != ''
			profesor: string != ''
		output:
			true/1 - daca obiectul a fost creat si modificat cu success
			false/0 - daca validarea a esuat
	*/

	const vector<Disciplina>& getAllService()noexcept;
	/*
		functie care returneaza toate obiectele din lista
		output:
			vector de Disciplina
	*/

	int getLungimeService() const noexcept;
	/*
		functie care returneaza lungimea listei de obiecte
		output:
			un numar reprezentand numarul de obiecte din lista
	*/
	const vector<Disciplina> filtrareOre(const int nrOre) ;

	const vector<Disciplina> filtrareProfesor(const string numeProfesor);

	const vector<Disciplina>  sortareDenumire();

	const vector<Disciplina>  sortareTip();

	const vector<Disciplina>  sortareOre();

	const vector<Disciplina>  sortareProfesor();


	void adaugaContractService(const string denumire);

	int getContractLungime() const;

	void deleteAllContract();

	void randomPopulateContract(const int nrDiscipline);

	void exportContract(const string path) const;

	void undo();
};
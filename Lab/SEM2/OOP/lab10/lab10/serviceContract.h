#pragma once
#include <Contract.h>
#include <exception.h>
#include <repo.h>
#include <random>


class ServiceContract
{
private:
	Repository repo;
	Contract contract;
public:
	ServiceContract(const Repository& repo, const Contract& contract)
		: repo(repo), contract(contract)
	{
	}

	void adaugaContractService(const string denumire)
	{
		for (const Disciplina& d : contract.getAll())
		{
			if (d.getDenumire() == denumire)
				return;
		}

		auto found = find_if(repo.getAll().begin(), repo.getAll().end(),
			[&denumire](const Disciplina& d) {
				return d.getDenumire() == denumire;
			});

		if (found != repo.getAll().end())
		{
			//adaug in contract
			Disciplina newDisciplina = *found;
			contract.adaugaContract(newDisciplina);
		}
		else {
			throw MyException("Service: Denumire inexistenta");
		}
	}

	int getContractLungime() const
	{
		return contract.getLungime();
	}

	void deleteAllContract()
	{
		contract.deleteAll();
	}

	void randomPopulateContract(const int nrDiscipline)
	{
		if (nrDiscipline <= 0)
			throw MyException("Service: Nr Discipline imposibil");

		if (repo.getLungime() <= 0)
			throw MyException("Service: Nu exista discipline");

		if (repo.getLungime() < nrDiscipline)
			throw MyException("Service: Nu exista discipline");

		mt19937 mt{ random_device{}() };
		uniform_int_distribution<> dist(0, repo.getLungime() - 1);

		int count = 0;
		while (count < nrDiscipline && contract.getLungime() + nrDiscipline - count <= repo.getLungime())
		{
			int rndNr = dist(mt);
			const Disciplina& d = repo.getAll()[rndNr];
			int prevCount = contract.getLungime();
			contract.adaugaContract(d);
			if (prevCount != contract.getLungime())
				count++;
		}
	}

	void exportContract(const string path) const
	{
		ofstream file(path);
		for (int index = 0; index < contract.getLungime(); ++index)
		{
			file << contract.getAll()[index].getDenumire() << "," << contract.getAll()[index].getOre() << "," << contract.getAll()[index].getTip() << "," << contract.getAll()[index].getProfesor() << "\n";
		}
		file.close();
	}

	const vector<Disciplina> getAllContract() const noexcept
	{
		return contract.getAll();
	}

};
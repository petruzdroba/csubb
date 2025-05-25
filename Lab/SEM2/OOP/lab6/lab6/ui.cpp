#include <iostream>
#include <string>
#include <vector>

#include "ui.h"
#include "repo.h"
#include "service.h"
#include "Disciplina.h"

using namespace std;

static int meniu(int& option)
{
	cout << "Contract de studii\n";
	cout << "1. Adauga\n";
	cout << "2. Sterge\n";
	cout << "3. Modifica\n";
	cout << "4. Print all\n";
	cout << "5. Filtrare nr ore mai mare\n";
	cout << "6. Filtrare nume profesor\n";
	cout << "7. Sortare\n";
	cout << "0. Exit\n";
	cin >> option;
	return option;
}

static int meniuSortare(int& option)
{
	cout << "Meniu sortare\n";
	cout << "1. Sortare Denumire\n";
	cout << "2. Sortare Ore\n";
	cout << "3. Sortare Tip\n";
	cout << "4. Sortare Profesor\n";
	cout << "0. Exit\n";
	cin >> option;
	return option;
}

void run()
{
	Repository repository;
	Service service{ repository };
	int option;
	meniu(option);
	while (option)
	{
		try
		{
			if (option == 0)
			{
				break;
			}
			else if (option == 1)
			{
				string denumire, tip, profesor;
				int ore;
				cout << "denumire-string>>>";
				cin.ignore();
				getline(cin, denumire);
				if (denumire.empty()) {
					throw invalid_argument("Input invalid: string is empty!");
				}

				cout << "ore-int>>>";
				cin >> ore;
				if (cin.fail()) {
					cin.clear();
					cin.ignore(numeric_limits<streamsize>::max(), '\n');
					throw invalid_argument("Input invalid: nu este un numar intreg!");
				}

				cin.ignore();
				cout << "tip-string>>>";
				getline(cin, tip);
				if (tip.empty()) {
					throw invalid_argument("Input invalid: string is empty!");
				}

				cout << "profesor-string>>>";
				getline(cin, profesor);
				if (profesor.empty()) {
					throw invalid_argument("Input invalid: string is empty!");
				}

				try {
					service.adaugaService(denumire, ore, tip, profesor);
				}
				catch (invalid_argument& e)
				{
					cout << e.what() << endl;
				}

				meniu(option);
			}
			else if (option == 2)
			{
				int pozitie;
				cout << "pozitie-int>>>";
				cin >> pozitie;
				if (cin.fail()) {
					cin.clear();
					cin.ignore(numeric_limits<streamsize>::max(), '\n');
					throw invalid_argument("Input invalid: nu este un numar intreg!");
				}
				service.stergeService(pozitie);
				meniu(option);
			}
			else if (option == 3)
			{
				int pozitie;
				cout << "pozitie-int>>>";
				cin >> pozitie;
				if (cin.fail()) {
					cin.clear();
					cin.ignore(numeric_limits<streamsize>::max(), '\n');
					throw invalid_argument("Input invalid: nu este un numar intreg!");
				}
				string denumire, tip, profesor;
				int ore;
				cout << "new denumire-string>>>";
				cin.ignore();
				getline(cin, denumire);
				if (denumire.empty()) {
					throw invalid_argument("Input invalid: string is empty!");
				}
				cout << "new ore-int>>>";
				cin >> ore;
				if (cin.fail()) {
					cin.clear();
					cin.ignore(numeric_limits<streamsize>::max(), '\n');
					throw invalid_argument("Input invalid: nu este un numar intreg!");
				}
				cin.ignore();
				cout << "new tip-string>>>";
				getline(cin, tip);
				if (tip.empty()) {
					throw invalid_argument("Input invalid: string is empty!");
				}
				cout << "new profesor-string>>>";
				getline(cin, profesor);
				if (profesor.empty()) {
					throw invalid_argument("Input invalid: string is empty!");
				}

				try {
					service.modificaService(pozitie, denumire, ore, tip, profesor);
				}
				catch (invalid_argument& e)
				{
					cout << e.what() << endl;
				}

				meniu(option);
			}
			else if (option == 4)
			{
				cout << "Denumire" << " " << "Ore" << " " << "Tip" << " " << "Profesor" << endl;
				int index = 0;
				for (const Disciplina& d : service.getAllService())
				{
					cout << index << " )" << d.getDenumire() << " " << d.getOre() << " " << d.getTip() << " " << d.getProfesor() << endl;
					index++;
				}
				meniu(option);
			}
			else if (option == 5)
			{
				int nrOre;
				cout << "ore>>";
				cin >> nrOre;
				if (cin.fail()) {
					cin.clear();
					cin.ignore(numeric_limits<streamsize>::max(), '\n');
					throw invalid_argument("Input invalid: nu este un numar intreg!");
				}
				int index = 0;
				for (const Disciplina& d : service.filtrareOre(nrOre))
				{
					cout << index << " )" << d.getDenumire() << " " << d.getOre() << " " << d.getTip() << " " << d.getProfesor() << endl;
					index++;
				}
				meniu(option);
			}
			else if (option == 6)
			{
				string profesor;
				cout << "new profesor-string>>>";
				getline(cin, profesor);
				if (profesor.empty()) {
					throw invalid_argument("Input invalid: string is empty!");
				}
				int index = 0;
				for (const Disciplina& d : service.filtrareProfesor(profesor))
				{
					cout << index << " )" << d.getDenumire() << " " << d.getOre() << " " << d.getTip() << " " << d.getProfesor() << endl;
					index++;
				}
				meniu(option);
			}
			else if (option == 7)
			{
				int option2;
				meniuSortare(option2);
				while (option2)
				{
					if (!option2)
						break;
					else if (option2 == 1)
					{
						int index = 0;
						for (const Disciplina& d : service.sortareDenumire())
						{
							cout << index << " )" << d.getDenumire() << " " << d.getOre() << " " << d.getTip() << " " << d.getProfesor() << endl;
							index++;
						}
						meniuSortare(option2);
					}
					else if (option2 == 2)
					{
						int index = 0;
						for (const Disciplina& d : service.sortareOre())
						{
							cout << index << " )" << d.getDenumire() << " " << d.getOre() << " " << d.getTip() << " " << d.getProfesor() << endl;
							index++;
						}
						meniuSortare(option2);
					}
					else if (option2 == 3)
					{
						int index = 0;
						for (const Disciplina& d : service.sortareTip())
						{
							cout << index << " )" << d.getDenumire() << " " << d.getOre() << " " << d.getTip() << " " << d.getProfesor() << endl;
							index++;
						}
						meniuSortare(option2);
					}
					else if (option2 == 4)
					{
						int index = 0;
						for (const Disciplina& d : service.sortareProfesor())
						{
							cout << index << " )" << d.getDenumire() << " " << d.getOre() << " " << d.getTip() << " " << d.getProfesor() << endl;
							index++;
						}
						meniuSortare(option2);
					}
				}
				meniu(option);
			}
		}
		catch (invalid_argument& e)
		{
			cout << e.what() << endl;
		}
	}
}
#include "service.h"
#include <stdexcept>
#include <algorithm>
#include <fstream>
#include <math.h>

using namespace std;

void valideaza(const string& nume, const string& tara) {
	bool space = false;
	for (char c : nume)
	{
		if (c == ' ')
			space = true;
	}

	if (!space)
		throw invalid_argument("Numele nu e valid");

	if (tara == "")
		throw invalid_argument("Tara nu e valid");

}

vector<Jucator>& Service::all()
{
	return repo.getAll();
}

int Service::len()
{
	return repo.getLen();;
}

bool Service::westbrook(Jucator& j)
{
	return j.getPuncte() / j.getMeciuri() >= 10 && j.getRebounds() / j.getMeciuri() >= 10 && j.getAssisturi() / j.getMeciuri() >= 10;
}

void Service::adauga(const string& nume, const string& tara, int meciuri, int puncte, int rebounds, int assist)
{
	valideaza(nume, tara);

	Jucator j(nume, tara, meciuri, puncte, rebounds, assist);

	repo.adauga(j);
}

void Service::sortOpt(vector<Jucator>& j, string option)
{
	vector<Jucator> sortat = all();

	if (option == "Puncte")
		sort(sortat.begin(), sortat.end(), [](const Jucator& a, const Jucator& b) {return a.getPuncte() > b.getPuncte(); });

	if (option == "Rebounds")
		sort(sortat.begin(), sortat.end(), [](const Jucator& a, const Jucator& b) {return a.getRebounds() > b.getRebounds(); });

	if (option == "Assist")
		sort(sortat.begin(), sortat.end(), [](const Jucator& a, const Jucator& b) {return a.getAssisturi() > b.getAssisturi(); });

	j = sortat;
}

void Service::exportProcentage(int proc, string option, string path)
{
	vector<Jucator> sortat = all();
	sortOpt(sortat, option);

	float nr = ( (float)proc/100 * len());

	ofstream file(path);

	for (int i = 0; i < nr; ++i)
	{
		file << sortat[i].getNume() << ',' << sortat[i].getTara() << ',' << sortat[i].getMeciuri() << ',' << sortat[i].getPuncte() << ',' << sortat[i].getRebounds() << ',' << sortat[i].getAssisturi() << '\n';
	}

	file.close();
}



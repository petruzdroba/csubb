#include <iostream>
#include <fstream>
#include <vector>

using namespace std;

//la fiecare pas se trece prin arbore, se alege o frunza (fara copii) si se elimina, pana se ajunge la radacina

vector<int> codPrufer(vector<int> arbore, vector<int> children)
{
	vector<int> sol;
	vector<bool> removed(arbore.size(), false);

	for (int j = 0; j < arbore.size() - 1; ++j)
	{
		int min = -1;
		for (int i = 0; i < arbore.size(); ++i)
		{
			if (!removed[i] && children[i] == 0)
			{
				min = i;
				break;
			}
		}

		if (min != -1)
		{
			sol.push_back(arbore[min]);
			children[arbore[min]]--;
			removed[min] = true;
		}
	}

	return sol;
}

int main(int argc, char** argv)
{
	ifstream in(argv[1]);
	ofstream out(argv[2]);

	int n;
	in >> n;
	vector<int> arbore, children;

	for (int i = 0; i < n; ++i)
	{
		arbore.push_back(0);
		children.push_back(0);
	}
	for (int index = 0; index < n; ++index)
	{
		int parent;
		in >> parent;
		arbore[index] = parent;
		if (parent != -1)
			children[parent] += 1;
	}

	vector<int> codat = codPrufer(arbore, children);

	out << codat.size()<<endl;

	for (int node : codat)
	{
		out << node << " ";
	}
}

#include <iostream>
#include <fstream>
#include <vector>

using namespace std;

vector<int> decodPrufer(const vector<int> cod)
{
    vector<int> grad(cod.size() + 1, 1);
    for (int node : cod)
        grad[node]++;

    vector<int> arbore(cod.size() + 1, -2);

    for (int x : cod) {
        int y = -1;
        for (int i = 0; i < cod.size() + 1; ++i) {
            if (grad[i] == 1) {
                y = i;
                break;
            }
        }
        grad[y]--;
        grad[x]--;
    }

    for (int i = 0; i < cod.size() + 1; ++i) {
        if (grad[i] == 1) {
            arbore[i] = -1;
            break;
        }
    }

    return arbore;
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

	vector<int> decodat = decodPrufer(arbore);

	out << decodat.size() << endl;

	for (int node : decodat)
	{
		out << node << " ";
	}
}

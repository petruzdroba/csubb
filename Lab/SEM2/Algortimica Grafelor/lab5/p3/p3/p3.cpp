#include <iostream>
#include <fstream>
#include <vector>

using namespace std;

void euler(int n, int node, int graf[][102], vector<int>& ciclu)
{
	for (int i = 0; i < n; ++i)
	{
		if (graf[node][i])
		{
			graf[node][i] = 0;
			graf[i][node] = 0;
			euler(n, i, graf, ciclu);
		}
	}
	ciclu.push_back(node);
}

int main(int argc, char** argv)
{
	ifstream in(argv[1]);
	ofstream out(argv[2]);

	int n, e;
	in >> n >> e;

	int graf[102][102];
	for (int i = 0; i < n; ++i)
		for (int j = 0; j < n; ++j)
			graf[i][j] = 0;

	for (int j = 0; j < e; ++j)
	{
		int node1, node2;
		in >> node1 >> node2;
		graf[node1][node2] = 1;
		graf[node2][node1] = 1;
	}

	vector<int> cicluEuler;
	int starter = 0;

	euler(n, starter, graf, cicluEuler);

	for (int node : cicluEuler)
		out << node << " ";

	in.close();
	out.close();
}


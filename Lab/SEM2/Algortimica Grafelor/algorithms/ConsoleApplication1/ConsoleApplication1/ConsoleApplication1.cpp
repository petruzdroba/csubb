#include <iostream>
#include <fstream>

using namespace std;

#define maxi 101

void read(int& n, int& e, int matrice[][maxi])
{
	ifstream in("graph.in");
	in >> n >> e;

	for (int index = 1; index <= n; ++index)
	{
		for (int jndex = 1; jndex <= n; ++jndex)
			matrice[index][jndex] = 0;

	}

	for (int index = 1; index <= e; ++index)
	{
		int node1, node2;
		in >> node1 >> node2;

		matrice[node1][node2] = 1;
		matrice[node2][node1] = 1;

	}
	in.close();
}

void dfs(int nodCurent, int n, int matrice[][maxi], int vizitati[], int parents[])
{
	vizitati[nodCurent] = 1;
	for (int index = 1; index <= n; ++index)
	{
		if (!vizitati[index] && matrice[nodCurent][index])
		{
			parents[index] = n;
			dfs(index, n, matrice, vizitati, parents);
		}
	}
}

int main()
{
	int n, e, matrice[maxi][maxi], parents[maxi] = { 0 };
	read(n, e, matrice);

	int node1, node2;
	cin >> node1 >> node2;
	matrice[node1][node2] = 0;
	matrice[node2][node1] = 0;

	int vizitati[maxi] = { 0 };
	dfs(1, n, matrice, vizitati, parents);

	for (int index = 1; index <= n; ++index)
	{
		if (!vizitati[index])
		{
			cout << "Nu";
			return 0;
		}
	}
	cout << "DA";
}
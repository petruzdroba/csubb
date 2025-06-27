#include <iostream>
#include <fstream>
#include <queue>

using namespace std;

bool bfs(int n, int graf[][100], int parents[100], int sursa,int destinatie)
{
	bool vizitati[100] = {0};
	vizitati[sursa] = true;

	queue<int> q;
	q.push(sursa);

	while (!q.empty())
	{
		int node = q.front();
		q.pop();

		for (int i = 0; i < n; ++i)
		{
			if (!vizitati[i] && graf[node][i])
			{
				vizitati[i] = true;
				q.push(i);
				parents[i] = node;
			}
		}
	}
	return vizitati[destinatie];
}

int fluxMaxim(int n , int graf[][100], int sursa, int destinatie)
{
	int rezidual[100][100];
	for (int i = 0; i < n; ++i)
	{
		for (int j = 0; j < n; ++j)
		{
			rezidual[i][j] = graf[i][j];
		}
		cout << endl;
	}

	int fluxMaxim = 0, parents[100];
	while (bfs(n, rezidual, parents, sursa, destinatie))
	{
		int fluxCurent = 100000;
		for (int i = destinatie; i != sursa; i = parents[i])
			fluxCurent = min(fluxCurent, rezidual[parents[i]][i]);

		for (int i = destinatie; i != sursa; i = parents[i])
		{
			rezidual[parents[i]][i] -= fluxCurent;
			rezidual[i][parents[i]] += fluxCurent;
		}

		fluxMaxim += fluxCurent;
	}
	return fluxMaxim;
}

int main()
{
	ifstream in("in.txt");
	int n, m, e;
	in >> n >> m >> e;

	int graf[100][100];

	for (int i = 0; i < n + m+2; ++i)
		for (int j = 0; j < n + m+2; ++j)
			graf[i][j] = 0;

	for (int i = 0; i < e; ++i)
	{
		int node1, node2;
		in >> node1 >> node2;
		graf[node1][node2 + n] = 1;

	}

	int sursa = 0, destinatie = n+m+1;

	for (int i = 1; i <= n; ++i)
	{
		graf[sursa][i] = 1;
	}

	for (int i = n+1; i <= m+n; ++i)
	{
		graf[i][destinatie] = 1;
	}

	//total varfuri n+m+1
	cout << fluxMaxim(n + m+2, graf, 0, n + m+1);
}
#include <iostream>
#include <fstream>
#include <queue>

using namespace std;

bool bfs(int n, int graf[][100], int parents[100], int sursa, int destinatie)
{
	bool vizitati[100] = { 0 };
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
				parents[i] = node;
				q.push(i);
			}
		}
	}
	return vizitati[destinatie];
}

int fluxMaxim(int n, int graf[][100], int sursa, int destinatie)
{
	int rezidual[100][100];
	for (int i = 0; i < n; ++i)
		for (int j = 0; j < n; ++j)
			rezidual[i][j] = graf[i][j];

	int fluxMaxim = 0, parents[100];

	while (bfs(n, rezidual, parents, sursa, destinatie))
	{
		int fluxCurent = 1000000;
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
	int n, e;
	in >> n >> e;

	int graf[100][100];

	for (int i = 0; i < n; ++i)
		for (int j = 0; j < n; ++j)
			graf[i][j] = 0;

	for (int i = 0; i < e; ++i)
	{
		int x, y, c;
		in >> x >> y >> c;
		graf[x][y] = c;
	}

	cout << fluxMaxim(n, graf, 0, n - 1);
}
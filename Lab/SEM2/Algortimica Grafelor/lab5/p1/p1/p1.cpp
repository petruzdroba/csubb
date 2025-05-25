#include <iostream>
#include <fstream>
#include <queue>

using namespace std;

bool bfs(int n, int graf[][100], int parent[100], int sursa, int destinatie)
{
	bool visited[100] = { 0 };

	queue<int> q;
	q.push(sursa);
	visited[sursa] = true;

	while (!q.empty())
	{
		int node = q.front();
		q.pop();

		for (int i = 0; i < n; ++i)
		{
			if (graf[node][i] && !visited[i])
			{
				q.push(i);
				parent[i] = node;
				visited[i] = true;
			}
		}
	}

	return visited[destinatie];
}

int FordFulkerson(int n, int graf[][100], int sursa, int destinatie)
{
	int rezidual[100][100];
	for (int i = 0; i < n; ++i)
		for (int j = 0; j < n; ++j)
			rezidual[i][j] = graf[i][j];

	int parent[100], fluxMax = 0;

	while (bfs(n, rezidual, parent ,sursa, destinatie))
	{
		int fluxCurent = 1000000;

		for (int i = destinatie; i != sursa; i = parent[i])
		{
			fluxCurent = min(fluxCurent, rezidual[parent[i]][i]);
		}

		for (int i = destinatie; i != sursa; i = parent[i])
		{
			rezidual[parent[i]][i] -= fluxCurent;
			rezidual[i][parent[i]] += fluxCurent;
		}

		fluxMax += fluxCurent;
	}
	return fluxMax;
}

int main(int argc, char** argv)
{
	ifstream in(argv[1]);
	ofstream out(argv[2]);

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

	out << FordFulkerson(n, graf, 0, n - 1);
	in.close();
	out.close();
}
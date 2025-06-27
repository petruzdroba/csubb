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

	int parents[100], fluxMaxim = 0;

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
		int node1, node2, flux;
		in >> node1 >> node2 >> flux;
		graf[node1][node2] = flux;
	}

	cout << fluxMaxim(n, graf, 0, n - 1);
}

/*
Problem: Emergency Water Distribution

After a major earthquake, a network of emergency water stations has been set up across a city. Each station can either send, receive, or pass along water to others via temporary pipelines. These pipelines have different maximum flow capacities (in liters per hour) depending on the terrain and equipment.

The city government needs your help to calculate the maximum amount of water that can be sent from the main supply depot to the emergency response center, using the network of stations and pipelines.

Input Format:

You are given:
- An integer n (1 ≤ n ≤ 100) — the number of water stations (numbered from 0 to n - 1).
- An integer m — the number of temporary pipelines.
- m lines follow, each containing three integers u, v, and c, meaning there is a pipeline from station u to station v with a capacity of c liters/hour.
- The main supply depot is station 0, and the emergency response center is station n - 1.

Constraints:
- The network may include multiple paths between stations.
- There will be no self-loops (u ≠ v).
- There may be more than one edge between two stations, but they should be considered as separate pipelines.
- All capacities c are positive integers (1 ≤ c ≤ 10,000).

Objective:

Compute the maximum amount of water (in liters per hour) that can be delivered from station 0 to station n - 1.

Sample Input:
4 5
0 1 1000
0 2 1000
1 2 1
1 3 1000
2 3 1000

Sample Output:
2000

*/
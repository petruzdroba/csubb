#pragma once

#include <iostream>
#include <queue>

using namespace std;

#define size_m 101

void bfs(int startNode, int n, int matrice[][size_m])
{
	int vizitati[size_m] = { 0 };
	queue<int> q;

	vizitati[startNode] = 1;
	q.push(startNode);

	while (!q.empty())
	{
		int currentNode = q.front();
		q.pop();
		cout << currentNode << " ";

		for (int index = 1; index <= n; ++index)
		{
			if (matrice[currentNode][index] && !vizitati[index])
			{
				q.push(index);
				vizitati[index] = 1;
			}
		}
	}
	cout << endl;
}
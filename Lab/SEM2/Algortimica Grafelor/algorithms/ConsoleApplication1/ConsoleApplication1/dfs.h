#pragma once

#include <iostream>

using namespace std;

#define siz_m 101

void dfs(int currentNode, int n, int matrice[][size_m], int vizitati[])
{
	vizitati[currentNode] = 1;
	cout << currentNode << " ";
	for (int index = 1; index <= n; ++index)
	{
		if (matrice[currentNode][index] && !vizitati[index])
		{
			dfs(index, n, matrice, vizitati);
		}
	}
}
#include <iostream>
#include <fstream>
#include <vector>

using namespace std;

void pompare(int capacitate[][100], int flux[][100], vector<int>& exces, int curent, int v)
{
	int minim = min(exces[curent], capacitate[curent][v] - flux[curent][v]);
	flux[curent][v] += minim;
	flux[v][curent] -= minim;
	exces[v] += minim;
	exces[curent] -= minim;
}

void inaltare(int n, int capacitate[][100], int flux[][100], vector<int>& height, int curent)
{
	int heightMin = 100000;
	for (int i = 0; i < n; ++i)
	{
		if (capacitate[curent][i] - flux[curent][i] > 0)
		{
			heightMin = min(heightMin, height[i]);
			height[curent] = heightMin + 1;
		}
	}
}

void descarcare(int n, int curent, int capacitate[][100], int flux[][100], vector<int>& exces, vector<int>& height, vector<int>& vizitati)
{
	while (exces[curent] > 0)
	{
		if (vizitati[curent] < n)
		{
			int v = vizitati[curent];
			if (capacitate[curent][v] - flux[curent][v] > 0 && height[curent] > height[v])
				pompare(capacitate, flux, exces, curent, v);
			else
				vizitati[curent] += 1;
		}
		else
		{
			inaltare(n, capacitate, flux, height, curent);
			vizitati[curent] = 0;
		}
	}
}

int pompareTopologica(int n, int capacitate[][100], int flux[][100], int sursa, int destinatie)
{
	vector<int> exces(n, 0), height(n, 0);
	vector<int> vizitati(n, 0);
	vector<int> L;

	for (int i = 0; i < n; ++i)
	{
		if (i != sursa && i != destinatie)
			L.push_back(i);
	}

	height[sursa] = n;
	exces[sursa] = 100000;

	for (int i = 0; i < n; ++i)
		pompare(capacitate, flux, exces, sursa, i);

	int k = 0;
	while (k < n - 2)
	{
		int curent = L[k];
		int curentH = height[curent];

		descarcare(n, curent, capacitate, flux, exces, height, vizitati);

		if (height[curent] > curentH)
		{
			int aux = L[k];
			for (int i = k; i > 0; --i)
				L[i] = L[i - 1];
			L[0] = aux;
			k = 0;
		}
		else
			k++;
	}

	int fluxMax = 0;
	for (int i = 0; i < n; ++i)
		fluxMax += flux[sursa][i];

	return fluxMax;
}

int main(int argc, char** argv)
{
	ifstream in(argv[1]);
	ofstream out(argv[2]);

	int n, e;
	in >> n >> e;

	int capacitate[100][100], flux[100][100];
	for (int i = 0; i < n; ++i)
	{
		for (int j = 0; j < n; ++j)
		{
			capacitate[i][j] = 0;
			flux[i][j] = 0;
		}
	}

	for (int i = 0; i < e; ++i)
	{
		int x, y, c;
		in >> x >> y >> c;
		capacitate[x][y] = c;
	}

	out << pompareTopologica(n, capacitate, flux, 0, n - 1);
	in.close();
	out.close();
}
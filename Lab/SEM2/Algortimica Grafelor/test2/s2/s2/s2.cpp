#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <math.h>

using namespace std;

class Edge
{
public:
	int a, b;
	float c;
	Edge(int a, int b, float c) : a(a), b(b), c(c) {};
};

static int root(int x, vector<int>& parents)
{
	if (parents[x] != x)
		parents[x] = root(parents[x], parents);
	return parents[x];
}

int main()
{
	ifstream in("in.txt");
	int n;
	in >> n;

	vector<float> capacitati;
	vector<pair<int,int>> coordonate(n);
	for (int i = 0; i < n; ++i)
	{
		int x, y;
		in >> x >> y;
		coordonate[i] = make_pair(x,y);
	}

	vector<Edge> edges, sol;
	for (int i = 0; i < n-1; ++i)
	{
		for (int j = i; j < n; ++j)
		{
			float cost = sqrt((coordonate[j].first - coordonate[i].first) * (coordonate[j].first - coordonate[i].first) + (coordonate[j].second - coordonate[i].second) * (coordonate[j].second - coordonate[i].second));
			edges.push_back(Edge(i, j, cost));
		}
	}

	sort(edges.begin(), edges.end(), [](const Edge& a, const Edge& b) {return a.c < b.c; });

	vector<int> parents(n);
	for (int i = 0; i < n; ++i)
		parents[i] = i;

	for (Edge& e : edges)
	{
		int rootX = root(e.a, parents);
		int rootY = root(e.b, parents);

		if (rootX != rootY)
		{
			sol.push_back(e);
			parents[rootY] = rootX;
		}
	}

	float cost = 0;
	for (Edge& e : sol)
		cost += e.c;
	cout << cost;
}
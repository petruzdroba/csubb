#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <unordered_map>



using namespace std;

class Edge
{
	public:
	int x, y, c;
	Edge(int x, int y, int c) : x(x), y(y), c(c) {};
};

int root(int x, vector<int>& parents)
{
	if (x != parents[x])
		parents[x] = root(parents[x], parents);
	return parents[x];
}

int main()
{
	ifstream in("in.txt");
	int n, e;
	in >> n >> e;

	vector<Edge> edges, sol;

	for (int i = 0; i < e; ++i)
	{
		int x, y, c;
		in >> x >> y >> c;

		edges.push_back(Edge(x, y, c));
	}

	sort(edges.begin(), edges.end(), [](const Edge& a, const Edge& b) {return a.c < b.c; });

	vector<int> parents(n);
	for (int i = 0; i < n; ++i)
		parents[i] = i;

	for (Edge& e : edges)
	{
		int rootX = root(e.x, parents);
		int rootY = root(e.y, parents);

		if (rootX != rootY)
		{
			sol.push_back(e);
			parents[rootY] = rootX;
		}
	}

	int cost = 0;
	for (Edge& e : sol)
	{
		cost += e.c;
	}
	cout << cost;
}
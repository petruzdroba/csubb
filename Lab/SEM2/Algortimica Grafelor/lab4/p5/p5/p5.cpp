#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <unordered_map>

using namespace std;

class Edge
{
public:
	int x, y, w;
	Edge(int nod1, int nod2, int pondere) : x(nod1), y(nod2), w(pondere) {};
};

int root(int x, unordered_map<int, int>& parent)// se mai miscsoreaza
{
	if (parent[x] != x)
		parent[x] = root(parent[x], parent);
	return parent[x];
}

int main(int argc, char** argv)
{
	ifstream in(argv[1]);
	ofstream out(argv[2]);

	int n, e;
	in >> n >> e;
	vector<Edge> edges, sol;
	unordered_map<int, int> parent;

	for (int i = 0; i < e; ++i)
	{
		int x, y, w;
		in >> x >> y >> w;
		edges.push_back(Edge(x, y, w));
	}

	for (int i = 1; i <= n; ++i)
		parent[i] = i;

	sort(edges.begin(), edges.end(), [](const Edge& a, const Edge& b) {return a.w < b.w; });

	for (Edge& e : edges)
	{
		int rootX = root(e.x, parent);
		int rootY = root(e.y, parent);

		if (rootX != rootY)
		{
			sol.push_back(e);
			parent[rootY] = rootX;
		}
	}

	sort(sol.begin(), sol.end(), [](const Edge& a, const Edge& b) {
		if (a.x == b.x)
			return a.y < b.y;
		return a.x < b.x;
		});

	int cost = 0;
	for (Edge& e : sol)
		cost += e.w;

	out << cost << "\n";
	out << sol.size() << "\n";

	for (Edge& e : sol)
	{
		out << e.x << " " << e.y << "\n";
	}

	in.close();
	out.close();
}
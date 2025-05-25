#include <iostream>
#include <fstream>

using namespace std;

#define INF 1000
#define siz 101

int minimumDistanceNode(int n, int d[], int vizitati[])
{
	int minCost = INF, minNode = -1;

	for (int index = 0; index < n; ++index)
	{
		if (!vizitati[index] && d[index] <= minCost)
		{
			minCost = d[index];
			minNode = index;
		}
	}
	return minNode;
}

void dijsktra(int startNode, int n, int matrice[][siz], int d[], int parents[])
{
	for (int index = 0; index < n; ++index)
	{
		d[index] = INF;
	}
	d[startNode] = 0;

	int vizitati[siz] = { 0 };

	for (int index = 0; index < n; ++index)
	{
		int minNode = minimumDistanceNode(n, d, vizitati);
		if (minNode == -1)
			break;
		vizitati[minNode] = 1;

		for (int jndex = 0; jndex < n; ++jndex)
		{
			if (!vizitati[jndex] && matrice[minNode][jndex] && d[minNode] != INF && d[minNode] + matrice[minNode][jndex] < d[jndex])
			{
				d[jndex] = d[minNode] + matrice[minNode][jndex];
				parents[jndex] = minNode;
			}
		}
	}
}

bool bellmanFord(int totalNodes, int edges[][3], int edgeCount, int h[]) {
	for (int i = 0; i < totalNodes; i++) h[i] = INF;
	h[totalNodes - 1] = 0;  // Last node is the new added node

	for (int i = 0; i < totalNodes - 1; ++i) {
		for (int j = 0; j < edgeCount; ++j) {
			int u = edges[j][0];
			int v = edges[j][1];
			int w = edges[j][2];
			if (h[u] != INF && h[u] + w < h[v]) {
				h[v] = h[u] + w;
			}
		}
	}

	for (int j = 0; j < edgeCount; ++j) {
		int u = edges[j][0];
		int v = edges[j][1];
		int w = edges[j][2];
		if (h[u] != INF && h[u] + w < h[v]) {
			return false;  
		}
	}

	return true;
}

int main(int argc, char* argv[])
{
	int n, e, startNode, matrice[siz][siz];
	

	ifstream in(argv[1]);
	ofstream out(argv[2]);
	in >> n >> e >> startNode;

	for (int index = 0; index < n; ++index)
	{
		for (int jndex = 0; jndex < n; ++jndex)
		{
			matrice[index][jndex] = 0;
		}
	}

	for (int index = 0; index < e; ++index)
	{
		int node1, node2, cost;
		in >> node1 >> node2 >> cost;
		matrice[node1][node2] = cost;
	}

	int edges[siz * siz][3];
	int edgeCount = 0;

	for (int i = 0; i < e; ++i) {
		int u, v, cost;
		in >> u >> v >> cost;
		matrice[u][v] = cost;

		edges[edgeCount][0] = u;
		edges[edgeCount][1] = v;
		edges[edgeCount][2] = cost;
		++edgeCount;
	}

	int totalNodes = n + 1;
	for (int i = 0; i < n; ++i) {
		edges[edgeCount][0] = n;  // New node
		edges[edgeCount][1] = i;
		edges[edgeCount][2] = 0;
		++edgeCount;
	}

	int h[siz];
	if (!bellmanFord(totalNodes, edges, edgeCount, h)) {
		out << "Graph contains a negative weight cycle\n";
		return 1;
	}

	int reweighted[siz][siz];
	for (int u = 0; u < n; ++u) {
		for (int v = 0; v < n; ++v) {
			if (matrice[u][v] != 0) {
				reweighted[u][v] = matrice[u][v] + h[u] - h[v];
			}
			else {
				reweighted[u][v] = 0;
			}
		}
	}

	for (int u = 0; u < n; ++u) {
		int d[siz];
		int parents[siz] = { 0 };
		dijsktra(u, n, reweighted, d, parents);

		for (int v = 0; v < n; ++v) {
			if (d[v] == INF)
				out << "INF ";
			else {
				int actualDist = d[v] - h[u] + h[v];
				out << actualDist << " ";
			}
		}
		out << "\n";
	}

	return 0;
}
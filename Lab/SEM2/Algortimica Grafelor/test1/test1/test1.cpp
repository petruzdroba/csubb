#include <iostream>
#include <fstream>

using namespace std;

#define maxi 101

void read(int &n, int &e, int matrice[][maxi])
{
    ifstream in("input.txt");
    in >> n>> e;

    for (int index = 1; index <= n; ++index)
    {
        for (int jndex = 1; jndex <= n; ++jndex)
            matrice[index][jndex] = 0;
    }


    for (int index = 1; index <= e; ++index)
    {
        int node1, node2;
        in >> node1 >> node2;
        matrice[node1][node2] = 1;
        matrice[node2][node1] = 1;
    }
}

void dfs(int currentNode, int n, int matrice[][maxi], int vizitati[])
{
    vizitati[currentNode] = 1;

    for (int index = 1; index <= n; ++index)
    {
        if (!vizitati[index] && matrice[currentNode][index])
        {
            dfs(index, n, matrice, vizitati);
        }
    }
}

int allVisited(int n, int vizitati[])
{
    //det primul nod nevizitat aka componenta conexa noua
    for (int index = 1; index <= n; ++index)
    {
        if (!vizitati[index])
            return index;
    }
    return 0;
}

int main()
{
    int n, e, matrice[maxi][maxi];
    read(n, e, matrice);

    int vizitati[maxi] = { 0 };
    int vizitati2[maxi] = { 0 };

    int firstNode = allVisited(n, vizitati);
    while (firstNode)
    {
        dfs(firstNode, n, matrice, vizitati);

        for (int index = 1; index <= n; ++index)
        {
            if (vizitati[index] && !vizitati2[index])
            {
                cout << index << " ";
                vizitati2[index] = 1;
            }
        }
        cout << endl;
        firstNode = allVisited(n, vizitati);
    }
}

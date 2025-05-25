#include <iostream>
#include <fstream>
#include <queue>
#include <stack>
#include <string.h>
#include "labirint.h"

using namespace std;

void zerorizeaza(int dimensiune, int matrice[100][100])
{
    for (int index = 0; index <= dimensiune; ++index)
    {
        for (int jndex = 0; jndex <= dimensiune; ++jndex)
        {
            matrice[index][jndex] = 0;
        }
    }
}

void citireMatriceAdiacenta(int &dimensiune, int matriceAdiacenta[100][100])
{
    ifstream in("in.txt");
    in >> dimensiune;
    zerorizeaza(dimensiune, matriceAdiacenta);
    for (int index = 1; index <= dimensiune; ++index)
    {
        int nod1, nod2;
        in >> nod1 >> nod2;
        matriceAdiacenta[nod1][nod2] = 1;
    }
}

void printBFS(int dimensiune, int nodStart, int viz[], int T[])
{
    for (int nodFinal = 1; nodFinal <= dimensiune; ++nodFinal)
    {
        cout << "" << nodFinal << "->   ";
        if (viz[nodFinal] && nodFinal != nodStart)
        {
            queue<int> path;
            path.push(nodFinal);
            while (path.back() != nodStart)
            {
                path.push(T[path.back()]);
            }

            stack<int> Stack;
            while (!path.empty())
            {
                Stack.push(path.front());
                path.pop();
            }
            while (!Stack.empty())
            {
                path.push(Stack.top());
                Stack.pop();
            }

            while (!path.empty())
            {
                cout << path.front() << " ";
                path.pop();
            }
        }
        else
        {
            cout << "Nu exista lant intre " << nodStart << " si " << nodFinal << endl;
        }
        cout << endl;
    }
}

void printArboreDescoperire(int dimensiune, int dist[])
{
    cout << "Arbore descoperire" << endl << "Nod" << " " << "Dist" << endl;
    for (int index = 1; index <= dimensiune; ++index)
    {
        cout <<" " << index << "   " << dist[index] << endl;
    }
}

void BFS(int dimensiune, int matrice[100][100])
{
    int nodStart;
    cout << "Nod start :";
    cin >> nodStart;

    if (nodStart > dimensiune)
        return;

    int viz[100] = { 0 }, T[100] = { -1 }, dist[100] = { -1 };
    queue<int> q;
    q.push(nodStart);
    viz[nodStart] = 1;
    dist[nodStart] = 0;//radacina

    while (!q.empty())
    {
        int mainNod = q.front();
        for (int index = 1; index <= dimensiune; ++index)
        {
            if (matrice[mainNod][index] && !viz[index]) // daca exista drum de la nod la index si nu a fost vizitat
            {
                q.push(index); // pui la final, dar inceputu ii sefu
                viz[index] = 1;
                dist[index] = dist[mainNod] + 1;
                T[index] = mainNod;
            }
        }
        q.pop();
    }

    printBFS(dimensiune, nodStart, viz, T);
    cout << endl;
    printArboreDescoperire(dimensiune, dist);
}

void inchidereaTranzitiva(int dimensiune, int matrice[100][100])
{
    for (int i = 1; i <= dimensiune; ++i)
        for (int j = 1; j <= dimensiune; ++j)
            for (int k = 1; k <= dimensiune; ++k)
                if (i != j && matrice[i][j] == 0 && matrice[i][k] && matrice[k][j])
                    matrice[i][j] = 1;

    for (int i = 1; i <= dimensiune; ++i)
    {
        for (int j = 1; j <= dimensiune; ++j)
            cout << matrice[i][j] << " ";
        cout << endl;
    }
}

void DFS(int dimensiune, int matrice[][100], int nodCurent, int v[])
{
    v[nodCurent] = 1;

    for (int index = 1; index <= dimensiune; index++)
    {
        if (matrice[nodCurent][index] == 1 && !v[index])//drum nodCurent-index , si nod index nevizitat
        {
            DFS(dimensiune, matrice, index, v);
        }
    }
}

int main()
{
    cout << "Labirint 1"<<endl;
    solutieLabirintLee(45,21, "labirint_1.txt");
    cout << "Labirint 2" << endl;
    solutieLabirintLee(102, 51, "labirint_2.txt");

    int dimensiune, matriceAdj[100][100];
    citireMatriceAdiacenta(dimensiune, matriceAdj);
    BFS(dimensiune, matriceAdj);
    
    int v[100] = { 0 }, nodStart;
    cout << "Nod start dfs>>>";
    cin >> nodStart;
    DFS(dimensiune, matriceAdj, nodStart, v);

    for (int index = 1; index <= dimensiune; index++)
    {
        if (v[index])
            cout << index << " ";
    }
    return 0;
}
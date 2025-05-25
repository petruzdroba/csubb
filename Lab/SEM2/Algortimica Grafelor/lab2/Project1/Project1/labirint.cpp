#include <iostream>
#include <fstream>
#include <queue>
#include <stack>
#include <string.h>
#include "labirint.h"

using namespace std;

void citireLabirint(int n, int m, char labirint[200][200], const char* file)
{
    ifstream in(file);
    for (int index = 0; index < m; ++index)
    {
        in.getline(labirint[index], n);
    }
}

pair<int, int> findStart(int n, int m, char labirint[200][200])
{
    for (int index = 0; index < m; ++index)
    {
        for (int jndex = 0; jndex < n; ++jndex)
        {
            if (labirint[index][jndex] == 'S')
                return make_pair(index, jndex);
        }
    }
    return make_pair(-1, -1);
}

void printPath(pair<int, int> v[200][200], pair<int, int> end, int n, int m)
{
    stack<pair<int, int>> path;
    pair<int, int> current = end;
    int labiririntSolutie[200][200];

    for (int index = 0; index < m; ++index)
    {
        for (int jndex = 0; jndex < n; ++jndex)
        {
            labiririntSolutie[index][jndex] = 0;
        }
    }

    while (current != make_pair(-1, -1))
    {
        path.push(current);
        current = v[current.first][current.second];
    }

    while (!path.empty())
    {
        // cout << "(" << path.top().first << ", " << path.top().second << ") ";
        labiririntSolutie[path.top().first][path.top().second] = 1;
        path.pop();
    }
    for (int index = 0; index < m; ++index)
    {
        for (int jndex = 0; jndex < n; ++jndex)
        {
            if (labiririntSolutie[index][jndex])
                cout << "*";
            else
                cout << " ";
        }
        cout << endl;
    }
}

void solutieLabirintLee(int n, int m, const char* file)
{
    char labirint[200][200];
    citireLabirint(n, m, labirint, file);

    int DEP_X[4] = { 0, 1, 0, -1 }, DEP_Y[4] = { -1, 0, 1, 0 };

    int viz[200][200] = { 0 };
    pair<int, int> v[200][200];

    for (int i = 0; i < m; ++i) // init
    {
        for (int j = 0; j < n; ++j)
        {
            v[i][j] = make_pair(-1, -1);
        }
    }

    pair<int, int> start = findStart(n, m, labirint);

    queue<pair<int, int>> q;
    q.push(start);
    viz[start.first][start.second] = 1;

    bool found = false;
    pair<int, int> finish = make_pair(-1, -1);

    while (!q.empty() && !found)
    {
        int crt_x = q.front().first, crt_y = q.front().second;
        q.pop();

        for (int index = 0; index < 4; ++index)
        {
            int new_x = crt_x + DEP_X[index];
            int new_y = crt_y + DEP_Y[index];

            if (0 <= new_x && new_x < m && 0 <= new_y && new_y < n && labirint[new_x][new_y] != '1' && viz[new_x][new_y] == 0)
            {
                viz[new_x][new_y] = viz[crt_x][crt_y] + 1;
                v[new_x][new_y] = make_pair(crt_x, crt_y);
                q.push(make_pair(new_x, new_y));

                if (labirint[new_x][new_y] == 'F')
                {
                    finish = make_pair(new_x, new_y);
                    found = true;
                    break;
                }
            }
        }
    }

    if (found)
    {
        printPath(v, finish, n, m);
    }
    else
    {
        cout << "Nu exista solutie" << endl;
    }
}

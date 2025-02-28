#include <iostream>
#include <fstream>

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

void afisareMatriceAdiacenta(int dimensiune, int matrice[100][100])
{
    cout << "Matrice de Adiacenta \n";
    cout << dimensiune << "\n";
    for (int index = 1; index <= dimensiune; ++index)
    {
        for (int jndex = 1; jndex <= dimensiune; ++jndex)
        {
            cout << matrice[index][jndex] << " ";
        }
        cout << "\n";
    }
}

void citireMatriceAdiacenta(int &dimensiune, int matrice[100][100])
{
    ifstream in("C:\\Users\\Petru\\Desktop\\Lab\\Algortimica Grafelor\\lab1\\in.txt");
    in >> dimensiune;
    zerorizeaza(dimensiune, matrice);
    for (int index = 1; index <= dimensiune; ++index)
    {
        int nod1, nod2;
        in >> nod1 >> nod2;
        matrice[nod1][nod2] = 1;
        matrice[nod2][nod1] = 1;
    }
    in.close();
    afisareMatriceAdiacenta(dimensiune, matrice);
}

void afisareListaAdiacenta(int dimensiune, int matrice[100][100])
{
    cout << "Lista de Incidenta \n";
    for (int index = 1; index <= dimensiune; ++index)
    {
        cout << index << ": ";
        for (int jndex = 1; jndex <= matrice[index][0]; ++jndex)
        {
            cout << matrice[index][jndex] << " ";
        }
        cout << '\n';
    }
}

void citireListaAdiacenta(int dimensiune, int matrice[100][100])
{
    ifstream in("C:\\Users\\Petru\\Desktop\\Lab\\Algortimica Grafelor\\lab1\\in.txt");
    in >> dimensiune;
    zerorizeaza(dimensiune, matrice);
    for (int index = 1; index < dimensiune; ++index)
    {
        int nod1, nod2;
        in >> nod1 >> nod2;

        matrice[nod1][0]++;
        matrice[nod1][matrice[nod1][0]] = nod2;

        matrice[nod2][0]++;
        matrice[nod2][matrice[nod2][0]] = nod1;
    }
    in.close();
    afisareListaAdiacenta(dimensiune, matrice);
}

void afisareMatriceIncidenta(int dimensiune, int nrMuchii, int matrice[100][100])
{
    cout << "Matrice de Incidenta\n";
    for (int index = 1; index <= dimensiune; ++index)
    {
        for (int jndex = 1; jndex <= nrMuchii; ++jndex)
        {
            cout << matrice[jndex][index] << " ";
        }
        cout << "\n";
    }
}

void citireMatriceIncidenta(int dimensiune, int matrice[100][100])
{
    ifstream in("C:\\Users\\Petru\\Desktop\\Lab\\Algortimica Grafelor\\lab1\\in.txt");
    in >> dimensiune;
    zerorizeaza(dimensiune, matrice);
    int count = 0;
    for (int index = 1; index <= dimensiune; ++index)
    {
        count++;
        int nod1, nod2;
        in >> nod1 >> nod2;

        matrice[count][nod1] = 1;
        matrice[count][nod2] = 1;
    }
    in.close();
    afisareMatriceIncidenta(dimensiune, count - 1, matrice);
}

void determinareNoduriIzolate(int dimensiune, int matrice[100][100])
{
    int count = 0;
    int noduriIzolate[5] = {0};

    for (int index = 1; index <= dimensiune; ++index)
    {
        bool eIzolat = true;
        for (int jndex = 1; jndex <= dimensiune; ++jndex)
        {
            if (matrice[index][jndex])
            {
                eIzolat = false;
            }
        }
        count++;
        noduriIzolate[count] = eIzolat;
    }

    cout << "Noduri izolate ";
    for (int index = 1; index <= count; ++index)
    {
        if (noduriIzolate[index])
        {
            cout << index;
        }
    }
    cout << "\n";
}

void determinareGrafRegulat(int dimensiune, int matrice[100][100])
{
    bool eRegulat = true;
    int gradReferinta = 0;

    for (int index = 1; index <= dimensiune; ++index)
    {
        if (matrice[1][index])
        {
            gradReferinta++;
        }
    }

    for (int index = 2; index <= dimensiune; ++index)
    {
        int gradCount = 0;
        for (int jndex = 1; jndex <= dimensiune; ++jndex)
        {
            if (matrice[index][jndex])
            {
                gradCount++;
            }
        }
        if (gradCount != gradReferinta)
        {
            eRegulat = false;
        }
    }

    cout << "Este regulat " << (eRegulat ? "Da" : "Nu");
}

void determinareMatriceDimensiuni(int dimensiune, int matrice[100][100])
{
}

int main()
{
    int dimensiune;

    int matrice_adiacenta[100][100];
    citireMatriceAdiacenta(dimensiune, matrice_adiacenta);
    // citireListaAdiacenta(dimensiune, matrice_adiacenta);
    // citireMatriceIncidenta(dimensiune, matrice_adiacenta);
    determinareNoduriIzolate(dimensiune, matrice_adiacenta);
    determinareGrafRegulat(dimensiune, matrice_adiacenta);
    return 0;
}
/*
0 1 0 1 0
1 0 0 0 1
0 0 0 0 1
1 0 0 0 0
0 1 1 0 0
*/

/*
1-2
1-2-5-3
1-4
1-2-5

2-1
2-5-3
2-1-4
2-5


*/

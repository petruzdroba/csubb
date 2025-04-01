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
    ifstream in("C:\\Users\\Petru\\Desktop\\UBB-IR\\Lab\\SEM2\\Algortimica Grafelor\\lab1\\in.txt");
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
    ifstream in("C:\\Users\\Petru\\Desktop\\UBB-IR\\Lab\\SEM2\\Algortimica Grafelor\\lab1\\in.txt");
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
    ifstream in("C:\\Users\\Petru\\Desktop\\UBB-IR\\Lab\\SEM2\\Algortimica Grafelor\\lab1\\in.txt");
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

    cout << "Este regulat " << (eRegulat ? "Da\n" : "Nu\n");
}

void afisareMatriceDrumurilor(int dimensiune, int matrice[100][100])
{
    cout << "Matrice de Drumurilor \n";
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
#define INF 1000;

void determinareMatriceDrumurilor(int dimensiune, int matrice[100][100])
{
    for (int i = 1; i <= dimensiune; ++i)
        for (int j = 1; j <= dimensiune; ++j)
            if (i != j && !matrice[i][j])
                matrice[i][j] = INF;

    for (int k = 1; k <= dimensiune; ++k)
        for (int i = 1; i <= dimensiune; ++i)
            for (int j = 1; j <= dimensiune; ++j)
                if (i != j && matrice[i][j] > matrice[i][k] + matrice[k][j])
                    matrice[i][j] = matrice[i][k] + matrice[k][j];

    afisareMatriceDrumurilor(dimensiune, matrice);
}

void determinareGrafConex(int dimensiune, int matrice[100][100])
{
    bool eConex = true;
    zerorizeaza(dimensiune, matrice);
    citireMatriceAdiacenta(dimensiune, matrice);

    for (int k = 1; k <= dimensiune; ++k)
        for (int i = 1; i <= dimensiune; ++i)
            for (int j = 1; j <= dimensiune; ++j)
                if (i != j && matrice[i][j] == 0 && matrice[i][k] == 1 && matrice[k][j] == 1)
                    matrice[i][j] = 1;

    for (int index = 1; index < dimensiune; ++index)
    {
        for (int jndex = index + 1; jndex <= dimensiune; ++jndex)
        {
            if (!matrice[index][jndex])
            {
                eConex = false;
                break;
            }
        }
    }

    cout << "E conex " << (eConex ? "Da\n" : "Nu\n");
}

int main()
{
    int dimensiune;

    int matrice_adiacenta[100][100];
    citireMatriceAdiacenta(dimensiune, matrice_adiacenta);
    afisareMatriceAdiacenta(dimensiune, matrice_adiacenta);
    // citireListaAdiacenta(dimensiune, matrice_adiacenta);
    // citireMatriceIncidenta(dimensiune, matrice_adiacenta);
    determinareNoduriIzolate(dimensiune, matrice_adiacenta);
    determinareGrafRegulat(dimensiune, matrice_adiacenta);
    determinareMatriceDrumurilor(dimensiune, matrice_adiacenta);
    determinareGrafConex(dimensiune, matrice_adiacenta);
    return 0;
}
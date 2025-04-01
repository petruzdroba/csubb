#include <stdio.h>
#include <stdbool.h>
#include <math.h>

float radicalBabilonian(float numar)
{
    /*
        Functia calculeaza o aproximatie a unui numar dat
        Input:
            -numar, float > 0
        Output:
            -rezultat, o aproximatie a radicalului, int > 0
    */
    float estimatie = numar;
    float rezultat;
    float precizie = 0.000001;

    while (1)
    {

        rezultat = (estimatie + (numar / estimatie)) / 2;

        if (fabs(estimatie - rezultat) < precizie)
        {
            break;
        }

        estimatie = rezultat;
    }

    return rezultat;
}

bool isPrime(int n)
{
    /*
    Functia verifica daca n este prim sau nu
    input:
        n -int, > 0
    output:
        true - bool, daca n este prim
        false - bool, daca n nu e prim
    */
    if (n <= 1)
        return false;
    if (n == 2)
        return true;
    if (n % 2 == 0)
        return false;
    for (int dindex = 3; dindex * dindex <= n; dindex += 2)
    {
        if (n % dindex == 0)
            return false;
    }
    return true;
}

void sumGoldbach(int n)
{
    /*
    Functia afiseaza pe ecran o perche de numere prime a caror suma este egala cu n
    input:
        n- int, par > 0
    output:
        void - o pereche de numere prime (impare), a caror suma este egala cu n
    */
    for (int index = 3; index < n; index += 2)
    {
        if (isPrime(index) && isPrime(n - index))
        {
            printf("%d %d\n", index, n - index);
            break;
        }
    }
}

void meniu()
{
    /*
        Functia meniu printeaza meniul
    */
    printf("1. Aproximatie radical\n");
    printf("2. Pare Goldbach\n");
    printf("Introduceti 0 pentru stop\n");
    int option;
    while (1)
    {
        printf(">>>");
        scanf("%d", &option);
        if (option == 1)
        {
            printf("Introduceti un numar>>>");
            float numar;
            scanf("%f", &numar);
            if (!numar || numar < 0)
            {
                printf("!Invalid input\n");
            }
            else
            {
                printf("%.6f\n", radicalBabilonian(numar));
            }
        }
        else if (option == 2)
        {
            printf("Introduceti un numar>>>");
            int numar;
            scanf("%d", &numar);
            if (numar % 2 || numar <= 2)
            {
                printf("!Invalid input\n");
            }
            else
            {
                sumGoldbach(numar);
            }
        }
        else if (!option)
        {
            break;
        }
        else
            printf("Comanda inexistenta");
    }
}

int main()
{
    meniu();
}
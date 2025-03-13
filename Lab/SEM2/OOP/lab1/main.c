#include <stdio.h>
#include <math.h>

int radicalBabilonian(float numar)
{
    /*
        Functia calculeaza o aproximatie a unui numar dat
        Input:
            -numar, float > 0
        Output:
            -rezultat, o aproximatie a radicalului, int > 0
    */
    if (numar < 0)
    {
        return 0;
    }

    float estimatie = numar;
    float rezultat;
    float precizie = 0.000001;
    int iteratiiMax = 100;
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

void meniu()
{
    /*
        Functia meniu printeaza meniul
    */
    printf("Introduceti 0 pentru stop");
    while (1)
    {
        float numar;
        printf("\nIntroduceti numar :");
        scanf("%f", &numar);
        if (!numar)
        {
            break;
        }
        else
        {
            printf("%.6f", radicalBabilonian(numar));
        }
    }
}

int main()
{
    meniu();
}
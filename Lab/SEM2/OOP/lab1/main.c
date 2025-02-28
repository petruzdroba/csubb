#include <stdio.h>
#include <math.h>

int main()
{
    float numar;
    printf("Introduceti numar :");
    scanf("%f", &numar);

    float estimatie = numar;
    float rezultat;
    int iteratiiMax = 100;
    while (1)
    {

        rezultat = (estimatie + (numar / estimatie)) / 2;

        if (fabs(estimatie - rezultat) < 0.000001)
        {
            break;
        }

        estimatie = rezultat;
    }

    printf("%.6f", rezultat);
}

// printf("Aproximatie: ");
// unsigned int aprox;
// scanf("%d", &aprox);
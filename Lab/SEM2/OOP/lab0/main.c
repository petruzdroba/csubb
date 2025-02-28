#include <stdio.h>

int main()
{
    int n;
    int suma = 0;
    scanf("%d", &n);

    for (int index = 1; index <= n; ++index)
    {
        int number;
        scanf("%d", &number);
        suma += number;
    }

    printf("Suma este: %d", suma);
}
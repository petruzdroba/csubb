/*Se citeste de la tastatura un sir de numere in baza 10, cu semn.
 Sa se determine valoarea minima din sir si sa se afiseze in fisierul min.txt (fisierul va fi creat)
 valoarea minima, in baza 16.
 */
#include <stdio.h>

extern void asmCompara(int *min, int n);

int main() {
    FILE *fout = fopen("min.txt", "w");
   
    int n, min, first = 1;

    printf("Introduceti numere (0 pentru a opri):\n");

    while (1) {
        scanf("%d", &n);
        if (n == 0)
            break;

        if (first) {
            min = n;
            first = 0;
        } else {
            asmCompara(&min, n); 
        }
    }

    fprintf(fout, "0x%x", min);
 

    fclose(fout);
    return 0;
}

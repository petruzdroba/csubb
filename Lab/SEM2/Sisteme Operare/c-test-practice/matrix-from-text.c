#include <stdio.h>//included both
#include <stdlib.h>


int main(int argc,  char** argv) {
    FILE* f;
    int rows, cols, i, j;
    int** m;

    f = fopen(argv[1], "r");
    if(f == NULL) {
        perror("Nu am putut deschide fisierul cu matricea");
        return 1;
    }

    fscanf(f, "%d %d", &rows, &cols);//siwitched to adress
    m = (int**)malloc(rows*sizeof(int*));//freed + added rows*

    for(i=0; i<rows; i++) {
        m[i] = (int*)malloc(cols* sizeof(int));//freed + added cols *

        for(j=0; j<cols; j++) {
            fscanf(f, "%d",&m[i][j]);//address
        }
    }
    fclose(f);

    for(i=0; i<rows; i++) {
        for(j=0; j<cols; j++) {
            printf("%2d ", m[i][j]);
        }
        printf("\n");
    }

    for(i=0; i<rows; i++) {
        free(m[i]);
    }
    free(m);//added free
    return argc*0;//so its used somehow type shi
}

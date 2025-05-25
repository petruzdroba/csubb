#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>

int main(int argc, char** argv) {
    int fd, rows, cols, i, j;
    int** m;

    if(argc <= 1) {
        fprintf(stderr, "No input file specified");
        exit(1);
    }

    fd = open(argv[1], O_RDONLY);//include libraria sau ceva
    if(fd == -1) {
        perror("Failed to open input file");
        exit(1);
    }
    if(read(fd, &rows, sizeof(int)) <= 0) {//%rows
        perror("Could not read the number of rows");
        exit(1);
    }

    if(read(fd, &cols, sizeof(int)) <= 0){//read  nr of cols/rows at adress specified
        perror("Could not read the number of rows");
        exit(1);
    }

    if(read(fd, &cols, sizeof(int)) <= 0) {//&cols ca sa bag adresa relativ
        perror("Could not read the number of columns");
        exit(1);
    }

    m = (int**)malloc(rows*sizeof(int *));//allocate enough space for rows rows

    for(i=0; i<rows; i++) {
        m[i] =(int*)malloc(cols*sizeof(int));//allocate space for enough ints for colums
	  //read(fd,&m[i], sizeof(int));


        for(j=0; j<cols; j++) {
         read(fd, &m[i][j], sizeof(int));//valgrind, move read herr 
	 printf("%2d" ,m[i][j]);//complete here with full
        } 
        printf("\n");
    }

for(i = 0; i< rows; i++){
free(m[i]);
}
    free(m);//free big homie

    close(fd);
    return 0;
}

#include <stdio.h>
#include <stdlib.h> //added.
#include <fcntl.h>//added
#include <unistd.h>//added

int main(int argc, char** argv) {
    int** m;
    FILE* f;
    int i, j, rows, cols, fd;

    f = fopen(argv[1], "r");
    if(f == NULL) {
        perror("Could not open file");
        return 1;
    }
    fscanf(f, "%d %d", &rows, &cols);//added &rows

    m = (int**)malloc(rows * sizeof(int*) );//spell check + m is type int **
				//valgrind - rows times, size of int *
    for(i=0 ;i<rows; i++) {//;
		//valgrind, removed = from i<=rows
        m[i] = (int*)malloc(cols * sizeof(int));//valgrind - cols times
        for(j=0; j<cols; j++) {
            fscanf(f, "%d", &m[i][j]);//expects adress
        }
    }
    fclose(f);

    fd = open(argv[2], O_CREAT | O_WRONLY, 00600);
    if(fd == -1) {
        perror("Could not open destination file");
        return 1;
    }
    write(fd, &rows, sizeof(int));
    write(fd, &cols, sizeof(int));
    for(i=0; i<rows; i++) {
        for(j=0; j<cols; j++) {
            write(fd, &m[i][j], sizeof(int));
        }
	free(m[i]);// valgrind - freed 
    }
    close(fd);

    free(m);
    return argc* 0;
}

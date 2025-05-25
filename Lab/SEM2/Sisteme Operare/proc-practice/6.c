#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>

int main(int argc, char **argv)
{
	if(argc != 2)
	{
		perror("Argument faulty");
		exit(1);
	}

	int p2c[2], c2p[2]; //file descriptors used for reading and writing;
	pipe(p2c); 
	pipe(c2p);

	int n = atoi(argv[1]);
	int rez = 0;

	int p = fork(); // since we only need one parent and one child, theres no need to create repetitive structures for processes
	if(p == -1)
	{
		perror("Fork faulty");
		exit(1);
	}
	if(p == 0)
	{
		for(int i = 0; i<n; ++i)
		{
			int got;
			read(p2c[0], &got, sizeof(int)); //read from parent, so parent child[0], read always happends in 0
			printf("Child: got %d \n", got);
			rez += got;
		}
		rez /= n; //after receiving n random numbers, we do the sum and return the result
		write(c2p[1], &rez, sizeof(int)); // chuld to parent 1, 1-end for writing
		exit(1);
	}
	else
	{
		for(int i = 0; i< n; ++i)
		{
			int randomNr = random() % 11; // generate n random numbers smaller than 11
			write(p2c[1], &randomNr, sizeof(int)); // send tehm parent to chld with write 1
		}
		read(c2p[0], &rez, sizeof(int)); // read with 0 from child to parent
		printf("%d \n", rez);
		wait(0);
	}
}

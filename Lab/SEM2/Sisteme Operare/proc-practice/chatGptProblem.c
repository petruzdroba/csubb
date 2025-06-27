#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <time.h>

int main(int argc, char** argv)
{
	if(argc != 2)
	{
		perror("argument faulty");
		exit(1);
	}

	srand(time(NULL));
	int n = atoi(argv[1]);

	for(int i = 0; i < n; i++)
	{
		int p2c[2] = {0}, c2p[2] = {0};
		pipe(p2c);
		pipe(c2p);

		int p = fork();

		if(p == -1)
		{
			perror("fork faulty");
			exit(1);
		}
		if( p == 0)
		{
			int got;
			read(p2c[0], &got, sizeof(int));

			got *=got;
			got += 10;
			write(c2p[1], &got, sizeof(int));
			close(p2c[0]);
			close(c2p[1]);
			exit(0);
		}
		else
		{
			int randomNumber = random() % 100 + 1;
			write(p2c[1], &randomNumber, sizeof(int));

			read(c2p[0], &randomNumber, sizeof(int));
			wait(0);
			printf("Parent received %d from child with pid %d \n", randomNumber, p);
			close(p2c[1]);
			close(c2p[0]);
		}
	}
}

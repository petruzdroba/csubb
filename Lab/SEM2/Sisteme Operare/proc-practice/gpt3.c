#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <time.h>

int isPrime(int n)
{
	if(n == 1)
		return 0;
	if(n == 2)
		return 1;
	if(n % 2== 0)
		return 0;
	for(int d = 3; d<=n/2; d += 2)
	{
		if(n %d == 0)
			return 0;
	}
	return 1;
}

int main(int argc, char **argv)
{
	if( argc != 2)
	{
		perror("argument faulty");
		exit(0);
	}

	int p2c[2];
	pipe(p2c);

	int n = atoi(argv[1]);
	srand(time(NULL));
	int p = fork();

	if( p == -1 )
	{
		perror("pipe faulty");
		exit(0);
	}
	if( p == 0)
	{
		for(int i = 0; i< n; i++)
		{
			int got;
			read(p2c[0], &got, sizeof(int));
			int prime = isPrime(got);
			printf("Received %d ->", got);

			if(prime == 0)
				printf("not prime \n");
			else
				printf("prime \n");
		}
		close(p2c[0]);
		exit(0);
	}
	else
	{
		for(int i = 0; i<n; i++)
		{
			int number = random() % 100 +1;
			write(p2c[1], &number, sizeof(int));
		}
		wait(0);
		close(p2c[1]);
	}
}

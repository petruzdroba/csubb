#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <unistd.h>

int main(int argc, char **argv)
{
	if(argc != 2)
	{
		perror("argument faulty");
		exit(0);
	}

	int n = atoi(argv[1]);

	for(int i = 0; i<n;++i)
	{
		int p = fork();

		if( p == 0)
		{
			printf("Child with pid %d will have %d children \n",getpid(), n - i - 1);

			for(int j = 0; j< n - i -1; j++)
			{
				fork();
			}
		
		}
		else
		{
			printf("Parent: %d \n", getpid());
			wait(NULL);
		}
	}
}

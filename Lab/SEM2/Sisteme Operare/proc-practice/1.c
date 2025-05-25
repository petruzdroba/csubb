#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>

int main(int argc, char **argv)
{
	if(argc != 2)
	{
		perror("One argument please");
		exit(1);
	}

	int n = atoi(argv[1]); //char to int

	for(int index = 0; index < n; index++)
	{
		int p = fork();
		if(p == -1)
		{
			perror("Fork failed");
			exit(1);
		}

		if(p == 0) //child gets 0 in fork() return
		{
			printf("Child: pid:%d parent_pid:%d \n", getpid(), getppid());
			exit(0);
		}
		else // parent receive the child pids in fork() return
		{
			printf("Parent: pid:%d child_pid:%d \n", getpid(), p);
			wait(NULL); //waits for child to end before creating another, preventing zombie procceses
		}
	}

	return 0;
}

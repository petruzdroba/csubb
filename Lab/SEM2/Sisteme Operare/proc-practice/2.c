#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>

int main(int argc, char**argv)
{
	if(argc != 2)
	{
		perror("Argument faulty");
		exit(1);
	}

	int n = atoi(argv[1]);

	for(int i = 0; i < n; i++)
	{
		int p = fork();
		
		if(p == -1)
		{
			perror("Fork failed");
			exit(1);
		}
		if(p == 0)
		{
			printf("Child: pid %d parent_pif %d \n", getpid(), getppid()); //child finishes and then becomes parent
			// exmaple
			// first for we have parent pid 0 and child pid 1
			// proc pid 0 waits for pid 1 to end and then exits
			//pid 1 goes in the if for child, and then resumes for, becoming itself a parent etc
		}
		else
		{
			printf("Parent: pif %d child_pid %d \n", getpid(), p);
			wait(0); // waits for child to end
			exit(0); //then exits, so to not create another child with for
		}
	}

	return 0;
}

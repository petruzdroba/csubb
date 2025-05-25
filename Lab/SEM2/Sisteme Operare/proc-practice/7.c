#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <unistd.h>

int main()
{
	int x2y[2]={0}, y2x[2]={0};
	pipe(x2y);
	pipe(y2x);

	int p = fork();
	int n;

	if(p == -1)
	{
		perror("Fork faulty");
		exit(1);
	}

	if(p == 0)
	{
		read(y2x[0], &n, sizeof(int));
		printf("X read %d \n", n);
		while(n != 10)
		{
			n = random() %10 +1;
			if(n != 10)
			{
				write(x2y[1], &n, sizeof(int));
			}
			read(y2x[0], &n, sizeof(int));
			printf("X read %d \n", n);
		}
		close(y2x[0]);
		close(x2y[1]);
		exit(0);
	}

	p = fork();
	if(p == -1)
	{
		perror("Fork faulty");
		exit(0);
	}

	if(p == 0)
	{
		n = 0;
		while(n != 10)
		{
			n = random() % 10 +1;
			write(y2x[1], &n, sizeof(int));
			if(n == 10)
			{
				break;
			}
			read(x2y[0], &n, sizeof(int));
			printf("Y read %d \n", n);
		}
		close(x2y[0]);
		close(y2x[0]);
		exit(0);
	}
	wait(0);
	wait(0);
	return 0;
}

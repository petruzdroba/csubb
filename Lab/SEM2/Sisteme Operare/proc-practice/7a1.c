#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/wait.h>
#include <time.h>

int main()
{
	//acest proces este responsabil pentru deschiderea fifo
	//acest proces este responsabil pt inchiderea fifo
	const char* fifo1 = "fifo1";
	const char* fifo2 = "fifo2";


	if(mkfifo(fifo1, 0666) < 0)
	{
                perror("fifo1 faulty");
                exit(0);
        }
	if(mkfifo(fifo2, 0666) < 0)
        {
                perror("fifo2 faulty");
                exit(0);
        }

	int fd1 = open(fifo1, O_WRONLY);
	if(fd1 == -1)
	{
		perror("descriptor1 faulty");
		exit(0);
	}

	int fd2 = open(fifo2, O_RDONLY);
        if(fd1 == -1)
        {
                perror("descriptor1 faulty");
                exit(0);
        }

	//this process will start writing
	srand(time(NULL));
	int n = 0;
	while(n != 10)
	{
		write(fd1, &n, sizeof(int));
		if(n != 10)
		{
			read(fd2, &n, sizeof(int));
			printf("Process 1: %d \n", n);
		}
		n = random()%10+1;
	}

	wait(NULL);
	unlink(fifo1);
	unlink(fifo2);
}

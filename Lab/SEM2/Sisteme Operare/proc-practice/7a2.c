#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/wait.h>
#include <time.h>

int main()
{
	const char* fifo1 = "fifo1";
        const char* fifo2 = "fifo2";

        int fd1 = open(fifo1, O_RDONLY);
        if(fd1 == -1)
        {
                perror("descriptor1 faulty");
                exit(0);
        }

        int fd2 = open(fifo2, O_WRONLY);
        if(fd1 == -1)
        {
                perror("descriptor1 faulty");
                exit(0);
        }

	// this process will start receiving
	srand(time(NULL));
	int n = 0;
	while(n!=10)
	{
		read(fd1, &n, sizeof(int));
		printf("Process 2:%d \n", n);
		if(n == 10)
		{
			break;
		}
		else
		{
			n = random()%10+1;
			write(fd2, &n, sizeof(int));
		}
	}

	exit(0);
}

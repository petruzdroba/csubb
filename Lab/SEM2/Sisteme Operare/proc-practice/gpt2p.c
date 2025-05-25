#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/stat.h>
#include <sys/wait.h>

int main(int argc, char** argv)
{
	const char *fifoName = "myFifo";// fifo name, which will be refrenced in the other file
	if(mkfifo(fifoName, 0666) == -1)	// here we create fifo and give it all permissions as a file
	{
		perror("fifo faulty");
		exit(0);
	}
	int fd = open(fifoName, O_WRONLY);	// open fifo with file descripto r giving it Write Only
	if(fd == -1)
	{
		perror("file descriptor failed");
		exit(0);
	}

	if(argc != 2)
	{
		perror("argument faulty");
		exit(0);
	}

	int number = atoi(argv[1]);

	write(fd, &number, sizeof(int)); // send data to the other end of the fifo
	wait(NULL);		// wait for the other process to end before unlinking the fifo, essesntially ending it
	unlink(fifoName);
}


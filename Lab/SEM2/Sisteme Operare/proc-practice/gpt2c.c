#include <stdlib.h>
#include<stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/stat.h>

int main()
{
	const char* fifoName = "myFifo";

	int fd = open(fifoName, O_RDONLY); // open fifo with file descriptor giving it Read Only
	if(fd == -1)
	{
		perror("descriptor faulty");
		exit(0);
	}

	int receivedNumber;
	read(fd, &receivedNumber, sizeof(int)); // read number  in receivedNumber with file descriptor
	receivedNumber *= receivedNumber;
	printf("Squared = %d \n" , receivedNumber);
	exit(0); // not sure if needded, but the other  proc waits, so might as well
}

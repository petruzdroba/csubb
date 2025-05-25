#include <stdio.h>
#include<stdlib.h>
#include <pthread.h>

int frecv[10] = {0};
pthread_mutex_t mutex[10]; // mutex

void* f(void* a)
{
	long nr = (long) a; // cast k to 64 bit
	while(nr)
	{
		int cif = nr%10;
		pthread_mutex_lock(&mutex[cif]); // cant be accesed until unlock 
		frecv[cif] += 1;
		pthread_mutex_unlock(&mutex[cif]);
		nr /= 10;
	}
	return 0;
}

int main(int argc, char** argv)
{
	int n = argc;
	pthread_t t[n -1]; // vector of n threads

	for(int i = 0; i<10; ++i)
	{
		pthread_mutex_init(&mutex[i], NULL);//initialize mutex
	}

	for(int i = 1; i<n; ++i)
	{
		long nr = atoi(argv[i]);
		pthread_create(&t[i-1], NULL, f,(void *)nr);// create thread, thread, create argument default, function, function arguments
	}

	for(int i= 1 ;i<n; ++i)
	{
		pthread_join(t[i-1], NULL);//waits for thread with i-1 to end
	}

	for(int i = 0; i<10;++i)
	{
		printf("cif:%d ap:%d\n", i, frecv[i]);
	}
}


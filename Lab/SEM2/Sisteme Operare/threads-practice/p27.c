#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <time.h>

char sir[129] = {0};
pthread_mutex_t mut;
pthread_cond_t cond;
int j = 0;

void* f(void* a)
{
	while(1)
	{
		char c = rand()% ('z' - 'a' + 1) + 'a';
		pthread_mutex_lock(&mut);
		if(j >= 128)
		{
			pthread_mutex_unlock(&mut);
			pthread_cond_signal(&cond);
			break;
		}
		sir[j++] = c;
		pthread_mutex_unlock(&mut);
	}
	return 0;
}

void* g(void* a)
{
	pthread_mutex_lock(&mut);
	pthread_cond_wait(&cond, &mut);
	printf("%s\n", sir);
	pthread_mutex_unlock(&mut);
	return 0;
}

int main(int argc, char** argv)
{
	if(argc<=2){
		perror("Too few arguments\n");
		exit(1);
	}
	srand(time(NULL));

	int n = atoi(argv[1]), m = atoi(argv[2]);

	pthread_t t[n], p;
	pthread_mutex_init(&mut, NULL);
	pthread_cond_init(&cond, NULL);

	 for(int i = 0 ; i<n;++i)
	{
		pthread_create(&t[i], NULL, f, NULL);
	}

	pthread_create(&p, NULL, g, NULL);

	for(int i = 0; i<n; ++i)
	{
		pthread_join(t[i], NULL);
	}

	pthread_join(p, NULL);
}

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>


pthread_barrier_t b;
pthread_t vector_elemente[128];

void* f(void* a){
	long int id = (long int) a;
	pthread_barrier_wait(&b);
	printf("%ld\n",id);
	int st=2*id;
	int dr=2*id+1;
	printf("Id %ld are stanga:%d,dreapta:%d\n",id,st,dr);

	return NULL;
}

int main(int argc, char** argv) {
	int n = atoi(argv[1]);
	argc++;
	// printf ("%d\n", n);
	int p = 1;
	while (p < n)
		p *= 2;
	printf("%d\n", p);

	int v[p];
	for (int i = 0; i < p; ++i) {
		if (i < n)
			scanf("%d", &v[i]);
		else
			v[i] = 0;
	}
	int h = p-1;
	pthread_barrier_init(&b,NULL,h);
	//pthread_t vector_elemente[h];
	for (long int i=0; i<h; ++i){
		pthread_create(&vector_elemente[i], NULL, f, (void*) (i+1));
		
	}
	getchar();
	getchar();
	getchar();
	getchar();
	
	
}

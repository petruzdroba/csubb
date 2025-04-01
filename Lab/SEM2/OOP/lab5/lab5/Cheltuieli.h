
#ifndef CHELTUIELI_H
#define CHELTUIELI_H
#define MAX_CHELTUIELI 100

typedef struct {
    int zi;
    float suma;
    char tip[50];
} Cheltuiala;

Cheltuiala* creeazaCheltuiala(int zi, float suma, const char *tip); 

Cheltuiala* copyCheltuiala(Cheltuiala* elemente);

void DistrugeCheltuiala(Cheltuiala *c);

#endif //CHELTUIELI_H

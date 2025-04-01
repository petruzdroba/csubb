#define _CRT_SECURE_NO_WARNINGS
#include "Cheltuieli.h"
#include <string.h>
#include <stdlib.h>

Cheltuiala* creeazaCheltuiala(int zi,float suma,const char* tip) {
    Cheltuiala* c = (Cheltuiala *)malloc(sizeof(Cheltuiala));
    c->zi = zi;
    c->suma = suma;
    strcpy(c->tip,tip);
    return c;
}

Cheltuiala* copyCheltuiala(Cheltuiala * elemente)
{
    return creeazaCheltuiala(elemente->zi, elemente->suma, elemente->tip);
}

void DistrugeCheltuiala(Cheltuiala* c) {
    //free(c->tip);
    free(c);
}

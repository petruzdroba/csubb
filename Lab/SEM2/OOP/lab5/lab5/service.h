#ifndef SERVICE_H
#define SERVICE_H
#include "Cheltuieli.h"
#include "MyList.h"

int add_cheltuiala(MyList *lista, int zi, float suma, const char *tip);
int modifica_cheltuiala(MyList *lista, int index, int zi, float suma, const char *tip);
int sterge_cheltuiala(MyList *lista, int index);

MyList* vizualizare(MyList *lista, const char *criteriu, const char *valoare);
void ordonare(MyList *lista,const char *criteriu,int(*compara)(const void *a, const void *b));
void ordonare_desc(MyList *lista,const char *criteriu,int(*compara)(const void *a, const void *b));
MyList* get_all_(MyList *lista);
int compara_suma(const void *a, const void *b);
int compara_tip(const void *a, const void *b);
int compara_suma_desc(const void *a, const void *b);
int compara_tip_desc(const void *a, const void *b);

MyList* filtrareMaiMare(MyList* lista, float suma);
MyList* filtrareMaiMic(MyList* lista, float suma);

#endif
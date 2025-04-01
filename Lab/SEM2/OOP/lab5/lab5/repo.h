#ifndef REPO_H
#define REPO_H
#include "cheltuieli.h"
#include "MyList.h"

int add(MyList *lista, int zi, float suma, const char *tip);
int modificare(MyList *lista, int index, int zi, float suma, const char *tip);
int stergere(MyList *lista, int index);
MyList* get_all(MyList *lista);

void distruge_lista(MyList *lista);
void redimensioneaza_lista(MyList *lista);

#endif //REPO_H

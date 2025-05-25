#include<stdio.h>
#include<string.h>
#include<assert.h>
#include <stdlib.h>
#include "Cheltuieli.h"
#include "service.h"
#include "repo.h"
#include "MyList.h"


void test_add() {
    MyList* lista = createEmpty();


    int rezultat = add(lista, 15, 200, "mancare");
    assert(rezultat == 0);
    assert(lista->lungime == 1);
    assert(((Cheltuiala *)lista->elemente[0])->zi == 15);
    assert(((Cheltuiala*)lista->elemente[0])->suma== 200);
    assert(strcmp(((Cheltuiala*)lista->elemente[0])->tip, "mancare") == 0);

    distruge_lista(lista);
    free(lista);
}

void test_modificare() {
  MyList* lista = createEmpty();
  add(lista, 10, 150, "transport");

  int rezultat = modificare(lista, 0, 12, 170, "telefon&internet");
  assert(rezultat == 0);
  assert(((Cheltuiala*)lista->elemente[0])->zi == 12);
  assert(((Cheltuiala*)lista->elemente[0])->suma== 170);
  assert(strcmp(((Cheltuiala*)lista->elemente[0])->tip, "telefon&internet") == 0);

  rezultat = modificare(lista, 10, 15, 180, "imbracaminte");
  assert(rezultat == -2);

  distruge_lista(lista);
  free(lista);
}


void test_stergere() {
   MyList* lista = createEmpty();

  add(lista, 25, 200, "mancare");

  int rezultat = stergere(lista, 0);
  assert(rezultat == 0);
  assert(lista->lungime == 0);

  add(lista, 25, 200, "mancare");
  add(lista, 26, 200, "mancare");
  rezultat = stergere(lista, 0);
  assert(rezultat == 0);
  assert(lista->lungime == 1);

  rezultat = stergere(lista, 0);
  assert(rezultat == 0);
  assert(lista->lungime == 0);



  rezultat = stergere(lista, 0);
  assert(rezultat == -2);

  rezultat = stergere(lista, 10);
  assert(rezultat == -2);
  
  distruge_lista(lista);
  free(lista);
}

void test_vizualizare() {
  MyList* lista = createEmpty();

  add(lista, 15, 100, "mancare");
  add(lista, 20, 200, "transport");
  add(lista, 15, 150, "telefon&internet");

  // Test pentru zi
  MyList *lista_filtrata = vizualizare(lista, "zi", "15");
  assert(lista_filtrata != NULL);
  assert(lista_filtrata->lungime == 2);
  free(lista_filtrata);

  // Test pentru suma
  lista_filtrata = vizualizare(lista, "suma", "100");
  assert(lista_filtrata != NULL);
  assert(lista_filtrata->lungime == 1);
  free(lista_filtrata);

  // Test pentru tip
  lista_filtrata = vizualizare(lista, "tip", "telefon&internet");
  assert(lista_filtrata != NULL);
  assert(lista_filtrata->lungime == 1);
  free(lista_filtrata);

  // Test pentru suma inexistenta
  lista_filtrata = vizualizare(lista, "suma", "999.9");
  assert(lista_filtrata == NULL);

  // Test pentru tip inexistent
  lista_filtrata = vizualizare(lista, "tip", "altele");
  assert(lista_filtrata == NULL);

  distruge_lista(lista);
  free(lista);

}

void test_ordonare(){
  MyList* lista = createEmpty();

  add(lista, 15, 100, "mancare");
  add(lista, 20, 50, "transport");
  add(lista, 15, 150, "telefon&internet");
  char* criteriu = "suma";
  ordonare(lista,criteriu,compara_suma);

  assert(((Cheltuiala *)lista->elemente[0])->suma == 50);
  assert(((Cheltuiala *)lista->elemente[1])->suma == 100);
  assert(((Cheltuiala* )lista->elemente[2])->suma == 150);

  criteriu = "tip";
  ordonare(lista,criteriu,compara_tip);

  assert(strcmp(((Cheltuiala*)lista->elemente[0])->tip ,"mancare")==0);
  assert(strcmp(((Cheltuiala*)lista->elemente[1])->tip,"telefon&internet" )==0);
  assert(strcmp(((Cheltuiala*)lista->elemente[2])->tip,"transport")==0);

  distruge_lista(lista);
  free(lista);
}


void test_ordonare_desc(){
   MyList* lista = createEmpty();

  add(lista, 15, 100, "mancare");
  add(lista, 20, 50, "transport");
  add(lista, 15, 150, "telefon&internet");
  char* criteriu = "suma";
  ordonare_desc(lista,criteriu,compara_suma_desc);

  assert(((Cheltuiala*)lista->elemente[0])->suma == 150);
  assert(((Cheltuiala*)lista->elemente[1])->suma == 100);
  assert(((Cheltuiala*)lista->elemente[2])->suma == 50);

  criteriu = "tip";
  ordonare_desc(lista,criteriu,compara_tip_desc);

  assert(strcmp(((Cheltuiala*)lista->elemente[0])->tip,"transport")==0);
  assert(strcmp(((Cheltuiala*)lista->elemente[1])->tip, "telefon&internet" )==0);
  assert(strcmp(((Cheltuiala*)lista->elemente[2])->tip,"mancare")==0);

  distruge_lista(lista);
  free(lista);
}

void test_add_service() {
    MyList* lista = createEmpty();

  int rezultat = add_cheltuiala(lista, 15, 200, "mancare");
  assert(rezultat == 0);
  assert(lista->lungime == 1);
  assert(((Cheltuiala *)lista->elemente[0])->zi == 15);
  assert(((Cheltuiala*)lista->elemente[0])->suma == 200);
  assert(strcmp(((Cheltuiala*)lista->elemente[0])->tip, "mancare") == 0);

  rezultat = add_cheltuiala(lista, 34, 100, "altele");
  assert(rezultat == 3);

  rezultat = add_cheltuiala(lista, 4, -4, "altele");
  assert(rezultat == 4);

  rezultat = add_cheltuiala(lista, 2, 100, "apa");
  assert(rezultat == 5);

  for (int i = 0; i < MAX_CHELTUIELI; i++) {
    add(lista, 1, 100, "altele");
  }
  rezultat = add_cheltuiala(lista, 1, 100, "altele");
  assert(rezultat == -1);

  distruge_lista(lista);
  free(lista);
}

void test_modificare_service() {
  MyList* lista = createEmpty();

  add(lista, 10, 150, "transport");

  int rezultat = modifica_cheltuiala(lista, 0, 12, 170, "telefon&internet");
  assert(rezultat == 0);
  assert(((Cheltuiala*)lista->elemente[0])->zi == 12);
  assert(((Cheltuiala*)lista->elemente[0])->suma == 170);
  assert(strcmp(((Cheltuiala*)lista->elemente[0])->tip, "telefon&internet") == 0);

  rezultat = modifica_cheltuiala(lista, 0, 36, 170, "telefon&internet");
  assert(rezultat == 3);

  rezultat = modifica_cheltuiala(lista, 0, 12, -170, "telefon&internet");
  assert(rezultat == 4);

  rezultat = modifica_cheltuiala(lista, 0, 12, 170, "telefon");
  assert(rezultat == 5);

  rezultat = modifica_cheltuiala(lista, 10, 15, 180, "imbracaminte");
  assert(rezultat == -2);

  distruge_lista(lista);
  free(lista);
}

void test_stergere_service() {
  MyList* lista = createEmpty();
  add(lista, 25, 200, "mancare");

  int rezultat = sterge_cheltuiala(lista, 0);
  assert(rezultat == 0);
  assert(lista->lungime == 0);

  rezultat = sterge_cheltuiala(lista, 10);
  assert(rezultat == -2);
  
  distruge_lista(lista);
  free(lista);
}
void test_get_all() {
  MyList* lista = createEmpty();
  add(lista, 1, 100, "Mancare");
  add(lista, 2, 200, "Transport");

  MyList *copia = get_all(lista);
  assert(copia != NULL);
  assert(copia->lungime == lista->lungime);
  assert(copia->capacitate == lista->capacitate);
  assert(copia->elemente != NULL);

  assert(((Cheltuiala*)copia->elemente[0])->zi == 1);
  assert(((Cheltuiala*)copia->elemente[1])->suma == 200);

  distruge_lista(copia);
  free(copia);

  free(lista->elemente);//wtf ?????, if it aint broke don t fix it
  free(lista);
}


void test_get_all_service() {
  MyList* lista = createEmpty();
  add(lista, 1, 100, "Mancare");
  add(lista, 2, 200, "Transport");

  MyList *copia = get_all_(lista);
  assert(copia != NULL);
  assert(copia->lungime == lista->lungime);
  assert(copia->capacitate == lista->capacitate);
  assert(copia->elemente != NULL);
  assert(((Cheltuiala*)copia->elemente[0])->zi == 1);
  assert(((Cheltuiala*)copia->elemente[1])->suma == 200);

  distruge_lista(copia);
  free(copia);

  free(lista->elemente);//wtf ?????, if it aint broke don t fix it
  free(lista);
}

void test_distruge_lista() {
  MyList* lista = createEmpty();
  add(lista, 1, 100, "Mancare");
  add(lista, 2, 200, "Transport");
  add(lista, 3, 100, "Mancare");
  add(lista, 4, 200, "Transport");
  assert(lista->lungime == 4);
  distruge_lista(lista);

  assert(lista->lungime == 0);
  free(lista);
}

void test_redimensioneaza_lista() {
  MyList* lista = createEmpty();
  lista->lungime = 5;
  redimensioneaza_lista(lista);

  assert(lista->capacitate == 10);
  assert(lista->lungime == 5);

  free(lista->elemente);
  free(lista);
}

void test_creeaza_cheltuiala() {

  Cheltuiala* c = creeazaCheltuiala(12,123,"mancare");
  assert(c->zi == 12);
  assert(c->suma == 123);
  assert(strcmp(c->tip, "mancare") == 0);
  DistrugeCheltuiala(c);//added
}

void testCopyCheltuiala()
{
    Cheltuiala* c = creeazaCheltuiala(12, 123, "mancare");
    Cheltuiala* c1 = copyCheltuiala(c);
    assert(c1->zi == 12);
    assert(c1->suma == 123);
    assert(strcmp(c1->tip, "mancare") == 0);
    DistrugeCheltuiala(c);
    DistrugeCheltuiala(c1);
}

void testCreazaEmptyUndoList()
{
    MyList* undoList = createEmpty();
    assert(undoList->capacitate == 5);
    assert(undoList->lungime == 0);

    destroyUndoList(undoList);
    assert(undoList->capacitate == 0);
    assert(undoList->lungime == 0);

    free(undoList);
}

void testDeepCopyMyList()
{
    MyList* lista1 = createEmpty();
    assert(lista1->lungime == 0);

    MyList* lista2 = deepCopyMyList(lista1);
    add(lista2, 15, 100, "mancare");
    assert(lista2->lungime == 1);
    assert(lista1->lungime == 0);

    distruge_lista(lista1);
    distruge_lista(lista2);
    free(lista1);
    free(lista2);
}

void testRedimensionare()
{
    MyList* undoList = createEmpty();

    undoList->capacitate = 1;
    assert(undoList->capacitate == 1);

    MyList* lista1 = createEmpty();
    assert(addElement(undoList, lista1, deepCopyMyList) == 1);
    assert(addElement(undoList, lista1, deepCopyMyList) == 1);

    assert(undoList->capacitate == 2);

    distruge_lista(lista1);
    free(lista1);

    destroyUndoList(undoList);
    free(undoList);
}

void testAddElementUndoList()
{
    MyList* undoList = createEmpty();
    MyList* lista1 = createEmpty();
    MyList* lista2 = createEmpty();

    assert(addElement(undoList, lista1, deepCopyMyList) == 1);
    assert(addElement(undoList, lista2, deepCopyMyList) == 1);

    assert(undoList->lungime == 2);

    assert(((MyList*)undoList->elemente[0])->lungime == 0);

    add(lista2, 2, 200, "Transport");
    assert(((MyList*)undoList->elemente[1])->lungime == 0);
    
    distruge_lista(lista1);
    distruge_lista(lista2);
    free(lista1);
    free(lista2);

    destroyUndoList(undoList);
    free(undoList);
}

void testRemoveLastElement()
{
    MyList* undoList = createEmpty();
    MyList* lista1 = createEmpty();
    addElement(undoList, lista1, deepCopyMyList);

    assert(removeLastElement(undoList) == 1);
    assert(removeLastElement(undoList) == 0);
    assert(undoList->lungime == 0);

    distruge_lista(lista1);
    free(lista1);

    destroyUndoList(undoList);
    free(undoList);
}

void testUndo()
{
    MyList* undoList = createEmpty();
    MyList* lista = createEmpty();
    
    for (int index = 0; index < lista->capacitate; index++)
    {
        addElement(undoList, lista, deepCopyMyList);
        add(lista, index, 100, "mancare");
    }
    assert(lista->lungime == 5);
    assert(undoList->lungime == 5);

    for (int index = 4; index >= 0; index--)
    {
        lista = undo(undoList, lista);
        assert(lista->lungime == index);
    }

    lista = undo(undoList, lista);
    assert(lista->lungime == 0);

    distruge_lista(lista);
    free(lista);

    destroyUndoList(undoList);
    free(undoList);
}

void testFiltrareMaiMare()
{
    MyList* lista = createEmpty();
    add(lista, 1, 1000, "mancare");
    add(lista, 2, 100, "mancare");

    MyList* lista2 = filtrareMaiMare(lista, 101);
    assert(lista2->lungime == 1);

    MyList* lista3 = filtrareMaiMare(lista, 1001);
    assert(lista3 == NULL);

    distruge_lista(lista);
    free(lista);

    free(lista2->elemente);
    free(lista2);
}

void testFiltrareMaiMic()
{
    MyList* lista = createEmpty();
    add(lista, 1, 100, "mancare");
    add(lista, 2, 10, "mancare");

    MyList* lista2 = filtrareMaiMic(lista, 99);
    assert(lista2->lungime == 1);

    MyList* lista3 = filtrareMaiMic(lista, 9);
    assert(lista3 == NULL);

    distruge_lista(lista);
    free(lista);

    free(lista2->elemente);
    free(lista2);
}

void ruleaza_toate_testele() {
  test_add();
  test_modificare();
  test_stergere();
  test_vizualizare();
  test_ordonare();
  test_ordonare_desc();
  test_add_service();
  test_modificare_service();
  test_stergere_service();
  test_get_all();
  test_get_all_service();
  test_distruge_lista();
  test_redimensioneaza_lista();
  test_creeaza_cheltuiala();

  testCopyCheltuiala();

  testCreazaEmptyUndoList();
  testDeepCopyMyList();
  testRedimensionare();
  testAddElementUndoList();
  testRemoveLastElement();
  testUndo();
  testFiltrareMaiMare();
  testFiltrareMaiMic();
}

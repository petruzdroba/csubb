#pragma once
#include "repo.h"

int adaugareMedicament(ListaMedicamente *farmacie, int cod, const char *nume, int cantitate, int concentratie);
/*
    Functie care adauga un medicament in lista farmaciei
        cod: int > 0
        nume : string != ""
        cantitate: int >0
        concentratie: int >0
    return:
        -0, obiectul nu s-a adaugat din cauza unei erori de validare
        -1, obiectul a fost adaugat cu succes
        -2 obiectul deja exita, stocul lui a fost updatat
*/

int modificareMedicament(ListaMedicamente *farmacie, int cod, const char *nume, int cantitate, int concentratie);
/*
    Functie care modifica un medicament dupa un cod
        cod: int > 0 - nu se schimba
        nume : string != "" - new
        cantitate: int >0 - new
        concentratie: int >0 -new
    return:
        -0, medicamentul cu codul introdus nu exista
        -1 , obiectul a fost modificat cu success
*/

int stergereMedicament(ListaMedicamente *farmacie, int cod);
/*
    Functie care sterge un medicament dupa un cod
        cod: int > 0
    return:
        -0, medicamentul cu codul introdus nu exista
        -1 , obiectul a fost sters cu success
*/

ListaMedicamente sortCrescatorNume(ListaMedicamente *farmacie, int(* comparaNume)(const void *a,const void *b));
/*
    Functie care returneaza lista farmacie sortata alfabetic
    return: o noua lista care este sortata dupa prima litera a numelui
*/

ListaMedicamente sortCantitateCrescator(ListaMedicamente *farmacie, int(* compareCantitateCrescator)(const void *a, const void *b));
/*
    Functie care returneaza lista farmacie sortata cresc dupa cantitate
    return: o noua lista care este sortata dupa cantitate
*/

ListaMedicamente sortCantitateDescrescator(ListaMedicamente *farmacie, int (* compareCantitateDescrescator)(const void *a, const void * b));
/*
    Functie care returneaza lista farmacie sortata desc dupa cantitate
    return: o noua lista care este sortata dupa cantitate
*/

ListaMedicamente filtrareCantitate(ListaMedicamente *farmacie, int cantitate);
/*
    Functie care returneaza o lista cu medicamentele care au cantitatea mai mica decat cea introdusa
        cantitate: int > 0
    return: o noua lista cu medicamente
*/

ListaMedicamente filtrareAlpha(ListaMedicamente *farmacie, char litera);
/*
    Functie care returneaza o lista cu medicamentele care au prima litera cea introdusa, case sensitive
        litera: char
    return: o noua lista cu medicamente
*/
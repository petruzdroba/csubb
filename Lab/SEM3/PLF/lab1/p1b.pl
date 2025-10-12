#!/usr/bin/env swipl
/*
1b) Sa se scrie un predicat care adauga intr-o lista dupa fiecare element par val 1.

adauga(L: lista, R: lista)

L: lista sursa
R: lista rezultat

Model matematic: 

adauga( l1...ln)={
    []      n=0
    l1 (+) 1 (+) adauga(l2...ln)        l1 mod 2=0
    l1 (+) adauga(l2...ln)          altfel
}
*/

adauga([], []).
adauga([H|T], [H,1|R]) :- 0 is H mod 2, adauga(T,R).
adauga([H|T], [H|R]) :- 1 is H mod 2, adauga(T,R).

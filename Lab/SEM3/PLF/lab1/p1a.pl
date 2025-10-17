#!/usr/bin/env swipl

/*
1a) Sa se scrie un predicat care intoarce diferenta a doua multimi.

exista(X: element, Y: lista)

X: elementul cautat in lista, input
Y: lsita in care este cautat elementul, input

Model matematic:

exista(x, y1...ym)={ #returneaza True daca elementul exista si False daca elementul nu exista
    False        m=0
    True       y1=x
    exista(x, y2...ym)    altfel
}


diferenta(X: lista, Y: lista, R: lista)

X: lista numarul 1, input
Y: lsita numarul 2, input
R: lista rezultata, output

Model matematic:

diferenta( x1...xn, y1...ym)={
    []      n=0
    x1 (+) diferenta( x2...xn, y1...ym)     exista(x1, y1...ym)=False
    diferenta( x2...xn, y1...ym)            altfel
}

Cazuri de testare:
    [1,2,3,4] si [2,3,4] => [1]
    [5,6,7] si [1,2,3] => [5,6,7]
    [2,3,4] si [4,3,2] => [] 
    
    diferenta([1,2,3,4], [2,3,4], R).

*/

exista(_, []) :- false.
exista(X, [X|_]) :- true.
exista(X, [_|T]) :- exista(X,T).


diferenta([], _, []).
diferenta([H|T], Y, [H|R]) :- \+ exista(H,Y), diferenta(T,Y,R).
/* x1 -Head , x2...xn - Tail, daca exista Head in Y atunci adaugam Head in Result */
diferenta([H|T], Y, R):- exista(H,Y), diferenta(T,Y,R).
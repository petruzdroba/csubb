/*
    6a. sa se inlocuiasca toate elementele E din lista L cu toate elementele altei lista
    exp [1,2,3,4], 1, [10,11] => [10,11,2,3,4]

    inlocuieste(L:lista, E:numar, A:lista, R:lista)
    
    L: lista originala de input
    E: elementul care se cauta
    A: lista cu care va fi inlocuit E
    R: lista rezultata

    Model matematic:

        adauga(l1...ln, x1...xm)={
            x1...xm     daca    n = 0
            l1 (+) adauga(l2...ln, x1...xm)
        }

        inlocuieste(l1...ln, e, a1...am)={
            []      daca    n = 0
            adauga(a1...am, inlocuieste(l2...ln, e, a1...am))     daca    l1=e
            l1(+) inlocuieste(l2...ln, e,a1...am)
        }
*/

adauga([], Acc, Acc).
adauga([H|T], Acc, [H|R]):- adauga(T, Acc, R).

inlocuieste([], _, _, []).
inlocuieste([H|T], E, A, R1):- H=E, !, adauga(A,R,R1), inlocuieste(T,E,A,R).
inlocuieste([H|T], E, A, [H|R]):- inlocuieste(T,E,A,R).
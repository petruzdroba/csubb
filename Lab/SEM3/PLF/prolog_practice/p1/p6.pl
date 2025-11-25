/*
    6a. - elimina elementele care apar de cel putin 2 ori
    [1,2,3,1,4,1,4] -> [2,3]

    elimina(L:lista, R:lista)

    L: lista de input
    R: lista originala

    Model matematic:
        count(e,x1...xn,c)={
            c       daca    n = 0
            count(e,x2...xn, c+1)   daca    x2=e
            count(e,x2...xn,c)
        }

        elimina(l1...ln, x1...xm)={
            []      daca    n=0
            l1(+)elimina(l2...ln,x1...xm)      daca    count(l1,x1...xm)=1
            elimina(l2...ln, x1...xm)          altfel
        }

        main_elimina(l1...ln)={ elimina(l1...ln, l1...ln)}
*/
count(_,[],C,C):-!.
count(E,[H|T], C, R):- H=E,!,C1 is C + 1, count(E,T,C1,R).
count(E,[_|T], C,R):- count(E,T,C,R).

elimina([],_,[]).
elimina([H|T], X, [H|R]):- count(H,X,0,G), 1 is G, !, elimina(T,X,R).
elimina([_|T], X, R):- elimina(T,X,R).

main_elimina(L,R):- elimina(L,L,R).

/*
    6b. - eliminarea tuturor aparitiilor elementului maxim dintr-o lista

    elimina(L:lista, E:element, R:rezultat)

    L: lista de input
    E: elemeentul maxim
    R: lsita rezultat

    Model matematic:

        maxim(e, l1...ln)={
            e   daca    n = 0
            maxim(l1, l2...ln)      daca    l1 > e
            maxim(e,l2...ln)        altfel
        }

        elimina_max(l1...ln, e)={
            []  daca    n = 0
            l1(+)elimina_max(l2...ln,e)     daca    e !=l1
            elimina_max(l2...ln, e)         altfel
        }

        main_elimina_max(l1...ln)={
            elimina_max(l1...ln, maxim(l1, l2...ln))
        }
*/

maxim(E,[], E):-!.
maxim(E, [H|T], R):- H>E, !, maxim(H,T,R).
maxim(E, [_|T], R):- maxim(E,T,R).

elimina_max([], _, []).
elimina_max([H|T], E, [H|R]):- H\=E, !, elimina_max(T,E,R).
elimina_max([_|T], E, R):- elimina_max(T,E,R).

main_elimina_max([H|T],R):- maxim(H,T,G), elimina_max([H|T],G,R).
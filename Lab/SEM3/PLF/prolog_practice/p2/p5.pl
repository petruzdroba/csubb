/*
    5a. Sa se det poz elem maxim 
    exemplu [1,2,5,1,2,5] -> [3,6]

    poz_maxim(L:lista, E:numar, I:numar, R:lista)

    L: lista de input
    E: elemeentul maxim
    I: indexul curent
    R: lista rezultata

    Model matematic:

        maxim(e,l1...ln)={
            e   daca    n=0
            maxim(l1,l2...ln)   daca    l1>e
            maxim(2, l2...ln)   altfel
        }

        poz_maxim(l1...ln,e,i)={
            []      daca    n=0
            i(+)poz_maxim(l2...ln,e,i+1)    daca    l1=e
            poz_maxim(l2...ln,e,i+1)        altfel
        }

        main_poz_maxim(l1...ln)={
            poz_maxim(l1...ln, maxim(l1,l2...ln), 1)
        }
*/

maxim(E,[],E):-!.
maxim(E, [H|T],R):- H>E, !, maxim(H,T,R).
maxim(E, [_|T], R):- maxim(E,T,R).

poz_maxim([], _,_,[]).
poz_maxim([H|T], E, I, [I|R]):- H=E,!, I1 is I + 1, poz_maxim(T,E,I1,R).
poz_maxim([_|T], E,I,R):- I1 is I + 1,  poz_maxim(T,E,I1,R).

main_poz_maxim([H|T],R):- maxim(H,T,M), poz_maxim([H|T], M,1,R).
/*
    4b. sa se elimine elementul de pe poz n

    elimina(L: lista, N:numar, I:numar, R: lista)

    L: lista de input
    N: pozitia care va fi eliminata
    I: indexul elementului curent
    R: lista rezultat

    Model matematic:

        elimina(l1...lm,n,i)={
            []      daca    n=0
            elimina(l2...lm,n,i+1)      daca    n=i
            l1(+)elimina(l2...ln,n,i+1) altfel
        }

        main_elimina(l1...ln,n)={
            elimina(l1...ln,m,1)
        }
*/

elimina([],_,_,[]):-!.
elimina([_|T], N,I,R):- N=I,!, I1 is I+1, elimina(T,N,I1, R).
elimina([H|T], N,I,[H|R]):-  I1 is I+1, elimina(T,N,I1, R).

main_elimina(L,N,R):- elimina(L,N,1,R).
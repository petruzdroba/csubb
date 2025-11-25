/*
8a. verifica daca o lista este multime

eMultime(L:lista, R:rezultat)

L: lista de numere initiala
R: rezultat 0 - false, 1 - true

Model matematic:
    exista(x, l1...ln)={
        1   daca    x = l1
        0   daca    n = 0
        exista(x, l2...ln)  altfel
    }


    eMultime(l1...ln)={
        1      daca     n = 1
        0      daca     exista(l1, l2...ln) = 1
        eMultime(l2...ln)   altfel
    }

Model de flux (i,o)
*/

exista(_, [], 0).
exista(X,[H|_], 1):- X=H.
exista(X, [_|T], R):- exista(X,T,R).

eMultime([_],1).
eMultime([H|T],0):- exista(H,T,G), 1 is G, !.
eMultime([_|T], R):- eMultime(T,R), !. 


/*
8b. elimina un element de cel mult 3 ori

elimina(L:lista, E:numar, C: numar, R:lista)

L: list de input
E: elementul de eliminat
C: counter pentru cate elemente de eliminat mai sunt
R: lista rezultat

Model matematic:
    elimina(l1...ln,e,c)={
        []  daca    n=0
        l1(+)elimina(l2...ln,e,c)   daca    l1!=e sau c=0
        elimina(l2...ln,e,c-1)      altfel
    }

    main_elimina(l1...ln,e)={
        elimina(l1...ln,c,3)
    }
*/

elimina([],_,_,[]).
elimina([H|T], E,0, [H|R]):-!, elimina(T,E,0,R).
elimina([H|T], E,C, [H|R]):- H\=E,!,  elimina(T,E,C,R).
elimina([_|T], E,C,R):- C1 is C - 1 , elimina(T,E,C1,R).

main_elimina(L,E,R):- elimina(L,E,3,R).
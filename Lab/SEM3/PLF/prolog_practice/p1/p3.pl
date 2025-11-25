/*
 3a - transforma lista in multime, dupa aparitie in lista [1,2,3,1,2] -> [1,2,3]
 [5,4,2,7,2,3] -> [5,4,2,7,3]

 multime(L:lista, R:lista)

 L: lista de inpuit
 R: lista rezultat, multime

 Model matematic:
    exista(x, l1...ln)={
        0   daca    n = 0
        1   daca    l1 = x
        exista(x, l2...ln)  altfel
    }

    multime(l1...ln)={
        multime_acc(l1...ln,[])
    }

    multime_acc(l1...ln, acc1...accm)={
        acc1...accm     daca    n = 0
        multime_acc(l2...ln, l1(+)acc1...accm)   daca    exista(l1,acc1...accm)=0
        multime_acc(l2...ln, acc1...accm)   altfel
    }

 Model de flux: (i,o)
*/

exista(_, [], 0).
exista(X,[H|_], 1):- X=H, !.
exista(X, [_|T], R):- exista(X,T,R).

multime_acc([],_,[]).
multime_acc([H|T],Acc,[H|R]) :- exista(H,Acc,G), 0 is G, !, multime_acc(T,[H|Acc],R).
multime_acc([_|T],Acc,R) :- multime_acc(T,Acc,R).

multime(L,R) :- multime_acc(L,[],R).

/*
3b - sa se imparta in 2 liste numerele pare si cele impare

imparte(L:lista,P:lista, I:lista,R:lista)

L: lista de input
P: lista cu numere pare
I: lista cu numere impare
R: lista rezultat care v-a contine 2 liste, 1 cu numere pare alta cu numere impare

Model matematic:
    imparte(l1...ln,p1...pn,i1...in)={
        [p1...pn,i1...im]   daca    n = 0
        imparte(l2...ln, l1(+)p1...pn, i1...in)     daca    l1 mod 2 = 0
        imparte(l2...ln, p1...pn, l1(+)i1...in)     altfel
    }

    main_imparte(l1...ln)= {
        imparte(l1...ln, [], [])
    }

Model de flux: (i,i,i,o)
*/

imparte([], P, I, [P|[I]]).
imparte([H|T], P, I, R):- H mod 2 =:= 0, !, imparte(T,[H|P],I,R).
imparte([H|T], P, I, R):-imparte(T,P,[H|I],R).

main_imparte(L,R):- imparte(L,[],[],R).
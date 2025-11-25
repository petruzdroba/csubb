/*
7a. determinati produsul unui numar(reprezentat ca lista) cu o cifra

    produs(L:lista, X: numar, C:numar, R:lista)

    L: lsita de nput
    X: numarul cu care se face produsul
    C: carry
    R: rezultat

    Model matematic:

        reverse(l1...ln, y1...ym)={
            y1...ym     daca    n = 0
            reverse(l2...ln, l1(+)y1...ym)  altfel
        }

        produs(l1...ln, x,c)={
            []      daca    n=0 si c=0
            [c]     daca    n = 0
            (l1 * x + c) mod 10 (+) produs(l2...ln,x,(l1 * x + c) div 10) altfel
        }

        main_produs(l1...ln,x)={
            reverse(produs(reverse(l1...ln), x, 0))
        }
*/

reverse([], Acc, Acc).
reverse([H|T], Acc, R):- reverse(T, [H|Acc],R).

produs([],_,0,[]):-!.
produs([],_, C, [C]):-!.
produs([H|T],X,C, [G1|R]):-   S is (H*X + C),
                        G1 is S mod 10,
                        G2 is S // 10,
                        produs(T,X,G2, R).

main_produs(L, X,R):-   reverse(L,[], L1),
                        produs(L1, X, 0, R1),
                        reverse(R1, [], R).
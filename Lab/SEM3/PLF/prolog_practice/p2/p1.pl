/*
    1a determina suma a doua numere scrise in reprezentare de lista

    suma(X:lista, Y:lista,C:numar, R: lista)

    X: lista input 1
    y: lista input 2
    C: carry
    R: rezultatul reprezentand suma a doua numere

    Model matematic:
        reverse(l1...ln, x1...xm)={
            x1...xm     daca    n = 0
            reverse(l2...ln, l1(+)x1...xm)    altfel
        }

        suma(x1...xn, y1...ym, c)={
            []      daca    n=0 si m=0 si c=0
            [c]     daca    n=0 si m=0
            (x1 + c) %10 (+) suma(x2...xn, [], (x1+c)/10)   daca    m = 0
            (y1 + c) %10 (+) suma([], y2...ym, (y1+c)/10)   daca    n = 0
            (x1 + y1 + c) % 10 (+) suma(x2...xm, y2...ym, (x1 + y1 + c)/10)     altfel 
        }

        main_suma(x1...xn, y1...ym)={
            reverse(suma(reverse(x1...xn), reverse(y1...ym)))
        }
*/

reverse([], Acc, Acc):-!.
reverse([H|T], Acc, R):- reverse(T, [H|Acc], R).

suma([],[],0,[]):-!.
suma([],[],C,[C]):-!.
suma([XH|XT], [YH|YT], C, [G1|R]):- G1 is (XH + YH + C) mod 10,
                              G2 is (XH + YH + C) // 10,
                              suma(XT,YT, G2, R). 

suma([XH|XT], [], C, [G1|R]) :- S is XH + C,
                                G1 is S mod 10,
                                C1 is S // 10,
                                suma(XT, [], C1, R).

suma([], [YH|YT], C, [G1|R]) :- S is YH + C,
                                G1 is S mod 10,
                                C1 is S // 10,
                                suma([], YT, C1, R).

main_suma(X, Y, R) :-   reverse(X, [], RX),
                        reverse(Y, [], RY),
                        suma(RX, RY, 0, RS),
                        reverse(RS, [], R).
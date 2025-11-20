/*
    6. Sa se genereze toate sirurile de n paranteze ce se inchid corect

    paranteze(Open: numar, Close: numar, Acc: lista, R:lista)
    - fix invers ca la cealalta, daca se poate adauga o paranteza inchisa, se adauga
    dupa se imparte in ramuri, unde se mai adauga una sau se adauga una deschisa -> nu vor fi mai multe deschise ca inchise

    Open: numarul de paranteze ramase deschise
    Close: numarul de paranteze ramase inchise
    Acc: Accumulator, rezultatul curent
    R: lista rezultat

    Model de flux (i,i,i,o)

    Model matematic:
        paranteze(open, close, l1...ln) =
           1. reverse(l1...ln)        open = 0 si close = 0
           2. paranteze(open, close - 1, ')' (+) l1...ln)     close > 0
           3. paranteze(open - 1, close, '(' (+) l1...ln)     daca open > close

    main_paranteze(N: numar, R:lista)

    N: numar dat de la tastatura, reprezinta numarul total de elemente dintr-o solutie
    R: Lista eterogena rezultata

    Model matematic:
        main_paranteze(N)={
        paranteze(N/2, N/2)
        }

    Model de flux (i,o)

    Cazuri de testare:
        4 -> (()), ()()
        6 ->((())),(()()),(())(),()(()),()()()
        2 -> ()
*/

paranteze(0,0,Acc,Acc).

paranteze(Open, Close, Acc, R) :-
    Close > 0,
    Close1 is Close - 1,
    paranteze(Open, Close1, [')'|Acc], R).

paranteze(Open, Close, Acc, R) :-
    Open > Close,
    Open1 is Open - 1,
    paranteze(Open1, Close, ['('|Acc], R).

main_paranteze(N,R) :- Half is N // 2, findall(X, paranteze(Half, Half, [], X), R).

e1:-write("4-> "), main_paranteze(4,R), write(R), nl.
e2:-write("6-> "), main_paranteze(6,R), write(R), nl.
e3:-write("2-> "), main_paranteze(2,R), write(R), nl.
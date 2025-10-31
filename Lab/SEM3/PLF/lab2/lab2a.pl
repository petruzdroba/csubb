/*
14 a. Determinati predecesorul unui numar natural reprezentat intr-o lista cifra cu cifra

reverse(A: lista, B: lista, R:lista)
-intoarce lista A folosind B([]) in R

A: lista originiala, input
B: lista peste care se adauga inversul lui A, input
R: lista rezultata, output

Model matematic:
    reverse(x1...xn, y1...ym) = {
    y1...ym     daca n=0
     reverse(x2...xn, x1 (+) y1...ym) altfel
    }


scade(A:lista, B: numar,R:lista)
-scade borrow B din lista A in R

A: lista originala, input
B: borrow, input
R: lista returnata, output

Model matematic:
    scade(x1...xn, b) = {
    []          daca n = 0
    9 (+) scade(x2...xn, 1) daca x1=0
    (x1 - b) (+) scade(x2...xn, 0) altfel
    }

predecesor(A: lista, R:lista)
-intoarce A in B, scade 1 din B in C, intoarce C in R

A: lista originala, inpuit
R: lista returnata, output

Model matematic:
    predecesor(x1...xn) = {
    B = reverse(x1...xn, []) 
    C = scade(B, 1)                   
    reverse(C, [])
    }

Cazuri de testare;
    [1,0,0,0] -> [0,9,9,9]
    [1,2,0] -> [1,1,9]
    [1] -> [0]

*/
reverse([], B, B).
reverse([H|T], BTail, B):- reverse(T, [H|BTail], B).

subtract([], _, []).
subtract([0|T], 1, [9|R]):- subtract(T, 1, R).
subtract([H|T], 1, [H1|R]):- H > 0, H1 is H - 1, subtract(T, 0, R).
subtract([H|T], 0, [H|R]):- subtract(T, 0, R).

predecesor(A, R):- reverse(A, [], B), subtract(B,1, C), reverse(C, [], R), !.

example1:- predecesor([1,0,0,0], R1), write(R1), nl.
example2:- predecesor([1,2,0], R1), write(R1), nl.
example3:- predecesor([1], R1), write(R1), nl.
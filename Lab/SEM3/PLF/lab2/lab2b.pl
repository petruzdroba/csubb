/*
14.b Se da o lista eterogena, formata din numere intregi si liste de cifre.
Pentru fiecare sublista sa se determine predecesorul numarului reprezentat
cifra cu cifra de lista respectiva.

eterogen(A:lista, R:lista)
-returneaza A, dar dupa cerinta in R

A: lista originala, input
R: lista returnata, output

Model matematic:
    asumam is_list ca fiind e_lista in limbaj matematic
    eterogen(x1...xn) = {
        []      daca n = 0
        predecesor(x1) (+) eterogen(x2...xn)    daca e_lista(x1)
        x1 (+) eterogen(x2...xn)            altfel
    }

Cazuri de testare:
    [1,[2,2],[2,0,0,0],999]                   -> [1,[2,1],[1,9,9,9],999]
    [1,2,3,4,5]                               -> [1,2,3,4,5]
    [1,[2,3],4,5,[6,7,9],10,11,[1,2,0],6]    -> [1,[2,2],4,5,[6,7,8],10,11,[1,1,9],6]
*/

reverse([], B, B).
reverse([H|T], BTail, B):- reverse(T, [H|BTail], B).

subtract([], _, []).
subtract([0|T], 1, [9|R]):- subtract(T, 1, R).
subtract([H|T], 1, [H1|R]):- H > 0, H1 is H - 1, subtract(T, 0, R).
subtract([H|T], 0, [H|R]):- subtract(T, 0, R).

predecesor(A, R):- reverse(A, [], B), subtract(B,1, BRes), reverse(BRes, [], R), !.

eterogen([],[]).
eterogen([H|T], [HRes|TRes]):- is_list(H), !, predecesor(H, HRes), eterogen(T, TRes).
eterogen([H|T], [H|TRes]) :- \+ is_list(H), H<5,!, eterogen(T, TRes).
eterogen([H|T], TRes) :- \+ is_list(H), H>=5, eterogen(T, TRes).

example1:- eterogen([1,[2,2], [2,0,0,0], 999], R1), write(R1), nl.
example2:- eterogen([1,2,3,4,5], R1), write(R1), nl.
example3:- eterogen([1, [2, 3], 4, 5, [6, 7, 9], 10, 11, [1, 2, 0], 6] , R1), write(R1), nl.

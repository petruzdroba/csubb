f([], -1).
f([H|T],S):- H>0, f(T,S1), S1<H, !, S is H.
f([_|T],S):-f(T, S1), S is S1.

#redifining a predicate means not switingg f/2 -> f/3 or adding other functions

g([], -1).
g([H|T], S):-
    g(T, S1),
    (
        H>0, S1<H -> S is H
        ; S is S1
    ).




f4([], 0).
f4([H|T], S):- f4(T, S1), S1 is S-H.
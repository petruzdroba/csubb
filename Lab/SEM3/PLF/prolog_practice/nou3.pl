subs(A, B, []):- A>B, !.
subs(A, B, [A|R]):- A1 is A+1, subs(A1, B,R).
subs(A, B, R):-A1 is A+1, subs(A1, B,R).

lent([],0).
lent([_|T],R):- lent(T,R1), R is R1+1.

subs2(A,B,N, R):-
    subs(A,B,S),
    lent(S, L),
    L =:= N, R = S.

suma([],0).
suma([H|T], R):-suma(T,R1), R is R1 + H.

regele(A,B,N,R):-
    subs2(A,B,N,S),
    suma(S, P), P mod 2=:=0, R = S.
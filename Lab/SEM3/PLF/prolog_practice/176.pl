checker([], 1).
checker([_],1).
checker([H1,H2|_], 0):- abs(H1 - H2) > 3, !.
checker([_|T],R):-checker(T,R).

inserare(E,[],[E]).
inserare(E, L, [E|L]).
inserare(E, [H|T], [H|R]):- inserare(E, T, R).

permutare([], []).
permutare([H|T], R):- permutare(T, RT), inserare(H,RT, R).
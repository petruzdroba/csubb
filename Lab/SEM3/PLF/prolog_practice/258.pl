sub([], []).
sub([H|T], [H|R]):- sub(T,R).
sub([_|T], R):-sub(T,R).

size([], 0).
size([_|T], R):- size(T,R1), R is R1+1.

selectK(L, K, R):- sub(L, S), size(S,RS), RS=:=K,  R=S.

suma([], 0).
suma([H|T], R):- suma(T, R1), R is R1+H.

regele(L, K,R):- selectK(L,K,S), suma(S, RS), RS mod 2 =:= 0, R=S.
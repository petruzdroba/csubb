inserare(E,[],[E]).
inserare(E, [H|T], [E,H|T]). 
inserare(E, [H|T], [H|R]):- inserare(E, T, R).

permutare([], []).
permutare([H|T], R):- permutare(T, RT), inserare(H,RT, R).

size([], 0).
size([_|T], R):- size(T, R1), R is R1+1.

selectK(_,0,[]).
selectK([H|T], K, [H|R]):- K1 is K-1, selectK(T,K1, R).
selectK([_|T], K, R):- selectK(T,K,R).

suma([], 0).
suma([H|T], R):- suma(T, R1), R is R1+H.

checker([H], R):-(H mod 2 =:= 1 -> R = 1 ; R = 0).
checker([_|T], R):-checker(T, R).

generator(L, K, S, R) :-
    selectK(L, K, PK),      
    permutare(PK, R),         
    checker(R, PC),
    PC =:= 1,
    suma(R, PS),
    PS =:= S. 

collect_generator(L, K, S, ListOfR) :-findall(R, generator(L, K, S, R), ListOfR).

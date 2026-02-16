has_even([],0).
has_even([H|_],1):- H mod 2 =:= 0,!.
has_even([_|T],R):- has_even(T,R).

has_odd([],0).
has_odd([H|_],1):- H mod 2 =:= 1,!.
has_odd([_|T],R):- has_odd(T,R).

subm([],P,I,[]):- P mod 2 =:= 0, I mod 2 =:= 1,!.
subm([H|T], P,I,[H|R]):- H mod 2 =:= 0, (   P mod 2 =:= 1; has_even(T,RT), RT =:= 1),P1 is P +1, subm(T,P1,I,R).
subm([H|T], P,I,[H|R]):- H mod 2 =:= 1, (   I mod 2 =:= 0; has_odd(T,RT), RT =:= 1),I1 is I +1, subm(T,P,I1,R).
subm([_|T], P,I,R):- subm(T,P,I,R). 
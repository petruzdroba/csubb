insereaza(E,L,[E|L]).
insereaza(E,[H|T], [H|R]):-insereaza(E,T,R).

check([], _).
check([_], _).
check([A,B|T], V) :-
    D is abs(A - B),
    D >= V,
    check([B|T], V).


perm([],_,[]).
perm([H|T],V,R):-perm(T,V,RT),check(RT,V) ,insereaza(H,RT,R).
perm([H1,H2|T],V,R):- perm([H1|T],V,RT),check(RT,V), insereaza(H2,RT,R).

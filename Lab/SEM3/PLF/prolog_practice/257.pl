maxi([],E,E).
maxi([H|T], E,R):- H>E, !, maxi(T,H,R).
maxi([_|T],E,R):- maxi(T,E,R).

maxer([],[]).
maxer([H|T], [M|R]):-is_list(H),!, H=[H1|_], maxi(H,H1, M), maxer(T,R).
maxer([_|T], R):-maxer(T,R).

enforce(_,[],[]).
enforce(E, [H|T], [P|R]):- P is E*H, enforce(E,T,R).

enforcer([],_,[]).
enforcer([H|T], J, [P|R]):- enforce(H,J,P), enforcer(T,J,R).

mainer(L,J,R):- maxer(L,LR),maxer(J,JR), enforcer(LR, JR, R).
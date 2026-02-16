generator(A,B,[]):- A>B,!.
generator(A,B,[A|R]):-A1 is A + 1, generator(A1,B,R).

check([],1):-!.
check([_],1):-!.
check([H1,H2|_],0):- abs(H1 - H2) > 2,!.
check([_|T],R):- check(T,R).

select([H|T], H,T).
select([H|T], E, [H|R]):- select(T,E,R).

perm([],[]).
perm(L, [H|R]):- select(L,H,T), perm(T,R), check([H|R], RT), RT =:= 1.

main(N,R):-NN is 2*N-1, generator(N,NN,G), findall(RT, perm(G, RT),R).

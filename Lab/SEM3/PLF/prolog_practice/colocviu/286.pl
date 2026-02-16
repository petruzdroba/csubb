insereaza(E,L,[E|L]).
insereaza(E, [H|T], [H|R]):-insereaza(E,T,R).

aranj(_, 0,S,C,[]):- S=C.
aranj([H|T],K,S,C,R):- K>0, C1 is C+H, C1 =< S, K1 is K-1, aranj(T,K1,S,C1,RT),insereaza(H,RT,R).
aranj([_|T],K,S,C,R):- K>0, aranj(T,K,S,C,R).

aranj_main(L,K,S,R):- findall(RT, aranj(L,K,S,0,RT),R).

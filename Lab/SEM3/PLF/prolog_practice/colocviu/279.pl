insereaza(E,L,[E|L]).
insereaza(E,[H|T], [H|R]):-insereaza(E,T,R).

aranj(_,0,P,C,[]):- P =:= C.
aranj([H|T], K,P,C,R):- K>0, C1 is C*H, C1 =< P, P mod C1 =:=0, K1 is K-1, aranj(T,K1,P,C1,RT), insereaza(H,RT,R).
aranj([_|T],K,P,C,R):- K>0, aranj(T,K,P,C,R).

aranj_main(L,K,P,R):- findall(RT, aranj(L,K,P,1,RT),R).

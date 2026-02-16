insereaza(E,L,[E|L]).
insereaza(E,[H|T],[H|R]):- insereaza(E,T,R).

aranj(_,0,1,[]).
aranj([H|T],K,P,R):- K>0, P mod H =:= 0, K1 is K-1, P1 is P // H, aranj(T,K1,P1, RT), insereaza(H,RT,R).
aranj([_|T],K,P,R):- aranj(T,K,P,R).

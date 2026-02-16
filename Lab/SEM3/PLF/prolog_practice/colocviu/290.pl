insereaza(E,L,[E|L]).
insereaza(E, [H|T], [H|R]):-insereaza(E,T,R).

aranj(_, 0,_,_,[]).
aranj([_|T], K,V,P,R):- K>0, aranj(T,K,V,P,R).
aranj([H|T], K,V,P,R):- K>0, P1 is P*H, P1<V, K1 is K-1 ,aranj(T,K1, V, P1, RT), insereaza(H,RT,R).

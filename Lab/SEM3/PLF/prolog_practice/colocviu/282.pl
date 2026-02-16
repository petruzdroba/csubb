comb(_,0,_,[]).
comb([H|T],K,-1,[H|R]):- K>0, K1 is K-1,comb(T,K1,H,R).
comb([H|T],K,P,[H|R]):- K>0,P \= -1, abs(H-P) mod 2 =:= 0, K1 is K-1, comb(T,K1,H,R).
comb([_|T], K,P,R):- K>0, comb(T,K,P,R).

gen(C,N,[]):- C>N.
gen(C,N,[C|R]):- C1 is C+1, gen(C1, N,R).

comb_main(N,K, R):- gen(1,N,G), findall(RT, comb(G,K,-1,RT), R).

generator(A,B,[]):- A>B,!.
generator(A,B,[A|R]):- A1 is A + 1, generator(A1,B,R).

has_odd([],0).
has_odd([H|_],1):- H mod 2 =:= 1,!.
has_odd([_|T],R):- has_odd(T,R).

comb(_, 0,S,[]):- S mod 2 =:= 0.
comb([H|T],K,S,[H|R]):- K>0, (S mod 2 =:= H mod 2 ->  true; has_odd(T,RT), RT =:= 1), K1 is K-1, S1 is S+H, comb(T,K1,S1,R).
comb([_|T],K,S,R):-K>0, comb(T,K,S,R).

c_main(A,B,K,R):- generator(A,B,G),findall(RT, comb(G,K,0,RT),R).

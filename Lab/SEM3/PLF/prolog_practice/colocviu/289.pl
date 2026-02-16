comb(A,B,P,I,[]):- A>B, P mod 2  =:= 0, I mod 2 =:=1.
comb(A,B,P,I,[A|R]):- A =< B, (0 is A mod 2 ->   P1 is P+1, I1 is I; P1 is P, I1 is I+1), A1 is A + 1, comb(A1,B, P1,I1, R).
comb(A,B,P,I,R):- A=<B, A1 is A+1, comb(A1, B,P,I,R).

comb_main(A,B,CR):- findall(R, comb(A,B,0,0,R), CR).

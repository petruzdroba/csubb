inserare(E,[],[E]).
inserare(E, [H|T], [E,H|T]). 
inserare(E, [H|T], [H|R]):- inserare(E, T, R).

permutare(A,B, []):- A>B,!.
permutare(A ,B, R):- A1 is A+1, permutare(A1, B, RT), inserare(A,RT, R).

checker([_],1).
checker([H1,H2|_],0):- abs(H1-H2) <2, !.
checker([_|T], R):-checker(T, R).

generator(N,R):- permutare(1,N,P), checker(P, C), C=:=1, R = P.
m_generator(N,R1):-findall(R, generator(N,R),R1).
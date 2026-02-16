generator(A,B,[]):- A>B,!.
generator(A,B,[A|R]):- A1 is A + 1, generator(A1,B,R).

has_odd([],0):-!.
has_odd([H|_],1):- H mod 2 =:= 1,!.
has_odd([_|T],R):- has_odd(T,R).

subm([],S,[]):- S mod 2 =:=1.
subm([H|T], S, [H|R]):- SR is S mod 2, HR is H mod 2, (SR \= HR -> true; has_odd(T,RT),RT =:= 1), S1 is S+H, subm(T,S1,R).
subm([_|T],S,R):- subm(T,S,R).

main(A,B,R):- generator(A,B,G), findall(RT, subm(G,0,RT),R).

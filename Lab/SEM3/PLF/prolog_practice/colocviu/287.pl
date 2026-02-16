has_odd([],0).
has_odd([H|_],1):- H mod 2=:=1,!.
has_odd([_|T],R):- has_odd(T,R).

subs([],S,[]):- S mod 2 =:= 1.
subs([H|T], S, [H|R]):- H mod 2 =:= 0, has_odd(T,RT),RT =:= 1, S1 is S + H,subs(T,S1, R).
subs([H|T], S, [H|R]):- H mod 2 =:= 0, S mod 2=:=1, S1 is S + H,subs(T,S1, R).
subs([H|T], S, [H|R]):- H mod 2 =:= 1, S1 is S+H, subs(T,S1,R).
subs([_|T], S,R):- subs(T,S,R).

genereaza(A,B,[]):- A>B.
genereaza(A,B,[A|R]):- A =< B, A1 is A+1, genereaza(A1,B,R).

subs_main(A,B,R):- genereaza(A,B,G), findall(RT, subs(G,0,RT), R).

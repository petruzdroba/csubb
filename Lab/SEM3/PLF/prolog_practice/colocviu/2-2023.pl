has_compl([],_,0).
has_compl([H|_],R1,1):- S is R1 +H, S mod 3 =:= 0.
has_compl([_|T],R1,R):- has_compl(T,R1,R).

subm(_,K,C,S,[]):- C >= K, S mod 3 =:= 0,!.
subm([H|T], K,C,S,[H|R]):- SH is S+H, (SH mod 3 =:= 0 -> true; has_compl(T,SH,RT), RT =:=1),
    C1 is C+1, subm(T,K,C1,SH,R).
subm([_|T],K,C,S,R):- subm(T,K,C,S,R).

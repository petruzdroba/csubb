check([], _).
check([_], _).
check([A,B|T], V) :-
    D is abs(A - B),
    D >= V,
    check([B|T], V).

select_elem([H|T], H, T).
select_elem([H|T], E, [H|R]) :- select_elem(T, E, R).

perm([], _, []).
perm(L, V, [H|R]) :-
    select_elem(L, H, Rest),
    perm(Rest, V, R),
    check([H|R], V).


p_main(L,R):- findall(RT, perm(L,2,RT),R).

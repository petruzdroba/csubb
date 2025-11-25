/*
  11. a - sa se scrie un predicat care testeaza daca o lista are aspect de vale
a. 
vale(L:lista, F:numar, R: numar)

L: lista primita de la tastatura
F: flag care reprezinta daca lista creste(1) sau descreste(0)
R: rezultat 0 sau 1, 0 daca nu e vale, 1 daca e vale

model matematic:
	vale(l1...ln, f) = {
		1	daca 	f=0 si n=1
		0	daca	f=1 si n=1
		vale(l2...ln, 1)	daca	l1 < l2
		vale(l2...ln, 0)	daca	l1 > l2 si f = 0
		0	daca	l1 >= l2 si f = 1	
	}

	main_vale(l1...ln) = {
		0	daca	l1 < l2
		vale(l2...ln,0)	altfel
	}

model de flux: (i, i, o)
*/

vale([_],0,0):-!.
vale([_],1,1):-!.
vale([H1,H2|T], _, R):- H2 > H1, !, vale([H2|T], 1, R).
vale([H1,H2|T], 0, R):- H2 < H1, !, vale([H2|T], 0, R).
vale([H1,H2|_], 1, 0) :- H2 =< H1, !.

main_vale([H1,H2|T],R):- H1>H2, !, vale([H2|T], 0,R).
main_vale(_,0).


/*
11. b - sa se calculeze suma alternanta a unei liste date (l1 - l2 + l3 ....)

alternant(L:lista, F:numar, R:numar)
L - lista de input
F - flag care determina semnul
R - rezultatul cumulat

Model matematic:

	alternant(l1...ln, f)={
		0	daca	n = 0
		l1 - alternant(l2...ln, 0)	daca	f=1
		l2 + alternant(l2...ln,1)	daca	f=0
	}

	main_alternant(l1...ln)={
		alternant(l1...ln, 1)
	}

Model de flux: (i,i,0)
*/


alternant([], _,0).
alternant([H|T],1,R):- alternant(T, 0,R1), R is R1-H.
alternant([H|T],0,R):- alternant(T, 1, R1), R is R1+H. 


main_alternant(L,R):-alternant(L,0,R),!.
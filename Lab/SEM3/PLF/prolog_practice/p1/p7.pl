/*
7a - returneaza reuninunea a doua multimi

reuniune(A:lista, B:lista, R:lista)

A- multimea 1
B- multimea 1
R- multimea formata din A reunit cu B

Model matematic:
	exista(a, b1...bm)={
		0 	daca	m = 0
		1	daca	b1 = a
		exista(a, b2...bm)	altfel
	}

	reuniune(a1...an, b1...bm)={
		b1...bm		daca	n = 0
		reuniune(a2...an, a1 (+) b1...bm)	daca 	!exista(a1, b1...bm)
		reuninue(a2...an, b1...bm)		altfel
	}

Model de flux: (i,i,o)
*/

exista(_,[],0):-!.
exista(A, [H|_], 1):- A=H, !.
exista(A, [_|T], R):- exista(A,T,R).

reuniune([],B,B):-!.
reuniune([H|T],B,R):- exista(H,B,G), 0 is G,!, reuniune(T, [H|B], R).
reuniune([H|T], B,R):- exista(H,B,G),1 is G,!, reuniune(T, B, R).



/*
7b - returneaza multimea tuturor perechilor distincte

perechi(L:lista, R:rezultat)

L-listade input\
R- rezultat

Model matematic:
	pair(a, l1...ln)={
		[]	daca	n = 1
		[a,l1](+)pair(a,l2...ln)	altfel
	}

	perechi(l1...ln)={
		[]	daca	n = 0
		pair(l1, l2...ln)(+)perechi(l2...ln)	altfel
	}
Model de flux: (i,o)
*/

pair(_, [], []).
pair(A, [H|T],R):- pair(A,T,RT), R=[[A,H]|RT].

perechi([_], []):-!.
perechi([H|T], R):- pair(H,T,G), perechi(T, RT), R=[G|RT].
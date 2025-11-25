/*
12a. Sa se inlocuiasca toate aparitiile unui element cu un element dat
	exemplu: [1,2,2,3] 2, 99 -> [1,99,99,3]

substituie(L:lista, E:numar, V:numar, R:rezultat)

L: lista de input, originala
E: elementul care va fi schimbat
V: valoarea noua a elementului
R: lista rezultata 

Model matematic:
	substituie(l1...ln, e,v)={
		[]	daca	n = 0
		v (+) substituie(l2...ln,e,v)	daca	l1=e
		l1 (+) substituie(l2...ln, v)	altfel
	}

Model de flux: (i,i,i,0)
*/

substituie([],_,_,[]).
substituie([H|T], E, V, [V|R]):- H=E,!, substituie(T,E,V,R).
substituie([H|T],E,V,[H|R]):-substituie(T,E,V,R).

/*
12 b. - Sa se construiasca sublista lm...ln a unei liste l1...lk

sublista(L:lista, N:numar, M:numar,I:numar, R:rezultat)

L:lista originala
N: lower bound
M: upper bound
I: indexul elementului curent
R: lista formata doar din ln...lm

Model matematic:
	subl(l1...lk,n,m,i)={
		[]	daca	k=0
		l1 (+) subl(l2...lk,n,m,i+1)	daca	i>=n si i<=m
		subl(l2...lk,n,m,i+i)			altfel
	}

	main_subl(l1...lk,n,m)={
		subl(l1...lk,n,m,1)
	}
*/

subl([],_,_,_,[]).
subl([H|T],N,M,I,[H|R]):- I>=N, I=<M, !, I1 is I + 1, subl(T,N,M,I1,R).
subl([_|T],N,M,I,R):- I1 is I+1, subl(T,N,M,I1,R).

main_subl(L,N,M,R):- subl(L,N,M,1,R).
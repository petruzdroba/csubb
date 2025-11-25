/*
2. a - sa se determine cmmmc al elem unei liste

multiplu(L:lista, C:numar, R: numar)

L - lista de inpuit
C - rezultatul accumulat curent
R - rezultat

Model matematic:

	gcd(a,b,d)={
		d	daca a mod d = 0 si b mod d = 0
		gcd(a,b, d - 1)		altfel
	}

	multiplu(l1...ln, c)={
		c	daca	n = 0
		multiplu(l2...ln, ( l1 * c)/gcd(l1,c, c))
	}

	main_multiplu(l1...ln) = { multiplu(l2...ln, l1) }

Model flux: (i,i, o)
*/

gcd(A,B,D, D):- 0 is A mod D, 0 is B mod D,!.
gcd(A,B,D, R):- D1 is D - 1, gcd(A,B,D1,R).

multiplu([], C, C).
multiplu([H|T], C, R) :- gcd(H,C,C,D), C1 is (H*C) // D, multiplu(T, C1,R).

main_multiplu([H1,H2|T], R):- multiplu([H2|T], H1, R). 


/*
2.b Sa se scrie un predicat care adauga dupa al 2^k poz un v dat
ex: 1,2,3,4,5,6,7,8 -> 1,v,2,v,3,4,v,5,6,7,8,v

adder(L:lista, V:numar, R:lista)

L: lista de input
V: valoarea adaugata
R: lista rezultata

Model matematic:
	powTwo(a)={
		1	daca 	a = 1
		0	daca	a % 2 == 1
		powTwo(a/2)	altfel
	}

	adder(l1...ln, v)={
		[]	daca	n=0
		l1 (+) adder(l2...ln, v)	daca 	powTwo(a) = 0
		l1 (+) v (+) adder(l2...ln, v)		altfel
	}

Model de fluc: (i,i,o)
*/


powTwo(1,1):-!.
powTwo(X,0):- 1 is X mod 2,!.
powTwo(X,R):- 0 is X mod 2,!,X2 is X//2, powTwo(X2, R).

adder([],_,[]).
adder([H|T], V, [H|R]):- powTwo(H,G), 0 is G,!,adder(T,V, R).
adder([H|T], V,[H,V|R]):-  powTwo(H,G), 1 is G,!, adder(T,V,R).
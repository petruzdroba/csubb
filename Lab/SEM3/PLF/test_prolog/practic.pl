/*
Sa se elimine toate aparitiile elementului maxim dintr-o lista de numere intregi

	maxim(E: numar, L:lista, R: numar)
	-determina elementul maxim din L, mai mare casi E

	E: input, elementul de start, elementul maxim curent
	L: lista de input
	R: rezultatul, numar, cel mai mare numar

	elimina(L: lista, E: numar, R: lista)
	-elimina toate aparitiile elementului E din L

	L: lista de input
	E: elementul care va fi eliminat
	R: lista rezultata, L fara E

	main_elimina(L: lista, R: lista)
	-determina maximul si il elimina din L

	L: lista de input
	R: lista rezultata, fara aparitia elem max

	Model matematic:

		maxim(e, l1...ln)={
			e	daca	n=0
			maxim(l1, l2...ln)	daca	l1 > e
			maxim(e, l2...ln)	altfel
		}

		elimina(l1...ln, e)={
			[]	daca	n=0
			elimina(l2...ln,e)	daca	l1=e
			l1(+)elimina(l2...ln,e)		altfel	
		}

		main_elimina(l1...ln)={
			elimina(l1...ln, maxim(l1, l2...ln))
		}

	Model de flux:
		maxim(i,i,o)
		elimina(i,i,o)
		main_elimina(i,o)

	Cazuri de testare:
		[1,2,3,2,1] -> [1,2,2,1]
		[7,7,7,7,7] -> []
		[10,10,10,1,2,3,4,5,21,22] -> [10,10,10,21,2,3,4,5,21]
*/

maxim(E,[],E):-!.
maxim(E, [H|T], R):- H>E, !, maxim(H,T,R).
maxim(E, [_|T], R):- maxim(E,T,R).

elimina([],_,[]):-!.
elimina([H|T], E, R):- H=E, !, elimina(T,E,R).
elimina([H|T],E, [H|R]):- elimina(T,E,R).

main_elimina([H|T], R):- maxim(H,T,M), elimina([H|T], M,R).
main_elimina([],[]).

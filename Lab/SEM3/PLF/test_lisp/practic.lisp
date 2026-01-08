#|
	1. Toti atomii nenumerici care apar pe orice nivel, dar in ordine inversa
	
	Model matematic

	L - lista originala
	J - lista accumulator
	Inverseaza lista adaugand primul element intr-o lista vida pana la final
	Apel -> invers(l1...n, [])	

	invers(l1...ln, j1...jm)={
		j1...jm	daca	n=0
		l1(+)invers(l2...ln,j1...jm) alftel
	}

	L - lista originala
	Daca e numar sare peste, daca e atom si nu numar in adauga in fata, daca e sublista le "concateneaza"

	nenumerici(l1...ln)={
		[]	daca	n=0
		l1(+)nenumerici(l2...ln)	daca	e_atom(l1) si nu e_numar(l1)
		nenumerici(l2...ln)		daca	e_atom(l1)
		append(nenumerici(l1),nenumerici(l2...ln))	altfel
	}

	L - lista originala
	Retturneaza inversul listei de atomi nenumerici de la orice nivel

	practic(l1...ln)={
		invers(nenumerici(l1...ln) [])
	}

	Cazuri de testare:
	(practic '(((A B) 2 C) 3 (D 1 E)) ) -> (E D C B A)
	(practic '(((((A))))) ) -> (A)
	(practic '(1 2 3 4 5 6 7 (8 (9 (10 A)B)C)D) ) -> (D C B A)

|#

(defun invers (L J)
	(cond
		((null L) J)
		(T (invers (cdr L) (cons (car L) J)))
	)
)

(defun nenumerici (L)
	(cond
		((null L) nil)
		((and (atom (car L)) (not (numberp (car L)))) (cons (car L) (nenumerici (cdr L))))
		((atom (car L)) (nenumerici (cdr L)))
		(T (append (nenumerici (car L)) (nenumerici (cdr L))))
	)
)


(defun practic (L)
	(invers (nenumerici L) nil)
)

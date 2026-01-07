#|
	9a) Sa se scrie o functie care intoarce diferenta a doua multimi.
	exista(e, l1...ln)={
		0	daca	n = 0
		1	daca 	l1=e
		exista(e, l2...ln)	altfel
	}

	diferenta(l1...ln, j1...jm)={
		[]	daca	n=0
		l1 (+) diferenta(l2...ln, j1...jm)	daca	exista(l1, j1...jm)=0
		diferenta(l2...ln, j1...jm)		altfel
	}
|#
(defun exista (E L)
	(cond
		((null L) 0)
		((= (car L) E) 1)
		(T (exista E (cdr L)))
	)
)

(defun diferenta (L J)
	(cond
		((null L) nil)
		((= 0 (exista (car L) J)) (cons (car L) (diferenta (cdr L) J)))
		(T (diferenta (cdr L) J))
	)
)



#|
	9b) Definiti o functie care inverseaza o lista impreuna cu toate sublistele
	sale de pe orice nivel.

	inverseaza(l1...ln, x1...xn)={
		x	daca	n = 0
		inverseaza(l2...ln, l1(+)x1...xn)	daca	e_lista(l1)=0
		inverseaza(l2...ln, inverseaza(l1, [])(+)x1...xn)	altfel
	}

	inverseaza_main(l1...ln)={ inverseaza(l1...ln, []) }
|#


(defun inverseaza (L X)
	(cond
		((null L) X)
		((atom (car L)) (inverseaza (cdr L) (cons (car L) X)))
		(T (inverseaza (cdr L) (cons (inverseaza (car L) nil) X)) )
	)
)

(defun inverseaza_main (L)
	(inverseaza L nil)
)

#|
	9c) Dandu-se o lista, sa se construiasca lista primelor elemente ale tuturor
	elementelor lista ce au un numar impar de elemente la nivel superficial.
	Exemplu: (1 2 (3 (4 5) (6 7)) 8 (9 10 11)) => (1 3 9).

	numara(l1...ln)={
		0	daca 	n=0
		1 + count (l2...ln)	altfel
	}

	
	elimpar(l1...ln)={
		[]	daca	n=0
		l1(+)elimpar(l2...ln)	daca	atom(l1) si numara(l1...ln)%2
		l1(+)elimpar(l2...ln)	daca	e_lista(l1) si numara(l1)%2
		elimpar(l2...ln)	altfel
	}

	elimpar_main(l1...ln)={ elimpar([l1...ln])}
|#


(defun numara (L)
	(cond
		((null L) 0)
		(T (+ 1 (numara (cdr L))))
	)
)


(defun elimpar (L)
	(cond
		((null L) nil)
		((and (listp (car L)) (oddp (numara (car L)))) (cons (car (car L)) (elimpar (cdr L))))
		(T (elimpar (cdr L)))
	)
)

(defun elimpar_main (L)
	(elimpar (list L))
)

#|
	9d) Sa se construiasca o functie care intoarce suma atomilor numerici dintr-o
	lista, de la nivelul superficial.

	suma(l1...ln)={
		0	daca	n=0
		l1+suma(l2...ln)	daca	e_numar(l1)
		suma(l2...ln)		altfel
	}
|#


(defun suma (L)
	(cond
		((null L) 0)
		((numberp (car L)) (+ (car L) (suma (cdr L))))
		(T (suma (cdr L)))
	)
)

#|
	13.Se da un arbore de tipul (2). Sa se afiseze calea de la radacina pana la un
	nod x dat.

	cale(l1...ln, e)={
		[]	daca	n=0
		l1	daca	e=l1 si e_atom(l1)
		(prim l1)(+)cale(l2...ln, e)	daca	e_lista(l1) si cale(l1, e)
		cale(l2...ln, e)	altfel
	}

	remove-last(l1...ln)={
		[]	daca	n=1
		l1(+)remove-last(l2...ln)	altfel
	}

	cale_main(l1...ln, e)={
		[A, remove-last(cale(l1...ln))]
	}
|#

(defun cale (L E)
  (cond
    ((null L) nil)
    ((and (atom (car L)) (equal (car L) E)) (list (car L)))
    ((and (listp (car L)) (cale (car L) E)) (cons (car (car L)) (cale (car L) E)))
    (t (cale (cdr L) E))))

(defun remove-last (L)
	(cond
		((null (cdr L)) nil)
		(T (cons (car L) (remove-last (cdr L))))
	)
)

(defun cale_main (L E)
	(cons (car L) (remove-last (cale L E)))
)


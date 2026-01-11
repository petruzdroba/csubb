#|
	p14 Definiti o functie care da adancimea unui arbore n-ar reprezentat sub forma
	(radacina lista_noduri_subarb1...lista_noduri_subarbn)
	Ex: adancimea arborelui este (a (b (c)) (d) (e (f))) este 3

	my-max(l1...ln, e)={
		e	daca	n=0
		my-max(l2...ln, l1)	daca	l1>e
		my-max(l2...ln,e)	altfel
	}

	adancime(l1...ln)={
		0	daca	n=0
		1 + adancime(l2...ln)	daca	e_atom(l1)
		my-max(reuniune de la i la n de adancime(li))
	}
|#

(defun my-max (L E)
	(cond
		((null L) E)
		((> (car L) E) (my-max (cdr L) (car L)))
		(T (my-max (cdr L) E))
	)
)

(defun adancime (L)
	(cond
		((null L) 0)
		((atom (car L)) (+ 1 (adancime (cdr L))))
		(T (my-max (mapcar #' adancime L) 0))
	)
)

(defun adancime (L)
  (cond
    ((atom L) 0)
    (t (+ 1 (my-max (mapcar #'adancime L) 0)))))
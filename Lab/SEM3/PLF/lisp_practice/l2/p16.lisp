#|
	16. Sa se decida daca un arbore de tipul (2) este echilibrat (diferenta dintre
	adancimile celor 2 subarbori nu este mai mare dec

	my-max(a, b)={
		a	daca	a>b
		b	altfel
	}

	adancime(l1...ln)={
		0	daca	n=0
		adancime(l2...ln)	daca	e_atom(l1)	//frunza
		my-max(adancime(l1)+1, adancime(l2...ln))	//maximul dintre adancime subarbore si adancime vecini
	}

	echilibrat(l1...ln)={
		1	daca	n=0
		echilibrat(l2...ln)	daca	e_atom(l1)
		0	daca	|adancime(l1) - adancime(l2...ln)| >1
		echilibrat(l1) si echilibrat(l2...ln)	altfel
	}
|#

(defun my-max (A B)
	(cond
		((> A B) A)
		(T B)
	)
)

(defun adancime (L)
	(cond
		((null L) 0)
		((atom (car L)) (adancime (cdr L)))
		(T (my-max (+ 1 (adancime (car L))) (adancime (cdr L))))
	)
)

(defun echilibrat (L)
	(cond
		((null L) 1)
		((atom (car L)) (echilibrat (cdr L)))
		((> (abs (- (adancime (car L)) (adancime (cdr L)))) 1) 0)
		(T (and (echilibrat (car L)) (echilibrat (cdr L) )))
	)
)

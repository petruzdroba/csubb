#|
	5a Definiti o functie care interclaseaza cu pastrarea dublurilor doua liste
	liniare sortate.

	interclasare (l1...ln, j1...jm)={
		l1...ln		daca	m=0
		j1...jm		daca	n=0
		[]		daca	m=0 si n=0
		l1(+)interclasare(l2...ln, j1...jm)	daca	l1<=j1
		j1(+)interclasare(l1...ln,j2...jm)	altfel
	}
|#

(defun interclasare (L J)
	(cond
		((null L) J)
		((null J) L)
		((and (null L) (null J)) nil)
		((<= (car L) (car J)) (cons (car L) (interclasare (cdr L) J)))
		(T (cons (car J) (interclasare L (cdr J))))
	)
)


#|
	5b b) Definiti o functie care substituie un element E prin elementele unei liste
	L1 la toate nivelurile unei liste date L.

	swap_flat(l1...ln, e, x1...xm)={
		[]	daca	n=0
		l1(+)swap_flat(l2...ln, e, x1...xm)	daca	l1!=e
		x1...xm(+)swap_flat(l2...ln, e, x1...xm)	altfel
	}

	swap_depth(l1...ln, e, x1...xm)={
		[]	daca	n=0
		l1(+)swap_depth(l2...ln,e, x1...xm)	daca	atom(l1) si l1!=e
		x1...xm(+)swap_depth(l2...ln, e, x1...xm)	daca	atom(l1)
		swap_depth(l1, e, x1...xm)(+)swap_depth(l2...ln, e, x1...xm)	altfel
	}
|#

(defun swap_depth (L E X)
	(cond
		((null L) nil)
		((and (atom (car L)) (not (equal (car L) E))) (cons (car L) (swap_depth (cdr L) E X)))
		((atom (car L)) (cons X (swap_depth (cdr L) E X)))
		(T (cons (swap_depth (car L) E X) (swap_depth (cdr L) E X)))
	)
)

#|
	5c Definiti o functie care determina suma a doua numere in reprezentare de
	lista si calculeaza numarul zecimal corespunzator sumei.

	rev(l1...ln, x1...xm)={
		x1...xm		daca	n=0
		rev(l2...ln, l1(+)x1...xm)	altfel
	}

	suma(l1...ln, j1...jm, c)={
		[C]	daca	n=0 si m=0
		l1...ln	daca	m=0
		j1...jm	daca	n=0
		(l1+l2+c)%10(+)suma(l2...ln, j2...jm, (l1+l2+c)/10)
	}
|#

(defun rev (L X)
	(cond
		((null L) X)
		(T (rev (cdr L) (cons (car L) X)))
	)
)

(defun suma (L J C)
	(cond
		((and (null L) (null J)) (if (> C 0) (list C) nil))
		((null L) J)
		((null J) L)
		(T (cons (mod (+ (car L) (car J) C) 10) (suma (cdr L) (cdr J) (truncate (+ (car L) (car J) C) 10) )))
	)	
)

(defun suma_main (L J)
	(rev (suma (rev L nil) (rev J nil) 0) nil)
)


#|
	5d Definiti o functie care intoarce cel mai mare divizor comun al numerelor
	dintr-o lista liniara.

	cmmmdc(x, y, c)={
		1	daca	c=1
		c	daca	x%c=0 and y%c=0
		cmmmdc(x,y, c-1)	altfel
	}

	cmmmdc_list(l1...ln, c)={
		c	daca	n=1
		cmmmdc_list(l2...ln, cmmmdc(l1, l2, c))
	}

	cmmmdc_main(l1...ln)={cmmmdc_list(l1...ln, l1)}
|#

(defun cmmmdc (X Y C)
	(cond
		((= c 1) 1)
		((and (= 0 (mod X C)) (= 0 (mod X C))) C)
		(T (cmmmdc X Y (- C 1)))
	)
)

(defun cmmmdc_list (L C)
	(cond
		((null L) C)
		(T (cmmmdc_list (cdr L) (cmmmdc (car L) (cadr L) C)))
	)
)

(defun cmmmdc_main(L) (cmmmdc_list L (car L)))

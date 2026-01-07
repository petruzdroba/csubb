#|
	10. Se da un arbore de tipul (2). Sa se precizeze nivelul pe care apare un nod
	x in arbore. Nivelul radacii se considera a fi 0.

	gaseste(l1...ln, e,curent)={
		0	daca	n=0	//nu a fost gasit
		curent	daca	e_atom(l1) si l1=e
		gaseste(l2...ln, e, curent)	e_atom(l1)	//continuam uc vecinii, acelasi nivel
		gaseste(l1, e, curent+1) + gaseste(l2...ln, e, curent)	altfel		//cautam in subarbore (+1), si cautam vecinii
	} 

	gaseste_main(l1...ln, e)={
		gaseste(l1...ln,e , 0)
	}
|#

(defun gaseste (L E C)
	(cond
		((null L) 0)
		((and (atom (car L)) (equal (car L) E)) C)
		((atom (car L)) (gaseste (cdr L) E C))
		(T (+ (gaseste (car L) E (+ C 1)) (gaseste (cdr L) E C)))
	)
)

(defun gaseste_main (L E)
	(gaseste L E 0)	
)

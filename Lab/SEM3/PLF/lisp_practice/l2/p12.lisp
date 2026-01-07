#|
	12. Sa se construiasca lista nodurilor unui arbore de tipul (2) parcurs in
 	preordine. root -> left -> right

	noduri(l1...ln)={
		[]	daca	n=0
		adauga([l1], noduri(l2))		daca	e_atom(l1)	//radacina, adaugam radacina dupa continuam recursiv pe subarbori
		adauga(noduri(l1), noduri(l2))	dac	e_lista(l1)		//subarbori, adaugam stanga dupa dreapta
	}
|#

(defun preordine (L)
	(cond
		((null L) nil)
		((atom (car L)) (append (list (car L)) (preordine (cdr L))))
		(T (append (preordine (car L)) (preordine (cdr L))))
	)	
)

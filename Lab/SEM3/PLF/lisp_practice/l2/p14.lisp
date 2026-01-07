#|
	14 Sa se construiasca lista nodurilor unui arbore de tipul (2) parcurs in
	postordine. left->right->root

	postordine(l1...ln)={
		[]	daca	n=0
		adauga(postordine(l2), [l1])	daca	e_atom(l1)	//radacina, adaugam ce se afla in subarbori inainte
		adauga(postordine(l1), postordine(l2))	daca	e_lista(l1) //subarbori, adaugam stanga dupa dreapta
	}
|#

(defun postordine (L)
	(cond
		((null L) nil)
		((atom (car L)) (append (postordine (cdr L)) (list(car L))))
		(T (append (postordine (car L)) (postordine (cdr L)) ))
	)
)



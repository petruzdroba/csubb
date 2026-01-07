#|
    8. Sa se construiasca lista nodurilor unui arbore de tipul (2) parcurs in
    inordine left->root->right

    inordine(l1...ln)={
        []      daca    n=0
        adauga(inordine(l2), l1, reuniune de la i la n de inordine de l3)    daca    e_atom(l1)
    }
 |#

 (defun inordine (L)
    (cond
        ((null L) nil)
        (T (append (inordine (cadr L)) (list (car L)) (apply #'append (mapcar #'inordine (cddr L)) )))
    )
 )
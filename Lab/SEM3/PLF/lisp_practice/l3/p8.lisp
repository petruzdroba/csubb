#|
    p8 Sa se construiasca o functie care intoarce maximul atomilor numerici
    dintr-o lista, de la orice nivel.

    my-max(l1...ln, e)={
        e   daca    n=0
        my-max(l2...ln, l1)     daaca l1 > e
        my-max(l2...ln, e)      altfel
    }

    maximum(l1...ln, e)={
        e   daca    n=0
        my-max(l2...ln, l1)     daca    e_atom(l1)
        my-max(reuniune de la i la n de maximum de li, 0)
    }
|#

(defun my-max (L E)
	(cond
		((null L) E)
		((> (car L) E) (my-max (cdr L) (car L)))
		(T (my-max (cdr L) E))
	)
)

(defun maximum (L)
    (cond
        ((null L) 0)
        ((atom (car L)) (my-max (cdr L) (car L)))
        (T (my-max (mapcar #'maximum L) 0))
    )
)
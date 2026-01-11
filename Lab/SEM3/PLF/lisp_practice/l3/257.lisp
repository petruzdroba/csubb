(defun my-max (L E)
	(cond
		((null L) E)
		((> (car L) E) (my-max (cdr L) (car L)))
		(T (my-max (cdr L) E))
    )
)

(defun searcher (L E C)
    (cond
        ((null L) -1)
        ((and (atom (car L)) (equal (car L) E)) C)
        ((atom (car L)) (searcher (cdr L) E C))
        (T (my-max (cons
                        (searcher (car L) E (+ 1 C))
                        (mapcar (lambda (X) (searcher X E C)) (list (cdr L))) 
                    )
             -1))
    )
) 
(defun swapper (L E C)
    (cond
        ((null L) nil)
        ((and (atom (car L)) (oddp C)) (cons E (swapper (cdr L) E C)))
        ((atom (car L)) (cons (car L) (swapper (cdr L) E C)))
        (T(cons (swapper (car L) E (+ 1 C))(apply #'append (mapcar (lambda (x) (swapper x E C)) (list (cdr L))))))
    )
)
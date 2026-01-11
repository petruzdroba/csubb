(defun swapper (L)
    (cond
        ((and (numberp L) (evenp L)) (+ 1 L))
        ((listp L) (mapcar #'swapper L))
        (T L)
    )
)

(defun F(N) 
    (cond
        ((= N 0) 0)
        (T ((lambda (X)
                 (cond
                    ((> X 1) (- N 2))
                    (T (+ X 1))
                )
            )(F (- N 1)))
        )
    )
)

(defun swapper2 (L)
    (cond
        ((null L) nil)
        ((and (atom (car L)) (evenp (car L))) (cons (+ 1 (car L)) (swapper2 (cdr L))))
        ((atom (car L)) (cons (car L) (swapper (cdr L))))
        ; (T (cons (swapper (car L)) (apply #'append (mapcar (lambda(X) (swapper X)) (list(cdr L))))))
        (T (apply #' append(mapcar #'swapper L)))
    )
)
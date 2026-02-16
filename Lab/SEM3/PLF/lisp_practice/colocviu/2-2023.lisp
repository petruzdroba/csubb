(defun  fun (X &REST Y)
    (cond
        ((null Y) X)
        (T (APPEND X (mapcar #'car Y)))
    )
)

(defun swapper (L C X)
    (cond
        ((null L) nil)
        ((and (atom (car L)) (= C X)) (cons 0 (swapper (cdr L) C X)))
        ((atom (car L)) (cons (car L) (swapper (cdr L) C X)))
        (T (cons (swapper (car L) (+ 1 C) X) (apply #'append (mapcar (lambda(Y) (swapper Y C X)) (list(cdr L))))))
    )
)
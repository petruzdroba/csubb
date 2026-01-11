(defun swapper (L)
    (cond
        ((null L) nil)
        ((and (numberp (car L)) (evenp (car L))) (cons (+ 1 (car L)) (swapper (cdr L))))
        ((atom (car L)) (cons (car L) (swapper (cdr L))))
        (T (mapcar #'swapper L))
    )
)
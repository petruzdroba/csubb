(defun finder (L C X)
    (cond
        ((or (null L) (> C X)) nil)
        ((and (atom (car L)) (= C X)) (cons (car L) (finder (cdr L) C X)))
        ((atom (car L)) (finder (cdr L) c X))
        (T (append (finder (car L) (+ 1 C) X) (apply #'append(mapcar (lambda (Y) (finder Y C X)) (list (cdr L))))))
    )
)
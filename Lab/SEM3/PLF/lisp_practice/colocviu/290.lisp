(defun height (L E D)
    (cond
	((null L) 0)
	((and (atom L) (equal L E)) D)
	((atom L) 0)
	(T (apply #'+ (mapcar (lambda (X) (height X E (+ D 1)))L)))
    )
)

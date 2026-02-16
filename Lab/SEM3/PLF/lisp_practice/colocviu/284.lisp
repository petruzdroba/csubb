(defun getter (L D)
	(cond
		((null L) nil)
		((and (atom L) (evenp D)) (list L))
		((atom L) nil)
		(T (mapcan (lambda (X) (getter X (+ 1 D))) L))
	)
)

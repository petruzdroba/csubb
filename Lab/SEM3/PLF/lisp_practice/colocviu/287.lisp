(defun elimina (L D)
	(cond
		((null L) nil)
		((and (numberp L) (evenp L) (oddp D)) nil)
		((atom L) (list L))
		(T(list (mapcan (lambda (X) (elimina X (+ 1 D))) L)))
	)
)

(defun main_el(L)
	(car (elimina L 0))
)

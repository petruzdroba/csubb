(defun elimina (L E)
	(cond
		((null L) nil)
		((and (atom L) (equal L E)) nil)
		((atom L) (list L))
		(T (list (mapcan (lambda (X) (elimina X E)) L)))
	)
)

(defun main_el(L E)
	(car (elimina L E))
)

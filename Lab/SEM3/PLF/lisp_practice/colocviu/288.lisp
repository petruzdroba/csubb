(defun my-or(L)
	(cond
		((null L) nil)
		((equal (car L) T) T)
		(T (my-or (cdr L)))
	)
)


(defun existor (L X D)
  (cond ((null L) nil)
        ((and (atom L) (equal L X) (evenp D)) T)
        ((atom L) nil)
        (T (funcall #'my-or (mapcar (lambda (Y) (existor Y X (+ 1 D))) L)))
   )
)

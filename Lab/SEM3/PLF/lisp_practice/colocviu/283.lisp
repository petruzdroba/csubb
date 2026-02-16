(defun swapper (L E1 E2)
	(cond
		((null L) nil)
		((and (atom L) (equal L E1)) E2)
		((atom L) L)
		(T (mapcar (lambda (X) (swapper X E1 E2)) L))
	)
)

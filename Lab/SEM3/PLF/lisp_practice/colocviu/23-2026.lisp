(defun finder (L K)
	(cond
		((null L) 0)
		((and (atom L) (equal L K)) 1)
		((atom L) 0)
		(T (apply #'+ (mapcar (lambda (X) (finder X K)) L)))
	)
)

(defun mainer (L K)
	(cond
		((> (finder L K) 0) T)
		(T nil)
	)
)

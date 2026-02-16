(defun finder (L K D)
	(cond
		((null L) nil)
		((and (atom L) (= K D)) (list L))
		((atom L) nil)
		(T (mapcan (lambda (X) (finder X K (+ 1 D))) L))
	)
)

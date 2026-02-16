(defun counter (L K D)
	(cond
		((null L) 0)
		((and (atom L) (= D K)) 1)
		((atom L) 0)
		(T (apply #'+ (mapcar (lambda (X) (counter X K (+ 1 D))) L)))
	)
)

(defun counter_main(L K) (counter L K -1))

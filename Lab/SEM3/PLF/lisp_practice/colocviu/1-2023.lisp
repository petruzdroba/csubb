(defun swapper (L E D)
	(cond
		((null L) nil)
		((and (atom L) (oddp D)) E)
		((atom L) L)
		(T (mapcar (lambda (X) (swapper X E (+ 1 D))) L))
	)
)

(defun main_s(L E) (swapper L E -1))

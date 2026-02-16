(defun swapper (L K E D)
	(cond
		((null L) nil)
		((and (atom L) (= K D)) E)
		((atom L) L)
		(T (mapcar (lambda (X) (swapper X K E (+ 1 D)) ) L))
	)
)

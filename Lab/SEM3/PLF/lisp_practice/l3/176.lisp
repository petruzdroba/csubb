(defun swapper (L)
    (cond
        ((and (numberp L) (evenp L)) (+ 1 L))
        ((listp L) (mapcar #'swapper L))
        (T L)
    )
)
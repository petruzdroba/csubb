#|
    6. Sa se construiasca o functie care intoarce produsul atomilor numerici
    dintr-o lista, de la orice nivel

    in(l1...ln)={
        1 daca n=0
    }

    produs(l1...ln)={
        1 daca  n=0
        l1*produs(l2...ln)  daca    e_numar(l1)
        inmultirea tuturor ( de la i la n de produs de li)
    }
|#

(defun produs (L)
    (cond
        ((null L) 1)
        ((numberp (car L)) (* (car L) (produs (cdr L))))
        (T (produs (mapcar #'produs L)))
    )
)
#|
    15. Sa se construiasca o functie care intoarce numarul atomilor dintr-o
    lista, dela orice nivel 

    suma(l1...ln)={
        0  daca     n=0
        l1+suma(l2...ln)    altfel
    }

    numar(l1...ln)={
        0   daca    n=0
        1+numar(l2...ln)  daca    e_atom(l1)
        (suma de la i la n de numar de li) altfel
    }
|#

(defun suma (L)
    (cond
        ((null L) 0)
        (T (+ (car L) (suma (cdr L))))
    )
)

(defun numar (L)
    (cond
        ((null L) 0)
        ((atom (car L)) (+ 1 (numar (cdr L))))
        (T (suma (mapcar #'numar L)))
    )
)
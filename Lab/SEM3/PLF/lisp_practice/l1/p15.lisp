#|
    15a Sa se scrie o functie care intoarce reuniunea a doua multimi.

    exista(e, l1...ln)={
        0   daca    n=0
        1   daca l1=e
        exista(e, l2...ln)  altfel   
    }

    reuniune(l1...ln, j1...jm)={
        j   daca    n=0
        l1(+)reuniune(l2...ln, j1...jm)     daca    exista(l1, j1...jm)=0
        reuniune(l2...ln, j1...jm)      altfel
    }
|#

(defun exista (E L)
    (cond
        ((null L) 0)
        ((equal (car L) E) 1)
        (T (exista E (cdr L)))
    )
)

(defun reuniune (L J)
    (cond
        ((null L) J)
        ((equal (exista (car L) J) 0) (cons (car L) (reuniune (cdr L) J)))
        (T (reuniune (cdr L) J))
    )
)

#|
    15b Sa se construiasca o functie care intoarce produsul atomilor numerici
    dintr-o lista, de la orice nivel

    produs(l1...ln)={
        1   daca    n=0
        l1 * produs(l2...ln)    daca    e_numar(l1)
        produs(l1) * produs(l2...ln)    daca    e_lista(l1)
        produs(l2...ln)     altfel
    }
|#

(defun produs (L)
    (cond
        ((null L) 1)
        ((numberp (car L)) (* (car L) (produs (cdr L))))
        ((listp (car L)) (* (produs (car L)) (produs (cdr L))))
        (T (produs (cdr L)))
    )
)

#|
    15c) Definiti o functie care sorteaza cu pastrarea dublurilor o lista liniara.

    insert(e, l1...ln)={
        [e]  daca    n=0
        e(+)l1...ln  daca    e<=l1
        l1(+)insert(e,l2...ln)  altfel
    }

    sort(l1...ln)={
        []  daca    n=0
        insert(l1, sort(l2...ln))
    }
    -> practic i fiecare element si il pune in pozitia corecta relativ de restul elementelor
|#

(defun insereaza (E L)
    (cond
        ((null L) (list E))
        ((<= E (car L)) (cons E L))
        (T (cons (car L) (insereaza E (cdr L))))
    )
)

(defun sorteaza (L)
    (cond
        ((null L) nil)
        (T (insereaza (car L) (sorteaza (cdr L))))
    )
)

#|
    15d Definiti o functie care construiește o listă cu pozițiile elementului
    minim dintr-o listă liniară numerică.

    minim(l1...ln, e)={
        e   daca n=0
        minim(l2...ln, l1)  daca   l1 < e
        minim(l2...ln, e)   altfel
    }

    poz(l1...ln, curent, min)={
        []  daca    n=0
        curent(+)poz(l2...ln, curent+1, min)    daca    min=l1
        poz(l2...ln, curent+1, min)         altfel
    }   

    poz_min(l1...ln)={
        poz(l1...ln, 1, minim(l2...ln, l1))
    }
|#

(defun minim (L E)
    (cond
        ((null L) E)
        ((< (car L) E) (minim (cdr L) (car L)))
        (T (minim (cdr L) E))
    )
)

(defun poz (L C E)
    (cond
        ((null L) nil)
        ((equal (car L) E) (cons C (poz (cdr L) (+ 1 C) E)))
        (T (poz (cdr L) (+ 1 C) E))
    )
)

(defun poz_min (L)
    (poz L 1 (minim (cdr L) (car L)))
)
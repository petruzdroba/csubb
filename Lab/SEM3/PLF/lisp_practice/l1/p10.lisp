#|
    10a .Sa se construiasca o functie care intoarce produsul atomilor numerici
    dintr-o lista, de la nivelul superficial.

    suma(l1...ln)={
        0   daca    n=0
        l1+suma(l2...ln)    daca    e_numar(l1)
        suma(l2...ln)   altfel
    }
 |#

 (defun suma (L)
    (cond
        ((null L) 0)
        ((numberp (car L)) (+ (car L) (suma (cdr L))))
        (T (suma (cdr L)))
    )
 )

 #|
    10b.Sa se scrie o functie care, primind o lista, intoarce multimea tuturor
    perechilor din lista. De exemplu: (a b c d) --> ((a b) (a c) (a d)(b c) (b
    d) (c d))

    tot(a, b1...bn)={
        []  daca    n=0
        [a, b1] (+)tot(a, b2...bn)
    }

    perechi(l1...ln)={
        []  daca    n=1
        tot(l1, l2...ln)(+)perechi(l2...ln) altfel
    }
  |#

  (defun tot (A B)
    (cond
        ((null B) nil)
        (T (cons (list A (car B)) (tot A (cdr B))))
    )
  )

  (defun perechi (L)
    (cond
        ((null (cadr L)) nil)
        (T (cons (tot (car L) (cdr L)) (perechi (cdr L))) )
    )
  )

  #|
    10 d.Definiti o functie care, dintr-o lista de atomi, produce o lista de
    perechi (atom n), unde atom apare in lista initiala de n ori. De ex:
    (A B A B A C A) --> ((A 4) (B 2) (C 1)).

    counter(e, l1...ln)={
        0   daca    n=0
        1+counter(e, l2...ln)   daca    l1=e
        counter(e, l2...ln)     altfel
    }

    elimina(e, l1...ln)={
        []  daca    n=0
        elimina(e, l2...ln)     daca    e=l1
        l1(+)elimina(e, l2...ln)    altfel
    }

    perechi2(l1...ln)={
        []  daca    n=0
        [l1, counter(l1, l1...ln)] (+) perechi2(elimina(l1, l1...ln))   altfel
    }
   |#

(defun counter (E L)
    (cond
        ((null L) 0)
        ((equal (car L) E) (+ 1 (counter E (cdr L))))
        (T (counter E (cdr L)))
    )
)

(defun elimina(E L)
    (cond
        ((null L) nil)
        ((equal (car L) E) (elimina E (cdr L)))
        (T (cons (car L) (elimina E (cdr L))))
    )
)

(defun perechi2 (L)
    (cond
        ((null L) nil)
        (T (cons (list (car L) (counter (car L) L)) (perechi2 (elimina (car L) L))))
    )
)
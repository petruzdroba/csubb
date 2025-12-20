#|
    (A (B) (C (D) (E)))
    p11.Se da un arbore de tipul (2). Sa se afiseze nivelul (si lista corespunzatoare a nodurilor)
    avand numar maxim de noduri. Nivelul rad. este 0

    Test:
        (main '(A (B (E)) (C) (D (F) (G)))) -> (1 (B C D))
        (main '(A (B (C (D))) (X (Y Z)))) -> (2 (C Y Z))
        (main '(ROOT (A (B (C (D (E (F (G (H (I (J)))))))))) (K)))  -> (1 (A K))
|#

(defun my-max (a b)
    (cond
        ((>= a b) a)
        (t b)
    )
)

#|
    daca primul element e lista (adica are copii) se calculeaza inaltimea lor
    "practic" la final se returneaza adancimea maxima

    face recursiv maximul dintre toti subarborii din arborele principal

    inaltime(l1...lm) = {
        0                                               daca m = 0
        0                                               daca atom(l1)
        1 + my-max(inaltime(l1), inaltime(l2...lm))    altfel
    }
|#
(defun inaltime (L)
  (cond
    ((null L) 0)
    ((atom L) 0)
    (t (+ 1 (my-max (inaltime (car L)) (inaltime (cdr L)))))
  )
)

#|
    returneaza numarul de noduri de la un nivel dat
    t- nivel target, c - nivel curent
    nr_noduri_at(l1...ln, t, c) = {
        0                                                   daca n = 0
        1                                                   daca atom(l1) si c = t
        0                                                   daca atom(l1) si c != t
        nr_noduri_at(l1, t, c+1) + nr_noduri_at(l2...ln, t, c)   altfel
    }
    remove evenp for all 
|#
(defun nr_noduri_at (L target current)
    (cond
        ((null L) 0)
        ((atom L) (if( and (evenp L) (= current target)) 1 0)) 
        (t (+ (nr_noduri_at (car L) target (+ current 1))
              (nr_noduri_at (cdr L) target current)))
    )
)

#|
    returneaza nivelul cu cele mai multe noduri
    m- max curent
    c- nivelul curent
    n-inaltimea arborelui

    max_lvl_nod(l1...lk, n, c, m) = {
        m   daca c > n
        max_lvl_nod(l1...lk, n, c+1, c)    daca nr_noduri_at(l1...lk, c, -1) > nr_noduri_at(l1...lk, m, -1)
        max_lvl_nod(l1...lk, n, c+1, m)    altfel
    }
|#
(defun max_lvl_nod (L height curent maxi)
    (cond
        ((> curent height) maxi)
        ((> (nr_noduri_at L curent -1) (nr_noduri_at L maxi -1)) (max_lvl_nod L height (+ 1 curent) curent))
        (t (max_lvl_nod L height (+ 1 curent) maxi))
    )
)

#|
    returneaza nodurile de la un nivel dat
    noduri_at(l1...ln, t, c) = {
        []                                          daca n = 0
        [l1]                                        daca atom(l1) si c = t
        []                                          daca atom(l1) si c != t
        noduri_at(l1, t, c+1) (+) noduri_at(l2...ln, t, c)    altfel
    }
|#
(defun noduri_at (L target curent)
    (cond
        ((null L) nil)
        ((atom L) (if (= curent target) (list L) nil))
        (t (append (noduri_at (car L) target (+ curent 1))
                   (noduri_at (cdr L) target curent)))
    )
)

#|
    main(l1...ln) = {
        [max_lvl_nod(l1...ln, inaltime(l1...ln), 0, 0), noduri_at(l1...ln, max_lvl_nod(l1...ln, inaltime(l1...ln), 0, 0), -1)]
    }

    -returneaza o lista in care primul element este nivelul , iar al doilea element este alta lista cre reprezinta 
    nodurile de la nivelul respectiv
|#
(defun main (L)
    (list
        (max_lvl_nod L (inaltime L) 0 0)
        (noduri_at L (max_lvl_nod L (inaltime L) 0 0) -1)
    )
)
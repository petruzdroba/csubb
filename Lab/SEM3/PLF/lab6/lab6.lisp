#| 2.
        Definiti o functie care obtine dintr-o lista data lista tuturor atomilor
        care apar, pe orice nivel, dar in aceeasi ordine. De exemplu
        (((A B) C) (D E)) --> (A B C D E)

        Model matematic:

            flatten(l1...ln)={
                [l]      daca    l e atom
                reunine de la i la n de flatten(li)     atfel
            }

        Cazuri de testare:
            (flatten '(((A B) C)(D E)))    ->      (A B C D E)
            (flatten '((((A)))))           ->      (A)
            (flatten '(1 (2 (4) (5(6 (7 (8 (9) (10)))))) (3 (11) (12 (13 (15) (16)) (14 (17 (18 (19) (20))))))) )
             -> (1 2 4 5 6 7 8 9 10 3 11 12 13 15 16 14 17 18 19 20)
 |#

 (defun flatten (L)
    (cond
        ((atom L) (list L))
        (T (apply #' append (mapcar #' flatten L)))
    )
 )
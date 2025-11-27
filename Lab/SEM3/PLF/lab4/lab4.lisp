#|
   14a -> elimina elemente din N in N
   Model matematic:
       elimina(l1...lm,i , n)={
            []  daca    m=0
            elimina(l2...lm, 1,n)     daca    i=n
            l1(+)elimina(l2...lm, i+1, n)       altfel
       }

       main_elimina(l1...lm, n)={
            elimina(l1...lm, 1, n)
       }

    Cazuri de testare:
        (main_elimina '(0 1 0 1 0 1 0 1) 2) -> [0 0 0 0]
        (main_elimina '(1 2 3 4 5 6 7 8 9) 1) -> NIL
        (main_elimina '(1 2 3 4 5 6 7 8 9) 3) -> [1 2 4 5 7 8]
 |#

(defun elimina (l i n)
    (cond
        ((null l) nil)
        ((= i n) (elimina (cdr l) 1 n))
        (T (cons (car l) (elimina (cdr l) (+ i 1) n)))
    )
)

(defun main_elimina (l n)
    (elimina l 1 n))


#|
    14b -> aspect de vale
    Model matematic:
        vale(l1...ln, f) = {
            1                        if n = 1 and f = 1
            0                        if l2 = l1
            vale(l2...ln, 1)         if l2 > l1 and f = 0
            vale(l2...ln, 0)         if l2 < l1 and f = 0
            0                        if l2 < l1 and f = 1
            vale(l2...ln, 1)         if l2 > l1 and f = 1
        }

        main_vale(l1...ln)={
            0   daca    l1<l2
            vale(l1...ln, 0)
        }

    Cazuri de testare:
        (main_vale '(1 2 3 4)) -> 0
        (main_vale '(4 3 2 1)) -> 0
        (main_vale '(4 3 2 1 5)) -> 1

        T - true , Nil - False
 |#

(defun vale (lista flag)
    (cond
        ((null (cdr lista)) (if (= flag 1) 1 0))
        ((= (cadr lista) (car lista)) 0)
        ((and (= flag 0) (> (cadr lista) (car lista))) (vale (cdr lista) 1))
        ((and (= flag 0) (< (cadr lista) (car lista))) (vale (cdr lista) 0))
        ((and (= flag 1) (< (cadr lista) (car lista))) 0)
        (T (vale (cdr lista) 1))
    )
)

(defun main_vale (lista)
    (if (< (car lista) (cadr lista))
        0
        (vale lista 0)))

#|
    14 c -> numarul atomilor numerici minimi dintr-o lista

    Model matematic:
        flatten(l)={ si elimina atomii nenumerici din lista
            []      daca    n=0
            []      daca    atom(l) si numar(l)= 0
            [l]    daca    n=1 si atom(l)
            flatten(l)(+)flatten(l2...ln)  daca    lista(l)
        }

        minim(e, l1...ln)={
            e   daca    n=0
            minim(l1, l2...ln)      daca    e>l1
            minim(e, l2...ln)
        }

        count(e, l1...ln)={
            0      daca     n=0
            1+(e,l2...ln)   daca    e=l1
            (e, l2...ln)    altfel
        }

        min_count(l1...ln)={
            x1...xn = flatten(l1...ln)
            count(minim(x1...xn), x1...xn)
        }
    
    Cazuri de testare:
        ( min_count '(1 2 3 4 5 6 7)) -> 1
        ( min_count '(1 2 3 (1 2) ((1) 1 2) ) ) -> 4
        ( min_count '(((((1)))))) -> 1

 |#

 (defun flatten (lst)
    (cond
        ((null lst) nil)
        ((and (atom lst) (not (numberp lst))) nil)
        ((atom lst) (list lst))
        (T (append ( flatten (car lst)) (flatten (cdr lst))))
    )
 )

 (defun minim (elem list)
    (cond
        ((null list) elem)
        ((< (car list) elem) (minim (car list) (cdr list)))
        (T (minim elem (cdr list)))
    )
 )

 (defun count_e (elem list)
    (cond
        ((null list) 0)
        ((= elem (car list)) (+ 1 (count_e elem (cdr list))))
        (T (count_e elem (cdr list)))
    )
 )

 (defun min_count (list)
    (let ((flattened (flatten list)))
        ( count_e (minim (car flattened) (cdr flattened)) flattened))
 )

#|
    14 d -> sterge dintr-o lista liniara toate aparitiile elementului maxim

    Model matematic:
        maxim(e, l1...ln)={
			e	daca	n=0
            maxim(e, l2...ln)   dac     numar(l1)=0
			maxim(l1, l2...ln)	daca	l1 > e
			maxim(e, l2...ln)	altfel
		}

        elimina2(l1...ln, e)={
			[]	daca	n=0
            l1(+)elimina2(l2...ln, 2)   numar(l1)=0
			elimina2(l2...ln,e)	daca	l1=e
			l1(+)elimina2(l2...ln,e)		altfel	
		}

		main_elimina2(l1...ln)={
            l1(+)main_elimina2(l2...ln)     daca    numar(l1)=0
			elimina2(l1...ln, maxim(l1, l2...ln))
		}

    Cazuri de testare:
        (main_elimina2 '(1 2 3 4 5 6 7)) -> [1 2 3 4 5 6]
        (main_elimina2 '(5 5 5 5 5)) -> NIL
        (main_elimina2 '(6 1 6 2 6 3 6 4 6 5 6 6)) -> [1 2 3 4 5]

 |#

 (defun maxim (elem list) 
    (cond
        ((null list) elem)
        ((not (numberp (car list))) (maxim elem (cdr list)))
        ((< elem (car list)) (maxim (car list) (cdr list)))
        (T (maxim elem (cdr list)))
    )
 )

 (defun elimina2 (list elem)
    (cond 
        ((null list) nil)
        ((not (numberp (car list))) (cons (car list) (elimina2 (cdr list) elem)))
        ((= elem (car list)) (elimina2 (cdr list) elem))
        (T (cons (car list) (elimina2(cdr list) elem)))
    )
 )

 (defun main_elimina2 (list)
    (cond
        ((not (numberp (car list))) (cons (car list) (main_elimina2(cdr list))))
        (T(elimina2 list (maxim (car list) (cdr list))))
    )
 )
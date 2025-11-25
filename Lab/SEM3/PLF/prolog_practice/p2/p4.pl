/*
    4a. Sa see interclaseze 2 liste sortate, fara pastrarea dublurilor
    [1,2,3,4], [2,3,4] -> [1,2,3,4]

    interclaseaza(X:lista, Y:lista, R:lista)

    X: lista 1 de input
    Y: lista 2 de input
    R: lista rezultata

    Model matematic:

        interclaseaza(x1...xn, y1...ym)={
            []      daca    n=0 si m=0
            x1(+)interclaseaza(x2...xn, y1...ym)    daca    m=0
            x1(+)interclaseaza(x2...xn, y1...ym)    daca    x1>y1
            y1(+)interclaseaza(x1...xn, y2...ym)    daca    n=0
            y1(+)interclaseaza(x1...xn, y2...ym)    daca    x1<y1
            y1(+)interclaseaza(x2...xn, y2...ym)    daca    x1=y1
        }
*/

interclaseaza([],[],[]):-!.
interclaseaza([X|XT], [],[X|R]):- interclaseaza(XT,[],R).
interclaseaza([], [Y|YT], [Y|R]):- interclaseaza([], YT,R).
interclaseaza([X|XT], [Y|YT],[Y|R]):- X>Y,!, interclaseaza([X|XT],YT,R).
interclaseaza([X|XT], [Y|YT],[X|R]):- X<Y,!, interclaseaza(XT,[Y|YT],R).
interclaseaza([X|XT], [Y|YT],[Y|R]):- X=Y,!, interclaseaza(XT,YT,R).
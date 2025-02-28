"""
Se da o lista de numere. Afisati toate posibilitatile de a insera operatorul
+ sau - intre numere incat rez sa fie pozitiv
0 inseamna -
1 inseamna + 
in lista de semne

care e spatiul de cautare
    o lista de n elemente unde xi apartine [0,1]
    [0,1]1 x [0,1]2 x ... x [0,1]n
    produs cartezian de [0,1]i de la i = 1 la n
ce e candidat
    suma listei de numere (cu tot cu semne) este pozitiva
    [x1, ... xk], xi apartine [0,1], cu suma de la i= 1 la k pozitiva
conditie de solutie
    lista de semne este candidat si are lungimea listei de numere
    [x1, ... xn], n - lungimea listei de numere cu, xi apartine [0,1] suma de la i=1 la n poz
"""


def e_solutie(lista_numere: list, lista_semne: list) -> bool:
    return len(lista_numere) == len(lista_semne)


def e_consistent(lista_numere: list, lista_semne: list) -> bool:
    sum = 0
    for index in range(0, len(lista_semne)):
        if lista_semne[index]:
            sum += lista_numere[index]
        else:
            sum += (-1) * lista_numere[index]
    return sum >= 0


def afiseaza(lista_numere: list, lista_semne: list) -> None:
    sum = 0
    sum_string = ""
    for index in range(0, len(lista_semne)):
        if not lista_semne[index]:
            sum += (-1) * lista_numere[index]
            if lista_numere[index] >= 0:
                sum_string += f"-{lista_numere[index]}"  # semn negativ nr poz
            else:
                sum_string += f"+{abs(lista_numere[index])}"  # semn neg nr neg
        else:
            sum += lista_numere[index]
            if lista_numere[index] >= 0:
                sum_string += f"+{lista_numere[index]}"  # semn poz nr poz
            else:
                sum_string += f"{lista_numere[index]}"  # semn neg nr neg
    sum_string += f" = {sum}"
    print(sum_string)


def backtracking(lista_numere: list, lista_semne: list) -> None:
    if e_solutie(lista_numere, lista_semne):
        if e_consistent(lista_numere, lista_semne):
            afiseaza(lista_numere, lista_semne)
        return

    for sign in [0, 1]:
        lista_semne.append(sign)
        backtracking(lista_numere, lista_semne)
        lista_semne.pop()


def citire_lista(lista_numere: list, dimensiune: int) -> None:
    for index in range(0, dimensiune):
        lista_numere.append(int(input(f"{index + 1}>>>")))


def main():
    n = int(input("Dimensiune lista>>>"))
    lista_numere = []
    citire_lista(lista_numere, n)
    backtracking(lista_numere, [])


main()

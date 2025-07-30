def validate_name_input(nume: str):
    """
    Functie care valideaza numele introdus de un utilizator
    input:
        nume:string
    output:
        nimic daca e ok
        raise ValueError cu mesajul "nume invalid"
    """
    if nume == "":
        raise ValueError("nume invalid")


def validate_score_input(score: int):
    """
    Functie care valideaza un scor introdus de la tastatura de un utilizator
    input:
        score : int
    output:
        nimic daca e ok
        raise ValueError cu mesajul "scor invalid"
    """
    if score < 0 or score > 10:
        raise ValueError("scor invalid")


def validate_position_input(position: int):
    """
    Functie care valideaza o pozitie data de la tastatura, pentru o etapa din concurs
    input:
        position:int
    output:
        nimic daca e ok
        raise ValueError cu mesajul "pozitie invalida"
    """
    if position < 0 or position > 10:
        raise ValueError("scor invalid")


def validate_interval_input(user_list: list, start: int, end: int):
    """
    Functie care valideaza un interval date de la tastatura
    input:
        start : int
        end: int
        user_list : list
    output:
        nimic daca e ok
        rasie ValueError cu mesajul "interval invalid"
    """
    if start < 0 or start > len(user_list):
        raise ValueError("interval invalid")

    if end < 0 or end > len(user_list):
        raise ValueError("interval invalid")

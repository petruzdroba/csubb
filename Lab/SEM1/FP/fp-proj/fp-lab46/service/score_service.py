import service.user_service
import domain.getters


def add_score_to_user(user_list: list, score: int, name: str, postition: int):
    """
    Function that adds score : int to the name : dictionary to a specified position : int
    input:
        user_list : list - not empty
        score : int - >=0 and <= 10
        name : string - not empty
        position : int - >=0 and <= 10
    output:
        good : nothing
        error: ValueError with a latter added message
    """
    key_id = service.user_service.get_id_by_name(user_list, name)
    user_list[key_id]["scor"][postition] = score


def user_score_reset(user_list: list, id: int):
    """
    Functie reseteaza scorul pentru un utilizator dat de id la toate etapele
    input:
        user_list - lista
        id - int
    """
    user_list[id]["scor"] = [f"id:{id}", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]


def get_total_score_user(user_list: list, id: int):
    """
    Functie care returneaza scorul total a unui utilizator cautat dupa id
    input:
        user_list - lista
        id - int
    """
    score_total = 0

    for scor in range(1, 11):
        score_total += domain.getters.get_scor(user_list[id])[scor]

    return score_total

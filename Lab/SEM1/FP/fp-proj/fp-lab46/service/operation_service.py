import service.score_service
import domain.getters


def calculate_avg_score_interval(user_list: list, start: int, end: int):
    """
    Functie care calculeaza media scorurilor totale pe un interval de participanti inchis
    input:
        user_list : lista - nevida
        start : int - >= 0
        end : int - < len(user_list)
    output:
        returneaza media scorurilor de pe un iterval : float
    """
    total_interval_score = 0

    for index in range(start, end + 1):
        total_interval_score += service.score_service.get_total_score_user(
            user_list, index
        )

    return total_interval_score / (end - start + 1)


def calculate_minimum_total_score(user_list: list, start: int, end: int):
    """
    Functie care returneaza scorul minim de pe un interval dat inchis
    input:
        user_list : lista - nevida
        start : int - >= 0
        end : int - < len(user_list)
    output:
        returneaza cel mai mic scor total de pe intervalul dat : int
    """
    minimum_score = 101

    for index in range(start, end + 1):
        if service.score_service.get_total_score_user(user_list, index) < minimum_score:
            minimum_score = service.score_service.get_total_score_user(user_list, index)

    return minimum_score


def calculate_ids_ten_multiple(user_list, start, end):
    """
    Functie care returneaza o lista cu nume:nume si scor_total : scor_total cu participantii care au scorul total multiplu de 10
    input:
        user_list : lista - nevida
        start : int - >= 0
        end : int - < len(user_list)
    output:
        o lista cu id-uri : int care reprezinta userii
    """
    model_list = []

    for index in range(start, end + 1):
        total_score = service.score_service.get_total_score_user(user_list, index)
        if total_score % 10 == 0:
            model_list.append(
                {
                    "nume": domain.getters.get_name(user_list[index]),
                    "scor_total": total_score,
                }
            )

    return model_list

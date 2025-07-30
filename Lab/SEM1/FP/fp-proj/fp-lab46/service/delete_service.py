import ui.user_input
import ui.print_user_data

import service.score_service
import service.user_service


def delete_user_score(user_list: list, name: str, position: int):
    """
    Functie care sterge scorul de la o etapa a unui participant
    input:
        user_list - lista
    """

    deleted_score = user_list[service.user_service.get_id_by_name(user_list, name)][
        "scor"
    ][position]

    service.score_service.add_score_to_user(user_list, 0, name, position)

    return deleted_score


def delete_interval_score(user_list: list, start: int, end: int):
    """
    Functie care sterge scorul la toate etapele pentru participantii dintr-un interval
    input:
        user_list - lista
    """

    if start < 1:
        start = 1

    if end > len(user_list):
        end = len(user_list)

    for index in range(start, end):
        service.score_service.user_score_reset(user_list, index)
        ui.print_user_data.print_score_user_by_id(user_list, index)

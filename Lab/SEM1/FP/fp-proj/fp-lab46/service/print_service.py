import ui.print_user_data
import ui.user_input
import service.score_service

import domain.getters


def get_users_smallers(user_list: list, target_score: int):
    """
    Functie care returneaza participantii care au un scor mai mic decat un scor dat :target_score
    input:
        user_list : list
        target_score : int >=0
    output:
        o lista care contine numele participantiilor cu scorul mai mic decat target_score
    """

    smaller_score_users = []

    for user in user_list:
        total_score = service.score_service.get_total_score_user(
            user_list, domain.getters.get_id(user)
        )

        if total_score <= target_score:
            smaller_score_users.append(domain.getters.get_name(user))

    return smaller_score_users


def sort_user_total_scor(user_list: list):
    """
    Functie care returneaza o lista ordonata crescator cu participantii si scorul lor total
    input:
        user_list - lista
    output:
        o lista de tipul [{'name': name, 'scor_total' : 100}] sortata crescator dupa scor_total
    """

    user_model = []

    for user in user_list:
        total_score = service.score_service.get_total_score_user(
            user_list, user["key_id"]
        )
        user_model.append(
            {"nume": domain.getters.get_name(user), "scor_total": total_score}
        )

    n = len(user_model)

    for i in range(n):
        for j in range(0, n - i - 1):
            if user_model[j]["scor_total"] < user_model[j + 1]["scor_total"]:
                user_model[j], user_model[j + 1] = user_model[j + 1], user_model[j]

    return user_model


def print_users_bigger_score(user_list: list, target_score: int):
    """
    Functie care returneaza o lista ordonata crescator cu participantii care au un scor mai mare ca target_score
    input:
        user_list - lista
        target_score - int
    output:
        o lista de tipul [{'name': name, 'scor_total' : 100}] sortata crescator dupa scor_total si scor_total >= target_score
    """
    user_model = []

    for user in user_list:
        total_score = service.score_service.get_total_score_user(
            user_list, user["key_id"]
        )
        if total_score >= target_score:
            user_model.append(
                {"nume": domain.getters.get_name(user), "scor_total": total_score}
            )

    n = len(user_model)

    for i in range(n):
        for j in range(0, n - i - 1):
            if user_model[j]["scor_total"] < user_model[j + 1]["scor_total"]:
                user_model[j], user_model[j + 1] = user_model[j + 1], user_model[j]

    return user_model

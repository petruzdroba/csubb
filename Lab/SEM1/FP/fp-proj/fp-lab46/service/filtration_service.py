import service.score_service
import domain.getters


def filter_users_multi_score(user_list: list, target_score: int):
    """
    Functie care "elimina" participantii cu scoruri multiple de un numar dat :target_score
    input:
        user_list : list
        target_score: int , >=0
    output:
        o lista de utilizatori care nu ii contin pe cei care au un scor multiplu de un numar dat
    """
    filtered_list = []
    for user in user_list:
        total_score = service.score_service.get_total_score_user(
            user_list, domain.getters.get_id(user)
        )
        if total_score % target_score != 0:
            filtered_list.append(user)

    return filtered_list


def filter_users_smaller_score(user_list: list, target_score: int):
    """
    Functie care "elimina" participantii cu scoruri mai mici ca unul dat : target_score
    input:
        user_list : list
        target_score: int , >=0
    output:
        o lista de utilizatori care nu ii contin pe cei care au scorul total mai mic ca target_scor
    """
    filtered_list = []
    for user in user_list:
        total_score = service.score_service.get_total_score_user(
            user_list, domain.getters.get_id(user)
        )
        if total_score > target_score:
            filtered_list.append(user)

    return filtered_list

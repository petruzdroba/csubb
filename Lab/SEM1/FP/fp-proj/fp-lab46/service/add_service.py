import service.user_service
import ui.user_input
import ui.print_user_data

import service.score_service

import repository.user_repo


def add_user(user_list: list, name: str):
    """
    Functie care adauga un participant nou cu numele dat de la tastatura
    input:
        user_list - lista
    """

    repository.user_repo.add_user(user_list, name)


def add_score_insert(user_list: list, name: str, scor: int, position: int):
    """
    Functie care insereaza un scor unui participant pentru o pozitie data
    input:
        user_list - lista
    """

    service.score_service.add_score_to_user(
        user_list,
        scor,
        name,
        position,
    )


def change_user_score_completely(user_list: list, name: str):
    """
    Functie care schimba scorul pentru un participant la toate etapele
    input:
        user_list - lista
    """

    forgotten_list = []

    for etapa in range(1, 11):
        forgotten_list.append(
            user_list[service.user_service.get_id_by_name(user_list, name)]["scor"][
                etapa
            ]
        )
        service.score_service.add_score_to_user(
            user_list, ui.user_input.input_score(f"Etapa {etapa}"), name, etapa
        )

    return forgotten_list

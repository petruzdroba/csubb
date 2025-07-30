import service
import service.add_service
import service.user_service


def add_function_to_call_stack(call_stack: list, reverse_function: str, arguments):
    """
    Functie care adauga la lista functilor apelate call_stack, ultima functie apelata de ui care a schimbat lista
    de date
    Probabil a sa contina doar functiile de revert, cu datele necesare, gata sa fie apelate
    input:
        call_stack : list
        called_function : function
    output:
        nimic daca totul e ok
        raise ValueError cu mesajul "appending to call_stack failed"
    """
    # note to self : use __name__ to get a functions name as a string
    call_stack.append((reverse_function, arguments))


def undo(call_stack: list, user_list: list):
    """
    Functie care apeleaza ultima functie adaugata (practic reverseul la ultima functie apelata) din call_stack
    si dupa o elimina
    """
    if not len(call_stack):
        return

    undo_function, arguments = call_stack.pop()

    if undo_function == "add_user":
        user_list.pop()
    elif undo_function == "add_score_insert":
        name, position = arguments
        user_list[service.user_service.get_id_by_name(user_list, name)]["scor"][
            position
        ] = 0
    elif undo_function == "sterge_scor":
        name, score, position = arguments
        service.add_service.add_score_insert(user_list, name, score, position)

import domain.getters


def create_user(user_list: list, name: str):
    """
    Functie care returneaza un user cu numele nume
    input:
        user_list - lista
        name - string
    output:
        un user cu nume nume id key_id si scor scor
    """
    if name == "":
        raise ValueError("Name is empty")
    user = {
        "key_id": len(user_list),
        "nume": name,
        "scor": [f"id:{len(user_list)}", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    }
    return user


def get_id_by_name(user_list: list, name: str):
    """
    Functie care returneaza id-ul unui participant cu numele nume
    input:
        user_list - lista
        nume - string
    output:
        id_ul tip int utilizatorului cu numele nume
    """

    found = False

    for user in user_list:
        if domain.getters.get_name(user).upper() == name.upper():
            return domain.getters.get_id(user)
    if not found:
        raise ValueError("User not found")

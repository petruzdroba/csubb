import service.user_service

def add_user_file(name):
    """
    Function that adds an user to the file_dataparticipants.txt file
    input:
        name : string - not empty
    output:
        nothing if checks out
        if not : raises ValueError with text : f"failed to add {name} to file"
    """
    data_file = open('file_data_participanti.txt', 'a')
    data_file.write(f"\n{name}")
    data_file.close()
    data_file = open('file_data_participanti.txt', 'r')

def add_user(user_list, name):
    """
    Function that adds user to our list
    input:
        user_list : list - not empty
        name : string -  not empty
        score : to be inserted later if used by menu 1 1
        key id : added automatically by counter
    output:
        nothing if everything checks out
        error : raises ValueError with one of these:
        "Name cannot be empty"
        "id index out of range"
        "counter out of range"
    """
    user_model = service.user_service.create_user(user_list , name)
    user_list.append(user_model)
    add_user_file(name)
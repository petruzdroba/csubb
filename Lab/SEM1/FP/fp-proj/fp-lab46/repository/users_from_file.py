# {'name', 'nr_concurs', '[scor1, scor 2 etc]'}


def read_user_data(user_list, file_directory):
    """
    Function that reads user data from text file
    This should only be called once at the beginning of runtime
    input:
        user_data : list of dictionary
    output:
        nothing if checks out
        error: ValueError with the text tbr
    """
    data_file = open(file_directory, "r")

    id_assigner = 0
    for line in data_file:
        user_model = {
            "key_id": id_assigner,
            "nume": line.strip(),
            "scor": [f"id:{id_assigner}", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        }
        id_assigner += 1
        user_list.append(user_model)

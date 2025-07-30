import domain.getters


def print_all_user_names(user_list):
    """
    Function that prints all the users names
    input:
        user_list : list - not empty
    output:
        all the names if checks out
        error : ValueError text : "No users exist"
    """
    counter = 0

    for user in user_list:
        print(f"{counter}. {domain.getters.get_name(user)}")
        counter += 1


def print_score_user_by_id(user_list, id):
    user_score = []
    for score in domain.getters.get_scor(user_list[id]):
        user_score.append(score)
    print(user_score)


def print_user_smaller_score(user_list):
    print(user_list)


def print_users_score_total(model_list):
    for user in model_list:
        print(f"{user["nume"]} - {user["scor_total"]} pts.")

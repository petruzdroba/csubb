import domain.validators


def input_name(message):
    new_user_name = input(f"{message} \n>>>")

    domain.validators.validate_name_input(new_user_name)

    return new_user_name


def input_score(message):
    new_score = int(input(f"{message}>>>"))

    domain.validators.validate_score_input(new_score)

    return new_score


def input_position():
    position = int(input("La ce proba doriti sa inserati scorul (1-10) ? \n>>>"))

    domain.validators.validate_position_input(position)

    return position


def input_interval(user_list):
    start = int(input("Introduceti inceputul intervalului: \n>>>"))

    end = int(input("Indroduceti finalul intervalului: \n>>>"))

    domain.validators.validate_interval_input(user_list, start, end)

    return start, end


def input_interval_number(user_list):
    start = int(input("Introduceti inceputul intervalului: \n>>>"))

    end = int(input("Indroduceti finalul intervalului: \n>>>"))

    # domain.validators.validate_interval_input(user_list, start, end)

    return start, end

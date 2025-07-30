import service.add_service
import service.delete_service
import service.filtration_service
import service.operation_service
import service.print_service

import service.undo_service
import ui.print_user_data
import ui.user_input


def main_menu_console(user_list, call_stack):
    print("1. Adauga un participanti si scoruri")
    print("2. Modificare scor")
    print("3. Tipareste lista de participanti")
    print("4. Operatii pe un subset de participanti")
    print("5. Filtrare")
    print("6. Undo")
    print("7. Exit")

    choice = int(input(">>>"))
    if choice == 1:
        menu_console_1(user_list, call_stack)
        main_menu_console(user_list, call_stack)
    elif choice == 2:
        menu_console_2(user_list, call_stack)
        main_menu_console(user_list, call_stack)
    elif choice == 3:
        menu_console_3(user_list)
        main_menu_console(user_list, call_stack)
    elif choice == 4:
        menu_console_4(user_list)
        main_menu_console(user_list, call_stack)
    elif choice == 5:
        menu_console_5(user_list)
        main_menu_console(user_list, call_stack)
    elif choice == 6:
        service.undo_service.undo(call_stack, user_list)
        main_menu_console(user_list, call_stack)
    else:
        return 0


def menu_console_1(user_list, call_stack):
    print("1. Adauga participant+scor")
    print("2. Inserare scor participant")
    print("3. Exit")
    choice = int(input(">>>"))
    if choice == 1:
        name = ui.user_input.input_name("Numele noului participant")
        position = ui.user_input.input_position()

        service.add_service.add_user(user_list, name)

        service.score_service.add_score_to_user(
            user_list,
            ui.user_input.input_score("Scor nou"),
            name,
            position,
        )

        print(f"Scorurile lui {name} sunt :")
        ui.print_user_data.print_score_user_by_id(
            user_list, service.user_service.get_id_by_name(user_list, name)
        )

        service.undo_service.add_function_to_call_stack(call_stack, "add_user", name)

        service.undo_service.add_function_to_call_stack(
            call_stack, "add_score_insert", (name, position)
        )
    elif choice == 2:
        name = ui.user_input.input_name("Cui doriti sa ii modificati scorul?")

        scor = ui.user_input.input_score("Scor nou")
        position = ui.user_input.input_score(
            "Pe ce pozitie doriti sa adaugati scorul ?"
        )

        service.add_service.add_score_insert(user_list, name, scor, position)

        print(
            f"Scorurile lui {name} sunt : {ui.print_user_data.print_score_user_by_id(user_list,service.user_service.get_id_by_name(user_list, name))}"
        )

        service.undo_service.add_function_to_call_stack(
            call_stack, "add_score_insert", (name, position)
        )
    else:
        return 0


def menu_console_2(user_list, call_stack):
    print("1. Sterge scorul pentru un participant")
    print("2. Sterge scorul pe un interval de la un participant")
    print("3. Modifica scorul pentru un participant")
    print("4. Exit")

    choice = int(input(">>>"))

    if choice == 1:
        name = ui.user_input.input_name("Cui doriti sa ii stergeti scorul?")
        position = ui.user_input.input_score(
            "Pe ce pozitie doriti sa stergeti scorul ?"
        )

        deleted_score = service.delete_service.delete_user_score(
            user_list, name, position
        )

        print(
            f"Scorurile lui {name} sunt : {ui.print_user_data.print_score_user_by_id(user_list,service.user_service.get_id_by_name(user_list, name))}"
        )

        service.undo_service.add_function_to_call_stack(
            call_stack, "sterge_scor", (name, deleted_score, position)
        )
    elif choice == 2:
        name = ui.user_input.input_name("Cui doriti sa ii stergeti scorul")

        start, end = ui.user_input.input_interval_number(user_list)

        list_deleted_scores = []

        for index in range(start, end + 1):
            list_deleted_scores.append(
                service.delete_service.delete_user_score(user_list, name, index)
            )
        for score in list_deleted_scores:
            service.undo_service.add_function_to_call_stack(
                call_stack, "sterge_scor", (name, score, start)
            )
            start += 1

    elif choice == 3:
        name = ui.user_input.input_name(
            "Cui doriti sa modificati scorul (toate etapele)"
        )

        forgotten_list = service.add_service.change_user_score_completely(
            user_list, name
        )
        print(forgotten_list)

        print(f"Scorurile lui {name} sunt :")
        ui.print_user_data.print_score_user_by_id(
            user_list, service.user_service.get_id_by_name(user_list, name)
        )

        index = 1
        for score in forgotten_list:
            service.undo_service.add_function_to_call_stack(
                call_stack, "sterge_scor", (name, score, index)
            )
            index += 1
    else:
        return 0


def menu_console_3(user_list):
    print("1. Tipareste participantii cu un scor total mai mic decat unul dat")
    print("2. Tipareste participantii ordonat dupa scor")
    print(
        "3. Tipărește participanții cu scor mai mare decât un scor dat, ordonat după scor."
    )
    print("4. Exit")

    choice = int(input(">>>"))

    if choice == 1:
        ui.print_user_data.print_user_smaller_score(
            service.print_service.get_users_smallers(
                user_list, ui.user_input.input_score("Scor maxim")
            )
        )
    elif choice == 2:
        ui.print_user_data.print_users_score_total(
            service.print_service.sort_user_total_scor(user_list)
        )
    elif choice == 3:
        ui.print_user_data.print_users_ascending(
            service.print_service.print_users_bigger_score(
                user_list, ui.user_input.input_score("Scor minim")
            )
        )
    else:
        return 0


def menu_console_4(user_list):
    print("1. Media scorurilor pe un interval")
    print("2. Scorul minim pe un interval")
    print("3. Participantii cu scor multiplu de 10")
    print("4. Exit")

    choice = int(input(">>>"))

    if choice == 1:
        ui.print_user_data.print_all_user_names(user_list)
        start, end = ui.user_input.input_interval(user_list)
        print(
            f"\nMedia scorurilor este: {service.operation_service.calculate_avg_score_interval(
                user_list, start, end
            )}"
        )
    elif choice == 2:
        ui.print_user_data.print_all_user_names(user_list)
        start, end = ui.user_input.input_interval(user_list)
        print(
            f"\nCel mai mic scor total de pe interval: {service.operation_service.calculate_minimum_total_score(
                user_list, start, end
            )}"
        )
    elif choice == 3:
        ui.print_user_data.print_all_user_names(user_list)
        start, end = ui.user_input.input_interval(user_list)
        ui.print_user_data.print_users_score_total(
            service.operation_service.calculate_ids_ten_multiple(user_list, start, end)
        )
    else:
        return 0


def menu_console_5(user_list):
    print("1. Filtratre participanti cu scor multiplu dat (fara ei)")
    print("2. Filtrare participanti scor mai mic decat cel dat (fara ei)")
    print("3. Exit")

    choice = int(input(">>>"))

    if choice == 1:
        ui.print_user_data.print_all_user_names(
            service.filtration_service.filter_users_multi_score(
                user_list, ui.user_input.input_score("Introduceti numarul \n")
            )
        )
        print("\n")
    elif choice == 2:
        ui.print_user_data.print_all_user_names(
            service.filtration_service.filter_users_smaller_score(
                user_list, ui.user_input.input_score("Introduceti numarul \n")
            )
        )
        print("\n")
    else:
        return 0

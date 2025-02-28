import menu

import date_participanti

participants_list = []

date_participanti.read_participants_data(participants_list)
menu.main_menu_console(participants_list)
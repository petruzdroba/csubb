import repository.users_from_file
import ui.menu
import tester.main_test

import os

tester.main_test.main_tester_function()

user_list = []

repository.users_from_file.read_user_data(user_list, "file_data_participanti.txt")

call_stack = []

os.system("cls")
ui.menu.main_menu_console(user_list, call_stack)

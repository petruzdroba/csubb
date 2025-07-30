import tester.test_score
import tester.test_undo
import tester.test_user
import repository.users_from_file


def run_all_tests(test_list, test_call_stack):
    tester.test_score.test_sort_user_total_score(test_list)
    tester.test_score.test_add_score_to_user(test_list)
    tester.test_score.test_change_user_score_completely(test_list)
    tester.test_score.test_score_delete(test_list)

    tester.test_score.test_get_users_smallers(test_list)
    tester.test_score.test_print_users_ascending(test_list)
    tester.test_score.test_print_users_bigger_score(test_list)
    tester.test_score.test_get_scor()

    tester.test_user.test_create_user(test_list)
    tester.test_user.test_get_id()
    tester.test_user.test_get_id_by_name(test_list)
    tester.test_user.test_get_name()

    # 4.1
    tester.test_score.test_calculate_avg_score_interval(test_list)
    # 4.2
    tester.test_score.test_calculate_minimum_total_score(test_list)
    # 4.3
    tester.test_score.test_calculate_ids_ten_multiple(test_list)

    # 5.1
    tester.test_score.test_filter_users_multi_score(test_list)
    # 5.2
    tester.test_score.test_filter_users_smaller_score(test_list)

    # 6.1
    tester.test_undo.test_add_function_to_call_stack(test_list, test_call_stack)


def main_tester_function():
    test_list = []

    repository.users_from_file.read_user_data(test_list, "test_data.txt")

    test_call_stack = []

    run_all_tests(test_list, test_call_stack)

import service.filtration_service
import service.operation_service
import service.print_service
import service.score_service
import domain.getters


def test_add_score_to_user(test_list):
    test_score = 9
    test_position = 3
    test_name = "Tester2"

    service.score_service.add_score_to_user(
        test_list, test_score, test_name, test_position
    )

    assert test_list[1]["scor"][test_position] == test_score


def test_score_delete(test_list):
    test_name = "Tester2"
    test_position = 3

    service.score_service.add_score_to_user(test_list, 0, test_name, test_position)

    assert test_list[1]["scor"][test_position] == 0


def test_change_user_score_completely(test_list):
    test_name = "Tester2"

    for etapa in range(1, 11):
        service.score_service.add_score_to_user(test_list, etapa, test_name, etapa)

    for etapa in range(1, 11):
        if test_list[1]["scor"][etapa] != etapa:
            assert False


def test_get_users_smallers(test_list):
    test_target_score = 4

    test_list_result = service.print_service.get_users_smallers(
        test_list, test_target_score
    )
    # Tester 2 already has scores
    assert ["Tester1", "Tester3"] == test_list_result


def test_sort_user_total_score(test_list):

    test_list_result = service.print_service.sort_user_total_scor(test_list)

    assert [
        {"nume": "Tester1", "scor_total": 0},
        {"nume": "Tester2", "scor_total": 0},
        {"nume": "Tester3", "scor_total": 0},
    ] == test_list_result


def test_print_users_ascending(test_list):
    # [{"nume", "scor_total"}]
    test_model = service.print_service.sort_user_total_scor(test_list)

    assert [
        {"nume": "Tester2", "scor_total": 52},
        {"nume": "Tester1", "scor_total": 0},
        {"nume": "Tester3", "scor_total": 0},
    ] == test_model


def test_print_users_bigger_score(test_list):

    test_list_result = service.print_service.print_users_bigger_score(test_list, 1)

    assert [{"nume": "Tester2", "scor_total": 52}] == test_list_result


def test_get_scor():
    test_user = {"key_id": 1, "nume": "John Doe", "scor": [1]}

    assert test_user["scor"] == domain.getters.get_scor(test_user)


def test_calculate_avg_score_interval(test_list):
    test_avg_score = service.operation_service.calculate_avg_score_interval(
        test_list, 0, 1
    )
    # 0 si 1 sunt Tester 1 0pct si Tester 2 52pct

    assert test_avg_score == 26.0


def test_calculate_minimum_total_score(test_list):
    test_minimum_score = service.operation_service.calculate_minimum_total_score(
        test_list, 1, 2
    )

    assert test_minimum_score == 0


def test_calculate_ids_ten_multiple(test_list):
    test_result_list = service.operation_service.calculate_ids_ten_multiple(
        test_list, 0, 1
    )

    assert test_result_list == [{"nume": "Tester1", "scor_total": 0}]


def test_filter_users_multi_score(test_list):

    test_result_list = service.filtration_service.filter_users_multi_score(test_list, 2)

    assert test_result_list == []


def test_filter_users_smaller_score(test_list):
    test_result_list = service.filtration_service.filter_users_smaller_score(
        test_list, 2
    )
    assert test_result_list == [
        {
            "key_id": 1,
            "nume": "Tester2",
            "scor": ["id:1", 1, 2, 0, 4, 5, 6, 7, 8, 9, 10],
        }
    ]

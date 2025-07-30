import service.undo_service
from service import add_service, delete_service


def test_add_function_to_call_stack(test_list: list, test_call_stack: list):
    add_service.add_user(test_list, "Tester4")

    service.undo_service.add_function_to_call_stack(
        test_call_stack, "add_user", "Tester4"
    )

    service.undo_service.undo(test_call_stack, test_list)

    assert len(test_list) == 3

    add_service.add_score_insert(test_list, "Tester1", 1, 1)

    service.undo_service.add_function_to_call_stack(
        test_call_stack, "add_score_insert", ("Tester1", 1)
    )

    # print(f"{test_list}\n\n")

    service.undo_service.undo(test_call_stack, test_list)

    # print(test_list)
    assert test_list[0]["scor"][1] == 0

    add_service.add_score_insert(test_list, "Tester1", 10, 1)
    test_deleted_score = delete_service.delete_user_score(test_list, "Tester1", 1)

    service.undo_service.add_function_to_call_stack(
        test_call_stack, "sterge_scor", ("Tester1", test_deleted_score, 1)
    )

    service.undo_service.undo(test_call_stack, test_list)

    assert test_list[0]["scor"][1] == 10

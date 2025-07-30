import service.user_service
import domain.getters


def test_create_user(test_list):
    """
    Function that tests service/user_service.py/add_user() function
    """
    name_good = "Tester4"
    test_model = service.user_service.create_user(test_list, name_good)
    assert test_model["nume"] == name_good


def test_get_id_by_name(test_list):
    searched_name = "Tester2"

    assert service.user_service.get_id_by_name(test_list, searched_name) == 1


def test_get_name():
    test_user = {"key_id": 1, "nume": "John Doe", "scor": []}

    assert test_user["nume"] == domain.getters.get_name(test_user)


def test_get_id():
    test_user = {"key_id": 1, "nume": "John Doe", "scor": []}

    assert test_user["key_id"] == domain.getters.get_id(test_user)

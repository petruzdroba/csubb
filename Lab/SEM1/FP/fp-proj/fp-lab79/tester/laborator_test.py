import unittest
from domain.problema_lab import Problema_Laborator
from validators.laborator_validator import LaboratorValidator
from repository.laborator_repo import LaboratorRepo
from service.probleme_service import Problema_Service


class LaboratorTest(unittest.TestCase):
    def __init__(self, methodName="runTest"):
        super().__init__(methodName)
        self.__test_validator = LaboratorValidator()
        self.__test_repo = LaboratorRepo("test_laborator.txt")
        self.__test_service = Problema_Service(self.__test_validator, self.__test_repo)

    def run_all_lab_test(self):
        self.__reset_test_repo()
        self.__test_read_from_file()
        self.__test_create_lab()
        self.__test_add()
        self.__test_search()

        self.__test_delete()

    def __test_read_from_file(self):
        self.assertEqual(len(self.__test_repo.get_laborators_list()), 0)

    def __reset_test_repo(self):
        self.__test_repo.get_laborators_list().clear()
        with open("test_laborator.txt", "w") as file:
            file.write("")

    def __test_create_lab(self):
        test_lab_nr = 13
        test_descriere = "Test"
        test_deadline = "12.12.2012"

        test_lab = Problema_Laborator(test_lab_nr, test_descriere, test_deadline)

        self.assertEqual(test_lab.get_laborator_numar(), test_lab_nr)
        self.assertEqual(test_lab.get_descriere(), test_descriere)
        self.assertEqual(test_lab.get_deadline(), test_deadline)

    def __test_add(self):
        test_lab_nr = 13
        test_descriere = "Test"
        test_deadline = "12.12.2012"

        self.__test_service.add_problem_to_list(
            test_lab_nr, test_descriere, test_deadline
        )

        self.assertIn(test_lab_nr, self.__test_repo.get_laborators_list())

        with self.assertRaises(ValueError) as context:
            self.__test_service.add_problem_to_list(
                test_lab_nr, test_descriere, test_deadline
            )
        self.assertEqual(str(context.exception), "numar laborator existent \n")

    def __test_delete(self):
        test_lab_nr = 13

        self.__test_service.delete_problem_from_list(test_lab_nr)

        self.assertNotIn(test_lab_nr, self.__test_repo.get_laborators_list())

        with self.assertRaises(ValueError) as context:
            self.__test_service.delete_problem_from_list(test_lab_nr)
        self.assertEqual(str(context.exception), "laborator inexistent")

    def __test_search(self):
        test_lab_nr = 13
        test_descriere = "Test"
        test_deadline = "12.12.2012"

        test_lab = self.__test_service.search_laborator_from_list(test_lab_nr)

        self.assertEqual(test_lab.get_laborator_numar(), test_lab_nr)
        self.assertEqual(test_lab.get_descriere(), test_descriere)
        self.assertEqual(test_lab.get_deadline(), test_deadline)

import unittest
from domain.note import Note
from domain.student import Student
from domain.problema_lab import Problema_Laborator
from domain.dto import DTO

from repository.note_repo import NoteRepo
from repository.student_repo import StudentRepo
from repository.laborator_repo import LaboratorRepo

from validators.note_validator import NoteValidator

from service.note_service import NoteService


class NotaTest(unittest.TestCase):
    def __init__(self, methodName="runTest"):
        super().__init__(methodName)
        self.__test_student_repo = StudentRepo("test_students.txt")
        self.__test_lab_repo = LaboratorRepo("test_laborator.txt")
        self.__test_note_repo = NoteRepo(
            self.__test_student_repo, self.__test_lab_repo, "test_note.txt"
        )
        self.__test_note_validator = NoteValidator()
        self.__test_service = NoteService(
            self.__test_note_validator,
            self.__test_note_repo,
            self.__test_student_repo,
            self.__test_lab_repo,
        )

    def run_all_nota_test(self):
        self.__reset_test_repo()
        self.__test_create_nota()
        self.__test_add_nota()
        self.__test_get_notes_by_lab_alpha()
        self.__test_get_notes_by_lab_grade()
        self.__test_get_grade_under()

    def __reset_test_repo(self):
        self.__test_note_repo.get_note_list().clear()
        with open("test_note.txt", "w") as file:
            file.write("")

    def __test_create_nota(self):
        test_id = 13
        test_nume = "John Doe"
        test_grupa = 217

        test_student = Student(test_id, test_nume, test_grupa)
        self.__test_student_repo.add_student(test_student)

        test_lab_nr = 13
        test_descriere = "Test"
        test_deadline = "12.12.2012"

        test_lab = Problema_Laborator(test_lab_nr, test_descriere, test_deadline)
        self.__test_lab_repo.add_laborator(test_lab)

        test_id_nota = 13
        test_nota = 5

        test_note = Note(test_id_nota, test_student, test_lab, test_nota)

        self.assertEqual(test_note.get_student(), test_student)
        self.assertEqual(test_note.get_laborator(), test_lab)
        self.assertEqual(test_note.get_id_nota(), test_id_nota)
        self.assertEqual(test_note.get_nota(), test_nota)

    def __test_add_nota(self):
        test_id_student = 13
        test_lab_nr = 13
        test_id_nota = 13
        test_nota = 5

        self.__test_service.add_nota_to_list(
            test_id_nota, test_id_student, test_lab_nr, test_nota
        )

        self.assertIn(test_id_nota, self.__test_note_repo.get_note_list())

        with self.assertRaises(ValueError) as context:
            self.__test_service.add_nota_to_list(
                test_id_nota, test_id_student, test_lab_nr, test_nota
            )
        self.assertEqual(str(context.exception), "nota existenta")

    def __test_get_notes_by_lab_alpha(self):
        test_lab_id = 13

        test_id = 14
        test_nume = "Aohn Doe"
        test_grupa = 217

        test_student = Student(test_id, test_nume, test_grupa)

        self.__test_student_repo.add_student(test_student)

        self.__test_service.add_nota_to_list(14, test_id, 13, 10)

        sorted_dict_alpha = self.__test_service.get_notes_by_lab_alpha(test_lab_id)

        self.assertIn(
            "ID: 14\n    STUDENT : NUME : Aohn Doe    ID : 14   GRUPA : 217  \n    LAB : LABORATOR : 13    DESCRIERE : Test  DEADLINE : 12.12.2012\n    NOTA : 10",
            sorted_dict_alpha,
        )

    def __test_get_notes_by_lab_grade(self):
        test_lab_id = 13

        sorted_dict_grade = self.__test_service.get_notes_by_lab_grade(test_lab_id)

        self.assertIn(
            "ID: 14\n    STUDENT : NUME : Aohn Doe    ID : 14   GRUPA : 217  \n    LAB : LABORATOR : 13    DESCRIERE : Test  DEADLINE : 12.12.2012\n    NOTA : 10",
            sorted_dict_grade,
        )

    def __test_get_grade_under(self):
        test_id = 15
        test_nume = "Xhing Doe"
        test_grupa = 217

        test_student = Student(test_id, test_nume, test_grupa)
        self.__test_student_repo.add_student(test_student)

        self.__test_service.add_nota_to_list(15, test_id, 13, 3)

        self.assertEqual(
            self.__test_service.get_grade_under()[15], DTO("Xhing Doe", 3.0)
        )

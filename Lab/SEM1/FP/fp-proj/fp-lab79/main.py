from ui.menu import Consola

from service.student_service import Student_Service
from validators.student_validator import StudentValidator
from repository.student_repo import StudentRepo
from tester.student_test import StudentTest

from validators.laborator_validator import LaboratorValidator
from repository.laborator_repo import LaboratorRepo
from service.probleme_service import Problema_Service
from tester.laborator_test import LaboratorTest

from validators.note_validator import NoteValidator
from repository.note_repo import NoteRepo
from service.note_service import NoteService
from tester.nota_test import NotaTest

from tester.sort_test import SortTest

import random
import os

StudentTest().run_all_student_test()
LaboratorTest().run_all_lab_test()
NotaTest().run_all_nota_test()
SortTest().run_all_tests()

os.system("cls")

validator_student = StudentValidator()
repo_student = StudentRepo("students.txt")
service_student = Student_Service(validator_student, repo_student)
# service_student.generate_nr_students_random(random.randint(10, 21))

validator_laborator = LaboratorValidator()
repo_laborator = LaboratorRepo("laborators.txt")
service_laborator = Problema_Service(validator_laborator, repo_laborator)

validator_note = NoteValidator()
repo_note = NoteRepo(repo_student, repo_laborator, "note.txt")
service_note = NoteService(validator_note, repo_note, repo_student, repo_laborator)

ui = Consola(service_student, service_laborator, service_note)

ui.run()

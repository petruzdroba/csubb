from domain.note import Note
from domain.dto import DTO

from validators.note_validator import NoteValidator
from sorting.sort import Sort

from repository.note_repo import NoteRepo
from repository.student_repo import StudentRepo
from repository.laborator_repo import LaboratorRepo


class NoteService(object):
    def __init__(
        self,
        validator_note: NoteValidator,
        repo_note: NoteRepo,
        repo_student: StudentRepo,
        repo_laborator: LaboratorRepo,
    ):
        self.__validator_note = validator_note
        self.__repo_note = repo_note
        self.__student_repo = repo_student
        self.__laborator_repo = repo_laborator

    def add_nota_to_list(
        self, id_nota: int, id_student: int, id_laborator: int, nota: int
    ):
        """
        Functie care adauga nota nota
        """
        student = self.__student_repo.get_student_by_id(id_student)
        laborator = self.__laborator_repo.get_lab_by_laborator_numar(id_laborator)

        nota_noua = Note(id_nota, student, laborator, nota)

        self.__validator_note.valideaza_nota(nota_noua)

        self.__repo_note.add_nota(nota_noua)

    def get_all_notes(self):
        note_list = self.__repo_note.get_note_list().values()
        return [str(note) for note in note_list]

    def __grades_by_lab(self, id_lab: int):
        note_list = self.__repo_note.get_note_list().values()

        note_filtered = {}

        for nota in note_list:
            if nota.get_laborator().get_laborator_numar() == id_lab:
                note_filtered[nota.get_id_nota()] = nota

        return note_filtered

    def get_notes_by_lab_alpha(self, id_laborator: int):
        """
        Functie care returneaza o lista de note la un laborator dat : id_laborator si o sorteaza
        alfabetic dupa numele studentilor
        """
        if id_laborator not in self.__laborator_repo.get_laborators_list():
            raise ValueError("id laborator inexistent")

        note_filtered = self.__grades_by_lab(id_laborator)

        # note_filtered = self.__filter_alpha(note_filtered).values()
        note_filtered = dict(
            Sort().insertion_sort(
                list(note_filtered.items()),
                lambda stud1, stud2: stud1[1].get_student().get_nume()[0].lower()
                > stud2[1].get_student().get_nume()[0].lower(),
            )
        ).values()
        return [str(note) for note in note_filtered]

    def get_notes_by_lab_grade(self, id_laborator: int):
        if id_laborator not in self.__laborator_repo.get_laborators_list():
            raise ValueError("id laborator inexistent")

        note_filtered = self.__grades_by_lab(id_laborator)
        # note_filtered = self.__filter_grade(note_filtered).values()
        note_filtered = dict(
            Sort().comb_sort(
                list(note_filtered.items()),
                lambda nota1, nota2: nota1[1].get_nota() > nota2[1].get_nota(),
                True,
            )
        ).values()

        return [str(note) for note in note_filtered]

    def __calculate_grade_average(self, id_student: int):
        """
        Analizare complexitate
            l94 - O(m), obtinerea notelor e constanta, dar .values() trece rpin toate
            l99->l106
                l101 - comparare O constanta
                l103 - .get_nota() O constanta
                etc pana la l106 e constanta
        Timp total O(n) - n este numarul de note din dictionar

        Worst Case Scenario - toate notele partin studentului cu id_student, se trece prin toate cele n note - O(n)
        Best Case Scenario - nicio nota nu apartie studentului cu id_student, tot se trece prin toate cele n note - O(n)

        """
        grades = self.__repo_note.get_note_list().values()
        total = 0
        nr = 0
        for nota in grades:
            if nota.get_student().get_id() == id_student:
                total += nota.get_nota()
                nr += 1
        if nr != 0:
            return total / nr
        else:
            return None

    def get_grade_under(self) -> dict:
        students = self.__student_repo.get_student_list().values()

        underachievers = {}

        for student in students:
            avg = self.__calculate_grade_average(student.get_id())
            if avg is not None and avg < 5.0:
                underachievers[student.get_id()] = DTO(student.get_nume(), avg)

        return underachievers

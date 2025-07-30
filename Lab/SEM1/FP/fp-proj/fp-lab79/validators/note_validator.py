from domain.note import Note

from validators.student_validator import StudentValidator
from validators.laborator_validator import LaboratorValidator


class NoteValidator(object):
    def __init__(self):
        pass

    def valideaza_nota(self, nota: Note):
        """
        Functie care valideaza un obiect de tipul nota
        input:
            nota : Note
        :return -
        raise ValueError cu mesajul "laborator invalid", "student invalid", "id nota invalida", "nota invalida"
        """
        # try:
        #     StudentValidator.valideaza_student(nota.get_student())
        # except ValueError:
        #     raise ValueError("student invalid")

        # try:
        #     LaboratorValidator.valideaza_laborator(nota.get_laborator())
        # except ValueError:
        #     raise ValueError("laborator invalid")

        if nota.get_id_nota() < 0:
            raise ValueError("id nota invalida")

        if nota.get_nota() < 1 or nota.get_nota() > 10:
            raise ValueError("nota invalida")

from domain.note import Note


class NoteRepo(object):
    def __init__(self, repo_stud, repo_lab, file_path):
        self.__note = {}
        self.__student = repo_stud
        self.__lab = repo_lab
        self.__file_path = file_path
        self.__citire_din_fisier()

    def __citire_din_fisier(self):
        with open(self.__file_path, "r") as f:
            lines = f.readlines()
            for line in lines:
                line = line.strip()
                if line != "":
                    parti = line.split(",")
                    id_nota = int(parti[0])
                    id_stud = int(parti[1])
                    id_lab = int(parti[2])
                    nota = int(parti[3])
                    try:
                        student = self.__student.get_student_by_id(id_stud)
                    except ValueError:
                        continue
                    try:
                        lab = self.__lab.get_lab_by_laborator_numar(id_lab)
                    except ValueError:
                        continue
                    self.__note[id_nota] = Note(id_nota, student, lab, nota)

    def __adauga_nota_in_fisier(self, nota: Note):
        with open(self.__file_path, "a") as f:
            f.write(
                f"{nota.get_id_nota()},{nota.get_student().get_id()},{nota.get_laborator().get_laborator_numar()},{nota.get_nota()}\n"
            )

    def get_note_list(self):
        return self.__note

    def add_nota(self, nota: Note):
        id_nota = nota.get_id_nota()

        if id_nota in self.__note:
            raise ValueError("nota existenta")

        self.__note[id_nota] = nota
        self.__adauga_nota_in_fisier(nota)

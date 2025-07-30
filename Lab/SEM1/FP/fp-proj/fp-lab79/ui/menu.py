import os


class Consola:
    def __init__(self, student_service, laborator_service, note_service):
        self.__student_service = student_service
        self.__laborator_service = laborator_service
        self.__note_service = note_service
        self.__comenzi = {
            "add_student": self.__ui_add_student,
            "add_laborator": self.__ui_add_laborator,
            "show_students": self.__ui_show_studenti,
            "show_laborators": self.__ui_show_laborators,
            "delete_student": self.__ui_delete_student,
            "delete_laborator": self.__ui_delete_laborator,
            "modify_student": self.__ui_modify_student,
            "modify_laborator": self.__ui_modify_laborator,
            "search_student": self.__ui_search_student,
            "search_laborator": self.__ui_search_laborator,
            "add_nota": self.__ui_add_nota,
            "show_note": self.__ui_show_note,
            "show_note_a": self.__ui_show_note_lab_name,
            "show_note_n": self.__ui_show_note_lab_grade,
            "show_u": self.__ui_show_underachievers,
        }

    def __ui_add_student(self):
        id_student = int(input("id>>>"))
        nume = input("nume>>>")
        grupa = int(input("grupa>>>"))
        try:
            self.__student_service.add_student_to_list(id_student, nume, grupa)
        except ValueError as ve:
            print(ve)

    def __ui_add_laborator(self):
        numar_lab = int(input("nrlab>>>"))
        descriere = input("descriere>>>")
        deadline = input("deadline>>>")

        self.__laborator_service.add_problem_to_list(numar_lab, descriere, deadline)

    def __ui_show_studenti(self):
        studenti = self.__student_service.get_all_students()
        for student in studenti:
            print(student)

    def __ui_show_laborators(self):
        labs = self.__laborator_service.get_all_laborators()
        for lab in labs:
            print(lab)

    def __ui_delete_student(self):
        student_id = int(input("id>>>"))

        self.__student_service.delete_student_from_list(student_id)

    def __ui_delete_laborator(self):
        lab_number = int(input("nrlab>>>"))

        self.__laborator_service.delete_problem_from_list(lab_number)

    def __ui_modify_student(self):
        id_student = int(input("id>>>"))
        nume = input("nume_n>>>")
        grupa = int(input("grupa_n>>>"))

        self.__student_service.modify_student_from_list(id_student, nume, grupa)

    def __ui_modify_laborator(self):
        numar_laborator = int(input("nrlab>>>"))
        descriere = input("descriere_n>>>")
        deadline = input("deadline_n>>>")

        self.__laborator_service.modify_laborator_from_list(
            numar_laborator, descriere, deadline
        )

    def __ui_search_student(self):
        id_student = int(input("id>>>"))

        print(self.__student_service.search_student_from_list(id_student))

    def __ui_search_laborator(self):
        numar_laborator = int(input("nrlab>>>"))

        print(self.__laborator_service.search_laborator_from_list(numar_laborator))

    def __ui_add_nota(self):
        id_nota = int(input("id_nota>>>"))
        id_student = int(input("id_student>>>"))
        numar_laborator = int(input("nrlab>>>"))
        nota = int(input("nota>>>"))

        self.__note_service.add_nota_to_list(id_nota, id_student, numar_laborator, nota)

    def __ui_show_note(self):
        note = self.__note_service.get_all_notes()
        for nota in note:
            print(nota)

    def __ui_show_note_lab_name(self):
        numar_laborator = int(input("nrlab>>>"))
        note = self.__note_service.get_notes_by_lab_alpha(numar_laborator)
        for nota in note:
            print(nota)

    def __ui_show_note_lab_grade(self):
        numar_laborator = int(input("nrlab>>>"))
        note = self.__note_service.get_notes_by_lab_grade(numar_laborator)
        for nota in note:
            print(nota)

    def __ui_show_underachievers(self):
        underachivers = self.__note_service.get_grade_under()
        for student in underachivers.values():
            print(student)

    def run(self):
        while True:
            nume_comanda = input(">>>")
            nume_comanda = nume_comanda.lower().strip()
            if nume_comanda == "":
                continue

            if nume_comanda == "exit":
                break

            if nume_comanda == "cls":
                os.system("cls")

            if nume_comanda in self.__comenzi:
                try:
                    self.__comenzi[nume_comanda]()
                except ValueError:
                    print("valoare numerica invalida!\n")
            else:
                print("comanda invalida!\n")

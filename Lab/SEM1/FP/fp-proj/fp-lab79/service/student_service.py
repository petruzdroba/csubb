from domain.student import Student
import random


class Student_Service(object):
    def __init__(self, validator_student, repo_student):
        self.__valideaza_student = validator_student
        self.__student_repo = repo_student

    def add_student_to_list(self, student_id: int, nume: str, grupa: int):
        """
        Functie care valideaza si creeaza obiectul student
        input:
            student_id : int
            nume : string
            grupa : int
        """

        student = Student(student_id, nume, grupa)

        self.__valideaza_student.valideaza_student(student)

        self.__student_repo.add_student(student)

    def get_all_students(self):
        student_list = self.__student_repo.get_student_list().values()
        return [str(student) for student in student_list]

    def delete_student_from_list(self, student_id: int):
        """
        Functie care sterge un student dintr-o lista
        input:
            student_id : int
            student_list : list
        :return -
            raise ValueError cu mesajul "student inexistent"
        """
        key_list = self.__student_repo.get_student_list()

        if student_id in key_list:
            self.__student_repo.delete_student(student_id)
        else:
            raise ValueError("student inexistent\n")

    def modify_student_from_list(
        self, student_id: int, nume_nou: str, grupa_noua: int
    ) -> None:
        """
        Functie care modifica datele unui student cu id-ul : student_id
        returneaza:
            -nothing
            -raises ValueError for empty name and for negative group or student_id
        """

        student_modificat = Student(student_id, nume_nou, grupa_noua)

        self.__valideaza_student.valideaza_student(student_modificat)

        self.__student_repo.modify_student(student_modificat)

    def search_student_from_list(self, id: int):
        """
        Functie care returneaza un obiect de tipul student, gasit in repo, pe baza idului acestuia
        input:
            id : int
        output:
            student : Student
        """

        self.__valideaza_student.valideaza_student(Student(id, "a", 1))

        return self.__student_repo.get_student_by_id(id)

    def __generate_random_string(self, number_char: int):
        new_name = ""
        new_name += random.choice("ABCDEFGHIJKLMNOPQRSTUVWXYZ")

        for index in range(0, number_char - 1):
            new_name += random.choice("qwertyuiopasdfghjklzxcvbnmeeaaiioouu")

        return new_name

    def __generate_random_int(self, end: int):
        return random.randint(3, end + 1)

    def generate_nr_students_random(self, nr: int):
        for i in range(nr):
            id = self.__generate_random_int(1000)
            name = (
                self.__generate_random_string(self.__generate_random_int(10))
                + " "
                + self.__generate_random_string(self.__generate_random_int(10))
            )
            group = self.__generate_random_int(100)
            self.add_student_to_list(id, name, group)

from domain.student import Student


class StudentValidator(object):
    def __init__(self) -> None:
        pass

    def valideaza_student(self, student: Student):
        """
        Functie care valideaza datele de intrare pentru creearea unui nou student
        input:
            student : Student
            student_list : list
        :return : -
                raise ValueError with the message "id invalid", "nume invalid", "grupa invalida"
        """

        if student.get_id() < 0:
            raise ValueError("id invalid")
        if student.get_nume() == "":
            raise ValueError("nume invalid")

        if student.get_grupa() < 0:
            raise ValueError("grupa invalida")

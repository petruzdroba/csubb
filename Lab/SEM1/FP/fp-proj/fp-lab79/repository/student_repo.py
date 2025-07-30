from domain.student import Student


class StudentRepo(object):
    def __init__(self, file_path):
        self.__student_list = {}
        self.__file_path = file_path
        self.__read_students_from_file()

    def __read_students_from_file(self):
        with open(self.__file_path, "r") as f:
            lines = f.readlines()
            for line in lines:
                line = line.strip()
                if line != "":
                    parts = line.split(",")
                    id_student = int(parts[0])
                    nume = parts[1]
                    grupa = int(parts[2])
                    student = Student(id_student, nume, grupa)
                    self.__student_list[id_student] = student

    def __append_students_to_file(self, student: Student):
        with open(self.__file_path, "a") as f:
            f.write(f"{student.get_id()},{student.get_nume()},{student.get_grupa()}\n")

    def __overwrite_students_in_file(self):
        with open(self.__file_path, "w") as f:
            self.__recursive_write_students(f, self.__student_list)

    def __recursive_write_students(self, file, student_dict: dict) -> None:
        if not student_dict:
            return  # base case

        id_student, student = next(iter(student_dict.items()))

        file.write(f"{id_student},{student.get_nume()},{student.get_grupa()}\n")

        remaining_students = {
            key: value for key, value in student_dict.items() if key != id_student
        }
        self.__recursive_write_students(file, remaining_students)

    def get_student_list(self):
        return self.__student_list

    def add_student(self, student: Student):
        id_student = student.get_id()

        if id_student in self.__student_list:
            raise ValueError("id existent \n")

        self.__student_list[id_student] = student
        self.__append_students_to_file(student)

    def delete_student(self, student_id: int):
        self.__student_list[student_id].delete()
        self.__student_list.pop(student_id)
        self.__overwrite_students_in_file()

    def modify_student(self, student: Student):
        id_student = student.get_id()

        if id_student not in self.__student_list:
            raise ValueError("id inexistent \n")

        self.__student_list[id_student] = student
        self.__overwrite_students_in_file()

    def get_student_by_id(self, id_student: int):
        if id_student not in self.__student_list:
            raise ValueError("id inexistent \n")

        return self.__student_list[id_student]

class Note:
    def __init__(self, id_nota, student, laborator, nota):
        self.__id_nota = id_nota
        self.__student = student
        self.__laborator = laborator
        self.__nota = nota

    def get_id_nota(self):
        return self.__id_nota

    def get_student(self):
        return self.__student

    def get_laborator(self):
        return self.__laborator

    def get_nota(self):
        return self.__nota

    def __str__(self) -> str:
        return f"""ID: {self.__id_nota}
    STUDENT : {self.__student}
    LAB : {self.__laborator}
    NOTA : {self.__nota}"""

    def __eq__(self, value):
        return self.__id_nota == value.get_id_nota()

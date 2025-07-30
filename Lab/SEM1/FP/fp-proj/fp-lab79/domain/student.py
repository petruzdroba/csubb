class Student(object):
    def __init__(self, student_id: int, nume: str, grupa: int):
        self.__student_id = student_id
        self.__nume = nume
        self.__grupa = grupa
        self.__deleted = False

    def set_nume(self, nume_nou: str):
        self.__nume = nume_nou

    def set_grupa(self, grupa: str):
        self.__grupa = grupa

    def get_id(self):
        return self.__student_id

    def get_nume(self):
        return self.__nume

    def get_grupa(self):
        return self.__grupa

    def is_deleted(self):
        return self.__deleted

    def delete(self):
        self.__deleted = True

    def __eq__(self, value: object) -> bool:
        return self.__student_id == value.get_id()

    def __str__(self):
        return f"""NUME : {self.__nume}    ID : {self.__student_id}   GRUPA : {self.__grupa}  """

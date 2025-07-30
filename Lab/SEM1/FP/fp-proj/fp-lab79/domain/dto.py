class DTO(object):
    def __init__(self, nume, avg):
        self.__nume = nume
        self.__avg = avg

    def get_nume(self):
        return self.__nume

    def get_avg(self):
        return self.__avg

    def __str__(self) -> str:
        return f"""Student: {self.__nume} avg:{self.__avg}"""

    def __eq__(self, value: object) -> bool:
        return self.__nume == value.get_nume()

    # and self.__avg == value.get_avg()

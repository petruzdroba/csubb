import datetime


class Sedinta:
    def __init__(
        self, data: datetime.date, ora: datetime.time, subiect: str, e_extra: bool
    ):
        self.__data = data
        self.__ora = ora
        self.__subiect = subiect
        self.__e_extra = e_extra

    def get_data(self):
        return self.__data

    def get_ora(self):
        return self.__ora

    def get_subiect(self):
        return self.__subiect

    def get_e_extra(self):
        return self.__e_extra

    def __extraordinar(self):
        if self.__e_extra == True:
            return "Extraordinar"
        else:
            return "Normal"

    def __eq__(self, value):
        return self.__subiect == value.get_subiect()

    def __str__(self):
        return f"{self.__data.day}.{self.__data.month}, {self.__ora}, {self.__subiect}, {self.__extraordinar()}"

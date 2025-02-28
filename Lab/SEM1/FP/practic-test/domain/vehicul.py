from domain.data_expirare import Data_Expirare


class Vehicul(object):
    def __init__(
        self, id: int, marca: str, pret: int, model: str, data: Data_Expirare
    ) -> None:
        self.__id = id
        self.__marca = marca
        self.__pret = pret
        self.__model = model
        self.__data = data

    def get_id(self):
        return self.__id

    def get_marca(self):
        return self.__marca

    def get_pret(self):
        return self.__pret

    def get_model(self):
        return self.__model

    def get_data(self):
        return self.__data

    def get_zi(self):
        return self.__data.get_zi()

    def get_luna(self):
        return self.__data.get_luna()

    def get_an(self):
        return self.__data.get_an()

    def __eq__(self, value):
        return self.__id == value.get_id()

    def __str__(self):
        return f"{self.__id}   {self.__marca}   {self.__pret}   {self.__model}  {self.get_zi()}     {self.get_luna()}   {self.get_an()}"

class Problema_Laborator(object):
    def __init__(self, laborator_numar: int, descriere: str, deadline: str):
        self.__laborator_numar = laborator_numar
        self.__descriere = descriere
        self.__deadline = deadline

    def set_descriere(self, descriere_noua: str):
        self.__descriere = descriere_noua

    def set_deadline(self, deadline_nou: str):
        self.__deadline = deadline_nou

    def get_laborator_numar(self):
        return self.__laborator_numar

    def get_descriere(self):
        return self.__descriere

    def get_deadline(self):
        return self.__deadline

    def __eq__(self, value: object) -> bool:
        return self.__laborator_numar == value.get_laborator_numar()

    def __str__(self):
        return f"""LABORATOR : {self.__laborator_numar}    DESCRIERE : {self.__descriere}  DEADLINE : {self.__deadline}"""

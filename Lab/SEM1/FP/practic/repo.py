import datetime
from sedinta import Sedinta


class Repository:
    def __init__(self, file_path: str):
        self.__sedinte = {}
        self.__file_path = file_path
        self.__read_from_file()

    def get_all(self):
        return self.__sedinte

    def __read_from_file(self):
        """
        Functie care citeste din fiser sedinta
        input:None
        output:None
        """
        with open(self.__file_path, "r") as f:
            lines = f.readlines()
            for line in lines:
                if line != "":
                    parti = line.split(";")
                    dater = parti[0].split(".")
                    data = datetime.date(2025, int(dater[0]), int(dater[1]))
                    orer = parti[1].split(".")
                    ora = datetime.time(int(orer[0]), int(orer[1]))
                    subiect = parti[2]
                    e_extra = bool(int(parti[3]))
                    sedinta = Sedinta(data, ora, subiect, e_extra)

                    self.__sedinte[subiect] = sedinta

    def __append_to_file(self, sedinta: Sedinta):
        """
        Functie care adauga la finalul fisierului o sedinta
        input:
            sedinta:Sedinta
        output:
            None
        """
        with open(self.__file_path, "a") as f:
            f.write(
                f"{sedinta.get_data().month}.{sedinta.get_data().day};{sedinta.get_ora().hour}.{sedinta.get_ora().minute};{sedinta.get_subiect()};{int(sedinta.get_e_extra())}\n"
            )

    def adauga(self, sedinta: Sedinta):
        """
        Functie care adauga in lista de sedinta un obiect numit sedinta de tipul Sedinta
        input:
            sedinta : sedinta
        output:
            -
        raises:
            Value Error daca exista deja o sedinta cu acelasi subiect
        """
        if sedinta.get_subiect() not in self.__sedinte:
            self.__sedinte[sedinta.get_subiect()] = sedinta
            self.__append_to_file(sedinta)
        else:
            raise ValueError("sedinta existenta")

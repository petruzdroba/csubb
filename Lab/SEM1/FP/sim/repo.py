from domain import Mobila


class Repository(object):
    def __init__(self, file_path: str):
        self.__mobila = {}
        self.__file_path = file_path
        self.__citeste_din_fisier()

    def get_all(self):
        return self.__mobila

    def __citeste_din_fisier(self):
        with open(self.__file_path, "r") as f:
            lines = f.readlines()
            for line in lines:
                line = line.strip()
                if line != "":
                    parti = line.split(",")
                    cod = parti[0]
                    tip = parti[1]
                    nume = parti[2]
                    stock = int(parti[3])
                    pret = float(parti[4])
                    self.__mobila[cod] = Mobila(cod, tip, nume, stock, pret)

    def __scrie_in_fisier(self):
        with open(self.__file_path, "w") as f:
            for mobila in self.__mobila.values():
                f.write(
                    f"{mobila.get_cod()},{mobila.get_tip()},{mobila.get_nume()},{mobila.get_stock()},{mobila.get_pret()}\n"
                )

    def modify_mobila(self, mobila: Mobila):
        cod_mobila = mobila.get_cod()
        if cod_mobila not in self.__mobila:
            raise ValueError("cod inexistent")
        else:
            self.__mobila[cod_mobila] = mobila
            self.__scrie_in_fisier()

from domain.vehicul import Vehicul
from domain.data_expirare import Data_Expirare


class Vehicul_Repo(object):
    def __init__(self, file_path: str):
        self.__vehicule = {}
        self.__file_path = file_path
        self.__read_from_file()

    def get_all(self):
        return self.__vehicule

    def __read_from_file(self):
        with open(self.__file_path, "r") as f:
            lines = f.readlines()
            for line in lines:
                line = line.strip()
                if line != "":
                    parts = line.split(";")
                    id = int(parts[0])
                    marca = parts[1]
                    pret = int(parts[2])
                    model = parts[3]
                    data = Data_Expirare(parts[4], parts[5], parts[6])

                    self.__vehicule[id] = Vehicul(id, marca, pret, model, data)

    def __append_to_file(self, vehicul: Vehicul):
        with open(self.__file_path, "a") as f:
            f.write(
                f"{vehicul.get_id()};{vehicul.get_marca()};{vehicul.get_pret()};{vehicul.get_model()};{vehicul.get_zi()};{vehicul.get_luna()};{vehicul.get_an()}\n"
            )

    def __write_to_file(self):
        with open(self.__file_path, "w") as f:
            for vehicul in self.__vehicule.values():
                f.write(
                    f"{vehicul.get_id()};{vehicul.get_marca()};{vehicul.get_pret()};{vehicul.get_model()};{vehicul.get_zi()};{vehicul.get_luna()};{vehicul.get_an()}\n"
                )

    def add_to_repo(self, vehicul: Vehicul):
        if vehicul.get_id() in self.__vehicule:
            raise ValueError("id existent")
        else:
            self.__vehicule[vehicul.get_id()] = vehicul
            self.__append_to_file(vehicul)

    def remove_from_repo(self, id: int):
        if id in self.__vehicule:
            self.__vehicule.pop(id)
            self.__write_to_file()

from domain.vehicul import Vehicul
from domain.data_expirare import Data_Expirare
from validator.vehicul_validator import Vehicul_Validaor
from validator.data_validator import Data_Validator


class Vehicul_Service(object):
    def __init__(
        self,
        repository,
        validator_masina: Vehicul_Validaor,
        validator_data: Data_Validator,
    ):
        self.__repository = repository
        self.__validator_masina = validator_masina
        self.__validator_data = validator_data
        self.__call_stack = []

    def adauga_vehicul(
        self, id: int, marca: str, pret: int, model: str, zi: int, luna: int, an: int
    ):
        self.__validator_masina.valideaza(id, marca, pret, model)
        self.__validator_data.valideaza_data(zi, luna, an)

        data = Data_Expirare(zi, luna, an)
        vehicul = Vehicul(id, marca, pret, model, data)

        self.__repository.add_to_repo(vehicul)
        self.__call_stack.append(f"delete;{id}")

    def stergere_vehicul(self, cifra: int) -> int:

        if cifra < 0:
            raise ValueError("cifra negative number")

        vehicule = self.__repository.get_all().values()
        lungime_initiala = len(vehicule)
        deleted_cars = []

        for vehicul in vehicule:
            if str(vehicul.get_pret()).find(str(cifra)) != -1:
                deleted_cars.append(vehicul.get_id())
                self.__call_stack.append(
                    f"add;{vehicul.get_id()};{vehicul.get_marca()};{vehicul.get_pret()};{vehicul.get_model()};{vehicul.get_zi()};{vehicul.get_luna()};{vehicul.get_an()}"
                )

        for ids in deleted_cars:
            self.__repository.remove_from_repo(ids)
        return lungime_initiala - len(self.__repository.get_all())

    def __filtreaza_marca(self, marca: str) -> list:
        vehicule = self.__repository.get_all()
        filtered = []

        for vehicul in vehicule.values():
            if vehicul.get_marca().find(marca) != -1:
                filtered.append(vehicul)

        return filtered

    def __filtreaza_pret(self, pret: int) -> list:
        vehicule = self.__repository.get_all()
        filtered = []

        for vehicul in vehicule.values():
            if pret >= vehicul.get_pret():
                filtered.append(vehicul)

        return filtered

    def __intersectie(self, list1: list, list2: list) -> list:
        ret_list = []
        for i in range(len(list1)):
            for j in range(len(list2)):
                if list1[i] == list2[j]:
                    ret_list.append(list2[j])

        return ret_list

    def filtrare(self, marca: str, pret: int):
        if marca == "" and pret < 0:
            raise ValueError("Cant filter by nothing")
        elif marca == "" and pret > -1:
            return self.__filtreaza_pret(pret)
        elif marca != "" and pret < 0:
            return self.__filtreaza_marca(marca)
        else:
            return self.__intersectie(
                self.__filtreaza_marca(marca), self.__filtreaza_pret(pret)
            )

    def undo(self):
        cmd = self.__call_stack.pop()
        parti = cmd.split(";")

        if parti[0] == "add":
            vehicul = Vehicul(
                int(parti[1]),
                parti[2],
                int(parti[3]),
                parti[4],
                Data_Expirare(parti[5], parti[6], parti[7]),
            )
            self.__repository.add_to_repo(vehicul)
            return f"Undo : remove car"
        elif parti[0] == "delete":
            self.__repository.remove_from_repo(int(parti[1]))
            return f"Undo : add car"

    def export(self, file_path: str, vehicles: list):
        with open(f"{file_path}", "w") as f:
            for vehicul in vehicles:
                f.write(
                    f"{vehicul.get_id()};{vehicul.get_marca()};{vehicul.get_pret()};{vehicul.get_model()};{vehicul.get_zi()};{vehicul.get_luna()};{vehicul.get_an()}\n"
                )
            f.close()

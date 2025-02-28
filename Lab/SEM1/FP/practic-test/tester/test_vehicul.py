from domain.vehicul import Vehicul
from domain.data_expirare import Data_Expirare

from repository.vehicul_repo import Vehicul_Repo
from service.vehicul_service import Vehicul_Service
from validator.data_validator import Data_Validator
from validator.vehicul_validator import Vehicul_Validaor


class Test_Vehicul(object):
    def __init__(self):
        self.__repo = Vehicul_Repo("test_vehicule.txt")
        self.__service = Vehicul_Service(
            self.__repo, Vehicul_Validaor(), Data_Validator()
        )

    def run_all_tests(self):
        self.__clear_file()
        self.__test_constructor_vehicul()
        self.__test_constructor_data_expirare()
        # self.__test_read_from_file()
        self.__test_adauga_vehicul()
        self.__test_sterge_vehicul()
        self.__test_filtreaza()
        self.__test_undo()
        print("All tests completed")

    def __clear_file(self):
        vehicule = self.__repo.get_all()
        vehicule.clear()

        with open("test_vehicule.txt", "w") as f:
            f.writelines(" ")

    def __test_constructor_data_expirare(self):
        test_zi = 13
        test_luna = 11
        test_an = 2000

        test_data = Data_Expirare(test_zi, test_luna, test_an)

        assert test_data.get_zi() == test_zi
        assert test_data.get_luna() == test_luna
        assert test_data.get_an() == test_an

    def __test_constructor_vehicul(self):
        test_id = 1
        test_marca = "Ford"
        test_pret = 1000
        test_model = "Fiesta"
        test_data = Data_Expirare(15, 11, 2099)

        test_vehicul = Vehicul(test_id, test_marca, test_pret, test_model, test_data)

        assert test_vehicul.get_id() == test_id
        assert test_vehicul.get_marca() == test_marca
        assert test_vehicul.get_pret() == test_pret
        assert test_vehicul.get_model() == test_model
        assert test_vehicul.get_data() == test_data

    # def __test_read_from_file(self):
    #     vehicule = self.__repo.get_all()
    #     test_vehicul = Vehicul(12, "Ford", 1000, "Fiesta", Data_Expirare(15, 11, 2005))
    #     assert vehicule[12] == test_vehicul

    def __test_adauga_vehicul(self):
        test_id = 1
        test_marca = "Ford"
        test_pret = 900
        test_model = "Fiesta"
        test_data = Data_Expirare(15, 11, 2099)

        try:
            self.__service.adauga_vehicul(
                -1, test_marca, test_pret, test_model, 15, 11, 2099
            )
        except ValueError as ve:
            assert str(ve) == "id negative number"

        try:
            self.__service.adauga_vehicul(
                test_id, "", test_pret, test_model, 15, 11, 2099
            )
        except ValueError as ve:
            assert str(ve) == "marca empty string"

        try:
            self.__service.adauga_vehicul(
                test_id, test_marca, -1, test_model, 15, 11, 2099
            )
        except ValueError as ve:
            assert str(ve) == "pret negative number"

        try:
            self.__service.adauga_vehicul(
                test_id, test_marca, test_pret, "", 15, 11, 2099
            )
        except ValueError as ve:
            assert str(ve) == "model empty string"

        try:
            self.__service.adauga_vehicul(
                test_id, test_marca, test_pret, test_model, -1, 11, 2099
            )
        except ValueError as ve:
            assert str(ve) == "zi impossible"

        try:
            self.__service.adauga_vehicul(
                test_id, test_marca, test_pret, test_model, 15, 13, 2099
            )
        except ValueError as ve:
            assert str(ve) == "luna impossible"

        try:
            self.__service.adauga_vehicul(
                test_id, test_marca, test_pret, test_model, 11, 11, -1
            )
        except ValueError as ve:
            assert str(ve) == "an impossible"

        self.__service.adauga_vehicul(
            test_id,
            test_marca,
            test_pret,
            test_model,
            test_data.get_zi(),
            test_data.get_luna(),
            test_data.get_an(),
        )

        try:
            self.__service.adauga_vehicul(
                1, test_marca, test_pret, test_model, 15, 11, 2099
            )
        except ValueError as ve:
            assert str(ve) == "id existent"

        vehicule = self.__repo.get_all()
        test_vehicul = Vehicul(test_id, test_marca, test_pret, test_model, test_data)

        assert vehicule[test_id] == test_vehicul

    def __test_sterge_vehicul(self):
        test_cifra = 5
        self.__service.adauga_vehicul(2, "s", 512, "s", 1, 1, 2000)

        vehicule = self.__repo.get_all()
        previous_len = len(vehicule)
        removed_cars = self.__service.stergere_vehicul(test_cifra)
        test_vehicul = Vehicul(2, "s", 512, "s", Data_Expirare(1, 1, 2000))

        assert test_vehicul.get_id() not in vehicule
        assert previous_len - len(vehicule) == removed_cars

    def __test_filtreaza(self):
        test_marca = "Ford"
        test_pret = 1002

        try:
            self.__service.filtrare("", -1)
        except ValueError as ve:
            assert str(ve) == "Cant filter by nothing"

        vehicule = self.__repo.get_all()

        filtered_list = self.__service.filtrare(test_marca, test_pret)
        assert list(vehicule.values()) == filtered_list

        filtered_list = self.__service.filtrare("", 999)
        assert vehicule[1] == filtered_list[0]

        filtered_list = self.__service.filtrare(test_marca, -1)
        assert list(vehicule.values()) == filtered_list

    def __test_undo(self):
        vehicul = Vehicul(33, "33", 33, "33", Data_Expirare(3, 3, 2003))
        self.__service.adauga_vehicul(33, "33", 33, "33", 3, 3, 2003)

        assert vehicul.get_id() in self.__repo.get_all()
        self.__service.undo()
        assert vehicul.get_id() not in self.__repo.get_all()

        self.__service.adauga_vehicul(33, "33", 33, "33", 3, 3, 2003)
        self.__service.stergere_vehicul(3)
        assert vehicul.get_id() not in self.__repo.get_all()
        self.__service.undo()
        assert vehicul.get_id() in self.__repo.get_all()

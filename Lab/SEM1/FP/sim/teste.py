from domain import Mobila
from repo import Repository
from service import Service


class Teste(object):
    def __init__(self):
        self.__test_repo = Repository("test_mobila.txt")
        self.__test_service = Service(self.__test_repo)

    def run_all_tests(self):
        self.__test_constructor()
        self.__test_citeste_din_fisier()
        self.__test_cautare_pe_baza_tipului()
        self.__test_cumpara_mobila()
        print("Test complete")

    def __test_constructor(self):
        test_cod = "1"
        test_tip = "corp iluminat"
        test_nume = "lampa"
        test_stock = 300
        test_pret = 99.0

        test_mobila = Mobila(test_cod, test_tip, test_nume, test_stock, test_pret)

        assert test_mobila.get_cod() == test_cod
        assert test_mobila.get_tip() == test_tip
        assert test_mobila.get_nume() == test_nume
        assert test_mobila.get_stock() == test_stock
        assert test_mobila.get_pret() == test_pret

    def __test_citeste_din_fisier(self):
        test_cod = "1"
        test_tip = "corp iluminat"
        test_nume = "lampa"
        test_stock = 300
        test_pret = 99.0

        test_mobila = Mobila(test_cod, test_tip, test_nume, test_stock, test_pret)
        assert self.__test_repo.get_all()[test_cod] == test_mobila

    def __test_cautare_pe_baza_tipului(self):
        test_cod = "1"
        test_tip = "corp iluminat"
        test_nume = "lampa"
        test_stock = 300
        test_pret = 99.0

        test_mobila = Mobila(test_cod, test_tip, test_nume, test_stock, test_pret)

        returned_array = self.__test_service.cautare_pe_baza_tipului(test_tip)

        assert returned_array[0] == test_mobila

        try:
            self.__test_service.cautare_pe_baza_tipului("nothing")
        except ValueError as ve:
            assert str(ve) == "tip inexistent"

    def __test_cumpara_mobila(self):
        test_cod = "1"
        test_piese_dorite = 10

        test_toata_mobila = self.__test_repo.get_all()
        previous_stock = test_toata_mobila[test_cod].get_stock()

        self.__test_service.cumpara_mobila(test_cod, test_piese_dorite)

        assert (
            test_toata_mobila[test_cod].get_stock()
            == previous_stock - test_piese_dorite
        )

        test_error_cod = "2"
        test_error_piese_dorite = 12

        try:
            self.__test_service.cumpara_mobila(test_error_cod, test_error_piese_dorite)
        except ValueError as ve:
            assert str(ve) == "stock mic"

        test_false_cod = -1
        try:
            self.__test_service.cumpara_mobila(test_false_cod, 1)
        except ValueError as ve:
            assert str(ve) == "cod inexistent"

        test_false_nr_piese = 0
        try:
            self.__test_service.cumpara_mobila("1", test_false_nr_piese)
        except ValueError as ve:
            assert str(ve) == "eroare logica"

        # resetez stockul de la test_cod = 1 ca sa nu puste la 30 de rulari
        test_toata_mobila[test_cod].set_stock(300)
        self.__test_repo.modify_mobila(test_toata_mobila[test_cod])

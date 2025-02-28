import datetime
from sedinta import Sedinta
from repo import Repository
from validator import Validator
from service import Service


class Tester:
    def __init__(self):
        self.__repo = Repository("test_sedinte.txt")
        self.__service = Service(self.__repo, Validator())

    def run_all_tests(self):
        self.__clear_file()
        self.__test_constructor_sedinta()
        self.__test_show_tomorrow()
        self.__test_adauga_sedinta()
        self.__test_tabel()
        self.__test_filter_and_export()
        print("All tests complete")

    def __clear_file(self):
        sedinte = self.__repo.get_all()
        sedinte.clear()
        with open("test_sedinte.txt", "w") as f:
            f.write(" ")

    def __test_constructor_sedinta(self):
        data = datetime.date(2025, 11, 15)
        ora = datetime.time(8, 0)
        subiect = "FP"
        e_extra = True

        sedinta = Sedinta(data, ora, subiect, e_extra)

        assert sedinta.get_data() == data
        assert sedinta.get_ora() == ora
        assert sedinta.get_subiect() == subiect
        assert sedinta.get_e_extra() == e_extra

    def __test_show_tomorrow(self):
        pass

    def __test_adauga_sedinta(self):
        zi = 31
        luna = 1
        ora = 8
        minut = 0
        subiect = "FP"
        e_extra = 1

        self.__service.adauga_sedinta(zi, luna, ora, minut, subiect, e_extra)

        test_sedinta = Sedinta(
            datetime.date(2025, 1, 31), datetime.time(8, 0), subiect, e_extra
        )
        sedinte = self.__repo.get_all()

        assert test_sedinta == sedinte[subiect]

    def __test_tabel(self):
        self.__service.adauga_sedinta(12, 11, 8, 0, "Ondra", 1)
        self.__service.adauga_sedinta(13, 11, 8, 0, "Ondra1", 1)
        self.__service.adauga_sedinta(14, 11, 8, 0, "Ondra2", 1)

        zi = 12
        luna = 11

        tabel = self.__service.tabel_three(zi, luna)

        assert (
            Sedinta(datetime.date(2025, 11, 12), datetime.time(8, 0), "Ondra", 1)
            == tabel[0]
        )

        assert (
            Sedinta(datetime.date(2025, 11, 13), datetime.time(8, 0), "Ondra1", 1)
            == tabel[1]
        )

        assert (
            Sedinta(datetime.date(2025, 11, 14), datetime.time(8, 0), "Ondra2", 1)
            == tabel[2]
        )

    def __test_filter_and_export(self):
        test_file_path = "test_export_file.txt"
        test_substring = "FP"

        test_reponse = self.__service.filter_and_export(test_file_path, test_substring)

        data = datetime.date(2025, 11, 15)
        ora = datetime.time(8, 0)
        subiect = "FP"
        e_extra = True

        sedinta = Sedinta(data, ora, subiect, e_extra)

        if test_reponse == 1:
            with open(test_file_path, "r") as f:
                line = f.readline()
                if line != "":
                    parti = line.split(";")
                    dater = parti[0].split(".")
                    data = datetime.date(2025, int(dater[0]), int(dater[1]))
                    orer = parti[1].split(".")
                    ora = datetime.time(int(orer[0]), int(orer[1]))
                    subiect = parti[2]
                    e_extra = bool(int(parti[3]))
                    test_sedinta = Sedinta(data, ora, subiect, e_extra)

                    assert test_sedinta == sedinta
                    f.close()

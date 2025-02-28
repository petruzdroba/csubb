import datetime
from sedinta import Sedinta
from repo import Repository


class Service:
    def __init__(self, repo: Repository, validator):
        self.__repo = repo
        self.__validator = validator

    def show_tomorrow(self) -> list:
        """
        Functie care returneaza o lista sortata de sedinte care se petrec maine
        input:
            -
        output:
            list of objects : Sedinta, sortate dupa ora petrecerii
        """
        sedinte = self.__repo.get_all().values()
        today = datetime.date.today()
        if today.day == 31:
            if today.month != 12:
                tomorrow = datetime.date(
                    today.year, today.month + 1, 31 - today.day + 1
                )
            else:
                tomorrow = datetime.date(today.year, 1, 31 - today.day + 1)
        else:
            tomorrow = datetime.date(today.year, today.month, today.day + 1)

        sedinte_maine = []

        for sedinta in sedinte:
            if sedinta.get_data() == tomorrow:
                sedinte_maine.append(sedinta)

        return sorted(sedinte_maine, key=lambda x: x.get_ora().hour)

    def adauga_sedinta(
        self, zi: int, luna: int, ora: int, minut: int, subiect: str, e_extra: bool
    ) -> None:
        """
        Functie care creeaza o sedinta noua si o adauga la toate sedintele
        input:
            zi : int
            luna:int
            ora:int
            minut:int
            subiect:str
            e_extra:bool
        output:nimic
        raises:
            - ValueError daca datele introduse nu sunt valide
        """

        self.__validator.valideaza(zi, luna, ora, minut, subiect, e_extra)

        try:
            data = datetime.date(2025, luna, zi)
        except ValueError:
            raise ValueError("data error")

        try:
            time = datetime.time(ora, minut)
        except ValueError:
            raise ValueError("ora error")

        sedinta = Sedinta(data, time, subiect, e_extra)

        self.__repo.adauga(sedinta)

    def tabel_three(self, zi: int, luna: int) -> list:
        """
        Functie care returneaza o lista de sedinte care se intampla in 3 zile de la o data introduse
        input:
            zi : int
            luna : int
        output:
            lista de obiecte Sedinta
        raises:
            ValueError daca ziua si luna nu sunt corespunzatoare
        """

        self.__validator.valideaza(zi, luna, 8, 8, "s", 1)

        set_date = datetime.date(2025, luna, zi)
        if zi >= 29 and zi <= 31:
            if luna != 12:
                t_set_date = datetime.date(2025, luna + 1, 31 - zi + 3)
            else:
                t_set_date = datetime.date(2025, 1, 31 - zi + 3)
        else:
            t_set_date = datetime.date(2025, luna, zi + 3)
        sedinte = self.__repo.get_all().values()
        tabel = []

        for sedinta in sedinte:
            if sedinta.get_data() >= set_date and sedinta.get_data() <= t_set_date:
                tabel.append(sedinta)

        return tabel

    def filter_and_export(self, file_path: str, word: str) -> bool:
        """
        Functie care filtreaza si apeleaza functia de export
        input:
            file_path : str, unde se va exporta constinutul filtrat
            word : str , sub string care va fi cautat
        output:
            bool : 1 daca e buna treaba , 0 daca nu e buna treaba
        raises:
            ValueError daca oricare dintre cele doua este empty string
        """
        if file_path == "" or word == "":
            raise ValueError("empty strings not allowed")
        sedinte = self.__repo.get_all().values()

        filtered = []

        for sedinta in sedinte:
            if sedinta.get_subiect().lower().find(word.lower()) != -1:
                filtered.append(sedinta)

        if filtered != []:
            self.__export(file_path, filtered)
            return 1
        return 0

    def __export(self, file_path: str, l: list) -> None:
        """
        Functie care exporteaza o lista l intr-un fisier numit file_path
        input:
            file_path : str
            l : list
        return : none
        """
        if file_path.find(".txt") == -1:
            file_path += ".txt"

        with open(file_path, "w") as f:
            for sedinta in l:
                f.write(
                    f"{sedinta.get_data().month}.{sedinta.get_data().day};{sedinta.get_ora().hour}.{sedinta.get_ora().minute};{sedinta.get_subiect()};{int(sedinta.get_e_extra())}\n"
                )

            f.close()

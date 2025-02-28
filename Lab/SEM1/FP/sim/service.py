from repo import Repository


class Service(object):
    def __init__(self, repository: Repository):
        self.__repository = repository

    def cautare_pe_baza_tipului(self, tip: str):
        toata_mobila = self.__repository.get_all()
        filtered_mobila = []

        if tip not in ["scaun", "canapea", "corp iluminat", "spatiu depozitare"]:
            raise ValueError("tip inexistent")

        for mobila in toata_mobila.values():
            if mobila.get_tip() == tip and mobila.get_stock() > 0:
                filtered_mobila.append(mobila)

        if filtered_mobila == []:
            raise ValueError("mobila nu e in stock")

        return filtered_mobila

    def cumpara_mobila(self, cod: str, nr: int):
        toata_mobila = self.__repository.get_all()

        if nr < 1:
            raise ValueError("eroare logica")
        if cod not in toata_mobila:
            raise ValueError("cod inexistent")

        mobila_cumparata = toata_mobila[cod]
        stock = mobila_cumparata.get_stock()

        if stock < nr:
            raise ValueError("stock mic")
        else:
            mobila_cumparata.set_stock(stock - nr)
            self.__repository.modify_mobila(mobila_cumparata)
            return mobila_cumparata

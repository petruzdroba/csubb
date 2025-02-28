from service import Service


class Ui(object):
    def __init__(self, service: Service):
        self.__service = service
        self.__comenzi = {"search": self.__cauta_prin_tip, "buy": self.__cumpara_mobila}

    def __cauta_prin_tip(self):
        input_tip = input("tip>>>")
        try:
            filtered_array = self.__service.cautare_pe_baza_tipului(input_tip)
            for mobila in filtered_array:
                print(mobila)
        except ValueError as ve:
            print(ve)

    def __cumpara_mobila(self):
        input_cod = input("cod>>>")
        input_nr_piese = int(input("nr_piese>>>"))
        try:
            mobila = self.__service.cumpara_mobila(input_cod, input_nr_piese)
            print(
                f"Cumparat {mobila.get_nume()}, stock ramas {mobila.get_stock()}, pret {input_nr_piese * mobila.get_pret()}"
            )
        except ValueError as ve:
            print(ve)

    def run(self):
        while True:
            comanda = input(">>>").lower().strip()

            if comanda == "":
                continue
            if comanda == "exit":
                break
            if comanda in self.__comenzi:
                try:
                    self.__comenzi[comanda]()
                except ValueError as ve:
                    print(ve)
            else:
                print("comanda inexistenta")

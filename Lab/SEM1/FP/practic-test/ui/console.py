from service.vehicul_service import Vehicul_Service


class UI(object):
    def __init__(self, service: Vehicul_Service):
        self.__service = service
        self.__commands = {
            "add": self.__adauga_vehicul,
            "remove": self.__remove_vehicul,
            "filter": self.__filtreaza_vehicule,
            "undo": self.__undo,
            "export": self.__export,
        }

    def __adauga_vehicul(self):
        id = int(input("id>>>"))
        marca = input("marca>>>")
        pret = int(input("pret>>>"))
        model = input("model>>>")
        print("data expirare")
        zi = int(input("zi>>>"))
        luna = int(input("luna>>>"))
        an = int(input("an>>>"))
        self.__service.adauga_vehicul(id, marca, pret, model, zi, luna, an)

    def __remove_vehicul(self):
        cifra = int(input("cifra>>>"))
        nr_deleted = self.__service.stergere_vehicul(cifra)
        print(f"Au fost sterse {nr_deleted} vehicule")

    def __filtreaza_vehicule(self):
        marca = input("marca>>>")
        pret = int(input("pret>>>"))

        if marca != "":
            print(f"Marca {marca}")
        if pret > -1:
            print(f"Pret sub {pret}")
        try:
            filtered_list = self.__service.filtrare(marca, pret)
            if len(filtered_list) > 0:
                for car in filtered_list:
                    print(car)
            else:
                print("Nu am gasit astfel de masini")
        except ValueError as ve:
            print(ve)

    def __undo(self):
        message = self.__service.undo()
        print(message)

    def __export(self):
        file_path = input("file_name>>>")
        marca = input("marca>>>")
        pret = int(input("pret>>>"))
        try:
            filtered_list = self.__service.filtrare(marca, pret)
            if len(filtered_list) > 0:
                self.__service.export(file_path, filtered_list)
            else:
                print("Nu am gasit astfel de masini")
        except ValueError as ve:
            print(ve)

    def run(self):
        while True:
            cmd = input(">>>")
            cmd = cmd.lower().strip()

            if cmd == "":
                continue
            if cmd == "exit" or cmd == "cls":
                break
            if cmd in self.__commands:
                self.__commands[cmd]()
            else:
                print("comanda inexistenta")

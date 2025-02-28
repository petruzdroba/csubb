from service import Service


class UI:
    def __init__(self, service: Service):
        self.__service = service
        self.__comenzi = {
            "add": self.__add,
            "filter": self.__next_three,
            "export": self.__export,
        }
        self.__current_table = []

    def __print_tommorow(self):
        sedinte = self.__service.show_tomorrow()

        print("\nSedintele de maine sunt:")
        if len(sedinte) != 0:
            for sedinta in sedinte:
                print(sedinta)
        else:
            print("Nu sunt sedinte maine")
        print("\n")

    def __add(self):
        try:
            zi = int(input("zi>>>"))
            luna = int(input("luna>>>"))
            ora = int(input("ora>>>"))
            minut = int(input("minut>>>"))
            subiect = input("subiect>>>")
            e_extra = input("extraordinara 0 sau 1>>>")

            try:
                self.__service.adauga_sedinta(
                    zi, luna, ora, minut, subiect, bool(e_extra)
                )
            except ValueError as ve:
                print(str(ve) + " => comanda nesatisfacuta")

        except ValueError:
            print("Date introduse gresit")

    def __next_three(self):
        try:
            zi = int(input("zi>>>"))
            luna = int(input("luna>>>"))

            try:
                table = self.__service.tabel_three(zi, luna)

                self.__current_table = table
            except ValueError as ve:
                print(ve)
        except ValueError:
            print("Dates are wrong")

    def __export(self):
        try:
            file_path = input("file_path>>>")
            sub_str = input("sub_string>>>")

            try:
                response = self.__service.filter_and_export(file_path, sub_str)
                if response:
                    print("file created succesfully")
                else:
                    print("error encountered during exporting process")
            except ValueError as ve:
                print(ve)
        except ValueError:
            print("Input error")

    def __show_annoying_table(self):
        if self.__current_table != []:
            for sedinta in self.__current_table:
                print(sedinta)

    def run(self):
        self.__print_tommorow()
        while True:
            cmd = input(">>>")
            cmd = cmd.lower().strip()
            if cmd == "":
                self.__show_annoying_table()
                continue
            elif cmd == "exit" or cmd == "cls":
                break
            elif cmd in self.__comenzi:
                self.__comenzi[cmd]()
                self.__show_annoying_table()

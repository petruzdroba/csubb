class Mobila(object):
    def __init__(self, cod: str, tip: str, nume: str, stock: int, pret: float):
        self.__cod = cod
        self.__tip = tip
        self.__nume = nume
        self.__stock = stock
        self.__pret = pret

    def get_cod(self):
        return self.__cod

    def get_nume(self):
        return self.__nume

    def get_tip(self):
        return self.__tip

    def get_stock(self):
        return self.__stock

    def get_pret(self):
        return self.__pret

    def set_nume(self, new_name):
        self.__nume = new_name

    def set_stock(self, new_stock):
        self.__stock = new_stock

    def set_tip(self, new_tip):
        self.__tip = new_tip

    def set_pret(self, new_pret):
        self.__pret = new_pret

    def __eq__(self, value):
        return self.__cod == value.get_cod()

    def __str__(self):
        return f"{self.get_cod()} {self.get_tip()} {self.get_nume()} {self.get_stock()} {self.get_pret()}"

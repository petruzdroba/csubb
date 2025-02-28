class Data_Expirare(object):
    def __init__(self, zi: int, luna: int, an: int):
        self.__zi = zi
        self.__luna = luna
        self.__an = an

    def get_zi(self):
        return self.__zi

    def get_luna(self):
        return self.__luna

    def get_an(self):
        return self.__an

    def __str__(self):
        return f"{self.__zi/self.__luna/self.__an}"

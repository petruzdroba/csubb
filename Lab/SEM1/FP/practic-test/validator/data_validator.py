class Data_Validator:
    def __init__(self):
        pass

    def valideaza_data(self, zi: int, luna: int, an: int):
        if zi < 0 or zi > 31:
            raise ValueError("zi impossible")

        if luna < 0 or luna > 12:
            raise ValueError("luna impossible")

        if an < 0 or an > 2300:
            raise ValueError("an impossible")

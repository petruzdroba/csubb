class Vehicul_Validaor(object):
    def __init__(self):
        pass

    def valideaza(self, id: int, marca: str, pret: int, model: str):
        if id < 0:
            raise ValueError("id negative number")
        if marca == "":
            raise ValueError("marca empty string")
        if pret < 0:
            raise ValueError("pret negative number")
        if model == "":
            raise ValueError("model empty string")

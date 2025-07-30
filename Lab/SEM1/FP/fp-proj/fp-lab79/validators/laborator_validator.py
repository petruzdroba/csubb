from domain.problema_lab import Problema_Laborator


class LaboratorValidator(object):
    def __init__(self) -> None:
        pass

    def valideaza_laborator(self, laborator: Problema_Laborator):
        """
        Fucntie care valideaza datele de intrare pt un obiect problema

        """
        if laborator.get_laborator_numar() < 0:
            raise ValueError("numar laborator invalid")
        if laborator.get_descriere() == "":
            raise ValueError("descriere invalida")
        if laborator.get_deadline() == "":
            raise ValueError("deadline invalid")

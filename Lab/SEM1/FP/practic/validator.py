class Validator:
    def __init__(self):
        pass

    def valideaza(
        self, zi: int, luna: int, ora: int, minut: int, subiect: str, e_extra: bool
    ) -> None:
        if zi < 1 or zi > 31:
            raise ValueError("zi error")

        if luna < 1 or luna > 12:
            raise ValueError("luna error")

        if ora < 1 or ora > 24:
            raise ValueError("ora error")

        if minut < 0 or minut > 59:
            raise ValueError("minut error")

        if subiect == "":
            raise ValueError("subiect error")

        if int(e_extra) != 0 and int(e_extra) != 1:
            raise ValueError("extra error")

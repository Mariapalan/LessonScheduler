from src.Users.Person import Person


class Instructor(Person):

    def __init__(
        self, p_id: str, name: str, phone: float, specialization: str, city: str
    ):
        super().__init__(p_id, name, phone)
        self.specialization = specialization

from abc import ABC

class Person(ABC):
    def __init__(self, id: str, name: str, phone: float):
        self.id = id
        self.name = name
        self.phone = phone
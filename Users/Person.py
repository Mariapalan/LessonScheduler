from abc import ABC, abstractmethod
class Person(ABC):
    def __init__(self, name: str, phone_number: float):
        self.name = name
        self.phone_number = phone_number

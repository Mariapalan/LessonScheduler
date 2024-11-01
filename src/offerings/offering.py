import uuid
from typing import Optional, List

from src.Users.instructor import Instructor


class Offering:
    def __init__(self, location, schedule, lesson_type):
        self.__isAvailable = False
        self.__location = location
        self.__schedule = schedule
        self.__lesson_type = lesson_type
        self.__instructor = None
        self.__id = generate_unique_offering_id()

    def __eq__(self, other):
        if isinstance(other, Offering):
            return (self.__location, self.__schedule) == (
                other.__location,
                other.__schedule,
            )
        return False

    def get_id(self) -> str:
        return self.__id

    def check_available(self):
        return self.__isAvailable

    def assign_instructor(self, instructor):
        if self.check_available():
            self.__instructor = instructor
            self.__isAvailable = False
            return True
        return False

    def get_location(self):
        return self.__location

    def get_schedule(self):
        return self.__schedule

    def get_instructor(self) -> Optional[Instructor]:
        return self.__instructor

    def __hash__(self):
        return hash(self.__id)


class OfferingCatalog:
    __offerings: List[Offering] = {}

    @staticmethod
    def is_offering_unique(location, schedule):
        for offering in OfferingCatalog.__offerings:
            if (
                offering.get_location() == location
                and offering.get_schedule() == schedule
            ):
                return False
        return True

    @staticmethod
    def create_offering(location, schedule, lesson_type):
        if OfferingCatalog.is_offering_unique(location, schedule):
            new_offering = Offering(location, schedule, lesson_type)
            OfferingCatalog.add(new_offering)
            return new_offering
        return None

    @staticmethod
    def add(offering):
        OfferingCatalog.__offerings.append(offering)

    @staticmethod
    def get_all_unassigned_offerings():
        return [
            offering
            for offering in OfferingCatalog.__offerings
            if offering.get_instructor() is None
        ]

    @staticmethod
    def select_offering(offering_id: str, instructor: Instructor) -> Optional[Offering]:
        if offering_id in OfferingCatalog.__offerings:
            offering = OfferingCatalog.__offerings[offering_id]
            if offering.assign_instructor(instructor):
                return offering
        return None


def generate_unique_offering_id():
    return str(uuid.uuid4())

class Offering:
    def __init__(self, location, schedule, lesson_type):
        self.isAvailable = False
        self.location = location
        self.schedule = schedule
        self.lesson_type = lesson_type

    def __equals__(self, other):
        if isinstance(other, Offering):
            return (self.location, self.schedule) == (other.location, other.schedule)
        return False


class Offerings:
    offerings_catalog = []

    @staticmethod
    def isOfferingUnique(location, schedule):
        for offering in Offerings.offerings_catalog:
            if offering.location == location and offering.schedule == schedule:
                return False
        return True

    def createOffering(location, schedule, lesson_type):
        if Offerings.isOfferingUnique(location, schedule):
            Offerings.add(Offering(location, schedule, lesson_type))
            return True
        return False

    @staticmethod
    def add(offering):
        Offerings.offerings_catalog.append(offering)

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


class OfferingCatalog:
    offerings = []

    @staticmethod
    def is_offering_unique(location, schedule):
        for offering in OfferingCatalog.offerings:
            if offering.location == location and offering.schedule == schedule:
                return False
        return True

    def create_offering(location, schedule, lesson_type):
        if OfferingCatalog.is_offering_unique(location, schedule):
            OfferingCatalog.add(Offering(location, schedule, lesson_type))
            return True
        return False

    @staticmethod
    def add(offering):
        OfferingCatalog.offerings.append(offering)

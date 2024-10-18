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


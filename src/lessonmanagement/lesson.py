class Lesson:
    def __init__(self, lesson_type: str):
        self.__type = lesson_type


class PrivateLesson(Lesson):
    pass


class GroupLesson(Lesson):
    pass

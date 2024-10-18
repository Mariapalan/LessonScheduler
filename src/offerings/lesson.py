from enum import Enum


Type = Enum("Type", ["GROUP", "PRIVATE"])


class Lesson:
    def __init__(self, lesson_type: Type):
        self.__type = lesson_type

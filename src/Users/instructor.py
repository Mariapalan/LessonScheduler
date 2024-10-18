class Instructor(Person):
    
    def __init__(self, id: str, name: str, phone: float, specialization: str, city: str):
 
        super().__init__(id, name, phone,specialization, city)
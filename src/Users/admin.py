from offerings.offering import Offering
from offerings.offering import OfferingCatalog

class Admin(Person):
    def __init__(self, id: str, name: str, phone: float):
 
        super().__init__(id, name, phone)


    def Create_Offering(self, offering):
       

       
        print(f"Creating offering: {offering.name}")
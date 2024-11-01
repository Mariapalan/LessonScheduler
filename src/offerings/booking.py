import uuid
from offerings.offering import Offering
from users.client import Client
from datetime import date, datetime 

class Booking:
    def __init__(self, client: Client, offering: Offering):
        self.offering = offering
        self.client = client
        self.booking_id = str(uuid.uuid4())
        self.date = date.today()
        self.status = True
        self.time = datetime.now()  
        self.guardian = None
        self.timeslot = None




    def make_Booking(self, client, offering, time_slot):
        if offering.check_available():
            self.client = client
            self.offering = offering
            self.time_slot = time_slot
            self.status = True  
            offering.update_availability(False)  
            return True
        else:
            print("Offering is not available.")
            return False
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
        

    def cancel_booking(self):
        self.status = False
        self.offering.update_availability(True)
        return True
    
    def isAvailable(self):
        return self.status
    
    def get_booking_details(self):

        details = (
        f"Booking ID: {self.booking_id}\n"
        f"Client Name: {self.client.name}\n"
        f"Client ID: {self.client.id}\n"
        f"Offering Location: {self.offering.get_location()}\n"
        f"Lesson Type: {self.offering.lesson_type}\n"
        f"Date: {self.date}\n"
        f"Time Slot: {self.time_slot}\n"
        f"Status: {'Active' if self.status else 'Canceled'}"
    )
        if self.guardian:
            details += f"\nGuardian: {self.guardian.name} (ID: {self.guardian.id})"
        return details

    def update_status(self, new_status: bool):
        self.status = new_status

    def set_guardian(self, guardian: Client):
        self.guardian = guardian

    def change_time_slot(self, new_time_slot):
        self.time_slot = new_time_slot

import uuid
from offerings.offering import Offering
from users.client import Client

class Booking:
    def __init__(self, Client, Offering):
        self.offering = Offering
        self.client = Client
        self.booking_id = str(uuid.uuid4())

 
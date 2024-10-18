from offerings.offering import Offering, OfferingCatalog
from users.admin import Admin
from offerings.location import Location, Schedule


class Console():
	def __init__(self):
		pass

class Console:
    def __init__(self):
        
        pass

    def process_offerings_admin(self, admin):
       
        print(f"Processing offerings for Admin: {admin.name}")
     
    def makeNewOffering(self, location: Location, schedule: Schedule, lesson_type: str):
        new_offering = OfferingCatalog.create_offering(location, schedule, lesson_type)
        if new_offering:
            print("New offering created successfully!")
        else:
            print("Offering already exists.")




    #+makeNewOffering(Location: location, schedule: Schedule)


    # def process_offerings_instructor(self, instructor):
    #     print(f"Processing offerings for Instructor: {instructor.name}")
      
    # def process_offerings_client(self, client):
    #     print(f"Processing offerings for Client: {client.name}")
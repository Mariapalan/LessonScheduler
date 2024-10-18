class Location:
    def __init__(self, address: str, city: str, province: str):
        self.address = address
        self.city = city
        self.province = province
       
    def full_address(self) -> str:
        return f"{self.address}, {self.city}, {self.province}"

    def __str__(self) -> str:
        return self.full_address()

    def get_city(self) -> str:
        return self.city

    def set_city(self, city: str) -> None:
         self.city = city

    def get_province(self) -> str:
        return self.province

    def set_province(self, province: str) -> None:
        self.province = province

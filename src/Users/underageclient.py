class UnderageClient(Client):
    def __init__(self, id: str, name: str, phone: float, guardian: Client):

        super().__init__(id, name, phone)
        self.guardian = guardian 
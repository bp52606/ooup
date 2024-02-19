from Izvor import Izvor


class DatotecniIzvor(Izvor):

    datoteka = None
    broj: list = None
    linije = None
    lineNumber = 0

    def __init__(self, datoteka):
        self.datoteka = datoteka
        datoteka = open(self.datoteka, 'r')
        self.linije = datoteka.readlines()

    def citajBrojeve(self):
        return self.linije[self.lineNumber]
        self.lineNumber+=1

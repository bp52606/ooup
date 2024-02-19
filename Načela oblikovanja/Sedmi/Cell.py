# celija
class Cell:
    exp = None  # sadrzaj celije u stringu
    value = None  # cacheirana vrijednost sadrzaja celije
    observers = set()
    sheet = None
    rowCol = ()

    def __init__(self, exp, value=None, sheet=None):
        self.exp = exp
        self.value = value
        self.observers = set()
        self.sheet = sheet

    def __str__(self):
        return str(self.value)

    def attach(self, observer):
        self.observers.add(observer)

    def detach(self, observer):
        self.observers.remove(observer)

    def setRowCol(self,row,col):
        self.rowCol = (row,col)

    def update(self):
        self.value = self.sheet.evaluate(self)
        self.sheet.cellDict[str(chr(65+self.rowCol[0]))+str(self.rowCol[1]+1)] = self.value
        self.notifyListeners()

    def notifyListeners(self):
        for obs in self.observers:
            obs.update()

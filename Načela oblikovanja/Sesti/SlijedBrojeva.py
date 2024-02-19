from Izvor import Izvor
from TipnickiIzvor import TipnickiIzvor
from File import DataToFile
from Sum import Sum
from Average import Average
from Median import Median
import time


class SlijedBrojeva:
    izvorBrojeva: Izvor = None
    brojevi = []
    observers = []

    def __init__(self, izvorBrojeva):
        self.izvorBrojeva = izvorBrojeva

    def postaviIzvor(self, izvorBrojeva):
        self.izvorBrojeva = izvorBrojeva

    def kreni(self):
        broj = self.izvorBrojeva.citajBrojeve()
        self.brojevi.append(float(broj))
        if broj != "-1":
            self.notify()
        return broj

    def attachObserver(self, observer):
        self.observers.append(observer)

    def detachObserver(self, observer):
        self.observers.remove(observer)

    def notify(self):
        for i in self.observers:
            i.update(self)


if __name__ == "__main__":

    sb = SlijedBrojeva(TipnickiIzvor())
    observer1 = DataToFile()
    observer2 = Sum()
    observer3 = Average()
    observer4 = Median()
    sb.attachObserver(observer1)
    sb.attachObserver(observer2)
    sb.attachObserver(observer3)
    sb.attachObserver(observer4)

    while True:
        n = sb.kreni()
        if int(n) == -1:
            break
        time.sleep(1)

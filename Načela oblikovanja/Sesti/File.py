from Observer import Observer
import datetime


class DataToFile(Observer):
    file = None

    def __init__(self):
        self.file = open("dataToFile.txt", "w")
        self.file.close()

    def update(self, subject):
        self.subject = subject
        self.file = open("dataToFile.txt", "a")
        self.file.write(
            str(self.subject.brojevi[len(self.subject.brojevi) - 1]) + " " + str(datetime.datetime.now()) + "\n")

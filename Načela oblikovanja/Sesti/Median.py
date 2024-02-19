from Observer import Observer
import numpy as np


class Median(Observer):

    def update(self, subject):
        self.subject = subject
        print("Median:", np.median(self.subject.brojevi))

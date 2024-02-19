from Observer import Observer
import numpy as np


class Average(Observer):

    def update(self, subject):
        self.subject = subject
        print("Average:", np.average(self.subject.brojevi))

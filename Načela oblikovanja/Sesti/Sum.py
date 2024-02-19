from Observer import Observer
import numpy as np


class Sum(Observer):

    def update(self, subject):
        self.subject = subject
        print("Sum:", np.sum(self.subject.brojevi))

import numpy as np

from NumberGenerator import NumberGenerator


class RandomGenerator(NumberGenerator):

    sigma = None
    mi = None
    n = None

    def __init__(self,sigma, mi, n):
        self.sigma = sigma
        self.mi = mi
        self.n = n


    def generateNumbers(self):
        return np.random.normal(self.mi, self.sigma, self.n)
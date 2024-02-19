from Percentile import Percentile
import numpy as np


class ClosestPercentile(Percentile):

    def __init__(self):
        super(ClosestPercentile,self).__init__()

    def calculatePercentile(self,numbers,p):
        N = len(numbers)
        n_p = p * N / 100 + 0.5
        min = None
        for i in range(1, N + 1):
            if not min:
                min = i
            elif np.abs(i - n_p) < min:
                min = i

        return numbers[min - 1]

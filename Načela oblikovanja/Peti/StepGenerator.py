import random

from NumberGenerator import NumberGenerator


class StepGenerator(NumberGenerator):
    lowBoundary = None
    highBoundary = None
    step = None

    def __init__(self, lowBoundary, highBoundary, step):
        self.lowBoundary = lowBoundary
        self.highBoundary = highBoundary
        self.step = step

    def generateNumbers(self):
        numbers = []
        for i in range(self.lowBoundary, self.highBoundary+1,self.step):
            numbers.append(i)
        return numbers

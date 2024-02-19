from NumberGenerator import NumberGenerator


class FibonacciGenerator(NumberGenerator):

    n = None

    def __init__(self,n):
        self.n = n

    def generateNumbers(self):
        numbers = []
        a = 0
        b = 1
        numbers.append(b)
        for i in range(1, self.n):
            c = b
            b = b + a
            a = c
            numbers.append(b)

        return numbers
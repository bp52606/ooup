from Percentile import Percentile


class InterpolationPercentile(Percentile):

    def __init__(self):
        super(InterpolationPercentile,self).__init__()

    def calculatePercentile(self,numbers,p):
        N = len(numbers)
        p1 = 100 * (1 - 0.5) / N
        pN = 100 * (N - 0.5) / N
        v1 = numbers[0] + N * (p - p1) * (numbers[1] - numbers[0]) / 100
        vN = numbers[N - 2] + N * (p - pN) * (numbers[N - 1] - numbers[N - 2]) / 100
        for i in range(1, N - 1):
            pi = 100 * (i - 0.5) / N
            # interpolirana vrijednost
            iv = numbers[i - 1] + N * (p - pi) * (numbers[i] - numbers[i - 1]) / 100
            if pi < p1:
                iv = v1

            elif pi > pN:
                iv = vN

            if pi < p < (100 * (i + 1 - 0.5) / N):
                return iv

        return iv
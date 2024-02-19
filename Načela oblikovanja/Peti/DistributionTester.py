from ClosestPercentile import ClosestPercentile
from FibonacciGenerator import FibonacciGenerator
from InterpolationPercentile import InterpolationPercentile
from RandomGenerator import RandomGenerator
from StepGenerator import StepGenerator


class DistributionTester:
    numberGenerator = None
    percentile = None

    def __init__(self, numberGenerator, percentile):
        self.numberGenerator = numberGenerator
        self.percentile = percentile

    def calculate(self):
        percentiles = StepGenerator(10, 90, 10).generateNumbers()
        result = []

        numbers = self.numberGenerator.generateNumbers()
        print("Generated numbers: ", numbers)

        for percent in percentiles:
            result.append(self.percentile.calculatePercentile(numbers, percent))

        return result


if __name__ == "__main__":
    generator = StepGenerator(lowBoundary=0, highBoundary=10, step=3)
    percentile = ClosestPercentile()
    dt = DistributionTester(numberGenerator=generator, percentile=percentile)
    result = dt.calculate()
    print("Result: ", result)
    print()

    generator = FibonacciGenerator(n=10)
    percentile = ClosestPercentile()
    dt = DistributionTester(numberGenerator=generator, percentile=percentile)
    result = dt.calculate()
    print("Result: ", result)
    print()

    generator = FibonacciGenerator(n=10)
    percentile = InterpolationPercentile()
    dt = DistributionTester(numberGenerator=generator, percentile=percentile)
    result = dt.calculate()
    print("Result: ", result)
    print()

    generator = RandomGenerator(mi=0, sigma=1,n=10)
    percentile = InterpolationPercentile()
    dt = DistributionTester(numberGenerator=generator, percentile=percentile)
    result = dt.calculate()
    print("Result: ", result)
    print()

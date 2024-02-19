import numpy as np
import re
import ast
from Cell import Cell


# tablica


class Sheet:
    objects = [[]]  # celije u tablici
    refs = set()
    cellDict = {}
    refsize = 0

    def __init__(self, a, b):
        self.objects = np.zeros((a, b), Cell)

    def cell(self, ref):
        for i in self.objects:
            for j in i:
                if j.exp == ref:
                    return self.objects[i][j]

    def set(self, ref, content):
        self.refs.add(ref)
        row = np.abs(65 - ord(ref[0]))
        column = int(ref[1:]) - 1
        cell = Cell(exp=content, sheet=self)
        if ref in self.cellDict:
            cell = self.objects[row][column]
            cell.exp = content

        self.objects[row][column] = cell

        refs = self.getrefs(cell)

        for refe in refs:
            refe.attach(cell)
            for obs in cell.observers:
                if obs == refe:
                    raise RuntimeError("Action not allowed")

        evaluated = self.evaluate(cell)
        cell.value = evaluated
        self.cellDict[ref] = evaluated

        self.objects[row][column].exp = content
        self.objects[row][column].value = evaluated
        cell.setRowCol(row, column)

        cell.notifyListeners()

    def getrefs(self, cell):
        cells = []
        split = re.split(r'\W+', cell.exp)
        if not split[0].isnumeric():
            for cellExp in split:
                row = np.abs(65 - ord(cellExp[0]))
                column = int(cellExp[1:]) - 1
                cells.append(self.objects[row][column])

        return cells

    def eval_expression(self, exp, variables={}):
        def _eval(node):
            if isinstance(node, ast.Num):
                return node.n
            elif isinstance(node, ast.Name):
                return variables[node.id]
            elif isinstance(node, ast.BinOp):
                return _eval(node.left) + _eval(node.right)
            else:
                raise Exception('Unsupported type {}'.format(node))

        node = ast.parse(exp, mode='eval')
        return _eval(node.body)

    def print(self):

        for i in self.objects:
            print("[ ", end="")
            for j in i:
                print(str(j), end="")
                print(" ", end="")
            print("]")

    def evaluate(self, cell):
        return self.eval_expression(cell.exp, self.cellDict)


if __name__ == "__main__":
    s = Sheet(5, 5)
    print()

    s.set('A1', '2')
    s.set('A2', '5')
    s.set('A3', 'A1+A2')
    s.print()
    print()

    s.set('A1', '4')
    s.set('A4', 'A1+A3')
    s.print()
    print()

    s.set('B1', '4')
    s.set('B4', 'A1+A3+B1')
    s.print()
    print()

    s.set('A1', '2')
    s.print()
    print()

    s.set('D1', '25')
    s.print()
    print()

    try:
        s.set('A1', 'A3')
    except RuntimeError as e:
        print("Caught exception:", e)
    s.print()
    print()

    # rječnik vrijednosti varijabli:
    D = {'a': 5, 'b': 3}
    # definirajmo izraz s varijablama:
    exp_var = 'a+b+a'
    # izračunajmo vrijednost izraza:
    rv = s.eval_expression(exp_var, D)
    # 5+3+5=13
    print(rv)

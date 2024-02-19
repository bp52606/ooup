#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

typedef struct Unary_Function Unary_Function;

typedef double (*PTRDBL)(double, Unary_Function*);
typedef void (*PTRVOID)(Unary_Function*);

typedef struct Unary_Function{
	int lower_bound;
	int upper_bound;
	PTRDBL* values;
	PTRVOID	tabulate;

}Unary_Function;

typedef struct Square {
	Unary_Function unaryFunc;
}Square;

typedef struct Linear {
	Unary_Function unaryFunc;
	double a;
	double b;
}Linear;


double value_at_Linear(double x, Unary_Function* uf)
{
	return ((Linear*)uf)->a * x + ((Linear*)uf)->b;
}

double negative_value_at_Linear(double x, Unary_Function* uf)
{
	return -value_at_Linear(x, uf);
}

double value_at_Square(double x, Unary_Function* uf)
{
	return x * x;
}

double negative_value_at_Square(double x, Unary_Function* uf)
{
	return -value_at_Square(x, uf);
}

void tabulate(Unary_Function* uf)
{
	for(int x = uf->lower_bound; x<=uf->upper_bound; x++)
	{
		printf("f(%d)=%lf\n", x, uf->values[0](x, uf));
	}
}

PTRVOID un = tabulate;
PTRDBL lin[2] = { value_at_Linear, negative_value_at_Linear };
PTRDBL sqr[2] = { value_at_Square, negative_value_at_Square };


Linear* createLinear(int lb, int ub, double a_coef, double b_coef)
{
	struct Linear* linear = malloc(sizeof(Linear));
	linear->a = a_coef;
	linear->b = b_coef;
	linear->unaryFunc.lower_bound = lb;
	linear->unaryFunc.upper_bound = ub;
	linear->unaryFunc.tabulate = tabulate;
	linear->unaryFunc.values = lin;
	return linear;
}

 Square* createSquare(int lb, int ub)
{
	struct Square *square = malloc(sizeof(Square));
	square->unaryFunc.lower_bound = lb;
	square->unaryFunc.upper_bound = ub;
	square->unaryFunc.tabulate = tabulate;
	square->unaryFunc.values = sqr;
	return square;
}

bool same_functions_for_ints(Unary_Function *f1, Unary_Function *f2, double tolerance)
{
	if (f1->lower_bound != f2->lower_bound) return false;
	if (f1->upper_bound != f2->upper_bound) return false;
	for (int x = f1->lower_bound; x <= f1->upper_bound; x++)
	{
		double delta = f1->values[0](x,f1) - f2->values[0](x,f2);
		if (delta < 0) delta = -delta;
		if (delta > tolerance) return false;
	}
	return true;
}

int main(void)
{
	struct Unary_Function* f1 = createSquare(-2, 2);
	f1->tabulate(f1);
	struct Unary_Function* f2 = createLinear(-2, 2, 5, -2);
	f2->tabulate(f2);
	printf("f1==f2: %s\n", same_functions_for_ints(f1, f2, 1E-6) ? "DA" : "NE");
	printf("neg_val f2(1) = %lf\n", f2->values[1](1.0, f2));
	free(f1);
	free(f2);
	return 0;
}
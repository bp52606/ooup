#include <stdio.h>

typedef class B B;

typedef int (*pfun)(void);
typedef int (*pfun2)(B*,int);

class B {
public:
	virtual int __cdecl prva() = 0;
	virtual int __cdecl druga(int) = 0;
};

class D : public B {
public:
	virtual int __cdecl prva() { return 42; }
	virtual int __cdecl druga(int x) { return prva() + x; }
};

void printFunctions(B *pb)
{
	pfun vptr = (int (*)())(*(int***)pb)[0];
	pfun2 vptr2 = (int (*)(B*,int))(*(int***)pb)[1];

	printf("Prva: %d\n", (*vptr)());
	printf("Druga: %d\n", (*vptr2)(pb, 1));
}

int main(void)
{
	B *d = new D();
	printFunctions(d);
}
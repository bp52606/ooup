#include <stdio.h>
class PlainOldClass {
public:
	void set(int x) { x_ = x; };
	int get() { return x_; };
private:
	int x_;
};

class CoolClass {
public:
	virtual void set(int x) { x_ = x; };
	virtual int get() { return x_; };
private:
	int x_;
};

int main() {
	printf("PlainOldClass: %u\n", sizeof(PlainOldClass));
	printf("CoolClass: %u", sizeof(CoolClass));
}
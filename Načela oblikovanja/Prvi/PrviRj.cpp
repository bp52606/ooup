#include <iostream>
#include <assert.h>
#include <stdlib.h>
#include <list>

using namespace std;

struct Point {
	int x; int y;
};

class Shape {
public:
	virtual void draw() = 0;

	virtual void move(Point p) = 0;
};

class Circle : public Shape {

	double radius_;
	Point center_;

	virtual void draw() {
		std::cerr << "in drawCircle\n";
	}

	virtual void move(Point p) {
		std::cerr << "in moveCircle\n";
	}

};
class Rhomb : public Shape {

	Point center_;
	double a;
	double b;


	virtual void draw() {
		std::cerr << "in drawRhomb\n";
	}

	virtual void move(Point p) {
		std::cerr << "in moveRhomb\n";
	}

};

class Square : public Shape {

	double side_;
	Point center_;

	virtual void draw() {
		std::cerr << "in drawSquare\n";
	}

	virtual void move(Point p) {
		std::cerr << "in moveSquare\n";
	}
};

void drawShapes(const std::list < Shape* >& fig) {
	std::list < Shape* >::const_iterator it;
	for (it = fig.begin(); it != fig.end(); ++it) {
		(*it)->draw();
	}
}

void moveShapes(const std::list < Shape* >& fig, Point p) {
	std::list < Shape* >::const_iterator it;
	for (it = fig.begin(); it != fig.end(); ++it) {
		(*it)->move(p);
	}
}

int main() {
	std::list<Shape*> my_list = { new Circle, new Square, new Square, new Rhomb };
	struct Point p;
	p.x = 3;
	p.y = 3;

	drawShapes(my_list);
	moveShapes(my_list, p);
}
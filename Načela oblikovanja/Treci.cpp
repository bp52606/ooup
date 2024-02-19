#include <stdio.h>
#include <iostream>
#include <vector>
#include <set>
#include <string.h>
using namespace std;

template <typename Iterator, typename Predicate>
Iterator mymax(
	Iterator first, Iterator last, Predicate pred) {
	Iterator max = first;
		while(first!=last) {
			if (pred(*first, *max)==1) {
				max = first;
			}
			++first;
		}

		return max;
}
int gt_int(int current, int max) {
	if (current > max) {
		return 1;
	}
	return 0;
}

int gt_char(char current, char max) {
	if (current > max) {
		return 1;
	}
	return 0;
}

int gt_str(const char* current, const char* max) {
	return strcmp(current, max);
}

int main() {

	//int
	int arr_int[] = { 1, 3, 5, 7, 4, 6, 9, 2, 0 };
	const int* maxint = mymax(&arr_int[0],
		&arr_int[sizeof(arr_int) / sizeof(*arr_int)], gt_int);
	std::cout << *maxint << "\n";
	std::vector<int> vect;

	for (int i : arr_int) {
		vect.push_back(i);
	}

	std::cout << *mymax(vect.begin(),
		vect.end(), gt_int) << "\n";

	std::set<int> s;
	for (int i : arr_int) {
		s.insert(i);
	}

	std::cout << *mymax(s.begin(),
		s.end(), gt_int) << "\n";

	char arr_char[] = "Suncana strana ulice";

	//char
	std::cout << *mymax(&arr_char[0],
		&arr_char[sizeof(arr_char) / sizeof(*arr_char)], gt_char) << "\n";

	std::vector<char> vectC;

	for (char i : arr_char) {
		vectC.push_back(i);
	}

	std::cout << *mymax(vectC.begin(),
		vectC.end(), gt_char) << "\n";

	std::set<char> sChar;
	for (char i : arr_char) {
		sChar.insert(i);
	}

	std::cout << *mymax(sChar.begin(),
		sChar.end(), gt_char) << "\n";


	//string

	const char* arr_str[] = {
	   "Gle", "malu", "vocku", "poslije", "kise",
	   "Puna", "je", "kapi", "pa", "ih", "njise"
	};

	std::cout << *mymax(&arr_str[0],
		&arr_str[sizeof(arr_str) / sizeof(*arr_str)], gt_str) << "\n";

	std::vector<const char*> vectS;

	for (const char* i : arr_str) {
		vectS.push_back(i);
	}

	std::cout << *mymax(vectS.begin(),
		vectS.end(), gt_str) << "\n";

	std::set<const char*> sStr;
	for (const char* i : arr_str) {
		sStr.insert(i);
	}

	std::cout << *mymax(sStr.begin(),
		sStr.end(), gt_str) << "\n";

}
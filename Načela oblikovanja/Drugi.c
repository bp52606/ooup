#include<stdio.h>
#include<string.h>


const void* mymax(
	const void* base, size_t nmemb, size_t size,
	int (*compar)(const void*, const void*)) {

	char* max = base;

	char *baza = base;

	for (int i = 1; i < nmemb; ++i) {
		size_t addr = i*size;
		char* curr = baza + addr;
		if (compar((const void*)curr,(const void*)max) > 0) {
			max = curr;
		}


	}
	return max;
}

int gt_int(const void* first, const void* second) {
	int f= *(int*)first;
	int s = *(int*)second;
	if (f > s) {
		return 1;
	}
	return 0;
}

int gt_char(const void* first, const void* second) {
	if (*(char*)first > *(char*)second) {
		return 1;
	}
	return 0;
}

int gt_str(const void* first, const void* second) {

	return strcmp(*(char**)first, *(char**)second);
}


int main() {
	int arr_int[] = { 1, 3, 5, 7, 4, 6, 9, 2, 0 };
	printf("%d\n", *(const int*)mymax((int*)arr_int, 9, sizeof(int), *gt_int));

	char arrayC[] = "Suncana strana ulice";
	printf("%c\n", *(const char*)mymax((char*)arrayC, 19, sizeof(char), *gt_char));

	const char* arr_str[] = {
   "Gle", "malu", "vocku", "poslije", "kise",
   "Puna", "je", "kapi", "pa", "ih", "njise"
	};
	printf("%s\n", *(const char**)mymax((char**)arr_str, 11, sizeof(char*), *gt_str));

}
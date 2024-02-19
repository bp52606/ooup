#include "myfactory.h"
#include <stdio.h>
#include <stdlib.h>

typedef char const* (*PTRFUN)();

typedef struct Tiger {
	PTRFUN* table;
	const char* name;
} Tiger;

char const* name(void *this) {
	return ((Tiger*)this)->name;
}

char const* greet() {
	return "rawr";
}

char const* menu() {
	return "sve oko sebe";
}

int size_of() {
	return sizeof(Tiger);
}

PTRFUN pfun[] = { name, greet, menu };

void construct(Tiger* memory, char const* name) {
	memory->name = name;
	memory->table = pfun;
}

void* create(char const* name) {
	struct Tiger* mal = malloc(size_of());
	construct(mal, name);
	return mal;
}
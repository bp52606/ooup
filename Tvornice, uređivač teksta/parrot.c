#include "myfactory.h"
#include <stdio.h>
#include <stdlib.h>

typedef char const* (*PTRFUN)();

typedef struct Parrot {
	PTRFUN* table;
	const char* name;
} Parrot;

char const* name(void* this) {
	return ((Parrot*)this)->name;
}

char const* greet() {
	return "paaapaa";
}

char const* menu() {
	return "sjemenke";
}

int size_of() {
	return sizeof(Parrot);
}

PTRFUN pfun[] = { name, greet, menu };

void construct(Parrot* memory, char const* name) {
	memory->name = name;
	memory->table = pfun;
}

void* create(char const* name) {
	struct Parrot* mal = malloc(size_of());
	construct(mal, name);
	return mal;
}
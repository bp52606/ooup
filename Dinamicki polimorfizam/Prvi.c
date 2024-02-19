#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef char const* (*PTRFUN)();


typedef struct Animal {
    char const* name;
    PTRFUN* pfun;
} Animal;

char const* dogGreet(void) {
    return "vau!";
}

char const* dogMenu(void) {
    return "kuhanu govedinu";
}

char const* catGreet(void) {
    return "mijau!";
}

char const* catMenu(void) {
    return "konzerviranu tunjevinu";
}

PTRFUN catPfun[] = { catGreet, catMenu };
PTRFUN dogPfun[] = { dogGreet, dogMenu };

void constructDog(Animal* memory, char const* name) {
    memory->name = name;
    memory->pfun = dogPfun;
}

void constructCat(Animal* memory, char const* name) {
    memory->name = name;
    memory->pfun = catPfun;
}

Animal* createDog(char const* name) {
    struct Animal* mal = malloc(sizeof(Animal));
    constructDog(mal, name);
    return mal;
}

Animal* createCat(char const* name) {
    struct Animal* mal = malloc(sizeof(Animal));
    constructCat(mal, name);
    return mal;
}


void animalPrintGreeting(struct Animal* p) {
    printf("%s pozdravlja: %s\n", p->name, p->pfun[0]());
}

void animalPrintMenu(struct Animal* p) {
    printf("%s voli %s\n", p->name, p->pfun[1]());
}

void testAnimals(void) {
    struct Animal* p1 = createDog("Hamlet");
    struct Animal* p2 = createCat("Ofelija");
    struct Animal* p3 = createDog("Polonije");

    animalPrintGreeting(p1);
    animalPrintGreeting(p2);
    animalPrintGreeting(p3);

    animalPrintMenu(p1);
    animalPrintMenu(p2);
    animalPrintMenu(p3);

    free(p1); free(p2); free(p3);
}

Animal* createNDogs(int n, char const* names[]) {
    Animal *mal = malloc(n * sizeof(Animal));
    for (int i = 0; i < n; ++i) {
        constructDog(&mal[i], names[i]);
    }
    return mal;
}

int main(void) {
    testAnimals();

    //kreiranje na gomili
    Animal* animal1 = malloc(sizeof(Animal));
    animal1->name = "AnimalName1";

    //kreiranje na stogu
    Animal animal2;
    animal2.name = "AnimalName2";
    printf("%s\n", animal1->name);
    printf("%s\n", animal2.name);

    //pozivanje funkcije kreairanja n pasa

    char const* names[3] = { "animal1", "animal2", "animal3" };
    Animal* created = createNDogs(3, names);
    for (int i = 0; i < 3; ++i) {
        printf("%s\n", created[i].name);
    }
}
#include "myfactory.h"
#include <stdio.h>
#include <stdlib.h>
#include <windows.h>

typedef void* (*PTRCREATE)(char const*);

void* myfactory(char const* libname, char const* ctorarg) {

    HINSTANCE hinstLib = LoadLibrary(libname);
    if (hinstLib == NULL) {
        printf("ERROR: unable to load DLL\n");
        return NULL;
    }
    else {
        PTRCREATE function = (PTRCREATE)GetProcAddress(hinstLib, "create");
        if (function == NULL) {
            printf("ERROR: unable to find DLL function\n");
            FreeLibrary(hinstLib);
            return NULL;
        }
        return function(ctorarg);
    }
}
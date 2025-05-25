#include <time.h>
#include <randomheader5.h>
#include <stdio.h>

void print_time() {
    time_t now;
    time(&now);
    printf("Current time: %s", ctime(&now));
}

int main() {
    printf("File 5: Time printing\n");
}

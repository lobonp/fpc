#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

void* do_sum(void* arg) {
    const char *path = (const char*) arg;
    FILE *file = fopen(path, "rb");

    int sum = 0;
    int byte;

    while ((byte = fgetc(file)) != EOF) {
        sum += byte;
    }

    fclose(file);
    if (sum >= 0) {
        printf("%s : %d\n", path, sum);
    }
}

int main(int argc, char *argv[]) {
    pthread_t threads[argc];

    for (int i = 1; i < argc; i++) {
        const char *path = argv[i];
        pthread_create(&threads[i], NULL, do_sum, (void*)path);
    }

    for (int i = 1; i < argc; i++) {
        pthread_join(threads[i], NULL);
    }

    return 0;
}

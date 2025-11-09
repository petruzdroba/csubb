#include <arpa/inet.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <stdlib.h>

int main(int argc, char *argv[])
{
    if (argc != 3)
    {
        printf("Err args 1. port tcp 2. ip\n");
        return 1;
    }
    int port_tcp = atoi(argv[1]);

    int client_tcp;
    struct sockaddr_in server;

    client_tcp = socket(AF_INET, SOCK_STREAM, 0);
    if (client_tcp < 0)
    {
        printf("Erroare de creatrea socketului clientului");
        return 1;
    }

    memset(&server, 0, sizeof(server));
    server.sin_port = htons(port_tcp);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = inet_addr(argv[2]);

    if (connect(client_tcp, (struct sockaddr *)&server, sizeof(server)) < 0)
    {
        printf("Eroare de conectare la server");
        return 1;
    }

    char sir[101], comanda[101];

    recv(client_tcp, &sir, sizeof(sir), MSG_WAITALL);
    recv(client_tcp, &comanda, sizeof(comanda), MSG_WAITALL);

    char sir_r[101];

    if (!strcmp(comanda, "UPPER"))
    {
        for (int i = 0; i < strlen(sir); ++i)
        {
            if (sir[i] >= 'a' && sir[i] <='z')
            {
                sir_r[i] = sir[i] - ('a' - 'A');
            }
            else
                sir_r[i] = sir[i];
        }
        sir_r[strlen(sir)] = sir[strlen(sir)];
    }
    else if (!strcmp(comanda, "LOWER"))
    {
        for (int i = 0; i < strlen(sir); ++i)
        {
            if (sir[i] >= 'A' && sir[i] <= 'Z')
            {
                sir_r[i] = sir[i] + ('a' - 'A');
            }
            else
                sir_r[i] = sir[i];
        }
        sir_r[strlen(sir)] = sir[strlen(sir)];
    }
    else if (!strcmp(comanda, "REVERSE"))
    {
        for (int i = 0; i < strlen(sir); ++i)
        {
            sir_r[strlen(sir) - 1 - i] = sir[i];
        }
        sir_r[strlen(sir)] = sir[strlen(sir)];
        printf("%s", sir_r);
    }
    else
    {
        strcpy(sir_r ,"comanda_invalida");
    }

    send(client_tcp, &sir_r, sizeof(sir_r), 0);

    close(client_tcp);
}
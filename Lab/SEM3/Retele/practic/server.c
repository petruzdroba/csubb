#include <arpa/inet.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <stdlib.h>

void deservireClient(int client){
    char sir[101], comanda[101];

    printf("sir = ");
    scanf("%s", sir);

    printf("comanda =");
    scanf(" %s", comanda);

    send(client, &sir, sizeof(sir), 0);
    send(client, &comanda, sizeof(comanda), 0);

    recv(client, &sir, sizeof(sir), 0);
    printf("Sir primit: %s\n", sir);
}

int main(int argc, char* argv[]){
    if(argc != 2){
        printf("Err args 1. port tcp\n");
        return 1;
    }
    int port_tcp = atoi(argv[1]);

    int socket_server_tcp, socket_client_tcp;
    struct sockaddr_in server, client_tcp;

    socket_server_tcp = socket(AF_INET, SOCK_STREAM, 0);
    if(socket_server_tcp < 0){
        printf("Err socket\n");
        return 1;
    }

    memset(&server, 0 , sizeof(server));
    server.sin_addr.s_addr = INADDR_ANY;
    server.sin_port = htons(port_tcp);
    server.sin_family = AF_INET;

    if (bind(socket_server_tcp, (struct sockaddr *)&server, sizeof(server)) < 0)
    {
        printf("Erroare la bind \n");
        return 1;
    }

    listen(socket_server_tcp, 5);
    int len_c = sizeof(client_tcp);
    memset(&client_tcp, 0, len_c);

    while(1){
        socket_client_tcp = accept(socket_server_tcp, (struct sockaddr*)&client_tcp,&len_c);

        if(fork() == 0){
            deservireClient(socket_client_tcp);
            return 0;
        }
    }
}
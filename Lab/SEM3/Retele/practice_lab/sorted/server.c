#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <stdlib.h>
#include <arpa/inet.h>

#include <stdint.h>

void bubble_sort(uint16_t n, uint16_t elements[])
{

    for (uint16_t i = 0; i < n - 1; ++i)
    {
        for (uint16_t j = 0; j < n - i - 1; ++j)
        {
            if (elements[j] > elements[j + 1])
            {
                uint16_t temp = elements[j];
                elements[j] = elements[j + 1];
                elements[j + 1] = temp;
            }
        }
    }
}

int main(int argc, char *argv[])
{
    // TCP
    if (argc != 2)
    {
        printf("Expected 1 argument, port");
        return 1;
    }
    int port = atoi(argv[1]);
    int generated_port = port + 1; // port sent back to client for data retreival

    int socket_server_tcp, socket_client_tcp;
    struct sockaddr_in server_tcp, client_tcp;

    socket_server_tcp = socket(AF_INET, SOCK_STREAM, 0);
    if (socket_server_tcp < 0)
    {
        printf("Eroare la crearea socketului\n");
        return 1;
    }

    memset(&server_tcp, 0, sizeof(server_tcp));
    server_tcp.sin_port = htons(port);
    server_tcp.sin_family = AF_INET;
    server_tcp.sin_addr.s_addr = INADDR_ANY;

    if (bind(socket_server_tcp, (struct sockaddr *)&server_tcp, sizeof(server_tcp)) < 0)
    {
        printf("Erroare la bind \n");
        return 1;
    }

    listen(socket_server_tcp, 5);
    int len_client_tcp = sizeof(client_tcp);
    memset(&client_tcp, 0, len_client_tcp);

    uint16_t n, elemente[101];
    while (1)
    {
        socket_client_tcp = accept(socket_server_tcp, (struct sockaddr *)&client_tcp, &len_client_tcp);
        printf("S-a connectat un client\n");

        recv(socket_client_tcp, &n, sizeof(n), MSG_WAITALL);
        n = ntohs(n);

        for (int i = 0; i < n; ++i)
        {
            uint16_t element;
            recv(socket_client_tcp, &element, sizeof(element), MSG_WAITALL);
            element = ntohs(element);
            elemente[i] = element;
        }
        bubble_sort(n, elemente);

        uint16_t net_generated_port = htons(generated_port);
        send(socket_client_tcp, &net_generated_port, sizeof(net_generated_port), 0);

        // UDP
        int socket_sender;
        struct sockaddr_in client_udp;

        socket_sender = socket(AF_INET, SOCK_DGRAM, 0);
        if (socket_sender < 0)
        {
            printf("Erroare la crearea socket serverului udp\n");
            return 1;
        }

        memset(&client_udp, 0, sizeof(client_udp));
        client_udp.sin_port = htons(generated_port);
        client_udp.sin_family = AF_INET;
        // pt ca nu facem recvfrom -> nu se umple automat
        client_udp.sin_addr.s_addr = client_tcp.sin_addr.s_addr;

        sleep(3);

        // if(bind(socket_sender, (struct sockaddr *)&server_udp, sizeof(server_udp)) <0){
        //     printf("Eroare la bind UDP server\n");
        //     return 1;
        // } fara bind pentru ca doar trimitem date

        n = htons(n);
        sendto(socket_sender, &n, sizeof(n), 0, (struct sockaddr *)&client_udp, sizeof(client_udp));

        n = ntohs(n);
        for (int i = 0; i < n; ++i)
        {
            uint16_t element = elemente[i];
            element = htons(element);
            sendto(socket_sender, &element, sizeof(element), 0, (struct sockaddr *)&client_udp, sizeof(client_udp));
        }
        close(socket_sender);

        close(socket_client_tcp);
    }
}
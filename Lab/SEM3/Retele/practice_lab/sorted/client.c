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
    // TCP
    if (argc != 2)
    {
        printf("Expected 1 argument, port");
        return 1;
    }
    int port = atoi(argv[1]);

    int client_tcp;
    struct sockaddr_in server_tcp;

    client_tcp = socket(AF_INET, SOCK_STREAM, 0);
    if (client_tcp < 0)
    {
        printf("Erroare de creatrea socketului clientului");
        return 1;
    }

    memset(&server_tcp, 0, sizeof(server_tcp));
    server_tcp.sin_port = htons(port);
    server_tcp.sin_family = AF_INET;
    server_tcp.sin_addr.s_addr = inet_addr("127.0.0.1");

    if (connect(client_tcp, (struct sockaddr *)&server_tcp, sizeof(server_tcp)) < 0)
    {
        printf("Eroare de conectare la server_tcp");
        return 1;
    }

    uint16_t n;
    printf("n= ");
    scanf("%hu", &n);

    n = htons(n);
    send(client_tcp, &n, sizeof(n), 0);

    for (int i = 0; i < ntohs(n); ++i)
    {
        uint16_t element;
        printf("e = ");
        scanf("%hu", &element);

        element = htons(element);
        send(client_tcp, &element, sizeof(element), 0);
    }

    uint16_t received_port;
    recv(client_tcp, &received_port, sizeof(received_port), 0);
    received_port = ntohs(received_port);
    printf("New communication port is %hu\n", received_port);

    // UDP

    int client_socket_udp;
    client_socket_udp = socket(AF_INET, SOCK_DGRAM, 0);
    if (client_socket_udp < 0)
    {
        printf("Eroare la crearea socketului clientului \n");
        return 1;
    }

    //Nu e necesar, decat  daca vrem sa primim date doar pe un singur port
    struct sockaddr_in udp_listener;
    memset(&udp_listener, 0, sizeof(udp_listener));
    udp_listener.sin_family = AF_INET;
    udp_listener.sin_addr.s_addr = INADDR_ANY;
    udp_listener.sin_port = htons(received_port);

    if (bind(client_socket_udp, (struct sockaddr *)&udp_listener, sizeof(udp_listener)) < 0)
    {
        printf("Erroare la bind UDP server\n");
        return 1;
    }
    //#

    struct sockaddr_in from;
    int len = sizeof(from);

    recvfrom(client_socket_udp, &n, sizeof(n), 0, (struct sockaddr *)&from, &len);
    n = ntohs(n);

    for (int i = 0; i < n; ++i)
    {
        uint16_t element;
        recvfrom(client_socket_udp, &element, sizeof(element), 0, (struct sockaddr *)&from, &len);
        element = ntohs(element);
        printf("%hu ", element);
    }

    close(client_socket_udp);
}
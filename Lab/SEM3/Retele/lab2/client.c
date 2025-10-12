#include <arpa/inet.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h> 
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <stdlib.h>

 
int main(int argc, char *argv[]) {
  int c;
  struct sockaddr_in server;
  uint16_t newPort;
  
  c = socket(AF_INET, SOCK_STREAM, 0);
  if (c < 0) {
    printf("Eroare la crearea socketului client\n");
    return 1;
  }
  
  memset(&server, 0, sizeof(server));
  server.sin_port = htons(9232);
  server.sin_family = AF_INET;
  server.sin_addr.s_addr = inet_addr("127.0.0.1");
  
  if (connect(c, (struct sockaddr *) &server, sizeof(server)) < 0) {
    printf("Eroare la conectarea la server\n");
    return 1;
  }
  
  recv(c, &newPort, sizeof(newPort), 0);
  newPort = ntohs(newPort);

  int c2;
  struct sockaddr_in server2;
  
  c2 = socket(AF_INET, SOCK_STREAM, 0);
  if (c2 < 0) {
    printf("Eroare la crearea socketului client\n");
    return 1;
  }
  
  memset(&server2, 0, sizeof(server2));
  server2.sin_port = htons(newPort);
  server2.sin_family = AF_INET;
  server2.sin_addr.s_addr = inet_addr("127.0.0.1");

  if (connect(c2, (struct sockaddr *) &server2, sizeof(server2)) < 0) {
    printf("Eroare la conectarea la server\n");
    return 1;
  }

  char secret;
  printf("Secretul este =");
  scanf(" %c", &secret);
  send(c2, &secret, sizeof(secret), 0);

  close(c);
}

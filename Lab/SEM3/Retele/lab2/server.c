#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <stdlib.h>
#include <arpa/inet.h>

 
int main(int argc, char *argv[]) {
  int s;
  struct sockaddr_in server, client;
  int c, l;
  
  s = socket(AF_INET, SOCK_STREAM, 0);
  if (s < 0) {
    printf("Eroare la crearea socketului server\n");
    return 1;
  }
  
  memset(&server, 0, sizeof(server));
  server.sin_port = htons(9232);
  server.sin_family = AF_INET;
  server.sin_addr.s_addr = INADDR_ANY;
  
  if (bind(s, (struct sockaddr *) &server, sizeof(server)) < 0) {
    printf("Eroare la bind\n");
    return 1;
  }
 
  listen(s, 5);
  
  l = sizeof(client);
  memset(&client, 0, sizeof(client));
  uint16_t newPort;
  
  while (1) {
      
      c = accept(s, (struct sockaddr *) &client, &l);
      printf("S-a conectat un client.\n");
      printf("new port =\n");
      scanf("%hu", &newPort);
    
    newPort = htons(newPort);
    send(c, &newPort, sizeof(newPort), 0);

    int s2;
  struct sockaddr_in server2, client2;
  int c2, l;
  
  s2 = socket(AF_INET, SOCK_STREAM, 0);
  if (s2 < 0) {
    printf("Eroare la crearea socketului server2\n");
    return 1;
  }
  
  memset(&server2, 0, sizeof(server2));
  server2.sin_port = htons(newPort);
  server2.sin_family = AF_INET;
  server2.sin_addr.s_addr = INADDR_ANY;
  
  if (bind(s2, (struct sockaddr *) &server2, sizeof(server2)) < 0) {
    printf("Eroare la bind\n");
    return 1;
  }

//   listen(s2, 5);

    
    
    char secret;
    c2 = accept(s2, (struct sockaddr *) &client2, &l);

    recv(c, &secret, sizeof(secret), 0);
    printf("Secretul este %c\n", secret);
  
  
    close(c);
      // sfarsitul deservirii clientului;

  }
}

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
  if(argc != 3){
    printf("2 Arguments expected. 1 IP address. 2 Port\n");
    return 1;
  }
  int port = atoi(argv[2]);

  int c;
  struct sockaddr_in server;
  uint16_t n, suma;
  
  c = socket(AF_INET, SOCK_STREAM, 0);
  if (c < 0) {
    printf("Eroare la crearea socketului client\n");
    return 1;
  }
  
  memset(&server, 0, sizeof(server));
  server.sin_port = htons(port);
  server.sin_family = AF_INET;
  server.sin_addr.s_addr = inet_addr(argv[1]);
  
  if (connect(c, (struct sockaddr *) &server, sizeof(server)) < 0) {
    printf("Eroare la conectarea la server\n");
    return 1;
  }
  
  printf("n = ");
  scanf("%hu", &n);

  n = htons(n);
  send(c, &n, sizeof(n), 0);

  for(int i = 0 ; i < ntohs(n) ; ++i){
    uint16_t e;
    printf("e = ");
    scanf("%hu", &e);
    e = htons(e);
    send(c, &e, sizeof(e), 0);
  }


  recv(c, &suma, sizeof(suma), 0);
  suma = ntohs(suma);
  printf("Suma este %hu\n", suma);
  close(c);
}

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
//   if(argc != 3){
//     printf("2 Arguments expected. 1 IP address. 2 Port\n");
//     return 1;
//   }
//   int port = atoi(argv[2]);

  int c;
  struct sockaddr_in server;
  uint16_t pozitie, lungime;
  char sir[101], subsir[101];
  
  c = socket(AF_INET, SOCK_STREAM, 0);
  if (c < 0) {
    printf("Eroare la crearea socketului client\n");
    return 1;
  }
  
  memset(&server, 0, sizeof(server));
  server.sin_port = htons(1234);
  server.sin_family = AF_INET;
  server.sin_addr.s_addr = inet_addr("127.0.0.1");
  
  if (connect(c, (struct sockaddr *) &server, sizeof(server)) < 0) {
    printf("Eroare la conectarea la server\n");
    return 1;
  }
  
  printf("sir = ");
  scanf("%s", sir);

  send(c, &sir, sizeof(sir), 0);

  printf("pozitie = ");
  scanf("%hu", &pozitie);
  pozitie = htons(pozitie);
  send(c, &pozitie, sizeof(pozitie), 0);

  printf("lungime = ");
  scanf("%hu", &lungime);
  lungime = htons(lungime);
  send(c, &lungime, sizeof(lungime), 0);


  recv(c, &subsir, sizeof(subsir), 0);
  printf("Subsirul este %s\n", subsir);
  close(c);
}

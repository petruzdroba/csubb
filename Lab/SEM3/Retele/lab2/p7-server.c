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
   if(argc != 2){
     printf("2 Arguments expected. 1 IP address. 2 Port\n");
     return 1;
   }
   int port = atoi(argv[1]);

  int s;
  struct sockaddr_in server, client;
  int c, l;
  
  s = socket(AF_INET, SOCK_STREAM, 0);
  if (s < 0) {
    printf("Eroare la crearea socketului server\n");
    return 1;
  }
  
  memset(&server, 0, sizeof(server));
  server.sin_port = htons(port);
  server.sin_family = AF_INET;
  server.sin_addr.s_addr =INADDR_ANY;
  
  if (bind(s, (struct sockaddr *) &server, sizeof(server)) < 0) {
    printf("Eroare la bind\n");
    return 1;
  }
 
  listen(s, 5);
  
  l = sizeof(client);
  memset(&client, 0, sizeof(client));
  
  while (1) {
    uint16_t pozitie, lungime;
    char sir[101], subsir[101];
    c = accept(s, (struct sockaddr *) &client, &l);
    printf("S-a conectat un client.\n");
    // deservirea clientului
    recv(c, &sir, sizeof(sir), MSG_WAITALL);
    
    recv(c, &pozitie, sizeof(pozitie), MSG_WAITALL);
    pozitie = ntohs(pozitie);

    recv(c, &lungime, sizeof(lungime), MSG_WAITALL);
    lungime = ntohs(lungime);

    strncpy(subsir, sir + pozitie, lungime);
    subsir[lungime] = '\0';

    send(c, &subsir, sizeof(subsir), 0);
    close(c);
    // sfarsitul deservirii clientului;
  }
}

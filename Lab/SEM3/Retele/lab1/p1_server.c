#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
 
int main() {
  int s;
  struct sockaddr_in server, client;
  int c, l;
  
  s = socket(AF_INET, SOCK_STREAM, 0);
  if (s < 0) {
    printf("Eroare la crearea socketului server\n");
    return 1;
  }
  
  memset(&server, 0, sizeof(server));
  server.sin_port = htons(1234);
  server.sin_family = AF_INET;
  server.sin_addr.s_addr = INADDR_ANY;
  
  if (bind(s, (struct sockaddr *) &server, sizeof(server)) < 0) {
    printf("Eroare la bind\n");
    return 1;
  }
 
  listen(s, 5);
  
  l = sizeof(client);
  memset(&client, 0, sizeof(client));
  
  while (1) {
    uint16_t n, suma;
    c = accept(s, (struct sockaddr *) &client, &l);
    printf("S-a conectat un client.\n");
    // deservirea clientului
    recv(c, &n, sizeof(n), MSG_WAITALL);
    n = ntohs(n);

    suma = 0;

	for(int i = 0; i < n; ++i){
		uint16_t e;
		recv(c, &e, sizeof(e), MSG_WAITALL);
		e = ntohs(e);
		suma += e;
	}

    suma = htons(suma);
    send(c, &suma, sizeof(suma), 0);
    close(c);
    // sfarsitul deservirii clientului;
  }
}

#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <unistd.h>

 
int main() {
  int s;
  struct sockaddr_in server, client;
  int c, l, i;
  
  s = socket(AF_INET, SOCK_DGRAM, 0);
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
 
  l = sizeof(client);
  memset(&client, 0, sizeof(client));
  char sir[101], ch;

  recvfrom(s, &sir, sizeof(sir),  MSG_WAITALL, (struct sockaddr *) &client, &l);
    recvfrom(s, &ch, sizeof(ch),  MSG_WAITALL, (struct sockaddr *) &client, &l);

    uint16_t n = 0;
  uint16_t poz[50] = {0};

  for(int i = 0 ; sir[i]; ++i){
    if(sir[i] == ch){
        
        poz[n] =i;
        n += 1;
    }
  }

  n = htons(n);
  sendto(s, &n, sizeof(n), 0,   (struct sockaddr *) &client, sizeof(client));

  for(int i = 0; i<n; ++i)
  {
    uint16_t el = poz[i];
    el = htons(el);
    sendto(s, &el, sizeof(el), 0,   (struct sockaddr *) &client, sizeof(client));
  }

  close(s);
}
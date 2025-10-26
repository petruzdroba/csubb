#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
 
int main() {
  int c;
  struct sockaddr_in server;
  unsigned int x;
  
  
  c = socket(AF_INET, SOCK_DGRAM, 0);
  if (c < 0) {
    printf("Eroare la crearea socketului client\n");
    return 1;
  }
  
  memset(&server, 0, sizeof(server));
  server.sin_port = htons(1234);
  server.sin_family = AF_INET;
  server.sin_addr.s_addr = inet_addr("127.0.0.1");
  
  printf("x = ");
  scanf("%u", &x);

  if(x > 255){
    printf("Not 8 bit\n");
    return 1;
  }

  uint8_t nr = (uint8_t) x;
  sendto(c, &nr, sizeof(nr), 0,  (struct sockaddr *) &server, sizeof(server));

  struct sockaddr_in from;
  int l = sizeof(from);

  uint16_t n;
  recvfrom(c, &n, sizeof(n),  0, (struct sockaddr *) &from,&l);
  n = ntohs(n);

  for(int i = 0 ; i< n; ++i){
    uint16_t el;
    recvfrom(c, &el, sizeof(el),  0, (struct sockaddr *) &from,&l);
    el = ntohs(el);
    printf("%hu ", el);
  }
  
  close(c);
}
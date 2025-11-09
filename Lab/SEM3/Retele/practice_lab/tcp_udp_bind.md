## UDP
- You can skip `bind()`. The OS will pick an ephemeral port automatically.
- You can still `sendto()` and `recvfrom()` using that socket.
- Works fine for simple clients sending/receiving, because the OS tracks where to send replies.

## TCP
- You cannot skip `bind()` if you want clients to connect to you.
- TCP is connection-oriented. The server must have a well-known port for `connect()` to succeed.
- If you don’t `bind()`, the OS may assign a random ephemeral port, but then the client has no idea which port to connect to, so `connect()` fails.
- Some OSes let you skip `bind()` and will pick a port when you call `listen()`, but then you have the same problem: the port is random and unknown to clients.
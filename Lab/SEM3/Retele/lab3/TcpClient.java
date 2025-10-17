import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TcpClient {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("2 Arguments expected. 1 IP address. 2 Port");
            System.exit(1);
        }

        String host = args[0];
        int port;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            System.err.println("Invalid port: " + args[1]);
            return;
        }

        try (
            Socket socket = new Socket(host, port);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.print("n = ");
            int n = readUnsigned16(scanner);

            out.writeShort((short) (n & 0xFFFF));
            out.flush();

            for (int i = 0; i < n; ++i) {
                System.out.print("e = ");
                int e = readUnsigned16(scanner);
                out.writeShort((short) (e & 0xFFFF));
            }
            out.flush();

            int suma = in.readUnsignedShort(); // 0..65535
            System.out.println("Suma este " + suma);

        } catch (UnknownHostException uhe) {
            System.err.println("Unknown host: " + host);
        } catch (IOException ioe) {
            System.err.println("I/O error: " + ioe.getMessage());
        }
    }

    private static int readUnsigned16(Scanner scanner) {
        int value;
        while (true) {
            String token;
            if (scanner.hasNext()) {
                token = scanner.next();
            } else {
                throw new IllegalStateException("No input");
            }
            try {
                int v = Integer.parseInt(token);
                if (v < 0 || v > 0xFFFF) {
                    System.out.print("Value out of range (0..65535). Enter again: ");
                    continue;
                }
                value = v;
                break;
            } catch (NumberFormatException ex) {
                System.out.print("Invalid number. Enter again: ");
            }
        }
        return value;
    }
}

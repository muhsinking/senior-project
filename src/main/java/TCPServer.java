/**
 * Created by Muhsin King on 11/14/2015.
 */
import java.io.*;
import java.net.*;

public class TCPServer {

    ServerSocket socket;

    TCPServer(int sock) throws IOException {
        socket = new ServerSocket(sock);
    }

    public static void main(String argv[]) throws Exception {
        TCPServer server = new TCPServer(6789);

        while(true){
            Socket connection = server.socket.accept();
            int rx = server.receive(connection);
            System.out.println("received: " + rx);
            server.send(connection,rx+3);
        }

    }

    public void send(Socket connection, int n)throws IOException {
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeInt(n);
    }

    public int receive(Socket connection) throws IOException {
        DataInputStream in = new DataInputStream(connection.getInputStream());
        return in.readInt();
    }

}
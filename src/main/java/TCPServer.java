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

    public void send(Socket connection, int n)throws IOException {
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeInt(n);
    }

    public int receive(Socket connection) throws IOException {
        DataInputStream in = new DataInputStream(connection.getInputStream());
        return in.readInt();
    }

}
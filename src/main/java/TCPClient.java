/**
 * Created by Muhsin King on 11/14/2015.
 */
import java.io.*;
import java.net.*;

public class TCPClient {
    Socket socket;

    /*

    what I need: send experimental controls.

     */

    TCPClient(String address, int sock) throws IOException {
        this.socket = new Socket(address, sock);
    }

    public static void main(String argv[]) throws Exception {
        TCPClient client = new TCPClient("localhost",6789);
        client.send(client.socket, 22);
        int rx = client.receive(client.socket);
        System.out.println(rx);

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
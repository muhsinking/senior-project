/**
 * Created by Muhsin King on 11/14/2015.
 */
import java.io.*;
import java.net.*;

public class TCPClient {
    Socket socket;

    /*

    send parameters to server
        train length
        # of trains
        size of Ph
        size of Pt

     */

    TCPClient(String address, int sock) throws IOException {
        this.socket = new Socket(address, sock);
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
/**
 * Created by Muhsin King on 11/14/2015.
 */
import java.io.*;
import java.net.*;

public class TCPClient
{
    Socket socket;

    TCPClient(String address, int sock) throws IOException {
        this.socket = new Socket(address, sock);
    }

    public static void main(String argv[]) throws Exception
    {
//        String sentence;
//        String modifiedSentence;
//        BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
//        Socket clientSocket = new Socket("localhost", 6789);
//        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
//        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//        sentence = inFromUser.readLine();
//        outToServer.writeBytes(sentence + '\n');
//        modifiedSentence = inFromServer.readLine();
//        System.out.println("FROM SERVER: " + modifiedSentence);
//        clientSocket.close();

        TCPClient client = new TCPClient("localhost",6789);
        client.send(20);
        int rx = client.receive();
        System.out.println(rx);

    }

    public void send(int n)throws IOException {
        DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
        outToServer.writeInt(n);
    }

    public int receive() throws IOException {
        DataInputStream inFromServer = new DataInputStream(socket.getInputStream());
        return inFromServer.readInt();
    }

}
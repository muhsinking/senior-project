/**
 * Created by Muhsin King on 11/14/2015.
 */
import java.io.*;
import java.net.*;

public class TCPServer
{

    ServerSocket socket;

    TCPServer(int sock) throws IOException {
        socket = new ServerSocket(sock);
    }

    public static void main(String argv[]) throws Exception
    {
        TCPServer server = new TCPServer(6789);

        while(true){
            Socket connection = server.socket.accept();
            int rx = server.receive(connection);
            server.send(connection,rx+1);
        }

//        String clientSentence;
//        String capitalizedSentence;
//        ServerSocket welcomeSocket = new ServerSocket(6789);
//
//        while(true)
//        {
//            Socket connectionSocket = welcomeSocket.accept();
//            BufferedReader inFromClient =
//                    new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
//            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
//            clientSentence = inFromClient.readLine();
//            System.out.println("Received: " + clientSentence);
//            capitalizedSentence = clientSentence.toUpperCase() + '\n';
//            outToClient.writeBytes(capitalizedSentence);
//        }
    }

    public void send(Socket connection, int n)throws IOException {
        DataOutputStream outToClient = new DataOutputStream(connection.getOutputStream());
        outToClient.writeInt(n);
    }

    public int receive(Socket connection) throws IOException {
        DataInputStream inFromClient = new DataInputStream(connection.getInputStream());
        return inFromClient.read();
    }

//    public int receive() throws IOException {
//        DataInputStream inFromServer = new DataInputStream(socket.getInputStream()));
//        return inFromServer.readInt();
//    }
}
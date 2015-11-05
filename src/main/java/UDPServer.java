/**
 * Created by muhsinking on 10/21/15.
 */
import java.io.*;
import java.net.*;
import java.util.Date;

public class UDPServer
{
    final static int RECIEVESIZE = 2048;
    final static int SENDSIZE = 2048;

    DatagramSocket socket;

    public UDPServer() throws Exception{
        socket = new DatagramSocket(9876);
    }

    public UDPServer(int port) throws Exception{
        socket = new DatagramSocket(port);
    }

    public void send(byte[] data, InetAddress IP, int port) throws IOException{
        DatagramPacket packet = new DatagramPacket(data, data.length, IP, port);
        socket.send(packet);
    }

    public DatagramPacket receive() throws IOException{
        byte[] data = new byte[RECIEVESIZE];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        socket.receive(packet);
        return packet;
    }

    public static void main(String args[]) throws Exception
    {
        UDPServer server = new UDPServer();

        DatagramPacket receivePacket = server.receive();

        String sentence = new String(receivePacket.getData());

        System.out.println("RECEIVED: " + sentence);

        String capitalizedSentence = sentence.toUpperCase(); // Capitalize the string received
        byte [] sendData = capitalizedSentence.getBytes();
        server.send(sendData, receivePacket.getAddress(), receivePacket.getPort());
    }
}
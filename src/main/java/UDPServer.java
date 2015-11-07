/**
 * Created by muhsinking on 10/21/15.
 */
import java.io.*;
import java.net.*;

public class UDPServer {
    final static int RECIEVESIZE = 2048;
    final static int SENDSIZE = 2048;

    DatagramSocket socket;

    public void startServer(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }

    public void closeServer(){if(socket != null) socket = null;}

    public void send(byte[] data, InetAddress IP, int port) throws IOException{
        DatagramPacket packet = new DatagramPacket(data, data.length, IP, port);
        socket.send(packet);
    }

    public DatagramPacket receive() throws IOException {
        if(socket != null){
            byte[] data = new byte[RECIEVESIZE];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            socket.receive(packet);
            return packet;
        }
        throw new NullPointerException("server not started");
    }

    public void run() throws Exception {
        try{
            startServer(9876);

            while(true){

                try{
                    DatagramPacket receivePacket = receive();
//                    String sentence = ByteUtils.getTrimString(receivePacket);
                    System.out.println("Received " + receivePacket.getData().length + " bytes.");
//                    String capitalizedSentence = sentence.toUpperCase(); // Capitalize the string received
//                    byte [] sendData = capitalizedSentence.getBytes();
//                    send(sendData, receivePacket.getAddress(), receivePacket.getPort());
                    socket.send(receivePacket);
                }
               catch (IOException err){
                   System.out.println("Error establishing connection "+err.getMessage());
               }
            }
        }
        catch (SocketException err){
            System.out.println("Couldn't connect to port "+err.getMessage());
        }
    }

    public static void main(String args[]) throws Exception {
        UDPServer server = new UDPServer();
        server.run();
    }
}
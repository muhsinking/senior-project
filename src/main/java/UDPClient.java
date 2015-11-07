/**
 * Created by muhsinking on 10/21/15.
 */

import java.io.*;
import java.net.*;

public class UDPClient
{
    DatagramSocket socket;

    public UDPClient() throws SocketException {
        socket = new DatagramSocket();
    }

    public DatagramPacket receive() throws IOException{
        byte[] data = new byte[2048];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        socket.receive(packet);
        return packet;
    }

    public void send(byte[] data, InetAddress IP, int port) throws IOException{
        DatagramPacket packet = new DatagramPacket(data, data.length, IP, port);
        socket.send(packet);
    }

    public void sendPacketPair() throws IOException {
        byte[]  send1 = new byte[2048],
                send2 = new byte[2048];

        DatagramPacket receive1, receive2;

        InetAddress IP = InetAddress.getByName("localhost");



        send(send1, IP, 9876);
        send(send2, IP, 9876);

        long startTime = System.nanoTime();

        receive1 = receive();
        receive2 = receive();

        long receiveTime = System.nanoTime();

        long elapsedTime = (receiveTime-startTime)/1000; // convert to microseconds

        System.out.println(elapsedTime);

        socket.close();
    }

    public static void main(String args[]) throws Exception
    {
        UDPClient client = new UDPClient();
        client.sendPacketPair();
    }


}
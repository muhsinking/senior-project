/**
 * Created by muhsinking on 10/21/15.
 */

import java.io.*;
import java.net.*;

public class UDPClient {
    DatagramSocket socket;

    public UDPClient() throws SocketException {
        socket = new DatagramSocket();
    }

    public DatagramPacket receive() throws IOException {
        byte[] data = new byte[2048];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        socket.receive(packet);
        return packet;
    }

    public void send(byte[] data, InetAddress IP, int port) throws IOException{
        DatagramPacket packet = new DatagramPacket(data, data.length, IP, port);
        socket.send(packet);
    }

    //returns time difference between two packet receipts, the "intra-probe gap"
    public long sendPacketPairIPG() throws IOException {
        byte[]  send1 = new byte[2048],
                send2 = new byte[2048];
        DatagramPacket receive1, receive2;
        InetAddress IP = InetAddress.getByName("10.70.170.166");
        send(send1, IP, 9876);
        send(send2, IP, 9876);

        long receive1Time = System.nanoTime();
        receive1 = receive();
        long receive2Time = System.nanoTime();
        receive2 = receive();

        long intraProbeGap = (receive2Time-receive1Time)/1000; // convert to microseconds

        return intraProbeGap;
    }

    // intra probe gap in microseconds, averaged over n executions
    public long avgIntraProbeGap(int n) throws IOException {
        long sum = 0;
        for(int i = 0; i < n; i++){ sum += sendPacketPairIPG(); }
        return sum/n;
    }

    public static void main(String args[]) throws Exception {
        UDPClient client = new UDPClient();
        long result = client.avgIntraProbeGap(500);
        System.out.println(result);
        client.socket.close();
    }

}
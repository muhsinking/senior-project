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
        byte[] data = new byte[1024];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        socket.receive(packet);
        return packet;
    }

    public void send(byte[] data, InetAddress IP, int port) throws IOException{
        DatagramPacket packet = new DatagramPacket(data, data.length, IP, port);
        socket.send(packet);
    }

    //returns time difference between two packet receipts, the "intra-probe gap"
    public long sendPacketPairIPG(String address, int port) throws IOException {
        byte[]  send1 = new byte[1024],
                send2 = new byte[1024];
        DatagramPacket receive1, receive2;
	    InetAddress IP = InetAddress.getByName(address);
	 
        send(send1, IP, port);
        send(send2, IP, port);

        receive1 = receive();
        long receive1Time = System.nanoTime();
        receive2 = receive();
        long receive2Time = System.nanoTime();

        long intraProbeGap = (receive2Time-receive1Time)/1000; // convert to microseconds

        return intraProbeGap;
    }

    // intra probe gap in microseconds, averaged over n executions
    public long avgIntraProbeGap(int n, String address, int port) throws IOException {
        sendNumTrials(n, address, port);
        long sum = 0;
        for(int i = 0; i < n; i++){ sum += sendPacketPairIPG(address,port); }
        return sum/n;
    }

    public void sendNumTrials(int n, String address, int port) throws IOException {
        byte [] trials = ByteUtils.intToBytes(n);
        InetAddress IP = InetAddress.getByName(address);
        send(trials, IP, 9876);
    }

    public static void main(String args[]) throws Exception {
        UDPClient client = new UDPClient();
        long result = client.avgIntraProbeGap(1000, "169.254.5.28", 9876);
        System.out.println(result);
        client.socket.close();
    }

}

/**
 * Created by muhsinking on 10/21/15.
 */

import java.io.*;
import java.net.*;

public class UDPClient extends UDPSendReceive{

    public UDPClient() throws SocketException {
        socket = new DatagramSocket();
    }

    //returns time difference between two packet receipts, the "intra-probe gap"
    public long packetPairIPG(int size, InetAddress IP, int port) throws IOException {
        byte[]  send1 = new byte[size],
                send2 = new byte[size];
        DatagramPacket receive1, receive2;

        send(send1, IP, port);
        send(send2, IP, port);

        receive1 = receive(size);
        long receive1Time = System.nanoTime();
        receive2 = receive(size);
        long receive2Time = System.nanoTime();

        long intraProbeGap = (receive2Time-receive1Time)/1000; // convert to microseconds

        return intraProbeGap;
    }

    // intra-probe gap between packet pair in microseconds, averaged over n executions
    public long avgPairIPG(int n, int size, InetAddress IP, int port) throws IOException {
        sendInt(n, IP, port);  // send a single int, indicating the number of packet pairs the server should expect to receive
        sendInt(size, IP, port); // send a single int, indicating the size of the packets the server should expect to receive
        DatagramPacket confirmation = receive(4);  // waits to receive confirmation from the server

        long sum = 0;
        for(int i = 0; i < n; i++){ sum += packetPairIPG(size, IP, port); }
        return sum/n;
    }

    public static void main(String args[]) throws Exception {
        UDPClient client = new UDPClient();
//        String address = "169.254.5.28";
        String address = "localhost";
        InetAddress IP = InetAddress.getByName(address);
        long result = client.avgPairIPG(1000, 1024, IP, 9876);
        System.out.println(result);
        client.socket.close();
    }

}

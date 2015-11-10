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
    public long packetPairIPG(int sizeH, int sizeT, InetAddress IP, int port) throws IOException {
        byte[]  send1 = new byte[sizeH],
                send2 = new byte[sizeT];
        DatagramPacket receive1, receive2;

        send(send1, IP, port);
        send(send2, IP, port);

        receive1 = receive(sizeH);
        long receive1Time = System.nanoTime();
        receive2 = receive(sizeT);
        long receive2Time = System.nanoTime();

        long intraProbeGap = (receive2Time-receive1Time)/1000; // convert to microseconds
        return intraProbeGap;
    }

    // intra-probe gap between a packet pair in microseconds, averaged over n executions
    public long avgPairIPG(int n, int sizeH, int sizeT, InetAddress IP, int port) throws IOException {
        System.out.println("Intra-probe gap between head packet of " +
                sizeH + " bytes and tail packet of " +
                sizeT + " bytes, averaged over " + n + " runs:");

        sendInt(n, IP, port);  // send a single int, indicating the number of packet pairs the server should expect to receive
        sendInt(sizeH, IP, port); // send a single int, indicating the size of the head packets the server should expect
        sendInt(sizeT, IP, port); // send a single int, indicating the size of the tail packets the server should expect

        DatagramPacket confirmation = receive(4);  // waits to receive confirmation from the server
        long sum = 0;

        for(int i = 0; i < n; i++){
            sum += packetPairIPG(sizeH, sizeT, IP, port);
        }

        long clientIPG = sum/n;

        DatagramPacket serverResponse = receive(4);
        int serverIPG = ByteUtils.bytesToInt(serverResponse.getData());
        System.out.println("Server - " + serverIPG);
        System.out.println("Client - " + clientIPG);
        return clientIPG;
    }

    public static void main(String args[]) throws Exception {
        UDPClient client = new UDPClient();
//        String address = "169.254.5.28";
//        String address = "localhost";
        String address = "10.70.170.166";
        InetAddress IP = InetAddress.getByName(address);
        long result = client.avgPairIPG(500, 1024, 1024, IP, 9876);
        client.socket.close();
    }
}

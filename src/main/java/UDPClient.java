/**
 * Created by muhsinking on 10/21/15.
 */

import java.io.*;
import java.net.*;

public class UDPClient extends UDPSendReceive{

    public UDPClient() throws SocketException {
        socket = new DatagramSocket();
    }

    //returns time difference between two packet receipts, the "intra-probe gap", in nanoseconds
    public long packetPairIPG(int sizeH, int sizeT, InetAddress IP, int port) throws IOException {
        byte[]  headTx = new byte[sizeH],
                tailTx = new byte[sizeT];
        DatagramPacket headRx, tailRx;

        send(headTx, IP, port);
        send(tailTx, IP, port);

        headRx = receive(sizeH);
        long headRxTime = System.nanoTime();
        tailRx = receive(sizeT);
        long tailRxTime = System.nanoTime();

        long intraProbeGap = (tailRxTime-headRxTime);
        return intraProbeGap;
    }

    // given a resolution string, return the relevant divisor to get this unit from nanoseconds
    // returns -1 if invalid string
    public int getResolutionDivider(String resolution){
        int div = -1;
        switch (resolution){
            case "":div = 1000000000;
                break;
            case "milli":div = 1000000;
                break;
            case "micro":div = 1000;
                break;
            case "nano":div = 1;
        }
        return div;
    }


    // intra-probe gap between a packet pair in microseconds, averaged over n executions
    public long avgPairIPG(int n, int sizeH, int sizeT, InetAddress IP, int port, String resolution) throws IOException {
        System.out.println("Intra-probe gap between head packet of " +
                sizeH + " bytes and tail packet of " +
                sizeT + " bytes, averaged over " + n + " runs:");

        sendInt(n, IP, port);  // send a single int, indicating the number of packet pairs the server should expect to receive
        sendInt(sizeH, IP, port); // send a single int, indicating the size of the head packets the server should expect
        sendInt(sizeT, IP, port); // send a single int, indicating the size of the tail packets the server should expect

        DatagramPacket confirmation = receive(4);  // waits to receive confirmation from the server

        long sum = 0;

        for(int i = 0; i < n; i++){sum += packetPairIPG(sizeH, sizeT, IP, port);}

        int div = getResolutionDivider(resolution);

        if(div < 0){
            div = 1;
            resolution = "nano";
        }

        long clientIPG = sum/n/div;
        DatagramPacket serverResponse = receive(4);
        int serverIPG = ByteUtils.bytesToInt(serverResponse.getData())/div;
        System.out.println("Server - " + serverIPG + " " + resolution + "seconds");
        System.out.println("Client - " + clientIPG + " " + resolution + "seconds");
        return clientIPG;
    }

    public static void main(String args[]) throws Exception {
        UDPClient client = new UDPClient();
//        String address = "169.254.5.28";
    //    String address = "localhost";
//        String address = "10.70.170.166";
      	String address = "169.254.8.188";
	InetAddress IP = InetAddress.getByName(address);
        long result = client.avgPairIPG(2000, 1000, 1000, IP, 9876, "nano");
        client.socket.close();
    }
}



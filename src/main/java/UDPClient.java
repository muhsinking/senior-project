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
}



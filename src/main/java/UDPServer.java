/**
 * Created by muhsinking on 10/21/15.
 */
import java.io.*;
import java.net.*;

public class UDPServer extends UDPSendReceive{

    public UDPServer(int port) throws SocketException{
        socket = new DatagramSocket(port);
    }

    public void closeServer(){if(socket != null) socket = null;}

    //returns time difference between two packet receipts, the "intra-probe gap", in nanoseconds
    public long packetPairIPG(int sizeH, int sizeT){
        long intraProbeGap = -1;
        try{
            DatagramPacket head = receive(sizeH);
            long headRxTime = System.nanoTime();
            DatagramPacket tail = receive(sizeT);
            long tailRxTime = System.nanoTime();
            intraProbeGap = (tailRxTime - headRxTime);
        }
        catch (IOException err){
            System.out.println("Error establishing connection "+err.getMessage());
        }
        return intraProbeGap;
    }
}
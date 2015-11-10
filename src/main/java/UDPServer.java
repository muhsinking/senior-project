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

    //returns time difference between two packet receipts, the "intra-probe gap"
    public long packetPairIPG(int sizeH, int sizeT){
        long intraProbeGap = -1;
        try{
            DatagramPacket receivePacket1 = receive(sizeH);
            long receiveTime1 = System.nanoTime();
            DatagramPacket receivePacket2 = receive(sizeT);
            long receiveTime2 = System.nanoTime();

            socket.send(receivePacket2);
            socket.send(receivePacket1);
            intraProbeGap = (receiveTime2 - receiveTime1) / 1000; // convert to microseconds
        }
        catch (IOException err){
            System.out.println("Error establishing connection "+err.getMessage());
        }
        return intraProbeGap;
    }

    // intra-probe gap between a packet pair in microseconds, averaged over n executions
    public long avgPairIPG() throws IOException{
        DatagramPacket trialPacket = receive(4);
        int n = ByteUtils.bytesToInt(trialPacket.getData());
        DatagramPacket sizeHPacket = receive(4);
        DatagramPacket sizeTPacket = receive(4);
        int sizeH = ByteUtils.bytesToInt(sizeHPacket.getData());
        int sizeT = ByteUtils.bytesToInt(sizeTPacket.getData());
        socket.send(sizeTPacket);
        long sum = 0;

        for(int i = 0; i < n; i++){
            long time = packetPairIPG(sizeH, sizeT);
            if(time >= 0) sum += time;
            else return -1; // returns -1 if any of the trials fails
        }

        sendInt((int)(sum/n),trialPacket.getAddress(),trialPacket.getPort());
        return sum / n;
    }

    public void continuousIPG() throws IOException {
        while(true){
            long avgIPG = avgPairIPG();

            System.out.println(avgIPG);
        }
    }

    public static void main(String args[]) throws Exception {
        UDPServer server = new UDPServer(9876);
        server.continuousIPG();
    }
}
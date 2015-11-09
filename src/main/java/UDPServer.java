/**
 * Created by muhsinking on 10/21/15.
 */
import java.io.*;
import java.net.*;

public class UDPServer {

    DatagramSocket socket;

    public void startServer(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }

    public void closeServer(){if(socket != null) socket = null;}

    public void send(byte[] data, InetAddress IP, int port) throws IOException{
        DatagramPacket packet = new DatagramPacket(data, data.length, IP, port);
        socket.send(packet);
    }

    public DatagramPacket receive(int size) throws IOException {
        if(socket != null){
            byte[] data = new byte[size];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            socket.receive(packet);
            return packet;
        }
        throw new NullPointerException("server not started");
    }

    //returns time difference between two packet receipts, the "intra-probe gap"
    public long packetPairIPG(int size){
        long intraProbeGap = -1;
        try{
            DatagramPacket receivePacket1 = receive(size);
            long receiveTime1 = System.nanoTime();
            DatagramPacket receivePacket2 = receive(size);
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

    // intra-probe gap between packet pair in microseconds, averaged over n executions
    public long avgPairIPG() throws IOException{
        startServer(9876);
        DatagramPacket trialPacket = receive(4);
        DatagramPacket sizePacket = receive(4);
        socket.send(sizePacket);
        int n = ByteUtils.bytesToInt(trialPacket.getData()), size = ByteUtils.bytesToInt(sizePacket.getData());
        long sum = 0;

        for(int i = 0; i < n; i++){
            long time = packetPairIPG(size);
            if(time >= 0) sum += time;
            else return -1; // returns -1 if any of the trials fails
        }

        return sum / n;
    }

    public void receiveContinuous (int size) throws IOException {
        startServer(9876);
        while(true){
            DatagramPacket receivePacket = receive(size);
        }
    }

    public static void main(String args[]) throws Exception {
        UDPServer server = new UDPServer();
        long avgIPG = server.avgPairIPG();
        System.out.println(avgIPG);
    }
}
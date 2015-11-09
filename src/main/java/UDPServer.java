/**
 * Created by muhsinking on 10/21/15.
 */
import java.io.*;
import java.net.*;

public class UDPServer {
    final static int RECEIVESIZE = 1024;

    DatagramSocket socket;

    public void startServer(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }

    public void closeServer(){if(socket != null) socket = null;}

    public void send(byte[] data, InetAddress IP, int port) throws IOException{
        DatagramPacket packet = new DatagramPacket(data, data.length, IP, port);
        socket.send(packet);
    }

    public DatagramPacket receive() throws IOException {
        if(socket != null){
            byte[] data = new byte[RECEIVESIZE];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            socket.receive(packet);
            return packet;
        }
        throw new NullPointerException("server not started");
    }

    public long receivePacketPairIPG(){
        long intraProbeGap = -1;
        try{
            DatagramPacket receivePacket1 = receive();
            long receiveTime1 = System.nanoTime();
            DatagramPacket receivePacket2 = receive();
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

    public long avgIntraProbeGap() throws IOException{
        startServer(9876);
        DatagramPacket trialPacket = receive();
        socket.send(trialPacket);
        int n = ByteUtils.bytesToInt(trialPacket.getData()), fails = 0;
        long sum = 0;

        for(int i = 0; i < n; i++){
            long time = receivePacketPairIPG();
            if(time >= 0) sum += time;
            else fails ++;
        }

        return sum / (n-fails);
    }

    public void receiveContinuous () throws IOException {
        startServer(9876);
        while(true){
            DatagramPacket receivePacket = receive();
        }
    }

    public static void main(String args[]) throws Exception {
        UDPServer server = new UDPServer();
        long avgIPG = server.avgIntraProbeGap();
        System.out.println(avgIPG);
    }
}
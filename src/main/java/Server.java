import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by muhsinking on 2/9/16.
 */

  /*

    parameters to receive
        train length
        # of trains
        size of Ph
        size of Pt

     */

public class Server {
    public static void main(String[] args) throws IOException {
        TCPServer control = new TCPServer(6789);
        UDPServer server = new UDPServer(9876);


        while(true){
            Socket connection = control.socket.accept();
            int trainLength = control.receive(connection);
            int numTrains = control.receive(connection);
            int sH = control.receive(connection);
            int sT = control.receive(connection);
            control.send(connection,1);

            long roundTripTest = server.packetPairIPG(sH,sT);

            for(int i = 0; i < numTrains; i++){
                int sum = 0;
                for(int j = 0; j < trainLength; j++){
                    sum += server.packetPairIPG(sH,sT);
                }
                sum /= trainLength;
                System.out.println(sum);
                control.send(connection,sum);
            }



        }
    }

/*
    // continually accept requests from the client for IPG averages
    public void continuousIPG() throws IOException {
        while(true){
            long avgIPG = avgPairIPG();
            System.out.println(avgIPG);
        }
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
*/

}

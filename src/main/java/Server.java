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

        // main server loop, continually accepts new packet trains
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
                int numValid = 0;

                for(int j = 0; j < trainLength; j++){
                    long IPG = server.packetPairIPG(sH,sT);
                    if(valid(IPG,sH,sT)){
                        sum += IPG;
                        numValid ++;
                    }
                }

                sum /= numValid;
                System.out.println(sum);
                control.send(connection,sum);
            }
        }
    }

    public static boolean valid(long IPG, int sH, int sT){
        return true;
    }
}

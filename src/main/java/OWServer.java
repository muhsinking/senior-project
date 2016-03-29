import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

  /*

    parameters to receive
        train length
        # of trains
        size of Ph
        size of Pt

     */

public class OWServer {
    public static void main(String[] args) throws IOException {
        TCPServer control = new TCPServer(6789);
        UDPServer server = new UDPServer(9876);
        int header = 42;
        PrintWriter writer = new PrintWriter("AvgIPG.txt", "UTF-8");

        // main server loop, continually accepts new packet trains




        while(true){
            Socket connection = control.socket.accept();
            int trainLength = control.receive(connection);
            int sH = control.receive(connection);
            int sT = control.receive(connection);
            int div = 1000;

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
            sum /= div;
            System.out.println("Head " + (sH+header) + " bytes\tTail " + (sT+header) + " bytes\tIntra-probe gap " + sum + " " + "micro" + "seconds");
            writer.println(sum);
        }

    }

    public static boolean valid(long IPG, int sH, int sT){
        return true;
    }
}

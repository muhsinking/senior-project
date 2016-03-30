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
        int header = 42, div = 1000;
        PrintWriter writer = new PrintWriter("AvgIPG.txt", "UTF-8");
	

        // main server loop, continually accepts new packet trains

        while(true){
            // receive control variables from client
            Socket connection = control.socket.accept();
            int trainLength = control.receive(connection);
            int sH = control.receive(connection);
            int sT = control.receive(connection);

            int sum = 0;
            int numValid = 0;
	    System.out.print("\nHead " + (sH+header) + " bytes\tTail " + (sT+header) + " bytes\t");
            for(int j = 0; j < trainLength; j++){
                long IPG = server.packetPairIPG(sH,sT);
                if(valid(IPG,sH,sT)){
                    sum += IPG;
                    numValid ++;
                }
            }

	    int whatever = control.receive(connection);

            sum /= numValid;
            System.out.print("Intra-probe gap " + sum/div + " " + "micro" + "seconds");
            writer.println(sum);
        }

    }

    public static boolean valid(long IPG, int sH, int sT){
        int sTTrasmission = sT * 8 / 10; // estimated transmission time of the tail packet through a 10Mbps link
        if(IPG < sTTrasmission){
            return false;
        }
        return true;
    }
}

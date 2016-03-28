/**
 * Created by muhsinking on 3/20/16.
 */
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by muhsinking on 2/9/16.
 */

public class DebugServer {
    public static void main(String[] args) throws IOException {
        UDPServer server = new UDPServer(9876);
        int sH = 1000, sT = 1000;

        // main server loop, continually accepts new packet pairs
        while(true){
            long IPG = server.packetPairIPG(sH,sT);
	    System.out.println(IPG);
        }
    }

}


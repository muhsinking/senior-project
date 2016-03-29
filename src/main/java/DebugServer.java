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
        int header = 42;
        int sH = Integer.parseInt(args[0])-header; // 46 bytes of header data
        int sT = Integer.parseInt(args[1])-header; // 46 bytes of header data

        // main server loop, continually accepts new packet pairs
        while(true){
            long IPG = server.packetPairIPG(sH,sT);
	        System.out.println(IPG);
        }

    }

}


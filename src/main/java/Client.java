import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by muhsinking on 2/9/16.
 */

/*

        IP addr
        train length (# of pairs)
        # trains
        size of Ph	(64 bytes default)
        size of Pt 	(1500 bytes default)
        interprobe gap (between two packet pairs)
            roundtrip time/2
        resolution

*/

public class Client {
    public static void main(String[] args) throws IOException {
        UDPClient client = new UDPClient();
        //        String address = "169.254.5.28";
                String address = "localhost";
        //        String address = "10.70.170.166";
//        String address = "169.254.8.188";
        InetAddress IP = InetAddress.getByName(address);
        long result = client.avgPairIPG(2000, 1000, 1000, IP, 9876, "nano");
        client.socket.close();
    }
}

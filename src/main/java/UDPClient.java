/**
 * Created by muhsinking on 10/21/15.
 */

import java.io.*;
import java.net.*;

public class UDPClient extends UDPSendReceive{

    public UDPClient() throws SocketException {
        socket = new DatagramSocket();
    }

    // sends two probing packets of given sizes to the server application at the given address
    public void packetPairIPG(int sizeH, int sizeT, InetAddress IP, int port) throws IOException {
        byte[]  headTx = new byte[sizeH],
                tailTx = new byte[sizeT];

        send(headTx, IP, port);
        send(tailTx, IP, port);
    }
}
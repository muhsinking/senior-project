import java.io.IOException;
import java.net.*;

/**
 * Created by muhsinking on 11/9/15.
 */
public class UDPSendReceive {
    DatagramSocket socket;

    // sends data to a specified address over a specified port
    public void send(byte[] data, InetAddress IP, int port) throws IOException {
        DatagramPacket packet = new DatagramPacket(data, data.length, IP, port);
        socket.send(packet);
    }

    // sends a single int
    public void sendInt(int n, InetAddress IP, int port) throws IOException {
        byte [] num = ByteUtils.intToBytes(n);
        send(num, IP, port);
    }

    // returns the next packet received of a given size
    public DatagramPacket receive(int size) throws IOException {
        byte[] data = new byte[size];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        socket.receive(packet);
        return packet;
    }

    public DatagramSocket getSocket(){return socket;}
    public void setSocket(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }
}

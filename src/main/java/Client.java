import javax.sound.midi.SysexMessage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
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

    private static UDPClient client;
    private static TCPClient control;

    // arguments: String IP, int trainLength, int numTrains, int sH, int sT, int IPG, String resolution
    public static void main(String[] args) throws IOException {
        String address = args[0];
        int trainLength = Integer.parseInt(args[1]);
        int numTrains = Integer.parseInt(args[2]);
        int sH = Integer.parseInt(args[3]);
        int sT = Integer.parseInt(args[4]);
        int IPG = Integer.parseInt(args[5]);
        String resolution = args[6];
        InetAddress IP = InetAddress.getByName(address);
        int div = getResolutionDivider(resolution);

        if(div < 0){
            System.out.println("Invalid resolution given, defaulting to milliseconds");
            div = 1;
            resolution = "milli";
        }

        client = new UDPClient();
        control = new TCPClient(address,6789);

        control.send(control.socket, trainLength);
        control.send(control.socket, numTrains);
        control.send(control.socket, sH);
        control.send(control.socket, sT);
        control.send(control.socket, trainLength);

        int[] results = new int[numTrains];

        if(control.receive(control.socket) == 1) {
            for (int i = 0; i < numTrains; i++) {
                for (int j = 0; j < trainLength; j++) {
                    client.packetPairIPG(sH, sT, IP, 9876);
                }
                results[i] = control.receive(control.socket)/div;
                System.out.println(results[i]/div + " " + resolution + "seconds");
            }
        }

        client.socket.close();
    }

    // given a resolution string, return the relevant divisor to get this unit from nanoseconds
    // returns -1 if invalid string
    public static int getResolutionDivider(String resolution){
        int div = -1;
        switch (resolution){
            case "":div = 1000000000;
                break;
            case "milli":div = 1000000;
                break;
            case "micro":div = 1000;
                break;
            case "nano":div = 1;
        }
        return div;
    }

    // intra-probe gap between a packet pair in microseconds, averaged over n executions
/*    public long avgPairIPG(int n, int sizeH, int sizeT, InetAddress IP, int port, String resolution) throws IOException {
        System.out.println("Intra-probe gap between head packet of " +
                sizeH + " bytes and tail packet of " +
                sizeT + " bytes, averaged over " + n + " runs:");

        sendInt(n, IP, port);  // send a single int, indicating the number of packet pairs the server should expect to receive
        sendInt(sizeH, IP, port); // send a single int, indicating the size of the head packets the server should expect
        sendInt(sizeT, IP, port); // send a single int, indicating the size of the tail packets the server should expect

        DatagramPacket confirmation = receive(4);  // waits to receive confirmation from the server

        long sum = 0;

        for(int i = 0; i < n; i++){sum += packetPairIPG(sizeH, sizeT, IP, port);}

        int div = getResolutionDivider(resolution);

        if(div < 0){
            div = 1;
            resolution = "nano";
        }

        long clientIPG = sum/n/div;
        DatagramPacket serverResponse = receive(4);
        int serverIPG = ByteUtils.bytesToInt(serverResponse.getData())/div;
        System.out.println("Server - " + serverIPG + " " + resolution + "seconds");
        System.out.println("Client - " + clientIPG + " " + resolution + "seconds");
        return clientIPG;
    }*/

}

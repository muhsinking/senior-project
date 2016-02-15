import javax.sound.midi.SysexMessage;
import java.io.IOException;
import java.io.PrintWriter;
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
    public static void main(String[] args) throws IOException, InterruptedException {
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
            System.out.println("Invalid resolution given, defaulting to microseconds");
            div = 1000;
            resolution = "micro";
        }

        client = new UDPClient();
        control = new TCPClient(address,6789);

        control.send(control.socket, trainLength);
        control.send(control.socket, numTrains);
        control.send(control.socket, sH);
        control.send(control.socket, sT);
        control.send(control.socket, trainLength);

        int[] results = new int[numTrains];

        // find the roundtrip time gap to space future runs
        long start = System.nanoTime();
        client.packetPairIPG(sH, sT, IP, 9876);
        long end = System.nanoTime();

        IPG = (int) ((end-start)/1000000) * 2;
        if(IPG < 1) IPG = 1;
        System.out.println("Inter-probe gap: " + IPG + " milliseconds.");

        if(control.receive(control.socket) == 1) {
            for (int i = 0; i < numTrains; i++) {
                for (int j = 0; j < trainLength; j++) {
                    client.packetPairIPG(sH, sT, IP, 9876);
                    Thread.sleep(IPG);
                }
                results[i] = control.receive(control.socket)/div;
                System.out.println(results[i] + " " + resolution + "seconds");
            }
        }

        client.socket.close();

        PrintWriter writer = new PrintWriter("AvgIPG.txt", "UTF-8");
        for(int i = 0; i < results.length; i++){
            writer.println(results[i]);
        }
        writer.close();

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
}

/**
 * Created by muhsinking on 3/20/16.
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;

/**
 * Created by muhsinking on 2/9/16.
 */


public class DebugClient {

    private static UDPClient client;
//    private static TCPClient control;

    // arguments: String IP, int trainLength, int numTrains, int sH, int sT, String resolution
    public static void main(String[] args) throws IOException, InterruptedException {
        int header = 42;
        String address = args[0];
        int trainLength = Integer.parseInt(args[1]);
        int numTrains = Integer.parseInt(args[2]);
        int sH = 1000-header; // 46 bytes of header data
        int sT = 1000-header; // 46 bytes of header data
        String resolution = "micro";
        InetAddress IP = InetAddress.getByName(address);
        int div = getResolutionDivider(resolution);

        // if resolution is invalid, use microseconds
        if(div < 0){
            System.out.println("Invalid resolution given, defaulting to microseconds");
            div = 1000;
            resolution = "micro";
        }

        client = new UDPClient();

        int[] results = new int[numTrains];

        // primary experimental loop
        for (int i = 0; i < numTrains; i++) {
            for (int j = 0; j < trainLength; j++) {
                client.packetPairIPG(sH, sT, IP, 9876);
                Thread.sleep(2,0);
            }
            System.out.println("Head " + (sH+header) + " bytes\tTail " + (sT+header) + " bytes\tIntra-probe gap " + results[i] + " " + resolution + "seconds");

            // reduce packet size by 100 bytes for each successive train
            sH -= 100;
            sT -= 100;
        }

        client.socket.close();

        // write to file
        PrintWriter writer = new PrintWriter("AvgIPG.txt", "UTF-8");
        for(int i = 0; i < results.length; i++){
            writer.println(results[i]);
        }
        writer.close();

    }

    // given a resolution string, return the relevant divisor to get this unit from nanoseconds
    // returns -1 if given invalid string
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

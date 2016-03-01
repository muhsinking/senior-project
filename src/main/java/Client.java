import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;

/**
 * Created by muhsinking on 2/9/16.
 */

/*
        IP addr
        train length (# of pairs)
        # trains
        size of Ph	(64 bytes default)
        size of Pt 	(1500 bytes default)
        resolution
*/

public class Client {

    private static UDPClient client;
    private static TCPClient control;

    // arguments: String IP, int trainLength, int numTrains, int sH, int sT, String resolution
    public static void main(String[] args) throws IOException, InterruptedException {
        int header = 42;
        String address = args[0];
        int trainLength = Integer.parseInt(args[1]);
        int numTrains = Integer.parseInt(args[2]);
        int sH = Integer.parseInt(args[3])-header; // 46 bytes of header data
        int sT = Integer.parseInt(args[4])-header; // 46 bytes of header data
        String resolution = args[5];
        InetAddress IP = InetAddress.getByName(address);
        int div = getResolutionDivider(resolution);

        // if resolution is invalid, use microseconds
        if(div < 0){
            System.out.println("Invalid resolution given, defaulting to microseconds");
            div = 1000;
            resolution = "micro";
        }

        client = new UDPClient();
        control = new TCPClient(address,6789);

        // send control variables to server
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

        int IPG = (int) ((end-start)/1000000) * 2;
        if(IPG < 1) IPG = 1;
        System.out.println("Inter-probe gap: " + IPG + " milliseconds.");

        // primary experimental loop
        if(control.receive(control.socket) == 1) {
            for (int i = 0; i < numTrains; i++) {
                for (int j = 0; j < trainLength; j++) {
                    client.packetPairIPG(sH, sT, IP, 9876);
                    Thread.sleep(IPG);
                }
                results[i] = control.receive(control.socket)/div;
                System.out.println("Head " + (sH+header) + " bytes\tTail " + (sT+header) + " bytes\tIntra-probe gap " + results[i] + " " + resolution + "seconds");

                // reduce packet size by 100 bytes for each successive train
                sH -= 100;
                sT -= 100;
            }
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

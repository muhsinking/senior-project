import java.io.IOException;
import java.net.InetAddress;

public class OWClient {

    private static UDPClient client;
    private static TCPClient control;

    // arguments: String IP, int trainLength, int numTrains, int sH, int sT, int hDec, int tDec, int IPGmicro
    public static void main(String[] args) throws IOException, InterruptedException {
        int header = 42;
        String address = args[0];
        int trainLength = Integer.parseInt(args[1]);
        int numTrains = Integer.parseInt(args[2]);
        int sH = Integer.parseInt(args[3])-header;
        int sT = Integer.parseInt(args[4])-header;
        int hDec = Integer.parseInt(args[5]);
        int tDec = Integer.parseInt(args[6]);
        int IPGmicro = Integer.parseInt(args[7]);
        InetAddress IP = InetAddress.getByName(address);
        client = new UDPClient();

        // calculate inter-probe gap
        int IPGnano = IPGmicro * 1000;
        int IPGmilli = IPGnano / 1000000;
        IPGnano = IPGnano % 1000000;
        System.out.println("Inter-probe gap: " + ((IPGmilli*1000)+(IPGnano/1000)) + " microseconds.");

        // send control variables to server
        control = new TCPClient(address,6789);
        control.send(control.socket, numTrains);
        control.send(control.socket, trainLength);
        control.send(control.socket, sH);
        control.send(control.socket, sT);
        control.send(control.socket, hDec);
        control.send(control.socket, tDec);

        // primary experimental loop
        for (int i = 0; i < numTrains; i++) {
            for (int j = 0; j < trainLength; j++) {
                client.packetPairIPG(sH, sT, IP, 9876);
                Thread.sleep(IPGmilli,IPGnano);
            }
            // reduce packet size by the given decrements for each successive train
            sH -= hDec;
            sT -= tDec;
        }
        client.socket.close();
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

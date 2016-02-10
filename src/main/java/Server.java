import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by muhsinking on 2/9/16.
 */
public class Server {
    public static void main(String[] args) throws IOException {
        TCPServer control = new TCPServer(6789);

        Socket connection = control.socket.accept();
        int rx = control.receive(connection);
        System.out.println("received: " + rx);
        control.send(connection,rx+3);


        UDPServer server = new UDPServer(9876);
        server.continuousIPG();
        
    }
}

/**
 * Created by muhsinking on 11/4/15.
 */
import java.net.DatagramPacket;
import java.nio.ByteBuffer;


public class ByteUtils {

    private static ByteBuffer buffer = ByteBuffer.allocate(Long.SIZE); // size, not bytes

    public static byte[] longToBytes(long x) {
        buffer.putLong(0, x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        buffer.put(bytes, 0, bytes.length);
        buffer.flip(); //need flip
        return buffer.getLong();
    }

    public static String getTrimString(DatagramPacket packet){
        return new String(packet.getData(),0,packet.getLength()).trim();
    }


}
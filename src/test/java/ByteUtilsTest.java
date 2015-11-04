import org.junit.Test;

import java.io.*;
import java.net.*;
import static org.junit.Assert.*;

/**
 * Created by muhsinking on 11/4/15.
 */
public class ByteUtilsTest {
    public ByteUtils BU = new ByteUtils();
    @Test
    public void testLongToBytes() throws Exception {
        System.out.println(BU.longToBytes(3));
    }

    @Test
    public void testBytesToLong() throws Exception {
        System.out.println(BU.bytesToLong("whatever".getBytes()));
    }
}
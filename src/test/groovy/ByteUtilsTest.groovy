/**
 * Created by muhsinking on 11/5/15.
 */

class ByteUtilsTest extends GroovyTestCase {
    def longArray = [0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0] as byte[]
    def x = 3
    def intArray = [0, 0, 0, 3] as byte[]

    void testLongToBytes() {
        def result = ByteUtils.longToBytes(x)
        assert result == longArray
    }

    void testBytesToLong() {
        def result = ByteUtils.bytesToLong(longArray)
        assert result == x
    }

    void testGetTrimString() {
        byte[] data = [116, 101, 115, 116, 32, 32, 32, 32]
        String str = ByteUtils.getTrimString(new DatagramPacket(data,data.length))
        assert str.equals("test")
    }

    void testIntToBytes() {
        def result = ByteUtils.intToBytes(x)
        assert result == intArray
    }

    void testBytesToInt() {
        def result = ByteUtils.bytesToInt(intArray)
        assert result == x
    }
}
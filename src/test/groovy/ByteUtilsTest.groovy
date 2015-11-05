/**
 * Created by muhsinking on 11/5/15.
 */

class ByteUtilsTest extends GroovyTestCase {
//    def bu = new ByteUtils()
    def longArray = [0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0] as byte[]
    def x = 3

    void testLongToBytes() {
        def result = ByteUtils.longToBytes(x)
        assert result == longArray

    }

    void testBytesToLong() {

        def result = ByteUtils.bytesToLong(longArray)
        assert result == x
    }
}
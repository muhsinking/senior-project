/**
 * Created by muhsinking on 11/4/15.
 */
class ByteUtilsTest extends GroovyTestCase {
    def bu = new ByteUtils()
    def array = [0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0] as byte[]
    def x = 3

    void testLongToBytes() {
        def result = bu.longToBytes(x)
        assert result == array

    }

    void testBytesToLong() {

        def result = bu.bytesToLong(array)
        assert result == x
    }
}

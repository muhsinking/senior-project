/**
 * Created by muhsinking on 11/9/15.
 */
class UDPServerTest extends GroovyTestCase {
    UDPServer server = new UDPServer(9876)

    void testCloseServer() {
        server.closeServer()
        assert (server.getSocket() == null)
    }

//    void testPacketPairIPG() {
//
//    }
//
//    void testAvgPairIPG() {
//
//    }
//
//    void testReceiveContinuous() {
//
//    }
}

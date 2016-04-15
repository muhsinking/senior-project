/**
 * Created by muhsinking on 4/12/16.
 */

// simulates a cross traffic stream
// delay = packet size / bit rate
public class TrafficSim {
    int packetSize;
    int linkSpeed;
    int delay;
    int intraPacketGap;

    public TrafficSim(int size, int speed){
        packetSize = size;
        linkSpeed = speed;
        intraPacketGap = (packetSize*8)/linkSpeed;
        delay = 1;
    }

    public int step(){
        delay --;
        if(delay == 0){
            delay += intraPacketGap;
            return packetSize*8;
        }
        return -1;
    }
}

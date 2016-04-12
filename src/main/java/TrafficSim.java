/**
 * Created by muhsinking on 4/12/16.
 */

// simulates a cross traffic stream
// delay = packet size / bit rate
public class TrafficSim {
    int packetSize;
    int linkSpeed;
    int spacing;
    int delay;

    public TrafficSim(int size, int speed){
        packetSize = size;
        linkSpeed = speed;
        spacing = (packetSize*8)/linkSpeed;
        delay = 0;
    }

    public int step(){
        if(delay == 0){
            delay += spacing*2;
            return packetSize*8;
        }
        delay --;
        return -1;
    }
}

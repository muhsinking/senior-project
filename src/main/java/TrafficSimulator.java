/**
 * Created by muhsinking on 4/12/16.
 */


public class TrafficSimulator {
    int packetSize;
    int linkSpeed;
    int delay;
    int interPacketGap;

    public TrafficSimulator(int size, int speed){
        packetSize = size;
        linkSpeed = speed;
        interPacketGap = (packetSize*8)/linkSpeed;
        delay = 1;
    }

    // returns in bits the size of the frame that is put on the output link, or -1 if no such frame is sent
    public int step(){
        delay --;
        if(delay == 0){
            delay += interPacketGap;
            return packetSize*8;
        }
        return -1;
    }
}
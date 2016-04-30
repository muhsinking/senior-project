import java.util.Random;

/**
 * Created by muhsinking on 4/15/16.
 */


public class ProbeSimulator {
    int headSize;
    int tailSize;
    int interProbeGap;
    int initialInterProbeGap;
    int intraProbeGap;
    int headDelay;
    int tailDelay;

    // suggested values: 64, 1500, 10000
    public ProbeSimulator(int hs, int ts, int interPG){
        interProbeGap = interPG;
        initialInterProbeGap = interPG;
        headSize = hs;
        tailSize = ts;
        intraProbeGap = tailSize*8/10;
        headDelay = 1;
        tailDelay = intraProbeGap+1;
        System.out.println(headSize + ", " + tailSize + ", " + interProbeGap  + ", " + (intraProbeGap-1));
    }

    // returns in bits the size of the frame that is put on the output link, or -1 if no such packet is sent
    public int step(){
        headDelay--;
        tailDelay--;

        if(headDelay < 1){
            interProbeGap =  initialInterProbeGap + (-1000) + (int)(Math.random() * 1000);
            headDelay += interProbeGap;
            return headSize * 8;
        }

        if(tailDelay < 1){
            tailDelay += interProbeGap;
            return tailSize * 8;
        }

        return -1;
    }

}

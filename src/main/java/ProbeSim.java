/**
 * Created by muhsinking on 4/15/16.
 */
public class ProbeSim {
    int headSize;
    int tailSize;
    int interProbeGap;
    int intraProbeGap;
    int headDelay;
    int tailDelay;

    // defaults: 64, 1500, 5000, 2470
    public ProbeSim(int hs, int ts, int interPG, int intraPG){
        headDelay = 1;
        tailDelay = intraProbeGap+1;
        headSize = hs;
        tailSize = ts;
        interProbeGap = interPG;
        intraProbeGap = intraPG;
    }

    // returns in bits the size of the frame that is put on the output link, or -1 if no such packet is sent
    public int step(){
        headDelay--;
        tailDelay--;
        if(headDelay == 0){
            headDelay = interProbeGap;
            return headSize * 8;
        }
        if(tailDelay == 0){
            tailDelay = headDelay + intraProbeGap;
            return tailSize * 8;
        }
        return -1;
    }

}

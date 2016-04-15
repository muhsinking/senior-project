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

    // suggested values: 64, 1500, 5000, 2470
    public ProbeSim(int hs, int ts, int interPG, int intraPG){
        interProbeGap = interPG;
        intraProbeGap = intraPG;
        headSize = hs;
        tailSize = ts;
        headDelay = 1;
        tailDelay = intraProbeGap+1;
        System.out.println(headSize + ", " + tailSize + ", " + interProbeGap  + ", " + intraProbeGap);
    }

    // returns in bits the size of the frame that is put on the output link, or -1 if no such packet is sent
    public int step(){
        headDelay--;
        tailDelay--;

        if(headDelay < 1){
            // delay between head packets is the inter-probe gap
            headDelay += interProbeGap;
            return headSize * 8;
        }

        if(tailDelay < 1){
            // delay between tail packets is the remaining head delay plus the intra-probe gap
            tailDelay += headDelay + intraProbeGap;
            return tailSize * 8;
        }

        return -1;
    }

}

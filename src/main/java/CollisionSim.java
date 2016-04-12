import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by muhsinking on 4/11/16.
 */

/*  simulates the collision of cross stream
    each loop represents one microsecond
 */
public class CollisionSim {
    public static void main(String[] args){
        LinkedList<Integer> queue = new LinkedList<Integer>();
        int OutputLC = 10;

        TrafficSim ct1 = new TrafficSim(500,5);
        TrafficSim ct2 = new TrafficSim(306,2);
        TrafficSim ct3 = new TrafficSim(71,1);

        while(true){
            if(queue.size() > 0){
                int tempLC = OutputLC;
                while(queue.size() > 0 && tempLC > 0){
                    int packet = queue.remove();
                    int dif = packet - tempLC;
                    if(dif > 0){
                        queue.addFirst(dif);
                        tempLC = 0;
                    }
                    else{
                        tempLC = dif;
                    }
                }
            }

            int packet1 = ct1.step();
            int packet2 = ct2.step();
            int packet3 = ct3.step();

            if(packet1 > 0){
                queue.add(packet1);
            }
            if(packet2 > 0) queue.add(packet2);
            if(packet3 > 0) queue.add(packet1);

            System.out.println("OHQ: " + queue.size());
        }

    }



}

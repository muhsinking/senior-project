import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by muhsinking on 4/11/16.
 */

/*
    simulates the collision of cross stream
    each loop represents one microsecond
 */
public class CollisionSim {
    public static void main(String[] args){
        LinkedList<Integer> queue = new LinkedList<Integer>();
        int OutputLC = 10;
        int elapsedTime = 0;
        TrafficSim ct1 = new TrafficSim(120,4);
        TrafficSim ct2 = new TrafficSim(150,2);
        TrafficSim ct3 = new TrafficSim(64,1);

        int last = 0;
        int queueSize = 0;
        int zeroTime = 0;
        int runs = 10000000;
        int IPG = -1;


        for(int i = 0; i < runs; i++){
            // the first position in the queue represents the output interface
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

            if(packet1 > 0) queue.add(packet1);
            if(packet2 > 0) queue.add(packet2);
            if(packet3 > 0) queue.add(packet1);

            queueSize = queue.size() > 0 ? queue.size() - 1 : 0;
            if(queueSize == 0) zeroTime++;

            if(queueSize != last)
                System.out.println(elapsedTime + " microseconds, OHQ = " + queueSize);



            last = queueSize;
            elapsedTime++;
        }
        double percentZeroTime = (double)zeroTime/runs * 100;
        System.out.println("percentage of time with zero queue: " + percentZeroTime);

    }



}

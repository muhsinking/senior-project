import com.sun.org.glassfish.external.probe.provider.annotations.Probe;

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
        LinkedList<List<Integer>> queue = new LinkedList<List<Integer>>();
        int OutputLC = 10;
        int elapsedTime = 0;
        TrafficSim ct1 = new TrafficSim(65,1);
        TrafficSim ct2 = new TrafficSim(65,1);
        TrafficSim ct3 = new TrafficSim(65,1);
        ProbeSim probe = new ProbeSim(64, 1500, 10000, 2470);

        int last = 0;
        int queueSize = 0;
        int zeroTime = 0;
        int runs = 10000000;
        boolean IPGCounting = false;
        int IPGCounter = 0;
        int probeCounter = 0;
        int reducedIPGCounter = 0;


        for(int i = 0; i < runs; i++){
            // the first position in the queue represents the output interface
            // each member of the queue is a list of two values:
            // the amount of the packet that still needs to be processed (index 0)
            // and the total size of the packet (index 1)
            if(queue.size() > 0){
                int tempLC = OutputLC;
                while(queue.size() > 0 && tempLC > 0){
                    List<Integer> packet = queue.remove();
                    int dif = packet.get(0) - tempLC;

                    // if packet was not completely processed, put it back into the queue
                    if(dif > 0){
                        packet.set(0,dif);
                        queue.addFirst(packet);
                        tempLC = 0;
                    }

                    // if it was completely processed, loop back around, and readout intra probe gap if applicable
                    else{
                        if(packet.get(1) == 64*8){
                            IPGCounting = true;
                        }
                        else if(packet.get(1) == 1500*8){
                            System.out.println(elapsedTime + " microseconds, Intra-probe gap: " + IPGCounter + " microseconds.");
                            if(IPGCounter < 3618) reducedIPGCounter++;
                            probeCounter++;
                            IPGCounting = false;
                            IPGCounter = 0;
                        }
                        tempLC = dif;
                    }
                }
            }

            if(IPGCounting) IPGCounter++;

            int packet1 = ct1.step();
            int packet2 = ct2.step();
            int packet3 = ct3.step();
            int probePacket = probe.step();

            if(packet1 > 0){
                List<Integer> p1= new ArrayList<Integer>();
                p1.add(0,packet1);
                p1.add(1,packet2);
                queue.add(p1);
            }

            if(packet2 > 0){
                List<Integer> p2= new ArrayList<Integer>();
                p2.add(0,packet2);
                p2.add(1,packet2);
                queue.add(p2);
            }

            if(packet3 > 0){
                List<Integer> p3= new ArrayList<Integer>();
                p3.add(0,packet3);
                p3.add(1,packet3);
                queue.add(p3);
            }

            if(probePacket > 0){
                List<Integer> pp= new ArrayList<Integer>();
                pp.add(probePacket);
                pp.add(probePacket);
                queue.add(pp);
                System.out.println(elapsedTime + " microseconds, added packet of size " + probePacket);
            }

            queueSize = queue.size() > 0 ? queue.size() - 1 : 0;
            if(queueSize == 0) zeroTime++;

//            if(queueSize != last)
//                System.out.println(elapsedTime + " microseconds, OHQ = " + queueSize);

            last = queueSize;
            elapsedTime++;
        }
        double percentZeroTime = (double)zeroTime/runs * 100;
        System.out.println("percentage of time with zero queue: " + percentZeroTime);
        System.out.println("Intra-probe gap reduced for " + reducedIPGCounter + " out of " + probeCounter + " probes.");

    }



}

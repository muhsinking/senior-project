
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;

/*
    simulates the an output queue in a router, supporting up to three cross traffic streams
    also simulates the probe-gap model for measuring queueing delay
    each loop represents one microsecond
*/

public class RouterSimulator {
    public static final int TRAFFICID = 0;
    public static final int HEADID = 1;
    public static final int TAILID = 2;
    public static void main(String[] args) throws IOException {

        FileWriter writer1 = new FileWriter("simulationQL1.csv");
        FileWriter writer2 = new FileWriter("simulationQL2.csv");
        FileWriter writer3 = new FileWriter("simulationQL3.csv");

        LinkedList<List<Integer>> queue = new LinkedList<List<Integer>>();

        int OutputLC = 10;
        int elapsedTime = 0;
        int headSize = 64;
        int tailSize = 1500;
        TrafficSimulator ct1 = new TrafficSimulator(64,4);
        TrafficSimulator ct2 = new TrafficSimulator(100,1);
        TrafficSimulator ct3 = new TrafficSimulator(150,2);
        ProbeSimulator probe = new ProbeSimulator(headSize, tailSize, 10000);

        // dispersion gap plus transmission time of tail packet
        int IPGTheoretical = (tailSize*8/10) - (headSize*8/10) + (tailSize*8/10);
        System.out.println(IPGTheoretical);
        int last = 0;
        int queueSize = 0;
        int zeroTime = 0;
        int runs = 10000000;
        boolean IPGCounting = false;
        int IPGCounter = 0;
        int probeCounter = 0;
        int reducedIPGCounter = 0;
        int compressionTotal = 0;
        int trueDelayTotal = 0;
        int headQueueSize = 0;
        int headQueueDelayCounter = 0;
        int headQueueDelay = 0;
        // tracks the total amount of dispersion for each size of queue at heading packet entry
        int[][] queueSizeCompressionTotals = new int[10][2];

        // set up CSV headers
        writer1.append("time (micoseconds),dispersion reduction,actual delay,packets ahead at entry,\n");
        writer2.append("time (micoseconds),dispersion reduction,actual delay,packets ahead at entry,\n");
        writer3.append("time (micoseconds),dispersion reduction,actual delay,packets ahead at entry,\n");

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
                    headQueueDelayCounter ++;


                    // if packet was not completely processed, put it back into the queue
                    if(dif > 0){
                        packet.set(0,dif);
                        queue.addFirst(packet);
                        tempLC = 0;
                    }

                    // if it was completely processed, loop back around, and readout intra probe gap if applicable
                    else{
                        // if it was a heading packet, begin measuring the intra-probe gap
                        if(packet.get(1) == HEADID){
                            IPGCounting = true;
                            headQueueSize = packet.get(2);
                            // actual queueing delay is the time it took from entering the queue to leaving the queue, minus the transmission time of the head
                            headQueueDelay = headQueueDelayCounter - (headSize*8/10);

                        }
                        // if it was a trailing packet, finish measuring the intra-probe gap
                        else if(packet.get(1) == TAILID){
//                            System.out.println(elapsedTime + " microseconds, Intra-probe gap: " + IPGCounter + " microseconds.");
//                            System.out.println(elapsedTime + " microseconds, dispersion reduction: " + (IPGTheoretical - IPGCounter) +
//                                    ", queue size at head entry: " + headQueueSize);

                            if(IPGCounter < IPGTheoretical && headQueueSize > 0){

                                System.out.println(elapsedTime + " microseconds, dispersion reduction: " + (IPGTheoretical - IPGCounter) +
                                        ", actual queueing delay of head: " + headQueueDelay + " packets ahead at head entry: " + headQueueSize);


                                // output to appropriate CSV file based on queue length
                                if(headQueueSize == 1) writer1.append(elapsedTime + "," + (IPGTheoretical-IPGCounter) + "," + headQueueDelay + "," + headQueueSize + "\n");
                                if(headQueueSize == 2) writer2.append(elapsedTime + "," + (IPGTheoretical-IPGCounter) + "," + headQueueDelay + "," + headQueueSize + "\n");
                                if(headQueueSize == 3) writer3.append(elapsedTime + "," + (IPGTheoretical-IPGCounter) + "," + headQueueDelay + "," + headQueueSize + "\n");

                                queueSizeCompressionTotals[headQueueSize-1][0] ++;
                                queueSizeCompressionTotals[headQueueSize-1][1] += IPGTheoretical - IPGCounter;

                                reducedIPGCounter++;
                                compressionTotal += IPGTheoretical - IPGCounter;
                                trueDelayTotal += headQueueDelay;
                            }

                            probeCounter++;
                            IPGCounting = false;
                            IPGCounter = 0;
                        }
                        // process dif bits in the next iteration
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
                p1.add(packet1);
                p1.add(TRAFFICID);
                queue.add(p1);
            }

            if(packet2 > 0){
                List<Integer> p2= new ArrayList<Integer>();
                p2.add(packet2);
                p2.add(TRAFFICID);
                queue.add(p2);
            }

            if(packet3 > 0){
                List<Integer> p3= new ArrayList<Integer>();
                p3.add(packet3);
                p3.add(TRAFFICID);
                queue.add(p3);
            }

            if(probePacket > 0){
                List<Integer> pp= new ArrayList<Integer>();
                pp.add(probePacket);
                if (probePacket == headSize*8){
                    pp.add(HEADID);
                    pp.add(queue.size()); // add the current size of the queue when the heading packet enters
                    headQueueDelayCounter = 0;
                }
                else pp.add(TAILID);
                queue.add(pp);
//                System.out.println(elapsedTime + " microseconds, added packet of size " + probePacket);
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
        if(reducedIPGCounter > 0){
            System.out.println("Average compression: " + compressionTotal/reducedIPGCounter + ", average true delay: " + trueDelayTotal/reducedIPGCounter);
        }

        for(int i = 0; i < queueSizeCompressionTotals.length; i++){
            if(queueSizeCompressionTotals[i][0] > 0){
                System.out.println("Average compression for queue of length " + (i+1) + ": " + queueSizeCompressionTotals[i][1] / queueSizeCompressionTotals[i][0]);
            }
        }


        writer1.flush();
        writer1.close();
        writer2.flush();
        writer2.close();
        writer3.flush();
        writer3.close();
    }



}

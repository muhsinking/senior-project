/**
 * Created by muhsinking on 10/21/15.
 */

import java.io.*;
import java.net.*;
import java.util.Date;

class UDPClient
{
    public static void main(String args[]) throws Exception
    {
        Date date = new Date();
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("localhost");
        byte[] sendData = new byte[2048];
        byte[] receiveData = new byte[20480];

        String sentence = inFromUser.readLine();

        sendData = sentence.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);

        long startTime = System.nanoTime();

//        Thread.sleep(1000);

        byte [] sendTime = ByteUtils.longToBytes(startTime);

        clientSocket.send(sendPacket);

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        clientSocket.receive(receivePacket);

        long receiveTime = System.nanoTime();

        long elapsedTime = (receiveTime-startTime)/1000000; // convert to milliseconds

        String modifiedSentence = new String(receivePacket.getData());

        System.out.println("FROM SERVER: " + modifiedSentence);

        System.out.println("Time elapsed: " + elapsedTime + " milliseconds.");

        clientSocket.close();
    }


}
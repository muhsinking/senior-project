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
        byte[] receiveData = new byte[2048];

//        String sentence = inFromUser.readLine();

        sendData = ByteUtils.longToBytes(date.getTime());
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
        clientSocket.send(sendPacket);
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        String modifiedSentence = new String(receivePacket.getData());
        System.out.println("FROM SERVER:" + modifiedSentence);
        clientSocket.close();
    }


}
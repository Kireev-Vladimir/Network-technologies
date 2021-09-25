package com.company;

import java.io.IOException;
import java.net.*;

public class MulticastReceiver implements Runnable {

    private final MulticastSocket mcSocket;
    DatagramPacket datagramPacket;
    private byte[] buffer = new byte[100];
    String receivedString;


    MulticastReceiver() throws IOException {

        InetAddress mcastaddr = InetAddress.getByName("228.5.6.7");
        InetSocketAddress group = new InetSocketAddress(mcastaddr, 34566);
        NetworkInterface netIf = NetworkInterface.getByName("bge0");


        mcSocket = new MulticastSocket(34566);
        mcSocket.joinGroup(group, netIf);
        datagramPacket = new DatagramPacket(buffer, buffer.length);
    }

    @Override
    public void run() {
        try {
            while (true) {
                mcSocket.receive(datagramPacket);
                receivedString = new String(buffer);
                System.out.println(receivedString);
                if(!receivedString.split(" ")[0].equals(LocalNetworkCopyFinder.processName)){
                    LocalNetworkCopyFinder.packageReceived(receivedString.split(" ")[0]);
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}

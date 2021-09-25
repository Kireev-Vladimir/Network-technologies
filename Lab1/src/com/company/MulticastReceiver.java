package com.company;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class MulticastReceiver implements Runnable {

    private final MulticastSocket mcSocket;
    DatagramPacket datagramPacket;
    private byte[] buffer = new byte[100];
    String receivedString;


    MulticastReceiver() throws IOException {
        mcSocket = new MulticastSocket(3456);
        mcSocket.joinGroup(LocalNetworkCopyFinder.group);
        datagramPacket = new DatagramPacket(buffer, buffer.length);
    }

    @Override
    public void run() {
        try {
            while (true) {
                mcSocket.receive(datagramPacket);
                receivedString = new String(buffer);
                if(!receivedString.split(" ")[0].equals(LocalNetworkCopyFinder.processName)){
                    LocalNetworkCopyFinder.packageReceived(receivedString.split(" ")[0]);
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.company;

import java.io.IOException;
import java.net.*;

public class MulticastReceiver implements Runnable {

    private final MulticastSocket mcSocket;
    DatagramPacket datagramPacket;
    private byte[] buffer = new byte[100];
    String receivedString;


    MulticastReceiver(InetAddress group) throws IOException {

        mcSocket = new MulticastSocket(LocalNetworkCopyFinder.PORT);
        mcSocket.joinGroup(group);
        datagramPacket = new DatagramPacket(buffer, buffer.length);
    }

    @Override
    public void run() {
        try {
            while (true) {
                mcSocket.receive(datagramPacket);
                receivedString = new String(buffer, 0, datagramPacket.getLength());
                if(!receivedString.equals(LocalNetworkCopyFinder.sendMessage)){
                    LocalNetworkCopyFinder.packageReceived(receivedString);
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}

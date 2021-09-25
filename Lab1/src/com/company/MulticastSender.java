package com.company;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class MulticastSender implements Runnable{

    private final MulticastSocket mcSocket;

    MulticastSender() throws IOException {
        mcSocket = new MulticastSocket();
    }

    @Override
    public void run() {
        String message;
        DatagramPacket datagramPacket;

        try {
            int i = 0;
            while(true) {
                message = LocalNetworkCopyFinder.processName + " Sent " + i + " times by process";
                datagramPacket = new DatagramPacket(message.getBytes(), message.length(), LocalNetworkCopyFinder.group, 3456);
                mcSocket.send(datagramPacket);
                Thread.sleep(1000);
                i++;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        mcSocket.close();
    }
}

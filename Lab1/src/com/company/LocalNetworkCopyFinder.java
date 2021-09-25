package com.company;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LocalNetworkCopyFinder extends Thread{
    static InetAddress group;
    static InetAddress procIp;
    static String processName;

    private static Map<String, Long> processes;

    static void packageReceived(String processName){
        if(!processes.containsKey(processName)){
            System.out.println("New process: " + processName);
            processes.put(processName, System.currentTimeMillis());
        }
        else {
            processes.replace(processName, System.currentTimeMillis());
        }
    }

    private void checkClosedProcesses(){
        Long curTime = System.currentTimeMillis();
        for (Map.Entry<String, Long> entry: processes.entrySet()) {
            Long time = entry.getValue();
            if(curTime - time > 2000){
                String closedName = entry.getKey();
                System.out.println(closedName + " closed");
                processes.remove(closedName);
            }
        }
    }



    LocalNetworkCopyFinder(String ip) throws UnknownHostException {
        processName = ManagementFactory.getRuntimeMXBean().getName();
        procIp = InetAddress.getLocalHost();
        group = InetAddress.getByName(ip);
        processes = new ConcurrentHashMap<>();
    }

    @Override
    public void run() {
        try {
            Thread multicastSender = new Thread(new MulticastSender());
            Thread multicastReceiver = new Thread(new MulticastReceiver());

            multicastReceiver.start();
            multicastSender.start();

            Timer timer = new Timer();

            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    checkClosedProcesses();
                }
            }, 0, 1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

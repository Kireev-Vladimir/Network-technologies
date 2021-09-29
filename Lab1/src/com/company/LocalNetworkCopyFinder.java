package com.company;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class LocalNetworkCopyFinder extends Thread{
    static InetAddress group;
    static String sendMessage;

    private static Map<String, Long> processes;

    private static void printProcesses(){
        System.out.println("===========================");
        for(Map.Entry<String, Long> entry: processes.entrySet())
            System.out.println(entry.getKey());
        System.out.println("===========================");
    }

    static void packageReceived(String processName){
        if(!processes.containsKey(processName)){
            processes.put(processName, System.currentTimeMillis());
            printProcesses();
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
                processes.remove(closedName);
                printProcesses();
            }
        }
    }



    LocalNetworkCopyFinder(String ip) throws UnknownHostException {
        sendMessage = ManagementFactory.getRuntimeMXBean().getName() + " " + InetAddress.getLocalHost().toString();
        group = InetAddress.getByName(ip);
        processes = new ConcurrentHashMap<>();
    }

    @Override
    public void run() {
        try {
            Thread multicastSender = new Thread(new MulticastSender());
            Thread multicastReceiver = new Thread(new MulticastReceiver(group));

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

package com.company;

import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) {
        try {
            new LocalNetworkCopyFinder("225.6.7.8").start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}

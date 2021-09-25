package com.company;

import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) {
        try {
            new LocalNetworkCopyFinder("225.1.1.1").start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}

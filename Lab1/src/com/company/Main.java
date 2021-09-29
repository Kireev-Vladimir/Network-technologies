package com.company;

import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) {
        try {
            new LocalNetworkCopyFinder(args[0]).start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}

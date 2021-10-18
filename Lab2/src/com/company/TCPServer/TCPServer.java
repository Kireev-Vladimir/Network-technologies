package com.company.TCPServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;

public class TCPServer {

    public static void main(String[] args) {

        ServerSocket startSocket;

        try {
            startSocket = new ServerSocket(Integer.parseInt(args[0]));
            System.out.println("Server started");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        File uploadDir = new File(Paths.get("").toAbsolutePath().toString() + "\\uploads");

        uploadDir.mkdir();

        boolean isWorking = true;
        while(isWorking){
            try {
                Socket clientSocket = startSocket.accept();
                System.out.println("Starting new download from " + clientSocket.getInetAddress().toString());
                new ServerFileTransfer(clientSocket, uploadDir).start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
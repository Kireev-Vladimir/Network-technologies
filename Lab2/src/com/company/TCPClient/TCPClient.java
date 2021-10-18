package com.company.TCPClient;

import com.company.Constants;

import java.io.*;
import java.net.Socket;

public class TCPClient {

    private static Socket clientSocket;

    private static class waitingForFinish extends Thread{

        InputStream stream;

        public waitingForFinish(InputStream stream){
            this.stream = stream;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[Constants.BUF_SIZE];
            int len;
            String tmp = "";
            try {
                while(!(tmp.startsWith("?") && tmp.endsWith("?") && tmp.length() > 1)) {
                    len = stream.read(buffer);
                    tmp = tmp.concat(new String(buffer, 0, len));
                }
                if ("?FINISHED?".equals(tmp)) {
                    finishCode = 1;
                    System.out.println("Successfully finised");
                } else if ("?ERROR?".equals(tmp)) {
                    finishCode = 2;
                    System.out.println("Finished with errors");
                }
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static int finishCode = 0;

    public static void main(String[] args) {
        if(args.length != 3){
            System.out.println("Wrong arguments");
            return;
        }

        try {

            if(args[2].length() > Constants.MAX_NAME_SIZE){
                System.out.println("Filename too long");
                return;
            }

            File file = new File(args[2]);

            if(!file.exists()){
                System.out.println("No file with this name");
                return;
            }

            if(file.length() > Constants.MAX_FILE_SIZE){
                System.out.println("File too large");
                return;
            }

            clientSocket = new Socket(args[0], Integer.parseInt(args[1]));

            InputStream inputStream = clientSocket.getInputStream();
            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());

            InputStream fileInput = new FileInputStream(file);

            byte[] outBuffer = new byte[Constants.BUF_SIZE];

            System.out.println("?" + args[2] + "?" + file.length() + "?");
            outputStream.writeBytes("?" + args[2] + "?" + file.length() + "?");

            byte[] inBuffer = new byte[Constants.BUF_SIZE];

            int len = inputStream.read(inBuffer);

            new waitingForFinish(inputStream).start();

            if("start".equals(new String(inBuffer, 0, len))){
                len = fileInput.read(outBuffer);
                while (len != -1 && finishCode == 0){
                    outputStream.write(outBuffer, 0, len);
                    len = fileInput.read(outBuffer);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
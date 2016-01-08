package com.kururu;

import java.io.*;
import java.net.*;


/**
 * Created by kururu on 2015/12/30.
 */
public class downloadFileToClient {

    private static final String fileToSendPath = "F:\\myJavaCodeInIntelliIdea\\FileManagementSystem\\serverFile\\";
    private ServerSocket server;
    private Socket client;
    private DataInputStream dis;
    private FileOutputStream fos;

    public downloadFileToClient(String fileName) {

        while (true) {
            ServerSocket welcomeSocket;
            Socket connectionSocket = null;
            BufferedOutputStream outToClient = null;

            try {
                welcomeSocket = new ServerSocket(2014);
                connectionSocket = welcomeSocket.accept();
                outToClient = new BufferedOutputStream(connectionSocket.getOutputStream());
            } catch (IOException ex) {
                // Do exception handling
                ex.printStackTrace();
            }

            if (outToClient != null) {
                File myFile = new File(fileToSendPath + fileName);
                byte[] mybytearray = new byte[(int) myFile.length()];

                FileInputStream fis = null;

                try {
                    fis = new FileInputStream(myFile);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                    // Do exception handling
                }
                BufferedInputStream bis = new BufferedInputStream(fis);

                try {
                    System.out.println("----BEGIN SEND FILE TO CLIENT<" + fileName +">----");
                    bis.read(mybytearray, 0, mybytearray.length);
                    outToClient.write(mybytearray, 0, mybytearray.length);
                    outToClient.flush();
                    System.out.println("----SEND FILE<" + fileName +">SUCCESSFULLY-------");
                    outToClient.close();
                    connectionSocket.close();

                    // File sent, exit the main method
                    return;
                } catch (IOException ex) {
                    // Do exception handling
                    ex.printStackTrace();
                }
            }
        }
    }
}

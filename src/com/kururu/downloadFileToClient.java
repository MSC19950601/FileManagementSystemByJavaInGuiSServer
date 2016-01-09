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
    private DataOutputStream dos;
    private BufferedOutputStream outToClient;

    public downloadFileToClient(String fileName) throws IOException{
        try{
            try {
                server = new ServerSocket(2014);
                while(true){
                    client = server.accept();
                    outToClient = new BufferedOutputStream(client.getOutputStream());
                    if (outToClient != null) {
                        File myFile = new File(fileToSendPath + fileName);
                        byte[] mybytearray = new byte[(int) myFile.length()];
                        FileInputStream fis = new FileInputStream(myFile);
                        BufferedInputStream bis = new BufferedInputStream(fis);
                        System.out.println("----BEGIN SEND FILE TO CLIENT<" + fileName +">----");
                        bis.read(mybytearray, 0, mybytearray.length);
                        outToClient.write(mybytearray, 0, mybytearray.length);
                        outToClient.flush();
                        System.out.println("----SEND FILE<" + fileName +">SUCCESSFULLY-------");
                        break;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(outToClient != null)
                outToClient.close();
            if(client != null)
                client.close();
            if(server != null)
                server.close();
        }
    }
}


/*class CreateDownloadFiletoClientThread extends Thread{
    private Socket clientOfDownload;
    public CreateDownloadFiletoClientThread(Socket s) throws IOException{
        clientOfDownload = s;
        start();
    }



    public Socket getClientOfDownload(){
        return clientOfDownload;
    }
}*/

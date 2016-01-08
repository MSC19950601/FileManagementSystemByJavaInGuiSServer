package com.kururu;

import java.io.*;
import java.net.*;

/**
 * Created by kururu on 2015/12/30.
 */
public class uploadFileFromClient {

    private static final String serverPath= "F:\\myJavaCodeInIntelliIdea\\FileManagementSystem\\serverFile\\";
    private static final int PORT =2013;

    private ServerSocket server;
    private Socket client;
    private DataInputStream dis;
    private FileOutputStream fos;

    public uploadFileFromClient(){
        try {
            try {
                server =new ServerSocket(PORT);
                while(true){
                    client = server.accept();
                    dis =new DataInputStream(client.getInputStream());
                    //文件名和长度
                    String fileName = dis.readUTF();
                    long fileLength = dis.readLong();
                    fos = new FileOutputStream(new File(serverPath + fileName));

                    byte[] sendBytes =new byte[1024];
                    int transLen =0;
                    System.out.println("----BEGIN RECEIVE FILE FROM CLIENT<" + fileName +">----");
                    System.out.println("----FILE LENGTH<" + fileLength +">----");
                    while(true){
                        int read = 0;
                        read = dis.read(sendBytes);
                        if(read == -1)
                            break;
                        transLen += read;
                        System.out.println("PROGRESS" +100 * transLen/fileLength +"%...");
                        fos.write(sendBytes,0, read);
                        fos.flush();
                    }
                    System.out.println("----RECEIVE FILE<" + fileName +">SUCCESSFULLY-------");

                    break;
                }
            }catch (Exception e) {
                e.printStackTrace();
            }finally {
                if(null != dis) dis.close();
                if(null != fos) fos.close();
                if(null != client) client.close();
                if(null != server) server.close();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

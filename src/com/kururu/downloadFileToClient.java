package com.kururu;
import com.kururu.basement.User;

import java.io.*;
import java.net.*;
import java.sql.Timestamp;
import java.util.Date;


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

                        int originalNum = operateDataBase.operationToGetRowNumOfDoc();
                        String originalNumInString = String.valueOf(originalNum);
                        String updateDOC_ID = "000" + originalNumInString;
                        User currentUser = operateDataBase.getCurrentUserFromOpe();
                        String updateDOC_CREATOR = currentUser.getName();
                        Timestamp updateDOC_TIMESTAMP = new Timestamp(new Date().getTime());
                        String updateDOC_DESCRIPTION = "download to client";
                        String updateDOC_FILENAME = fileName;
                        String updateDOC_LOCATION = "C";
                        String insertSqlStrForUpdate = "INSERT INTO doc (DOC_ID, DOC_CREATOR, DOC_TIMESTAMP, DOC_DESCRIPTION, DOC_FILENAME, DOC_LOCATION) VALUES "
                                + "('" + updateDOC_ID + "', '" + updateDOC_CREATOR + "', '" + updateDOC_TIMESTAMP.toString() + "', '" + updateDOC_DESCRIPTION + "', '" + updateDOC_FILENAME + "' ,'" + updateDOC_LOCATION + "')";
                        if(operateDataBase.operationToInsertDoc(insertSqlStrForUpdate))
                            System.out.println("insert successful");
                        else
                            System.out.println("false!");

                        break;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(outToClient != null) outToClient.close();
            if(client != null) client.close();
            if(server != null) server.close();
        }
    }
}


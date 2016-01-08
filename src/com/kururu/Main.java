package com.kururu;

import com.kururu.basement.User;

import java.io.*;
import java.net.*;

/**
 * Created by kururu on 2015/12/30.
 */

public class Main{

    private static final int FINAL_PORT = 8899;
    private static ServerSocket serverSocketFinal;

    public static void main(String[] args) throws IOException {

        try{
            serverSocketFinal = new ServerSocket(FINAL_PORT);
            serverSocketFinal.setSoTimeout(60000);

            while(true) {
                MAINPANEL:
                {
                    Socket socketFinal = serverSocketFinal.accept();
                    CreateMainServerThread t = new CreateMainServerThread(socketFinal);
                    if (t.getResultClick().equals("upload")) {
                        new uploadFileFromClient();
                        break;
                    } else if (t.getResultClick().equals("downloadForOpe")) {
                        String resultFileName = t.getCommand();
                        System.out.println(resultFileName);
                        new downloadFileToClient(resultFileName);
                        break;
                    } else if (t.getResultClick().equals("login")) {
                        String resultCommand = t.getCommand();
                        User resultUser = operateDataBase.operationToSearchUser(resultCommand);
                        System.out.println(resultUser.getName());
                        operateDataBase.transForSearchUser(resultUser);
                        break MAINPANEL;
                    } else if (t.getResultClick().equals("getUserTable")) {
                        String resultCommand = t.getCommand();
                        operateDataBase.operationToGetAllUser(resultCommand);
                        //break MAINPANEL;
                    } else if (t.getResultClick().equals("getDocTable")){
                        String resultCommand = t.getCommand();
                        operateDataBase.operationToGetAllDoc(resultCommand);
                        //break MAINPANEL;
                    } else if (t.getResultClick().equals("addUser")){
                        String resultCommand = t.getCommand();
                        operateDataBase.operationToInsertUser(resultCommand);
                    } else if (t.getResultClick().equals("deleteUser")){
                        String resultCommand = t.getCommand();
                        operateDataBase.operationToDeleteUser(resultCommand);
                    } else if (t.getResultClick().equals("changeUserInfo")){
                        String resultCommand = t.getCommand();
                        operateDataBase.operationToupdateUserForNameAndPassword(resultCommand);
                    }
                }
            }
        }catch (SocketTimeoutException timeOut){
            System.out.println("time out");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //socketFinal.close();
            serverSocketFinal.close();
        }
    }
}


class CreateMainServerThread extends Thread{

    private String resultClick;
    private String command;

    private Socket client;

    public CreateMainServerThread(Socket s)throws IOException {
        client = s;
        BufferedReader bufferedReader =new BufferedReader(
                new InputStreamReader(client.getInputStream()));
        resultClick = bufferedReader.readLine();
        command = bufferedReader.readLine();
        System.out.println(resultClick);
        System.out.println(command);
        start();
    }

    public String getResultClick(){
        return resultClick;
    }

    public String getCommand(){
        return command;
    }

}


package com.kururu;

import com.kururu.basement.*;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.Vector;

/**
 * Created by kururu on 2016/1/3.
 */
public class operateDataBase {

    public static Doc resDoc;
    public static String resID,resCreator,resDescription,resFilename,resLocation;
    public static Timestamp resTime;

    final public static String connectionAddress= "jdbc:mysql://127.0.0.1:3306/baseforsystemcharger";
    final public static String connectionName = "root";
    final public static String connectionPassword = "mo123456";


    public static User resUser;
    private static User currentUserForDB;
    public static String resName,resPassword,resRole;


    public static void getCurrentUserFromMainToDB(User currentUser){
        currentUserForDB = currentUser;
    }

    public static User getCurrentUserFromOpe(){
        return currentUserForDB;
    }

    public static void operationToGetAllDoc(String command){
        Vector colHead = new Vector();
        Vector rows = new Vector();
        Connection connection;
        Statement statement;
        ResultSet resultSet;
        ResultSetMetaData rsmd;
        try{

            System.out.println("connecting to database now, loading...");
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    connectionAddress, connectionName, connectionPassword);
            if (!connection.isClosed()) {
                System.out.println("Connecting successfully!");
                String query = command;
                statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
                rsmd = resultSet.getMetaData();
                for(int i = 1; i < rsmd.getColumnCount() + 1; i++){
                    colHead.addElement(rsmd.getColumnName(i));
                    while(resultSet.next()){
                        Vector currentRow = new Vector();
                        for(int j = 1; j < rsmd.getColumnCount() + 1; j++){
                            currentRow.addElement(resultSet.getString(j));
                        }
                        rows.addElement(currentRow);
                    }
                }
                try {
                    ServerSocket ss = new ServerSocket(2001);
                    Socket s = ss.accept();
                    ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                    oos.writeObject(colHead);
                    oos.writeObject(rows);
                    oos.close();
                    s.close();
                    ss.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            System.err.println("Connecting Failed");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static boolean operationToInsertDoc(String command){
        try{
            System.out.println("connecting to database now, loading...");
            Class.forName("com.mysql.jdbc.Driver");
            Connection conForInsert = DriverManager.getConnection(
                    connectionAddress, connectionName, connectionPassword);
            if(!conForInsert.isClosed()){
                System.out.println("Connecting successfully!");
                Statement staForInsert = conForInsert.createStatement();
                String insertSqlStr = command;
                staForInsert.executeUpdate(insertSqlStr);
                staForInsert.close();
                conForInsert.close();
            }else{
                System.out.println("Sorry, Connection is closed!");
            }
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            return true;
        }
    }

    public static Doc operationToSearchDoc(String command) throws Exception{

        try{

            System.out.println("connecting to database now, loading...");
            Class.forName("com.mysql.jdbc.Driver");
            Connection conForSearch = DriverManager.getConnection(
                    connectionAddress, connectionName, connectionPassword);
            if(!conForSearch.isClosed()){
                System.out.println("Connecting successfully!");
                Statement staForSearch = conForSearch.createStatement();
                String SearchSqlStr = command;
                ResultSet res = staForSearch.executeQuery(SearchSqlStr);
                while(res.next()) {
                    resID = res.getString("DOC_ID");
                    resCreator = res.getString("DOC_CREATOR");
                    resTime = res.getTimestamp("DOC_TIMESTAMP");
                    resDescription = res.getString("DOC_DESCRIPTION");
                    resFilename = res.getString("DOC_FILENAME");
                    resLocation = res.getString("DOC_LOCATION");
                    resDoc = new Doc(resID,resCreator,resTime,resDescription,resFilename,resLocation);
                }
                res.close();
                staForSearch.close();
                conForSearch.close();
            }else{
                System.out.println("Sorry, Connection is closed!");
            }
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(resDoc == null)
                return null;
            else
                return resDoc;
        }
    }

    public static boolean operationTodeleteDoc(String command) throws Exception{
        try{
            System.out.println("connecting to database now, loading...");
            Class.forName("com.mysql.jdbc.Driver");
            Connection conForDel = DriverManager.getConnection(
                    connectionAddress, connectionName, connectionPassword);
            if(!conForDel.isClosed()){
                System.out.println("Connecting successfully!");
                Statement staForsel = conForDel.createStatement();
                String delSqlStr = command;
                staForsel.executeUpdate(delSqlStr);
                staForsel.close();
                conForDel.close();
            }else{
                System.out.println("Sorry, Connection is closed!");
            }
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            return true;
        }
    }

    public static int operationToGetRowNumOfDoc(){
        Vector colHead = new Vector();
        Vector rows = new Vector();
        Connection connection;
        Statement statement;
        ResultSet resultSet;
        ResultSetMetaData rsmd;
        try{

            System.out.println("connecting to database now, loading...");
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    connectionAddress, connectionName, connectionPassword);
            if (!connection.isClosed()) {
                System.out.println("Connecting successfully!");
                String query = "SELECT * FROM doc";
                statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
                rsmd = resultSet.getMetaData();
                for(int i = 1; i < rsmd.getColumnCount() + 1; i++){
                    colHead.addElement(rsmd.getColumnName(i));
                    while(resultSet.next()){
                        Vector currentRow = new Vector();
                        for(int j = 1; j < rsmd.getColumnCount() + 1; j++){
                            currentRow.addElement(resultSet.getString(j));
                        }
                        rows.addElement(currentRow);
                    }
                }
            }
        }catch (Exception e){
            System.err.println("Connecting Failed");
            e.printStackTrace();
            System.exit(0);
        }finally {
            return rows.size();
        }
    }

    public static void operationToGetAllUser(String command){
        Vector colHead = new Vector();
        Vector rows = new Vector();
        Connection connection;
        Statement statement;
        ResultSet resultSet;
        ResultSetMetaData rsmd;
        try {
            System.out.println("connecting to database now, loading...");
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    connectionAddress, connectionName, connectionPassword);
            if (!connection.isClosed()) {
                System.out.println("Connecting successfully!");
                String query = command;
                statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
                rsmd = resultSet.getMetaData();
                for(int i = 1; i < rsmd.getColumnCount() + 1; i++){
                    colHead.addElement(rsmd.getColumnName(i));
                    while(resultSet.next()){
                        Vector currentRow = new Vector();
                        for(int j = 1; j < rsmd.getColumnCount() + 1; j++){
                            currentRow.addElement(resultSet.getString(j));
                        }
                        rows.addElement(currentRow);
                    }
                }
                try {
                    ServerSocket ss = new ServerSocket(1578);
                    Socket s = ss.accept();
                    ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                    oos.writeObject(colHead);
                    oos.writeObject(rows);
                    oos.close();
                    s.close();
                    ss.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }catch (Exception e){
            System.err.println("Connecting Failed");
            e.printStackTrace();
            System.exit(0);
        }

    }

    public static User operationToSearchUser(String command) throws Exception{

        try{
            System.out.println("connecting to database now, loading...");
            Class.forName("com.mysql.jdbc.Driver");
            Connection conForSearch = DriverManager.getConnection(
                    connectionAddress, connectionName, connectionPassword);
            if(!conForSearch.isClosed()){
                System.out.println("Connecting successfully!");
                Statement staForSearch = conForSearch.createStatement();
                String SearchSqlStr = command;
                ResultSet res = staForSearch.executeQuery(SearchSqlStr);
                while(res.next()) {
                    resName = res.getString("USER_NAME");
                    //System.out.println(resName);
                    resPassword = res.getString("USER_PASSWORD");
                    //System.out.println(resPassword);
                    resRole = res.getString("USER_ROLE");
                    //System.out.println(resRole);
                    if(resRole.equals("operator")){
                        System.out.println("operator");
                        resUser = new Operator(resName,resPassword,resRole);
                    }else if(resRole.equals("browser")){
                        System.out.println("browser");
                        resUser = new Browser(resName,resPassword,resRole);
                    }else if(resRole.equals("administrator")){
                        System.out.println("administrator");
                        resUser = new Administrator(resName,resPassword,resRole);
                    }
                }
                res.close();
                staForSearch.close();
                conForSearch.close();
            }else{
                System.out.println("Sorry, Connection is closed!");
            }
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SQLException e) {
            e.printStackTrace();
        }finally{
            if(resUser == null)
                return null;
            else
                return resUser;
        }
    }

    public static void transForSearchUser(User user){
        try {
            ServerSocket ss = new ServerSocket(7809);
            Socket s = ss.accept();
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            oos.writeObject(user);
            oos.close();
            s.close();
            ss.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean operationTopdateUser(String command) throws Exception{
        try{
            System.out.println("connecting to database now, loading...");
            Class.forName("com.mysql.jdbc.Driver");
            Connection conForUpdate = DriverManager.getConnection(
                    connectionAddress, connectionName, connectionPassword);
            if(!conForUpdate.isClosed()){
                System.out.println("Connecting successfully!");
                Statement staForUpdate = conForUpdate.createStatement();
                String insertSqlStr = command;
                staForUpdate.executeUpdate(insertSqlStr);
                staForUpdate.close();
                conForUpdate.close();
            }else{
                System.out.println("Sorry, Connection is closed!");
            }
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            return true;
        }
    }

    public static boolean operationToInsertUser(String command) throws Exception{
        try{
            System.out.println("connecting to database now, loading...");
            Class.forName("com.mysql.jdbc.Driver");
            Connection conForInsert = DriverManager.getConnection(
                    connectionAddress, connectionName, connectionPassword);
            if(!conForInsert.isClosed()){
                System.out.println("Connecting successfully!");
                Statement staForInsert = conForInsert.createStatement();
                String insertSqlStr = command;
                staForInsert.executeUpdate(insertSqlStr);
                staForInsert.close();
                conForInsert.close();
            }else{
                System.out.println("Sorry, Connection is closed!");
            }
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            return true;
        }
    }

    public static boolean operationToDeleteUser(String command) throws Exception{
        try{
            System.out.println("connecting to database now, loading...");
            Class.forName("com.mysql.jdbc.Driver");
            Connection conForDel = DriverManager.getConnection(
                    connectionAddress, connectionName, connectionPassword);
            if(!conForDel.isClosed()){
                System.out.println("Connecting successfully!");
                Statement staForsel = conForDel.createStatement();
                String delSqlStr = command;
                staForsel.executeUpdate(delSqlStr);
                staForsel.close();
                conForDel.close();
            }else{
                System.out.println("Sorry, Connection is closed!");
            }
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            return true;
        }
    }

    public static boolean operationToupdateUserForNameAndPassword(String command) throws Exception{
        try{
            System.out.println("connecting to database now, loading...");
            Class.forName("com.mysql.jdbc.Driver");
            Connection conForUpdate = DriverManager.getConnection(
                    connectionAddress, connectionName, connectionPassword);
            if(!conForUpdate.isClosed()){
                System.out.println("Connecting successfully!");
                Statement staForUpdate = conForUpdate.createStatement();
                staForUpdate.executeUpdate(command);
                staForUpdate.close();
                conForUpdate.close();
            }else{
                System.out.println("Sorry, Connection is closed!");
            }
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            return true;
        }
    }
}

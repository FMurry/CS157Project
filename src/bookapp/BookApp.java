/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookapp;

import java.sql.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fredericmurry
 */
public class BookApp {

    private String userName;
    private String password;
    
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    private static final String DB_URL = "jdbc:mysql://localhost";
    private static String CREATE_DATABASE = "CREATE DATABASE booksdb IF NOT EXISTS";

    
    
    public void initializeDB(){
        Connection con = null;
        try{
            String databaseName = "booksdb";
            userName = "root";
            password = "";
            Scanner in = new Scanner(System.in);
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(DB_URL, userName, password);
            System.out.println("Please enter database username (default root)");
            userName = in.nextLine();
            System.out.println("Enter database password (default blank)");
            password = in.nextLine();
            System.out.println("Mysql Connection Established");
            Statement s = con.createStatement();
            int success = s.executeUpdate(CREATE_DATABASE);
            if(success == 0){
                System.out.println("Database Created");
            }
            else{
                System.out.println("Database Creation Failed");
            }
            
            
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        } catch (ClassNotFoundException ex){
            System.err.print(ex.getMessage());
            System.err.println("Class not found");
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String databaseName = "booksdb";
        Scanner in = new Scanner(System.in);
        int userChoice = 0;
        BookApp app = new BookApp();
        while(userChoice != -1){
            System.out.println("Welcome to Frederic's Book database");
            System.out.println("Please choose an option:");
            System.out.println("1. Initialize Database (First Time startup)");
            System.out.println("-1. Quit");
            try{
                userChoice = Integer.parseInt(in.nextLine());
            }catch(Exception ex){
                userChoice = 0;
            }
            switch(userChoice){
                case -1:
                    //Exit
                    break;
                case 1:
                    app.initializeDB();
                    break;
                case 2:
                    
                    break;
                default:
                    userChoice = 0;
                    break;
            }
        }
        
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookapp;

import java.sql.*;
import java.util.Scanner;

/**
 *
 * @author fredericmurry
 */
public class BookApp {

    private String userName = "root";
    private String password = "";
    private Connection connection = null;
    
    private String databaseName = "booksdb";
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    private static final String DB_URL = "jdbc:mysql://localhost";
    private static String CREATE_DATABASE = "CREATE DATABASE IF NOT EXISTS ";
    private static String DATABASE_EXISTS = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ";
    private static String DROP_DATABASE = "DROP DATABASE booksdb";
    
    
    public void initializeDB(){
        
        try{
            
            Scanner in = new Scanner(System.in);
            Class.forName("com.mysql.jdbc.Driver");
            if(connection == null){
                connection = DriverManager.getConnection(DB_URL, userName, password);
            }            
            System.out.println("Please enter database username (default root)");
            userName = in.nextLine();
            if(userName.equals("")){
                userName = "root";
            }
            System.out.println("Enter database password (default blank)");
            password = in.nextLine();
            System.out.println("Mysql Connection Established");
            Statement s = connection.createStatement();
            s.executeUpdate(CREATE_DATABASE+databaseName);
            boolean success = s.execute(DATABASE_EXISTS+"'"+databaseName+"'");
            if(success){
                connection = DriverManager.getConnection(DB_URL+"/booksdb", userName, password);
                System.out.println("Database Created\n\n\n");
                Statement c = connection.createStatement();
                String createAuthorTable = "CREATE TABLE authors( "+
                    "authorID int NOT NULL AUTO_INCREMENT , "
                    +"firstName CHAR(20) NOT NULL, "
                    +"lastName Char(20) NOT NULL, "
                    + "PRIMARY KEY (authorID) "
                    + ");";
                String createPublishersTable = "CREATE TABLE publishers( "
                    + "publisherID int NOT NULL AUTO_INCREMENT , "
                    + "publisherName CHAR(100) NOT NULL, "
                    + "PRIMARY KEY (publisherID) "
                    + ");";
           
                String createTitlesTable = "CREATE TABLE titles( "
                    + "isbn CHAR(10) NOT NULL , "
                    + "editionNumber int NOT NULL , "
                    + "Year CHAR(4) NOT NULL , "
                    + "publisherID int NOT NULL , "
                    + "price Decimal(8,2) NOT NULL , "
                    + "title VARCHAR(500) NOT NULL , "
                    + "FOREIGN KEY (publisherID) REFERENCES publishers(publisherID) ON DELETE CASCADE , "
                    + "PRIMARY KEY (isbn) "
                    + ");";
                String createAuthorISBNTable = "CREATE TABLE authorISBN( "
                    + "authorID int NOT NULL , "
                    + "isbn CHAR(10) NOT NULL , "
                    + "FOREIGN KEY (authorID) REFERENCES authors(authorID) ON DELETE CASCADE ,"
                    + "FOREIGN KEY (isbn) REFERENCES titles(isbn) ON DELETE CASCADE"
                    + ");";
            
                c.executeUpdate(createAuthorTable);
                c.executeUpdate(createPublishersTable);
                c.execute(createTitlesTable);
                c.executeUpdate(createAuthorISBNTable);
            }
            else{
                System.out.println("Database Creation Failed\n\n\n");
            }
            
            
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    /**
     * Drops the database
     */
    public void removeDB(){
        try{
            userName = "root";
            password = "";
            Scanner in = new Scanner(System.in);
            Class.forName("com.mysql.jdbc.Driver");
            if(connection == null){
                connection = DriverManager.getConnection(DB_URL, userName, password);
            }
            System.out.println("Please enter database username (default root)");
            userName = in.nextLine();
            if(userName.equals("")){
                userName = "root";
            }
            System.out.println("Enter database password (default blank)");
            password = in.nextLine();
            System.out.println("Mysql Connection Established");
            Statement s = connection.createStatement();
            s.executeUpdate(DROP_DATABASE);
            System.out.println("Database "+databaseName+" dropped\n\n\n");
        }catch(ClassNotFoundException | SQLException ex){
            System.err.println(ex.getMessage());
        }
    }
    
    public void createAuthor(){
        try{
            userName = "root";
            password = "";
            Scanner in = new Scanner(System.in);
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL+"/"+databaseName, userName, password);
            System.out.println("Enter Author's First Name");
            String firstName = in.nextLine();
            System.out.println("Enter author's Last Name");
            String lastName = in.nextLine();
            Statement s = connection.createStatement();
            boolean success = s.execute(DATABASE_EXISTS+"'"+databaseName+"'");
            if(success){
                String insertQuery = "INSERT INTO authors (firstName, lastName) "
                    + "VALUES ('"
                    + firstName
                    + "', '"
                    +lastName
                    + "'); ";
                System.out.println(insertQuery);
                s.executeUpdate(insertQuery);
                System.out.println("Entry Created\n\n\n");
            }
            else{
                this.initializeDB();
                this.createAuthor();
            }
            
        }catch(SQLException | ClassNotFoundException ex){
            if(ex.getMessage().equals("Unknown database 'booksdb'")){
                System.out.println("Database Does not exist, Initializng it now.......");
                this.initializeDB();
                this.createAuthor();
            }
            else{
                System.err.println(ex.getMessage());
            }
        }
    }
    public void manual(){
        
    }
    
    public void Settings(Scanner input){
        System.out.println("Please choose an option:");
        System.out.println("1. Change username and password");
        System.out.println("2. Change Database Name");
        String in = input.nextLine();
        try{
         int choice = Integer.parseInt(in);
         switch(choice){
            case 1:
                 System.out.println("Enter new Username");
                 userName = input.nextLine();
                 System.out.println("Enter new password");
                 password = input.nextLine();
                 System.out.println("This application will now use the new username and password");
                 break;
            case 2:
                
                break;
            default:
                System.out.println("Invalid input");
                System.out.println("Exiting Settings");
                break;
         }
        }catch(NumberFormatException ex){
            System.out.println("Invalid input");
            System.out.println("Exiting Settings");
        }
        
    }
    /**
     * Quits the application
     */
    public void quit(){
        try{
            if(connection == null){
                connection.close();
            }
        }catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
        System.out.println("Goodbye!");
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int userChoice = 0;
        BookApp app = new BookApp();
        
        while(userChoice != -1){
            System.out.println("Welcome to Frederic's Book database");
            System.out.println("Please choose an option:");
            System.out.println("1. Initialize Database (First Time startup)");
            System.out.println("2. Create Author");
            System.out.println("7. Manual Queries (Type your own SQL Statements)");
            System.out.println("8. Drop Database");
            System.out.println("9. Settings");
            System.out.println("-1. Quit");

            try{
                userChoice = Integer.parseInt(in.nextLine());
            }catch(NumberFormatException ex){
                userChoice = 0;
            }
            switch(userChoice){
                case -1:
                    app.quit();
                    break;
                case 1:
                    app.initializeDB();
                    break;
                case 2:
                    app.createAuthor();
                    break;
                case 8:
                    app.removeDB();
                    break;
                case 9:
                    app.Settings(in);
                default:
                    userChoice = 0;
                    break;
            }
        }
        
    }
    
}

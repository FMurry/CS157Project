/*
 *  This is the project for Ezzat's CS157A Project
 */
package bookapp;

import java.sql.*;
import java.util.Scanner;
import javax.sound.midi.SysexMessage;

/**
 *
 * @author Frederic Murry
 * Student ID: 007890870
 * CS157A
 * 
 */
public class BookApp {

    private String userName = "root";
    private String password = "";
    private Connection connection = null;
    
    private String databaseName = "booksdb";
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    private static final String DB_URL = "jdbc:mysql://localhost";
    private static final String CREATE_DATABASE = "CREATE DATABASE IF NOT EXISTS ";
    private static final String DATABASE_EXISTS = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ";
    private static final String DROP_DATABASE = "DROP DATABASE booksdb";
    
    
    public void initializeDB(Scanner input){
        
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
    
    public void seedDatabase() {
        
    }
    
    /**
     * Allows user to view, and update author table
     * @param input 
     */
    public void manageAuthor(Scanner input){
        try{
            Scanner in = new Scanner(System.in);
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL+"/"+databaseName, userName, password);
            System.out.println("Manage Author:\nPlease choose an option");
            System.out.println("1. Show Authors");
            System.out.println("2. Add new author");
            System.out.println("3.Update Author");
            System.out.println("4.Delete Author");
            System.out.println("5. Main Menu");
            int choice = Integer.parseInt(input.nextLine());
            
            switch (choice) {
                case 1:
                    String selectQuery = "SELECT * FROM authors";
                    Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery(selectQuery);
                    if(!rs.next()){
                        System.out.println("No Results");
                    }
                    else{
                        rs.beforeFirst();
                        while(rs.next()){
                            int id = rs.getInt("authorID");
                            String firstName = rs.getString("firstName");
                            String lastName = rs.getString("lastName");
                            System.out.print("ID: "+id+"\t");
                            System.out.print("First Name: "+firstName+"\t");
                            System.out.println("Last Name: "+lastName);
                        }
                    }
                    System.out.println("\n");
                    break;
                case 2:
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
                        this.initializeDB(in);
                        this.manageAuthor(in);
                    }   
                    break;
                case 3:
                    String authorQuery = "SELECT * FROM authors";
                    Statement authorstmt = connection.createStatement();
                    ResultSet authors = authorstmt.executeQuery(authorQuery);
                    if(!authors.next()){
                        System.out.println("No Authors to edit");
                    }
                    else{
                        authors.beforeFirst();
                        while(authors.next()){
                            int id = authors.getInt("authorID");
                            String firstName1 = authors.getString("firstName");
                            String lastName1 = authors.getString("lastName");
                            System.out.print("ID: "+id+"\t");
                            System.out.print("First Name: "+firstName1+"\t");
                            System.out.println("Last Name: "+lastName1);
                        }
                    }
                    System.out.println("\nChoose an author to edit (Type the integer next to them");
                    int authorid = Integer.parseInt(in.nextLine());
                    System.out.println("Enter Author's new First Name");
                    String newFName = in.nextLine();
                    System.out.println("Enter Author's new Last Name");
                    String newLName = in.nextLine();
                    String updateQuery = "UPDATE authors SET firstName = "
                            + "'"+newFName+"', lastName = "
                            + "'"+newLName+"' WHERE authorID = "+authorid+";";
                    authorstmt.executeUpdate(updateQuery);
                    System.out.println("Author Updated\n");
                    break;
                    case 4:
                    String authorQuery2 = "SELECT * FROM authors";
                    Statement authorstmt2 = connection.createStatement();
                    ResultSet authors2 = authorstmt2.executeQuery(authorQuery2);
                    if(!authors2.next()){
                        System.out.println("No Authors to edit");
                    }
                    else{
                        authors2.beforeFirst();
                        while(authors2.next()){
                            int id = authors2.getInt("authorID");
                            String firstName1 = authors2.getString("firstName");
                            String lastName1 = authors2.getString("lastName");
                            System.out.print("ID: "+id+"\t");
                            System.out.print("First Name: "+firstName1+"\t");
                            System.out.println("Last Name: "+lastName1);
                        }
                    }
                    System.out.println("\nChoose an author to Delete (Type the integer next to them");
                    int authorid2 = Integer.parseInt(in.nextLine());
                    String deleteQuery = "DELETE FROM authors WHERE authorID = "+authorid2;
                    authorstmt2.executeUpdate(deleteQuery);
                    System.out.println("Author Deleted\n");
                    break;
            //Do Nothing and Exit
                default:
                    break;
            }
            
        }catch(SQLException ex){
            if(ex.getMessage().equals("Unknown database 'booksdb'")){
                System.out.println("Database Does not exist, Initializng it now.......");
                this.initializeDB(input);
                this.manageAuthor(input);
            }
            else{
                System.err.println(ex.getMessage());
            }
        }catch(NumberFormatException ex){
            System.err.println("Not a valid option......\n\n\n");
            this.manageAuthor(input);
        }catch(ClassNotFoundException ex){
            System.err.println("Application not installed correctly, please refer to documentation");
        }
    }
    
    
    /**
     * Allows user to view, and update publisher table
     * @param input 
     */
    public void managePublisher(Scanner input){
        try{
            Scanner in = new Scanner(System.in);
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL+"/"+databaseName, userName, password);
            System.out.println("Manage Publishers:\nPlease choose an option");
            System.out.println("1. Show Publishers");
            System.out.println("2. Add new Publisher");
            System.out.println("3.Update Publisher");
            System.out.println("4.Main Menu");
            int choice = Integer.parseInt(input.nextLine());
            
            switch (choice) {
                case 1:
                    String selectQuery = "SELECT * FROM publishers";
                    Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery(selectQuery);
                    if(!rs.next()){
                        System.out.println("No Results");
                    }
                    else{
                        rs.beforeFirst();
                        while(rs.next()){
                        
                            int id = rs.getInt("publisherID");
                            String name = rs.getString("publisherName");
                            System.out.print("ID: "+id+"\t");
                            System.out.println("Name: "+name);
                        }
                    }
                    System.out.println("\n");
                    break;
                case 2:
                    System.out.println("Enter Name of Publisher");
                    String name = in.nextLine();
                    Statement s = connection.createStatement();
                    boolean success = s.execute(DATABASE_EXISTS+"'"+databaseName+"'");
                    if(success){
                        String insertQuery = "INSERT INTO publishers (publisherName) "
                                + "VALUES ('"
                                + name
                                + "'); ";
                        System.out.println(insertQuery);
                        s.executeUpdate(insertQuery);
                        System.out.println("Entry Created\n\n\n");
                    }
                    else{
                        this.initializeDB(in);
                        this.manageAuthor(in);
                    }   break;
                case 3:
                    //Update Author
                    
                    break;
            //Do Nothing and Exit
                default:
                    break;
            }
            
        }catch(SQLException ex){
            if(ex.getMessage().equals("Unknown database 'booksdb'")){
                System.out.println("Database Does not exist, Initializng it now.......");
                this.initializeDB(input);
                this.manageAuthor(input);
            }
            else{
                System.err.println(ex.getMessage());
            }
        }catch(NumberFormatException ex){
            System.err.println("Not a valid option......\n\n\n");
            this.managePublisher(input);
        }catch(ClassNotFoundException ex){
            System.err.println("Application not installed correctly, please refer to documentation");
        }
    }
    
    public void manageISBN(Scanner input){
        try{
            Scanner in = new Scanner(System.in);
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL+"/"+databaseName, userName, password);
            System.out.println("Manage ISBN:\nPlease choose an option");
            System.out.println("1. Show ISBN");
            System.out.println("2. Add new ISBN");
            System.out.println("3.Update ISBN");
            System.out.println("4.Main Menu");
            int choice = Integer.parseInt(input.nextLine());
            switch (choice) {
                case 1:
                    String selectQuery = "SELECT authorISBN.authorID, authorISBN.isbn, authors.firstName, authors.lastName "
                            + "FROM authorISBN left join authors on (authors.authorID = authorISBN.authorID);";
                    Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery(selectQuery);
                    if(!rs.next()){
                        System.out.println("No Results");
                    }
                    else{
                        rs.beforeFirst();
                        while(rs.next()){
                        
                            int id = rs.getInt("");
                            String name = rs.getString("publisherName");
                            System.out.print("ID: "+id+"\t");
                            System.out.println("Name: "+name);
                        }
                    }
                    System.out.println("\n");
                    break;
                case 2:
                    System.out.println("Enter Name of Publisher");
                    String name = in.nextLine();
                    Statement s = connection.createStatement();
                    boolean success = s.execute(DATABASE_EXISTS+"'"+databaseName+"'");
                    if(success){
                        String insertQuery = "INSERT INTO publishers (publisherName) "
                                + "VALUES ('"
                                + name
                                + "'); ";
                        System.out.println(insertQuery);
                        s.executeUpdate(insertQuery);
                        System.out.println("Entry Created\n\n\n");
                    }
                    else{
                        this.initializeDB(in);
                        this.manageAuthor(in);
                    }   break;
                case 3:
                    //Update Author
                    
                    break;
            //Do Nothing and Exit
                default:
                    break;
            }
            
        }catch(ClassNotFoundException ex){
            System.err.println("Application not installed correctly, please refer to documentation");
        }catch(SQLException ex){
            if(ex.getMessage().equals("Unknown database 'booksdb'")){
                System.out.println("Database Does not exist, Initializng it now.......");
                this.initializeDB(input);
                this.manageISBN(input);
            }
            else{
                System.err.println(ex.getMessage());
            }
        }
    }
    
    public void manageTitle(Scanner input){
        try{
            Scanner in = new Scanner(System.in);
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL+"/"+databaseName, userName, password);
            System.out.println("Manage Titles:\nPlease choose an option");
            System.out.println("1. Show Titles");
            System.out.println("2. Add new Title");
            System.out.println("3.Update Title");
            System.out.println("4.Main Menu");
            int choice = Integer.parseInt(input.nextLine()); 
            switch (choice) {
                case 1:
                    String selectQuery = "SELECT * FROM Titles";
                    Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery(selectQuery);
                    if(!rs.next()){
                        System.out.println("No Results");
                    }
                    else{
                        rs.beforeFirst();
                        while(rs.next()){
                            String isbn = rs.getString("isbn");
                            int editionNumber = rs.getInt("editionNumber");
                            String title = rs.getString("title");
                            String year = rs.getString("year");
                            int publisherID = rs.getInt("publisherID");
                            double price = rs.getDouble("price");
                        
                            System.out.print("isbn: "+isbn+"   ");
                            System.out.print("Title: "+title+"   ");
                            System.out.print("Edition: "+editionNumber+"   ");
                            System.out.print("Year: "+year+"\t");
                            System.out.print("publisher: "+publisherID+"   ");
                            System.out.println("price: "+price);
                        }
                    }
                    System.out.println("\n");
                    break;
                case 2:
                    //SHOW PUBLISHERS HERE
                    Statement publisherSelect = connection.createStatement();
                    String publisherQuery = "SELECT * FROM Publishers";
                    ResultSet publishers = publisherSelect.executeQuery(publisherQuery);
                    if(!publishers.next()){
                        System.out.println("No publishers in database");
                        System.out.println("Must add a publisher before adding title");
                        break;
                    }
                    else{
                        publishers.beforeFirst();
                        System.out.println("-------------------");
                        while(publishers.next()){
                            System.out.println(publishers.getInt("publisherID")+"\t"+publishers.getString("publisherName"));
                        }
                        System.out.println("-1. If Publisher Not Found");
                        System.out.println("-------------------");
                        int id = Integer.parseInt(in.nextLine());
                        if(id == -1){
                            break;
                        }
                    }
                    //END
                    System.out.println("Enter ISBN of Book");
                    String isbn = in.nextLine();
                    System.out.println("Enter the edition number of book (Integer)");
                    int editionNumber = Integer.parseInt(in.nextLine());
                    System.out.println("Enter release year of book");
                    String year = in.nextLine();
                    
                    System.out.println("Enter price of title ($$.$$)");
                    Statement s = connection.createStatement();
                    boolean success = s.execute(DATABASE_EXISTS+"'"+databaseName+"'");
                    if(success){
                        String insertQuery = "INSERT INTO titles (isbn, editionNumber, Year, publisherID,"
                                + " price, title) "
                                + "VALUES ('"
                                + isbn
                                +", "
                                +editionNumber
                                +", "
                                +year
                                
                                + "'); ";
                        System.out.println(insertQuery);
                        s.executeUpdate(insertQuery);
                        System.out.println("Entry Created\n\n\n");
                    }
                    else{
                        this.initializeDB(in);
                        this.manageAuthor(in);
                    }   break;
                case 3:
                    //Update Title
                    
                    break;
            //Do Nothing and Exit
                default:
                    break;
            }
        }catch(ClassNotFoundException ex){
            System.err.println("Application not installed correctly, please refer to documentation");
        }catch(SQLException ex){
            if(ex.getMessage().equals("Unknown database 'booksdb'")){
                System.out.println("Database Does not exist, Initializng it now.......");
                this.initializeDB(input);
                this.manageTitle(input);
            }
            else{
                System.err.println(ex.getMessage());
            }
        }
    }
    /**
     * Allows user of app to change username and password, and database name
     * @param input The scanner that already exists
     */
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
                System.out.println("Enter new database name");
                databaseName = input.nextLine();
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
            if(connection != null){
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
            System.out.println("2. Manage Authors");
            System.out.println("3. Manage Publishers");
            System.out.println("4. Manage Author ISBN");
            System.out.println("5. Manage Titles");
            System.out.println("6. Drop Database");
            System.out.println("7. Settings");
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
                    app.initializeDB(in);
                    break;
                case 2:
                    app.manageAuthor(in);
                    break;
                case 3:
                    app.managePublisher(in);
                    break;
                case 4:
                    app.manageISBN(in);
                    break;
                case 5:
                    app.manageTitle(in);
                    break;
                case 6:
                    app.removeDB();
                    break;
                case 7:
                    app.Settings(in);
                    break;
                default:
                    userChoice = 0;
                    break;
            }
        }
        
    }
    
}

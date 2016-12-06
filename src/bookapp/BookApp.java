/*
 *  This is the project for Ezzat's CS157A Project
 */
package bookapp;

import java.sql.*;
import java.util.Scanner;

/**
 *
 * @author Frederic Murry
 * Student ID: 007890870
 * CS157A
 * 
 */
public class BookApp {

    private String userName;
    private String password;
    private Connection connection;
    private String databaseName = "booksdb";
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306";
    private static final String CREATE_DATABASE = "CREATE DATABASE IF NOT EXISTS ";
    private static final String DATABASE_EXISTS = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ";
    private static final String DROP_DATABASE = "DROP DATABASE booksdb";
    
    public BookApp(){
        databaseName = "booksdb";
        userName = "root";
        password = "";
        connection = null;
    }
    
    public void initializeDB(Scanner input){
        
        try{
            
            Scanner in = new Scanner(System.in);
            Class.forName("com.mysql.jdbc.Driver");
            if(connection == null){
                connection = DriverManager.getConnection(DB_URL+"jdbc:mysql://localhost:3306?autoReconnect=true&useSSL=false", userName, password);
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
                connection = DriverManager.getConnection(DB_URL+"/booksdb"+"?autoReconnect=true&useSSL=false", userName, password);
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
                    + "FOREIGN KEY (publisherID) REFERENCES publishers(publisherID) ON DELETE CASCADE "
                    + "ON UPDATE CASCADE, "
                    + "PRIMARY KEY (isbn) "
                    + ");";
                String createAuthorISBNTable = "CREATE TABLE authorISBN( "
                    + "authorID int NOT NULL , "
                    + "isbn CHAR(10) NOT NULL , "
                    + "FOREIGN KEY (authorID) REFERENCES authors(authorID) ON DELETE CASCADE "
                    + "ON UPDATE CASCADE, "
                    + "FOREIGN KEY (isbn) REFERENCES titles(isbn) ON DELETE CASCADE"
                    + ");";
            
                c.executeUpdate(createAuthorTable);
                c.executeUpdate(createPublishersTable);
                c.execute(createTitlesTable);
                c.executeUpdate(createAuthorISBNTable);
                seedDatabase();
            }
            else{
                System.out.println("Database Creation Failed\n\n\n");
                
            }
            
            
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println(ex.getMessage());
             this.removeDB();
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
                connection = DriverManager.getConnection(DB_URL+"?autoReconnect=true&useSSL=false", userName, password);
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
    
    /**
     * Populates the Database
     */
    public void seedDatabase() {
        
    }
    
    /**
     * Allows user to view, and update author table
     * @param input 
     */
    public void manageAuthor(Scanner input){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL+"/"+databaseName+"?autoReconnect=true&useSSL=false", userName, password);
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
                    String firstName = input.nextLine();
                    System.out.println("Enter author's Last Name");
                    String lastName = input.nextLine();
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
                        System.out.println(insertQuery);
                    }
                    else{
                        this.initializeDB(input);
                        this.manageAuthor(input);
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
                    int authorid = Integer.parseInt(input.nextLine());
                    System.out.println("Enter Author's new First Name");
                    String newFName = input.nextLine();
                    System.out.println("Enter Author's new Last Name");
                    String newLName = input.nextLine();
                    String updateQuery = "UPDATE authors SET firstName = "
                            + "'"+newFName+"', lastName = "
                            + "'"+newLName+"' WHERE authorID = "+authorid+";";
                    authorstmt.executeUpdate(updateQuery);
                    System.out.println(updateQuery);
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
                    int authorid2 = Integer.parseInt(input.nextLine());
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
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL+"/"+databaseName+"?autoReconnect=true&useSSL=false", userName, password);
            System.out.println("Manage Publishers:\nPlease choose an option");
            System.out.println("1. Show Publishers");
            System.out.println("2. Add new Publisher");
            System.out.println("3.Update Publisher");
            System.out.println("4.Delete Publisher");
            System.out.println("5.Main Menu");
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
                        System.out.println("--------------------");
                        while(rs.next()){
                        
                            int id = rs.getInt("publisherID");
                            String name = rs.getString("publisherName");
                            System.out.print("ID: "+id+"\t");
                            System.out.println("Name: "+name);
                        }
                    }
                    System.out.println("--------------------");
                    System.out.println("\n");
                    break;
                case 2:
                    System.out.println("Enter Name of Publisher");
                    String name = input.nextLine();
                    name = name.replace("'", "");
                    name = name.replace(",", "");
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
                        this.initializeDB(input);
                        this.managePublisher(input);
                    }   break;
                case 3:
                    //Update publishers
                    String publisherQuery = "SELECT * FROM publishers";
                    Statement publisherstmt = connection.createStatement();
                    ResultSet publishers = publisherstmt.executeQuery(publisherQuery);
                    if(!publishers.next()){
                        System.out.println("No Publishers to edit");
                    }
                    else{
                        publishers.beforeFirst();
                        System.out.println("------------------------------");
                        while(publishers.next()){
                            int id = publishers.getInt("publisherID");
                            String pubName = publishers.getString("publisherName");
                            System.out.print("ID: "+id+"\t");
                            System.out.println("Name: "+pubName+"\t");
                        }
                    }
                    System.out.println("--------------------------");
                    System.out.println("\nChoose an Publisher to edit (Type the integer next to them");
                    int publisherid = Integer.parseInt(input.nextLine());
                    System.out.println("Enter Publishers's new Name");
                    String newName = input.nextLine();
                    newName = newName.replace("'", "");
                    String updateQuery = "UPDATE publishers SET publisherName = "
                            + "'"+newName+"'"
                            +" WHERE publisherID = "+publisherid+";";
                    publisherstmt.executeUpdate(updateQuery);
                    System.out.println("Publisher Updated\n");
                    
                    break;
                case 4:
                    String publisherQuery2 = "SELECT * FROM publishers";
                    Statement publisherstmt2 = connection.createStatement();
                    ResultSet publishers2 = publisherstmt2.executeQuery(publisherQuery2);
                    if(!publishers2.next()){
                        System.out.println("No Publishers to edit");
                    }
                    else{
                        publishers2.beforeFirst();
                        System.out.println("-----------------------");
                        while(publishers2.next()){
                            int id = publishers2.getInt("publisherID");
                            String name1 = publishers2.getString("publisherName");
                            System.out.print("ID: "+id+"\t");
                            System.out.println("Name: "+name1+"\t");
                        }
                        System.out.println("-----------------------");
                    }
                    System.out.println("\nChoose an publisher to Delete (Type the integer next to them");
                    int publisherid2 = Integer.parseInt(input.nextLine());
                    String deleteQuery = "DELETE FROM publishers WHERE publisherID = "+publisherid2;
                    publisherstmt2.executeUpdate(deleteQuery);
                    System.out.println("Publisher Deleted\n");
                    break;
                case 5:
            //Do Nothing and Exit
                    break;
                default:
                    
                    break;
            }
            
        }catch(SQLException ex){
            if(ex.getMessage().equals("Unknown database 'booksdb'")){
                System.out.println("Database Does not exist, Initializng it now.......");
                this.initializeDB(input);
                this.managePublisher(input);
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
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL+"/"+databaseName+"?autoReconnect=true&useSSL=false", userName, password);
            System.out.println("Manage ISBN:\nPlease choose an option");
            System.out.println("1. Show ISBN");
            System.out.println("2.Main Menu");
            int choice = Integer.parseInt(input.nextLine());
            switch (choice) {
                case 1:
                    String selectQuery = "SELECT * FROM authorISBN;";
                    Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery(selectQuery);
                    if(!rs.next()){
                        System.out.println("No Results");
                    }
                    else{
                        System.out.println("--------------------------");
                        rs.beforeFirst();
                        while(rs.next()){
                        
                            int id = rs.getInt("authorID");
                            String isbn = rs.getString("isbn");
                            System.out.println("ID: "+id+"\t"+"ISBN: "+isbn);
                        }
                    }
                    System.out.println("--------------------------");
                    System.out.println("\n");
                    break;
                case 2:
                    
                    break;
                
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
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL+"/"+databaseName+"?autoReconnect=true&useSSL=false", userName, password);
            System.out.println("Manage Titles:\nPlease choose an option");
            System.out.println("1. Show Titles");
            System.out.println("2. Add new Title");
            System.out.println("3.Update Title");
            System.out.println("4.Main Menu");
            int choice = Integer.parseInt(input.nextLine()); 
            switch (choice) {
                case 1:
                    String selectQuery = "SELECT * FROM  titles, authorISBN, authors, publishers WHERE titles.publisherID = publishers.publisherID AND titles.isbn = authorISBN.isbn AND authorISBN.authorID = authors.authorID;";
                    Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery(selectQuery);
                    if(!rs.next()){
                        System.out.println("No Results");
                    }
                    else{
                        rs.beforeFirst();
                        System.out.println("----------------------------------------------------------------------");
                        while(rs.next()){
                            String isbn = rs.getString("isbn");
                            int editionNumber = rs.getInt("editionNumber");
                            String title = rs.getString("title");
                            String year = rs.getString("year");
                            String publisher = rs.getString("publisherName");
                            String author = rs.getString("firstName")+" "+rs.getString("lastName");
                            double price = rs.getDouble("price");
                        
                            System.out.print("Author: "+author+"   ");
                            System.out.print("isbn: "+isbn+"   ");
                            System.out.print("Title: "+title+"   ");
                            System.out.print("Edition: "+editionNumber+"   ");
                            System.out.print("Year: "+year+"   ");
                            System.out.print("publisher: "+publisher+"   ");
                            System.out.println("price: $"+price);
                        }
                        System.out.println("----------------------------------------------------------------------");
                    }
                    System.out.println("\n");
                    break;
                case 2:
                    //Add new book title
                    //SHOW PUBLISHERS HERE
                    Statement publisherSelect = connection.createStatement();
                    String publisherQuery = "SELECT * FROM Publishers";
                    ResultSet publishers = publisherSelect.executeQuery(publisherQuery);
                    int publisherID;
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
                        System.out.println("Choose the integer that corresponds to the publisher");
                        publisherID = Integer.parseInt(input.nextLine());
                        if(publisherID == -1){
                            break;
                        }
                    }
                    //END Publisher
                    //SHOW Authors HERE
                    Statement authorSelect = connection.createStatement();
                    String authorQuery = "SELECT * FROM authors";
                    ResultSet authors = authorSelect.executeQuery(authorQuery);
                    int authorID;
                    if(!authors.next()){
                        System.out.println("No Authors in database");
                        System.out.println("Must add an Author before adding title");
                        break;
                    }
                    else{
                        authors.beforeFirst();
                        System.out.println("-------------------");
                        while(authors.next()){
                            System.out.println(authors.getInt("authorID")+"\t"+authors.getString("firstName")+" "+authors.getString("lastName"));
                        }
                        System.out.println("-1. If Author Not Found");
                        System.out.println("-------------------");
                        System.out.println("Choose the integer that corresponds to the Author");
                        authorID = Integer.parseInt(input.nextLine());
                        if(publisherID == -1){
                            break;
                        }
                    }
                    //END Author
                    System.out.println("Enter ISBN of Book");
                    String isbn = input.nextLine();
                    isbn = isbn.replace("'", "");
                    isbn = isbn.replace(",","");
                    System.out.println("Enter the edition number of book (Integer)");
                    int editionNumber = Integer.parseInt(input.nextLine());
                    System.out.println("Enter release year of book");
                    String year = input.nextLine();
                    year = year.replace("'", "");
                    year = year.replace(",", "");
                    
                    System.out.println("Enter price of title ($$.$$)");
                    double price = Double.parseDouble(input.nextLine());
                    System.out.println("Enter the title name of book");
                    String title = input.nextLine();
                    title = title.replace("'", "");
                    title = title.replace(",", "");
                    Statement s = connection.createStatement();
                    boolean success = s.execute(DATABASE_EXISTS+"'"+databaseName+"'");
                    if(success){
                        String insertQuery = "INSERT INTO titles (isbn, editionNumber, Year, publisherID,"
                                + " price, title) "
                                + "VALUES ('"
                                + isbn
                                +"', "
                                +editionNumber
                                +", '"
                                +year
                                +"', "
                                +publisherID
                                +", "
                                +price
                                +", '"
                                +title
                                + "'); ";
                        String isbnInsertQuery = "INSERT INTO authorISBN (authorID, isbn)"
                                + " VALUES ("
                                + authorID
                                + ", '"
                                + isbn
                                + "'); ";
                        //System.out.println(insertQuery);
                        s.executeUpdate(insertQuery);
                        s.executeUpdate(isbnInsertQuery);
                        //Now Insert into AUthor ISBN TABLE
                        System.out.println("Entry Created\n\n\n");
                    }
                    else{
                        this.initializeDB(input);
                        this.manageAuthor(input);
                    }   break;
                case 3:
                    //Update Title
                    String selectQuery2 = "SELECT * FROM  titles, authorISBN, authors, publishers WHERE titles.publisherID = publishers.publisherID AND titles.isbn = authorISBN.isbn AND authorISBN.authorID = authors.authorID;";
                    Statement stmt2 = connection.createStatement();
                    ResultSet rs2 = stmt2.executeQuery(selectQuery2);
                    if(!rs2.next()){
                        System.out.println("No Results");
                    }
                    else{
                        int i = 1;
                        rs2.beforeFirst();
                        System.out.println("----------------------------------------------------------------------");
                        while(rs2.next()){
                            
                            String isbn2 = rs2.getString("isbn");
                            int editionNumber2 = rs2.getInt("editionNumber");
                            String title2 = rs2.getString("title");
                            String year2 = rs2.getString("year");
                            String publisher2 = rs2.getString("publisherName");
                            String author2 = rs2.getString("firstName")+" "+rs2.getString("lastName");
                            double price2 = rs2.getDouble("price");
                            System.out.print(i+". ");
                            System.out.print("Author: "+author2+"   ");
                            System.out.print("isbn: "+isbn2+"   ");
                            System.out.print("Title: "+title2+"   ");
                            System.out.print("Edition: "+editionNumber2+"   ");
                            System.out.print("Year: "+year2+"   ");
                            System.out.print("publisher: "+publisher2+"   ");
                            System.out.println("price: $"+price2);
                            i++;
                        }
                        System.out.println("----------------------------------------------------------------------");
                        System.out.println("Enter the integer that corresponds to the title you want to change");
                        int titleChoice = Integer.parseInt(input.nextLine());
                        rs2.beforeFirst();
                        for(int j = 1; j<i;j++){
                            rs2.next();
                        }
                        String currentTitle = rs2.getString("title");
                        System.out.println("Enter new Title");
                        String newTitle = input.nextLine();
                        newTitle = newTitle.replace("'", "");
                        newTitle = newTitle.replace(",", "");
                        newTitle = newTitle.replace("\"", "");
                        System.out.println("Enter new edition");
                        int newEdition = Integer.parseInt(input.nextLine());
                        System.out.println("Enter new Year");
                        String newYear = input.nextLine();
                        newYear = newYear.replace("'", "");
                        newYear = newYear.replace(",", "");
                        newYear = newYear.replace("\"", "");
                        System.out.println("Enter new price ($$.$$)");
                        double newPrice = Double.parseDouble(input.nextLine());
                        System.out.println("Enter new ISBN");
                        String newISBN = input.nextLine();
                        newISBN = newISBN.replace("'", "");
                        newISBN = newISBN.replace("\"", "");
                        newISBN = newISBN.replace(",", "");
                        //SHOW PUBLISHERS HERE
                    Statement publisherSelect2 = connection.createStatement();
                    String publisherQuery2 = "SELECT * FROM Publishers";
                    ResultSet publishers2 = publisherSelect2.executeQuery(publisherQuery2);
                    int publisherID2;
                    if(!publishers2.next()){
                        System.out.println("No publishers in database");
                        System.out.println("Must add a publisher before adding title");
                        break;
                    }
                    else{
                        publishers2.beforeFirst();
                        System.out.println("-------------------");
                        while(publishers2.next()){
                            System.out.println(publishers2.getInt("publisherID")+"\t"+publishers2.getString("publisherName"));
                        }
                        System.out.println("-1. If Publisher Not Found");
                        System.out.println("-------------------");
                        System.out.println("Choose the integer that corresponds to the publisher");
                        publisherID2 = Integer.parseInt(input.nextLine());
                        if(publisherID2 == -1){
                            break;
                        }
                    }
                    //END Publisher
                    String updateTitleQuery = "UPDATE titles SET isbn = '"
                            + newISBN
                            +"', editionNumber = "
                            + newEdition
                            +", Year = '"
                            + newYear
                            +"', publisherID = "
                            +publisherID2
                            +", price = "
                            +newPrice
                            +", title = '"
                            + newTitle
                            +"' WHERE title = '"
                            + currentTitle
                            +"';";
                    stmt2.executeUpdate(updateTitleQuery);
                        
                        
                    }
                    System.out.println("\n");
                    break;
                case 4:
                    
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
                System.err.println(ex.getMessage());
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
                    System.out.println("Not a valid Option......");
                    userChoice = 0;
                    break;
            }
        }
        
    }
    
}

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFileChooser;
import static javax.swing.UIManager.getString;


public class LoadDatabaseTables {

     private static Statement state; 
     private static Connection conn = null; 
     private static ObjectInputStream in = null; 
     private static String dbName = null; 
     private static String path = null;
   
    public static void main(String[] args) {
        
        
        
        try{
            //loads the driver
        Class.forName("org.sqlite.JDBC"); 
        }catch(ClassNotFoundException e){
        e.printStackTrace();
        }
        System.out.println("JDBC driver was loaded successfully");
        
        try{
        dbName = "\\DvdStoreDatabase.db";
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("Select database output directory");
        chooser.showSaveDialog(null); 
        path = chooser.getSelectedFile().getCanonicalPath();
        }catch(IOException e){
        e.printStackTrace();
        }catch(NullPointerException e){
            System.exit(0);
        e.printStackTrace();
        }
        
        
        try{
            //creates connection to driver
        conn = DriverManager.getConnection("jdbc:sqlite:"+path+dbName); 
        System.out.println("Connection to database was established");
        state = conn.createStatement(); 
        
        //drops all previously created table to prevent SQL errors
        //state.executeUpdate("drop table customer");
        //state.executeUpdate("drop table dvd");
        //state.executeUpdate("drop table rental");
        
        
        
             //creates the customer table in the database
        state.executeUpdate("create table customer(custNumber integer  primary key not null, firstName text not null,"
                + "surname text not null , phoneNumber text not null, credit float not null, canRent boolean not null)"); 
       System.out.println("customer table was created");
            //creates the dvd table in the database
       state.executeUpdate("create table dvd(dvdNumber integer primary key, title text, category text, "
              + "newRelease boolean, availableForRent boolean)"); 
       System.out.println("dvd table was created");
             //creates the rental table in the database
       state.executeUpdate("create table rental(rentalNumber integer not null primary key, dateRented text,"
               + "dateReturned text, custNumber int not null, dvdNumber integer not null)"); 
       System.out.println("rental table was created");
       
        }catch(SQLException e){
            e.printStackTrace();
        }
        
        
      
        
       //runs LoadCustomerTable
        LoadCustomerTable loadcus = new LoadCustomerTable(); 
        loadcus.run();
        System.out.println("LoadCustomerTable has started");
         
        //runs LoadDvdTable
        LoadDvdTable loadDvd = new LoadDvdTable(); 
        loadDvd.run();
        System.out.println("LoadDvdTable has started");
         
        //runs LoadRentalTable
        LoadRentalTable loadRental = new LoadRentalTable(); 
        loadRental.run();
        System.out.println("LoadRentalTable has started");
        
         try{
             //counts all rows from the customer table
        int a = 0; 
        ResultSet rs = state.executeQuery("select * from customer"); 
        while(rs.next()){
        a++; 
        }
        System.out.println(a+" rows were added to the cutomer table");
        
            //counts all rows from the dvd table
        int b = 0; 
        ResultSet rs1 = state.executeQuery("select * from dvd"); 
        while(rs1.next()){
        b++; 
        }
        System.out.println(b+" rows were added to the dvd table");
        
            //counts all rows from the rental table
        int c = 0; 
         ResultSet rs2 = state.executeQuery("select * from rental"); 
        while(rs2.next()){
        c++; 
        }
        System.out.println(c+" rows were added to the rental table");
        
        }catch(SQLException e){
        e.printStackTrace();
        }
      
         System.out.println("Closing database connection...");
        //closes connection to database
        try{
        if(conn != null && state !=null){
        state.close();
        conn.close();
        }
            }catch(SQLException e){
            e.printStackTrace();
            }
      System.out.println("Database connection has been closed");
       
    }
    
    public String path(){
    return path+dbName; 
    }
        
    }
    
    
    




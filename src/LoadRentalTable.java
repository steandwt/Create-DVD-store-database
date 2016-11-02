import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;


public class LoadRentalTable{
    
     private static Statement state; 
     private static Connection conn = null; 
     private static ObjectInputStream in = null; 
     private static Rental rental; 
     private static LoadDatabaseTables l = new LoadDatabaseTables();
    
    public void run() {
        
          try{
        Class.forName("org.sqlite.JDBC"); 
        }catch(ClassNotFoundException e){
        e.printStackTrace();
        }
           try{
        conn = DriverManager.getConnection("jdbc:sqlite:"+l.path()); 
        state = conn.createStatement(); 
           }catch(SQLException e){
           e.printStackTrace();
           }
           
            try{
        in = new ObjectInputStream(new FileInputStream("rental.ser")); 
       
        }catch(IOException e){
        e.printStackTrace();
        }
            
         try{
        while((rental = (Rental) in.readObject()) !=null){
            
         int rentalNum = rental.getRentalNumber(); 
         String dateRented = rental.getDateRented(); 
         String dateReturned = rental.getDateReturned(); 
         int cusNum = rental.getCusNumber(); 
         int dvdNum = rental.dvdNumber(); 
          
          try(PreparedStatement stmt = conn.prepareStatement("insert into rental(rentalNumber, dateRented, dateReturned, "
                  + "custNumber, dvdNumber) values(?, ?, ?, ?, ?)")){
          
              stmt.setInt(1, rentalNum);
              stmt.setString(2, dateRented);
              stmt.setString(3, dateReturned);
              stmt.setInt(4, cusNum);
              stmt.setInt(5, dvdNum);
              stmt.executeUpdate(); 
              
          }catch(SQLException e){
          e.printStackTrace();
          }
        
        }
        
        }catch(EOFException e){
                return; 
                }catch(IOException e){
        e.printStackTrace();
        }catch(ClassNotFoundException e){
        e.printStackTrace();
        }
         
            try{
        if(conn != null && state != null){
        state.close();
        conn.close();
        }
            }catch(SQLException e){
            e.printStackTrace();
            }
            
           
    }
    
}
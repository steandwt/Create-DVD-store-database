
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


public class LoadCustomerTable{
    
     private static Statement state; 
     private static Connection conn = null; 
     private static ObjectInputStream in = null; 
     private static Customer customer; 
     private static LoadDatabaseTables l = new LoadDatabaseTables();

    
    public void run() {
        
          try{
                //loads database driver
        Class.forName("org.sqlite.JDBC"); 
        }catch(ClassNotFoundException e){
        e.printStackTrace();
        }
           try{
                //connects to database
        conn = DriverManager.getConnection("jdbc:sqlite:"+l.path()); 
        state = conn.createStatement(); 
           }catch(SQLException e){
           e.printStackTrace();
           }
           
            try{
        in = new ObjectInputStream(new FileInputStream("Customers.ser")); 
       
        }catch(IOException e){
        e.printStackTrace();
        }
            
         try{
        while((customer = (Customer) in.readObject()) !=null){
            
          int custNumber = customer.getCustNumber(); 
          String firstName = customer.getName(); 
          String surname = customer.getSurname(); 
          String phoneNum = customer.getPhoneNum(); 
          double credit = customer.getCredit(); 
          boolean canRent = customer.canRent(); 
          
          try(PreparedStatement stmt = conn.prepareStatement("insert into customer(custNumber, firstName, surname, "
                  + "phoneNumber, credit, canRent) values(?, ?, ?, ?, ?, ?)")){
          
              stmt.setInt(1, custNumber);
              stmt.setString(2, firstName);
              stmt.setString(3, surname);
              stmt.setString(4, phoneNum);
              stmt.setDouble(5, credit);
              stmt.setBoolean(6, canRent);
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

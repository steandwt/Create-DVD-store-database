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


public class LoadDvdTable{
    
     private static Statement state; 
     private static Connection conn = null; 
     private static ObjectInputStream in = null; 
     private static DVD dvd; 
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
        in = new ObjectInputStream(new FileInputStream("Movies.ser")); 
       
        }catch(IOException e){
        e.printStackTrace();
        }
            
         try{
        while((dvd = (DVD) in.readObject()) !=null){
            
          int dvdNum = dvd.getDvdNumber(); 
          String title = dvd.getTitle(); 
          String cat = dvd.getCategory(); 
          boolean newRelease = dvd.isNewRelease(); 
          boolean available = dvd.isAvailable(); 
          
          try(PreparedStatement stmt = conn.prepareStatement("insert into dvd(dvdNumber, title, category, "
                  + "newRelease, availableForRent) values(?, ?, ?, ?, ?)")){
          
              stmt.setInt(1, dvdNum);
              stmt.setString(2, title);
              stmt.setString(3, cat);
              stmt.setBoolean(4, newRelease);
              stmt.setBoolean(5, available);
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

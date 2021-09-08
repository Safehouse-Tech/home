/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shopgateway.config;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author gagan
 */
public class ODBConnection {
    
    InitialContext context;
    DataSource datasource;
    public Connection connection;
    
    private static ODBConnection db_instance = null;               // static variable single_instance of type Singleton 
  
    private ODBConnection()      // private constructor restricted to this class itself 
    { 
        try 
        {
            context = new InitialContext();
            datasource = (DataSource) context.lookup("jdbc/__AWS");
            connection = datasource.getConnection();
        } catch (SQLException | NamingException ex) 
        {
            connection = null;
            ex.printStackTrace();
        }
    }
    
    public static ODBConnection getInstance() 
    { 
       // System.out.println("oracle_database_instance: "+ database_instance);

        if (db_instance == null) 
        {
            db_instance = new ODBConnection();
        }
        return db_instance; 
    } 
    
    /*
    if counter goes more than 50 refresh 
    

    */
    
}

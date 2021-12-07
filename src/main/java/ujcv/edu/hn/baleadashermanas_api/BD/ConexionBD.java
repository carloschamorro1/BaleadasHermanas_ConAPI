/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baleadashermanas.BD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Carlos
 */
public class ConexionBD {
    
    private static Connection con = null;
   
    private ConexionBD() throws SQLException{
        
       String url = "jdbc:sqlserver://localhost:1433;"
               + "databaseName=baleadashermanas;user=admin;"
               + "password=admin";
       String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";     
       try{
       Class.forName(driver);
       con = DriverManager.getConnection(url);
       } catch(ClassNotFoundException | SQLException e){
                e.printStackTrace();
        }
    }
    
    public static Connection obtenerConexion() throws SQLException{
        if (con == null){
            ConexionBD conectorSQL = new ConexionBD(); 
        }
        return con;
    }  
}

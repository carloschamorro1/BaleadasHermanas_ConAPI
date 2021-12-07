/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baleadashermanas.utilidades;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Carlos
 */
import baleadashermanas.BD.ConexionBD;
public class Queries {
    
    Connection con;
    ResultSet rs;
    
    public Queries() throws SQLException{
        this.con = ConexionBD.obtenerConexion();
    }
    
    public ArrayList<String> llenarClientes() throws SQLException{
         ArrayList<String> lista = new ArrayList<String>();
         String q= "SELECT * from cliente";
         Statement st;
         st = con.createStatement();
         try{
            rs=st.executeQuery(q);
            while(rs.next()){
                lista.add(rs.getString("idcliente") +". " + rs.getString("primer_nombre_cliente") + " " + rs.getString("primer_apellido_cliente"));
            }
         }
        catch(Exception e){ 
            
              }
          return lista;  
    }
    
    public ArrayList<String> llenarID() throws SQLException{
         ArrayList<String> lista = new ArrayList<String>();
         String q= "SELECT * from cliente";
         Statement st;
         st = con.createStatement();
         try{
            rs=st.executeQuery(q);
            while(rs.next()){
                lista.add(rs.getString("idcliente") +"|" + rs.getString("primer_nombre_cliente") + "|" + rs.getString("primer_apellido_cliente"));
            }
         }
        catch(Exception e){ 
            
              }
          return lista;  
    }
    
}

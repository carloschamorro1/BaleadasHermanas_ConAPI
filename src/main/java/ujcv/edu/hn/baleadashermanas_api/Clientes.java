/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ujcv.edu.hn.baleadashermanas_api;

import com.google.gson.Gson;
import com.placeholder.PlaceHolder;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import ujcv.edu.hn.baleadashermanas_api.Empleados;
import ujcv.edu.hn.baleadashermanas_api.Principal;
import ujcv.edu.hn.baleadashermanas_api.model.Cliente;
import ujcv.edu.hn.baleadashermanas_api.model.Empleado;

/**
 *
 * @author Carlos
 */
public class Clientes extends javax.swing.JFrame {

    String res = "";
    String URL = "http://192.168.178.34:8080/api/v1/cliente";
    boolean existeCliente = false;
    boolean existeRTN = false;
    Long idCliente = 0L;
    /**
     * Creates new form Clientes
     * @param nombreUsuario
     * @throws java.sql.SQLException
     */
    public Clientes(String nombreUsuario){
        initComponents();
        informacionGeneral();
        holders();
        lbl_nombreUsuario.setText(nombreUsuario);
        lbl_Dni.setVisible(false);
    }
    
    public Clientes(){
        
    }
    
    public final void informacionGeneral(){
        this.setTitle("Clientes");
        this.setLocationRelativeTo(null);
        this.setIconImage(new javax.swing.ImageIcon("src\\main\\java\\ujcv\\edu\\hn\\Img\\Titulo.png").getImage());
    }
    
    public final void holders(){
        PlaceHolder holder;
        holder = new PlaceHolder(txt_primerNombreCliente,Color.gray,Color.black,"Ingrese su primer nombre",false,"Roboto",25);
        holder = new PlaceHolder(txt_segundoNombreCliente,Color.gray,Color.black,"Ingrese su segundo nombre",false,"Roboto",25);
        holder = new PlaceHolder(txt_primerApellidoCliente,Color.gray,Color.black,"Ingrese su primer apellido",false,"Roboto",25);
        holder = new PlaceHolder(txt_segundoApellidoCliente,Color.gray,Color.black,"Ingrese su segundo apellido",false,"Roboto",25);
        
        holder = new PlaceHolder(txt_telefonoCliente,Color.gray,Color.black,"Ingrese su número de teléfono",false,"Roboto",25);
        holder = new PlaceHolder(txt_emailCliente,Color.gray,Color.black,"Ingrese su correo eléctronico",false,"Roboto",25);
        holder = new PlaceHolder(txt_dniCliente,Color.gray,Color.black,"Ingrese su DNI",false,"Roboto",25); 
        holder = new PlaceHolder(txt_rtnCliente,Color.gray,Color.black,"Ingrese su RTN",false,"Roboto",25);
        
        
    }
    
    public void rellenar(){
        String input = "";
        input = JOptionPane.showInputDialog(this, "¿A quien desea buscar?","Consulta de cliente",JOptionPane.QUESTION_MESSAGE);
        if(input == null){
                JOptionPane.showMessageDialog(this,"La acción fue cancelada","¡AVISO!",JOptionPane.INFORMATION_MESSAGE);
        }
        else if(input.equals("")){ 
            JOptionPane.showMessageDialog(this,"Favor de ingresar los datos del cliente\n que desea buscar","¡AVISO!",JOptionPane.INFORMATION_MESSAGE);
        }
        else{
            try {
                getByDNI(input);
                if(!existeCliente){
                    return;
                }
                colorear();
                habilitarAccionesBuscar();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }  
     }
           
    }
    
    public void getByDNI(String dni){
        try{
                   existeCliente = false;
                   Client client= ClientBuilder.newClient();
                   
                   String path = URL+"/dni/"+dni;
                   WebTarget target = client.target(path);

                   Invocation.Builder solicitud =target.request();

                   Response get = solicitud.get();

                   String responseJson = get.readEntity(String.class);
                   
                   Cliente data = new Gson().fromJson(responseJson, Cliente.class);

                   switch (get.getStatus()) {
                       case 200:
                           res = "Datos recuperados";                
                           txt_primerNombreCliente.setText(data.getPrimer_nombre_cliente());
                           txt_segundoNombreCliente.setText(data.getSegundo_nombre_cliente());
                           txt_primerApellidoCliente.setText(data.getPrimer_apellido_cliente());
                           txt_segundoApellidoCliente.setText(data.getSegundo_apellido_cliente());
                           txt_telefonoCliente.setText(data.getTelefono_cliente());
                           txt_emailCliente.setText(data.getEmail_cliente());
                           txt_dniCliente.setText(data.getDnicliente());
                           txt_rtnCliente.setText(data.getRtncliente()); 
                           lbl_Dni.setText(data.getDnicliente());
                           idCliente = data.getIdcliente();
                           existeCliente = true;
                           break;
                       default:
                           res = "El DNI no existe";
                           existeCliente = false;
                           JOptionPane.showMessageDialog(this, res);
                           break;
                   }
               }catch(Exception e){
                   res = e.getMessage();
               }
    }
    
    public void colorear(){
        txt_primerNombreCliente.setForeground(Color.black);
        txt_segundoNombreCliente.setForeground(Color.black);
        txt_primerApellidoCliente.setForeground(Color.black);
        txt_segundoApellidoCliente.setForeground(Color.black);
        txt_telefonoCliente.setForeground(Color.black);
        txt_emailCliente.setForeground(Color.black);
        txt_dniCliente.setForeground(Color.black);
        txt_rtnCliente.setForeground(Color.black);
        lbl_Dni.setForeground(Color.black);
    }
    
     public void habilitarAccionesBuscar(){
         btn_agregar.setEnabled(false);
         btn_actualizar.setEnabled(true);
         btn_eliminar.setEnabled(true);
     }
     
         public boolean estaVacio(){
        if(txt_primerNombreCliente.getText().equals("Ingrese su primer nombre")){
            JOptionPane.showMessageDialog(this,"Por favor ingrese el primer nombre del cliente","Ingrese el primer nombre",JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        if(txt_segundoNombreCliente.getText().equals("Ingrese su segundo nombre")){
            JOptionPane.showMessageDialog(this,"Por favor ingrese el segundo nombre del cliente","Ingrese el segundo nombre",JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        if(txt_primerApellidoCliente.getText().equals("Ingrese su primer apellido")){
            JOptionPane.showMessageDialog(this,"Por favor ingrese el primer apellido del cliente","Ingrese el primer apellido",JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        if(txt_primerApellidoCliente.getText().equals("Ingrese su segundo apellido")){
            JOptionPane.showMessageDialog(this,"Por favor ingrese el segundo apellido del cliente","Ingrese el segundo apellido",JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        if(txt_telefonoCliente.getText().equals("Ingrese su número de teléfono")){
            JOptionPane.showMessageDialog(this,"Por favor ingrese el teléfono del cliente","Ingrese el teléfono",JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        if(txt_emailCliente.getText().equals("Ingrese su correo eléctronico")){
            JOptionPane.showMessageDialog(this,"Por favor ingrese el email del cliente","Ingrese el email",JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        if(txt_dniCliente.getText().equals("Ingrese su DNI")){
            JOptionPane.showMessageDialog(this,"Por favor ingrese el DNI del cliente","Ingrese el DNI",JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        if(txt_rtnCliente.getText().equals("Ingrese su RTN")){
            JOptionPane.showMessageDialog(this,"Por favor ingrese el nombre de usuario del cliente","Ingrese el nombre de usuario",JOptionPane.INFORMATION_MESSAGE);
            return true;
        }

        
        return false;
    }
         
    public void existeDNI(String dni){
        try{
                   existeCliente = false;
                   Client client= ClientBuilder.newClient();
                   
                   String path = URL+"/dni/"+dni;
                   WebTarget target = client.target(path);

                   Invocation.Builder solicitud =target.request();

                   Response get = solicitud.get();

                   String responseJson = get.readEntity(String.class);

                   switch (get.getStatus()) {
                       case 200:
                           res = "El DNI ya existe";
                           existeCliente = true;
                           break;
                       case 405:
                           res = "El DNI no existe";
                           existeCliente = false;
                           break;
                   }
               }catch(Exception e){
                   res = e.getMessage();
               }
               finally{
             if(!existeCliente){        
                 return;        
             }
             JOptionPane.showMessageDialog(this, res);
        }
    }
    
    
   
 
      
     private boolean validarLongitudTelefono(JTextField texto, int longitud){
       if(texto.getText().length() == longitud){
                Pattern pattern = Pattern.compile("[23789]");
                Matcher matcher=pattern.matcher(texto.getText().substring(0,1));
                if(matcher.matches()){ 
                        return true;
                    }else{
                        JOptionPane.showMessageDialog(null, "El número de teléfono debe comenzar con: 2,3,7,8 o 9");
                        return false;
                    } 
       }
        else{
       }
       JOptionPane.showMessageDialog(null, "El número de teléfono debe ser de 8 dígitos", "Longitud del número de telefono",JOptionPane.INFORMATION_MESSAGE);
       return false;
    }
     
     private boolean validarDni(String identidad){
        String id = identidad.substring(0, 1);
        
        if(identidad.length() < 13){
             JOptionPane.showMessageDialog(null, "El DNI es de 13 dígitos, usted ha ingresado solamente "+identidad.length()+" dígitos.", "DNI muy corto", JOptionPane.ERROR_MESSAGE);
        }
        if(identidad.length() == 13){
             if("0".equals(id)){
                 return true;
             }
             else if("1".equals(id)){
                 return true;
             }
             else{
                 JOptionPane.showMessageDialog(null, "El DNI sólo puede comenzar con 0 o 1 ", "Error en campo DNI", JOptionPane.ERROR_MESSAGE);
                 return false;
             }
        }
        else{
           return false; 
        }    
    }
     
     public void limpiar(){
        txt_primerNombreCliente.setText("");
        txt_segundoNombreCliente.setText("");
        txt_primerApellidoCliente.setText("");
        txt_segundoApellidoCliente.setText("");
        txt_telefonoCliente.setText("");
        txt_emailCliente.setText("");
        txt_dniCliente.setText("");
        txt_rtnCliente.setText("");
        lbl_Dni.setText("");
    }
     
     public void restablecer(){
         limpiar();
         holders();
         btn_agregar.setEnabled(true);
         btn_buscar.setEnabled(true);
         btn_actualizar.setEnabled(false);
         btn_eliminar.setEnabled(false);
         txt_primerNombreCliente.requestFocus();
     }
     

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        kGradientPanel1 = new keeptoo.KGradientPanel();
        jPanel1 = new javax.swing.JPanel();
        txt_primerNombreCliente = new javax.swing.JTextField();
        txt_segundoNombreCliente = new javax.swing.JTextField();
        txt_primerApellidoCliente = new javax.swing.JTextField();
        txt_segundoApellidoCliente = new javax.swing.JTextField();
        txt_telefonoCliente = new javax.swing.JTextField();
        txt_emailCliente = new javax.swing.JTextField();
        txt_dniCliente = new javax.swing.JTextField();
        txt_rtnCliente = new javax.swing.JTextField();
        lbl_nombreUsuario = new javax.swing.JLabel();
        btn_agregar = new javax.swing.JButton();
        btn_actualizar = new javax.swing.JButton();
        btn_buscar = new javax.swing.JButton();
        btn_eliminar = new javax.swing.JButton();
        lbl_home = new javax.swing.JLabel();
        lbl_usuario = new javax.swing.JLabel();
        lbl_tituloClientes = new javax.swing.JLabel();
        lbl_segundoNombreCliente = new javax.swing.JLabel();
        lbl_telefonoCliente = new javax.swing.JLabel();
        lbl_primerApellidoCliente = new javax.swing.JLabel();
        lbl_segundoApellidoCliente = new javax.swing.JLabel();
        lbl_primerNombreCliente = new javax.swing.JLabel();
        lbl_emailCliente = new javax.swing.JLabel();
        lbl_dniCliente = new javax.swing.JLabel();
        lbl_rtnEmpleado = new javax.swing.JLabel();
        lbl_Dni = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        kGradientPanel1.setkEndColor(new java.awt.Color(40, 74, 172));
        kGradientPanel1.setkStartColor(new java.awt.Color(205, 63, 145));
        kGradientPanel1.setkTransparentControls(false);
        kGradientPanel1.setPreferredSize(new java.awt.Dimension(1319, 821));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        txt_primerNombreCliente.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N
        txt_primerNombreCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_primerNombreClienteActionPerformed(evt);
            }
        });
        txt_primerNombreCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_primerNombreClienteKeyTyped(evt);
            }
        });

        txt_segundoNombreCliente.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N
        txt_segundoNombreCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_segundoNombreClienteActionPerformed(evt);
            }
        });
        txt_segundoNombreCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_segundoNombreClienteKeyTyped(evt);
            }
        });

        txt_primerApellidoCliente.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N
        txt_primerApellidoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_primerApellidoClienteActionPerformed(evt);
            }
        });
        txt_primerApellidoCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_primerApellidoClienteKeyTyped(evt);
            }
        });

        txt_segundoApellidoCliente.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N
        txt_segundoApellidoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_segundoApellidoClienteActionPerformed(evt);
            }
        });
        txt_segundoApellidoCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_segundoApellidoClienteKeyTyped(evt);
            }
        });

        txt_telefonoCliente.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N
        txt_telefonoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_telefonoClienteActionPerformed(evt);
            }
        });
        txt_telefonoCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_telefonoClienteKeyTyped(evt);
            }
        });

        txt_emailCliente.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N
        txt_emailCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_emailClienteActionPerformed(evt);
            }
        });
        txt_emailCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_emailClienteKeyTyped(evt);
            }
        });

        txt_dniCliente.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N
        txt_dniCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_dniClienteActionPerformed(evt);
            }
        });
        txt_dniCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_dniClienteKeyTyped(evt);
            }
        });

        txt_rtnCliente.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N
        txt_rtnCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_rtnClienteActionPerformed(evt);
            }
        });
        txt_rtnCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_rtnClienteKeyTyped(evt);
            }
        });

        lbl_nombreUsuario.setFont(new java.awt.Font("Roboto Black", 0, 24)); // NOI18N
        lbl_nombreUsuario.setText("Nombre de Usuario");

        btn_agregar.setBackground(new java.awt.Color(205, 63, 145));
        btn_agregar.setFont(new java.awt.Font("Roboto Black", 0, 24)); // NOI18N
        btn_agregar.setForeground(new java.awt.Color(255, 255, 255));
        btn_agregar.setText("Agregar");
        btn_agregar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_agregar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_agregarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_agregarMouseExited(evt);
            }
        });
        btn_agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_agregarActionPerformed(evt);
            }
        });

        btn_actualizar.setBackground(new java.awt.Color(205, 63, 145));
        btn_actualizar.setFont(new java.awt.Font("Roboto Black", 0, 24)); // NOI18N
        btn_actualizar.setForeground(new java.awt.Color(255, 255, 255));
        btn_actualizar.setText("Actualizar");
        btn_actualizar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_actualizar.setEnabled(false);
        btn_actualizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_actualizarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_actualizarMouseExited(evt);
            }
        });
        btn_actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_actualizarActionPerformed(evt);
            }
        });

        btn_buscar.setBackground(new java.awt.Color(205, 63, 145));
        btn_buscar.setFont(new java.awt.Font("Roboto Black", 0, 24)); // NOI18N
        btn_buscar.setForeground(new java.awt.Color(255, 255, 255));
        btn_buscar.setText("Buscar");
        btn_buscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_buscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_buscarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_buscarMouseExited(evt);
            }
        });
        btn_buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_buscarActionPerformed(evt);
            }
        });

        btn_eliminar.setBackground(new java.awt.Color(205, 63, 145));
        btn_eliminar.setFont(new java.awt.Font("Roboto Black", 0, 24)); // NOI18N
        btn_eliminar.setForeground(new java.awt.Color(255, 255, 255));
        btn_eliminar.setText("Eliminar");
        btn_eliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_eliminar.setEnabled(false);
        btn_eliminar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_eliminarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_eliminarMouseExited(evt);
            }
        });
        btn_eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_eliminarActionPerformed(evt);
            }
        });

        lbl_home.setIcon(new javax.swing.ImageIcon("src\\main\\java\\ujcv\\edu\\hn\\Img\\home-icon-silhouette.png"));
        lbl_home.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_home.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl_homeMousePressed(evt);
            }
        });

        lbl_usuario.setIcon(new javax.swing.ImageIcon("src\\main\\java\\ujcv\\edu\\hn\\Img\\profile.png"));
        lbl_usuario.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_tituloClientes.setFont(new java.awt.Font("Roboto Black", 0, 48)); // NOI18N
        lbl_tituloClientes.setText("Clientes");

        lbl_segundoNombreCliente.setIcon(new javax.swing.ImageIcon("src\\main\\java\\ujcv\\edu\\hn\\Img\\name.png"));
        lbl_segundoNombreCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl_segundoNombreClienteMousePressed(evt);
            }
        });

        lbl_telefonoCliente.setIcon(new javax.swing.ImageIcon("src\\main\\java\\ujcv\\edu\\hn\\Img\\phone-call.png"));
        lbl_telefonoCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl_telefonoClienteMousePressed(evt);
            }
        });

        lbl_primerApellidoCliente.setIcon(new javax.swing.ImageIcon("src\\main\\java\\ujcv\\edu\\hn\\Img\\name2.png"));
        lbl_primerApellidoCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl_primerApellidoClienteMousePressed(evt);
            }
        });

        lbl_segundoApellidoCliente.setIcon(new javax.swing.ImageIcon("src\\main\\java\\ujcv\\edu\\hn\\Img\\name2.png"));
        lbl_segundoApellidoCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl_segundoApellidoClienteMousePressed(evt);
            }
        });

        lbl_primerNombreCliente.setIcon(new javax.swing.ImageIcon("src\\main\\java\\ujcv\\edu\\hn\\Img\\name.png"));
        lbl_primerNombreCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl_primerNombreClienteMousePressed(evt);
            }
        });

        lbl_emailCliente.setIcon(new javax.swing.ImageIcon("src\\main\\java\\ujcv\\edu\\hn\\Img\\email.png"));
        lbl_emailCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl_emailClienteMousePressed(evt);
            }
        });

        lbl_dniCliente.setIcon(new javax.swing.ImageIcon("src\\main\\java\\ujcv\\edu\\hn\\Img\\id-card.png"));
        lbl_dniCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl_dniClienteMousePressed(evt);
            }
        });

        lbl_rtnEmpleado.setIcon(new javax.swing.ImageIcon("src\\main\\java\\ujcv\\edu\\hn\\Img\\tarjeta-de-identificacion.png"));
        lbl_rtnEmpleado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl_rtnEmpleadoMousePressed(evt);
            }
        });

        lbl_Dni.setText("prueba");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(90, 90, 90)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(lbl_segundoNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbl_primerApellidoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbl_segundoApellidoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbl_primerNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(txt_primerNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lbl_telefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txt_telefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txt_segundoApellidoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txt_primerApellidoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txt_segundoNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(42, 42, 42)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(lbl_emailCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lbl_dniCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lbl_rtnEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txt_rtnCliente)
                                        .addComponent(txt_dniCliente)
                                        .addComponent(txt_emailCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(338, 338, 338)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btn_agregar, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btn_actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(30, 30, 30)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btn_eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btn_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbl_usuario)
                        .addGap(18, 18, 18)
                        .addComponent(lbl_nombreUsuario)
                        .addGap(238, 238, 238)
                        .addComponent(lbl_tituloClientes)))
                .addContainerGap(144, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_Dni)
                    .addComponent(lbl_home))
                .addGap(21, 21, 21))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btn_actualizar, btn_agregar, btn_buscar, btn_eliminar});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_home, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbl_tituloClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_nombreUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txt_primerNombreCliente)
                        .addComponent(lbl_primerNombreCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lbl_telefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_telefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(txt_segundoNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_segundoNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txt_emailCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_emailCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_primerApellidoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_primerApellidoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(53, 53, 53)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_segundoApellidoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_segundoApellidoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_dniCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_dniCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(50, 50, 50)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_rtnEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_rtnCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(50, 50, 50)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_agregar)
                    .addComponent(btn_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_eliminar))
                .addGap(32, 32, 32)
                .addComponent(lbl_Dni, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btn_actualizar, btn_agregar, btn_buscar, btn_eliminar});

        javax.swing.GroupLayout kGradientPanel1Layout = new javax.swing.GroupLayout(kGradientPanel1);
        kGradientPanel1.setLayout(kGradientPanel1Layout);
        kGradientPanel1Layout.setHorizontalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );
        kGradientPanel1Layout.setVerticalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 763, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(47, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1305, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_primerNombreClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_primerNombreClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_primerNombreClienteActionPerformed

    private void txt_segundoNombreClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_segundoNombreClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_segundoNombreClienteActionPerformed

    private void txt_primerApellidoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_primerApellidoClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_primerApellidoClienteActionPerformed

    private void txt_segundoApellidoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_segundoApellidoClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_segundoApellidoClienteActionPerformed

    private void txt_telefonoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_telefonoClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_telefonoClienteActionPerformed

    private void txt_emailClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_emailClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_emailClienteActionPerformed

    private void txt_dniClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_dniClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_dniClienteActionPerformed

    private void txt_rtnClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_rtnClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_rtnClienteActionPerformed

    private void btn_agregarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_agregarMouseEntered
        btn_agregar.setBackground(new Color(156,2,91));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_agregarMouseEntered

    private void btn_agregarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_agregarMouseExited
        btn_agregar.setBackground(new Color(205,63,145));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_agregarMouseExited

    private void btn_actualizarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_actualizarMouseEntered
        btn_actualizar.setBackground(new Color(156,2,91));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_actualizarMouseEntered

    private void btn_actualizarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_actualizarMouseExited
        btn_actualizar.setBackground(new Color(205,63,145));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_actualizarMouseExited

    private void btn_buscarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_buscarMouseEntered
        btn_buscar.setBackground(new Color(156,2,91));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_buscarMouseEntered

    private void btn_buscarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_buscarMouseExited
        btn_buscar.setBackground(new Color(205,63,145));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_buscarMouseExited

    private void btn_eliminarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_eliminarMouseEntered
        btn_eliminar.setBackground(new Color(156,2,91));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_eliminarMouseEntered

    private void btn_eliminarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_eliminarMouseExited
        btn_eliminar.setBackground(new Color(205,63,145));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_eliminarMouseExited

    private void lbl_homeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_homeMousePressed
        Principal principal = new Principal(lbl_nombreUsuario.getText());
        this.dispose();
        principal.setVisible(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_homeMousePressed

    private void lbl_segundoNombreClienteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_segundoNombreClienteMousePressed
        txt_segundoNombreCliente.requestFocus();
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_segundoNombreClienteMousePressed

    private void lbl_telefonoClienteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_telefonoClienteMousePressed
        txt_telefonoCliente.requestFocus();
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_telefonoClienteMousePressed

    private void lbl_primerApellidoClienteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_primerApellidoClienteMousePressed
        txt_primerApellidoCliente.requestFocus();
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_primerApellidoClienteMousePressed

    private void lbl_segundoApellidoClienteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_segundoApellidoClienteMousePressed
        txt_segundoApellidoCliente.requestFocus();
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_segundoApellidoClienteMousePressed

    private void lbl_primerNombreClienteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_primerNombreClienteMousePressed
        txt_primerNombreCliente.requestFocus();
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_primerNombreClienteMousePressed

    private void lbl_emailClienteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_emailClienteMousePressed
        txt_emailCliente.requestFocus();
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_emailClienteMousePressed

    private void lbl_dniClienteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_dniClienteMousePressed
        txt_dniCliente.requestFocus();
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_dniClienteMousePressed

    private void lbl_rtnEmpleadoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_rtnEmpleadoMousePressed
        txt_rtnCliente.requestFocus();
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_rtnEmpleadoMousePressed

    private void btn_actualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_actualizarActionPerformed
        btn_actualizar.setBackground(new Color(40,74,172));
        
        String nombreCliente = txt_primerNombreCliente.getText() + " " + txt_primerApellidoCliente.getText();
         if(JOptionPane.showConfirmDialog(null,"¿Está seguro que desea actualizar el registro del cliente "+nombreCliente+"?","Confirmación de actualización",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE
        )==JOptionPane.YES_OPTION){
            try{
                if(estaVacio()){
                    return;
                }

                if(!validarLongitudTelefono(txt_telefonoCliente,8)){
                    return;
                }

                 if(!validarDni(txt_dniCliente.getText())){
                    return;

                }
                 
                 Client client= ClientBuilder.newClient();

                   WebTarget target = client.target(URL + "");

                   Invocation.Builder solicitud =target.request();

                   Cliente cliente = new Cliente();
                   cliente.setIdcliente(idCliente);
                   cliente.setPrimer_nombre_cliente(txt_primerNombreCliente.getText());
                   cliente.setSegundo_nombre_cliente(txt_segundoNombreCliente.getText());
                   cliente.setPrimer_apellido_cliente(txt_primerApellidoCliente.getText());
                   cliente.setSegundo_apellido_cliente(txt_segundoApellidoCliente.getText());
                   cliente.setTelefono_cliente(txt_telefonoCliente.getText());
                   cliente.setEmail_cliente(txt_emailCliente.getText());
                   cliente.setDnicliente(txt_dniCliente.getText());
                   cliente.setRtncliente(txt_rtnCliente.getText());

                   Gson gson = new Gson();
                   String jsonString = gson.toJson(cliente);

                   Response put = solicitud.put(Entity.json(jsonString));


                   String responseJson = put.readEntity(String.class);
                   Empleado data = new Gson().fromJson(responseJson, Empleado.class);


                   switch (put.getStatus()) {
                       case 200:
                           res = "Cliente actualizado";                    
                           JOptionPane.showMessageDialog(null,res);
                           restablecer();
                           break;
                       default:
                           res = "Error";
                           JOptionPane.showMessageDialog(null,res);
                           break;
                   }

         
                /*PreparedStatement ps;
                ResultSet rs;
                int telefono = Integer.parseInt(txt_telefonoCliente.getText());
                ps=con.prepareStatement("Update cliente\n" +
                                            "Set primer_nombre_cliente = ?,\n" +
                                            "segundo_nombre_cliente = ?,\n" +
                                            "primer_apellido_cliente = ?,\n" +
                                            "segundo_apellido_cliente = ?," +
                                            "telefono_cliente =?,\n" +
                                            "email_cliente = ?,\n" +
                                            "dnicliente = ?,\n" +
                                            "rtncliente =?\n" +
                                            "where dnicliente =?");
                    ps.setString(1, txt_primerNombreCliente.getText());
                    ps.setString(2, txt_segundoNombreCliente.getText());
                    ps.setString(3, txt_primerApellidoCliente.getText());
                    ps.setString(4, txt_segundoApellidoCliente.getText());                 
                    ps.setInt(5, telefono);
                    ps.setString(6, txt_emailCliente.getText());
                    ps.setString(7, txt_dniCliente.getText());
                    ps.setString(8, txt_rtnCliente.getText());
                    ps.setString(9, lbl_Dni.getText());
                    int res= ps.executeUpdate();
                    if(res >0){
                        JOptionPane.showMessageDialog(this, "Cliente actualizado");
                        restablecer();
                    }*/
                

            }catch(Exception e){

            }
         }
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_actualizarActionPerformed

    private void btn_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminarActionPerformed
         btn_buscar.setBackground(new Color(40,74,172));
        String nombreCliente = txt_primerNombreCliente.getText() + " " + txt_primerApellidoCliente.getText();
         if(JOptionPane.showConfirmDialog(null,"¿Está seguro que desea actualizar el registro del cliente "+nombreCliente+"?","Confirmación de eliminación",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE
        )==JOptionPane.YES_OPTION){
             
            try{
                Client client= ClientBuilder.newClient();

                   WebTarget target = client.target(URL + "/delete/"+idCliente);
                   Invocation.Builder solicitud =target.request();
                   Response delete = solicitud.delete();
                   String responseJson = delete.readEntity(String.class);
                   switch (delete.getStatus()) {
                       case 200:
                           res = "Cliente eliminado";
                           JOptionPane.showMessageDialog(null,res);
                           restablecer();
                           break;
                       default:
                           res = "Error";
                           JOptionPane.showMessageDialog(null,res);
                           break;
                   }
           
            }catch(Exception e){
                
            }
            
         }
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_eliminarActionPerformed

    private void btn_agregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_agregarActionPerformed
        btn_agregar.setBackground(new Color(40,74,172));
        
        try{
            if(estaVacio()){
                return;
            }


            if(!validarLongitudTelefono(txt_telefonoCliente,8)){
                return;
            }

             if(!validarDni(txt_dniCliente.getText())){
                return;
            }
             existeDNI(txt_dniCliente.getText());
            if(existeCliente){
                return;
            }
            
            Client client= ClientBuilder.newClient();

                   WebTarget target = client.target(URL + "/addcliente");

                   Invocation.Builder solicitud =target.request();

                   Cliente cliente = new Cliente();
                   cliente.setPrimer_nombre_cliente(txt_primerNombreCliente.getText());
                   cliente.setSegundo_nombre_cliente(txt_segundoNombreCliente.getText());
                   cliente.setPrimer_apellido_cliente(txt_primerApellidoCliente.getText());
                   cliente.setSegundo_apellido_cliente(txt_segundoApellidoCliente.getText());
                   cliente.setTelefono_cliente(txt_telefonoCliente.getText());
                   cliente.setEmail_cliente(txt_emailCliente.getText());
                   cliente.setDnicliente(txt_dniCliente.getText());
                   cliente.setRtncliente(txt_rtnCliente.getText());

                   Gson gson = new Gson();
                   String jsonString = gson.toJson(cliente);

                   Response post = solicitud.post(Entity.json(jsonString));


                   String responseJson = post.readEntity(String.class);
                   Cliente data = new Gson().fromJson(responseJson, Cliente.class);           

                   switch (post.getStatus()) {
                       case 201:
                           res = "Cliente agregado exitosamente";
                           JOptionPane.showMessageDialog(null,res);
                           restablecer();
                           break;
                       default:
                           res = "Error";
                           JOptionPane.showMessageDialog(null,res);
                           break;
                   }
            
            
    
       
                  
        }catch(Exception e){
            
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_agregarActionPerformed

    private void btn_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscarActionPerformed
        btn_buscar.setBackground(new Color(40,74,172));
        rellenar(); 
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_buscarActionPerformed

    private void txt_primerNombreClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_primerNombreClienteKeyTyped
        char a=evt.getKeyChar();
   
            if (evt.getKeyChar() == 8 || evt.getKeyChar() == 127 || 
                 evt.getKeyChar() == 0 || evt.getKeyChar() == 3 || evt.getKeyChar() == 22 
                 || evt.getKeyChar() == 26 || evt.getKeyChar() == 24) {
        return;
        }
         if(evt.getKeyChar() == 32){
             if(txt_primerNombreCliente.getText().length() == 0){
                 evt.consume();
                 Toolkit.getDefaultToolkit().beep();
                 return;
             }
             if(txt_primerNombreCliente.getText().substring(txt_primerNombreCliente.getText().length() - 1).equals(" ")){
                 evt.consume();
                 Toolkit.getDefaultToolkit().beep();
             }
             return; 
         }
        if(txt_primerNombreCliente.getText().length() >=40){
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Número máximo de caracteres admitidos");
        }
        
        if(Character.isDigit(a) || !Character.isLetterOrDigit(a) ){
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Sólo letras");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_primerNombreClienteKeyTyped

    private void txt_segundoNombreClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_segundoNombreClienteKeyTyped
        char a=evt.getKeyChar();
   
            if (evt.getKeyChar() == 8 || evt.getKeyChar() == 127 || 
                 evt.getKeyChar() == 0 || evt.getKeyChar() == 3 || evt.getKeyChar() == 22 
                 || evt.getKeyChar() == 26 || evt.getKeyChar() == 24) {
        return;
        }
         if(evt.getKeyChar() == 32){
             if(txt_segundoNombreCliente.getText().length() == 0){
                 evt.consume();
                 Toolkit.getDefaultToolkit().beep();
                 return;
             }
             if(txt_segundoNombreCliente.getText().substring(txt_segundoNombreCliente.getText().length() - 1).equals(" ")){
                 evt.consume();
                 Toolkit.getDefaultToolkit().beep();
             }
             return; 
         }
        if(txt_segundoNombreCliente.getText().length() >=40){
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Número máximo de caracteres admitidos");
        }
        
        if(Character.isDigit(a) || !Character.isLetterOrDigit(a) ){
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Sólo letras");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_segundoNombreClienteKeyTyped

    private void txt_primerApellidoClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_primerApellidoClienteKeyTyped
        char a=evt.getKeyChar();
   
            if (evt.getKeyChar() == 8 || evt.getKeyChar() == 127 || 
                 evt.getKeyChar() == 0 || evt.getKeyChar() == 3 || evt.getKeyChar() == 22 
                 || evt.getKeyChar() == 26 || evt.getKeyChar() == 24) {
        return;
        }
         if(evt.getKeyChar() == 32){
             if(txt_primerApellidoCliente.getText().length() == 0){
                 evt.consume();
                 Toolkit.getDefaultToolkit().beep();
                 return;
             }
             if(txt_primerApellidoCliente.getText().substring(txt_primerApellidoCliente.getText().length() - 1).equals(" ")){
                 evt.consume();
                 Toolkit.getDefaultToolkit().beep();
             }
             return; 
         }
        if(txt_primerApellidoCliente.getText().length() >=40){
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Número máximo de caracteres admitidos");
        }
        
        if(Character.isDigit(a) || !Character.isLetterOrDigit(a) ){
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Sólo letras");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_primerApellidoClienteKeyTyped

    private void txt_segundoApellidoClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_segundoApellidoClienteKeyTyped
         char a=evt.getKeyChar();
   
            if (evt.getKeyChar() == 8 || evt.getKeyChar() == 127 || 
                 evt.getKeyChar() == 0 || evt.getKeyChar() == 3 || evt.getKeyChar() == 22 
                 || evt.getKeyChar() == 26 || evt.getKeyChar() == 24) {
        return;
        }
         if(evt.getKeyChar() == 32){
             if(txt_segundoApellidoCliente.getText().length() == 0){
                 evt.consume();
                 Toolkit.getDefaultToolkit().beep();
                 return;
             }
             if(txt_segundoApellidoCliente.getText().substring(txt_segundoApellidoCliente.getText().length() - 1).equals(" ")){
                 evt.consume();
                 Toolkit.getDefaultToolkit().beep();
             }
             return; 
         }
        if(txt_segundoApellidoCliente.getText().length() >=40){
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Número máximo de caracteres admitidos");
        }
        
        if(Character.isDigit(a) || !Character.isLetterOrDigit(a) ){
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Sólo letras");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_segundoApellidoClienteKeyTyped

    private void txt_telefonoClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_telefonoClienteKeyTyped
         char a=evt.getKeyChar();
            if (evt.getKeyChar() == 8 || evt.getKeyChar() == 127 || 
                 evt.getKeyChar() == 0 || evt.getKeyChar() == 3 || evt.getKeyChar() == 22 
                 || evt.getKeyChar() == 26 || evt.getKeyChar() == 24) {
        return;
        }
        if(txt_telefonoCliente.getText().length() >=8){
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Número máximo de dígitos admitidos");
        }
     
        if(Character.isLetter(a) || !Character.isLetterOrDigit(a)){
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Sólo números");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_telefonoClienteKeyTyped

    private void txt_emailClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_emailClienteKeyTyped
         char a=evt.getKeyChar();
            if (evt.getKeyChar() == 8 || evt.getKeyChar() == 127 || 
                 evt.getKeyChar() == 0 || evt.getKeyChar() == 3 || evt.getKeyChar() == 22 
                 || evt.getKeyChar() == 26 || evt.getKeyChar() == 24) {
        return;
        }
        if(txt_emailCliente.getText().length() >=50){
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Número máximo de caracteres admitidos");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_emailClienteKeyTyped

    private void txt_dniClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_dniClienteKeyTyped
        char a=evt.getKeyChar();
            if (evt.getKeyChar() == 8 || evt.getKeyChar() == 127 || 
                 evt.getKeyChar() == 0 || evt.getKeyChar() == 3 || evt.getKeyChar() == 22 
                 || evt.getKeyChar() == 26 || evt.getKeyChar() == 24) {
        return;
        }
        if(txt_dniCliente.getText().length() >=13){
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Número máximo de dígitos admitidos");
        }
        if(Character.isLetter(a) || !Character.isLetterOrDigit(a)){
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Solo números");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_dniClienteKeyTyped

    private void txt_rtnClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_rtnClienteKeyTyped
        char a=evt.getKeyChar();
            if (evt.getKeyChar() == 8 || evt.getKeyChar() == 127 || 
                 evt.getKeyChar() == 0 || evt.getKeyChar() == 3 || evt.getKeyChar() == 22 
                 || evt.getKeyChar() == 26 || evt.getKeyChar() == 24) {
        return;
        }
        if(txt_rtnCliente.getText().length() >=14){
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Número máximo de dígitos admitidos");
        }
        if(Character.isLetter(a) || !Character.isLetterOrDigit(a)){
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Solo números");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_rtnClienteKeyTyped

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Clientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Clientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Clientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Clientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Clientes().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_actualizar;
    private javax.swing.JButton btn_agregar;
    private javax.swing.JButton btn_buscar;
    private javax.swing.JButton btn_eliminar;
    private javax.swing.JPanel jPanel1;
    private keeptoo.KGradientPanel kGradientPanel1;
    private javax.swing.JLabel lbl_Dni;
    private javax.swing.JLabel lbl_dniCliente;
    private javax.swing.JLabel lbl_emailCliente;
    private javax.swing.JLabel lbl_home;
    private javax.swing.JLabel lbl_nombreUsuario;
    private javax.swing.JLabel lbl_primerApellidoCliente;
    private javax.swing.JLabel lbl_primerNombreCliente;
    private javax.swing.JLabel lbl_rtnEmpleado;
    private javax.swing.JLabel lbl_segundoApellidoCliente;
    private javax.swing.JLabel lbl_segundoNombreCliente;
    private javax.swing.JLabel lbl_telefonoCliente;
    private javax.swing.JLabel lbl_tituloClientes;
    private javax.swing.JLabel lbl_usuario;
    private javax.swing.JTextField txt_dniCliente;
    private javax.swing.JTextField txt_emailCliente;
    private javax.swing.JTextField txt_primerApellidoCliente;
    private javax.swing.JTextField txt_primerNombreCliente;
    private javax.swing.JTextField txt_rtnCliente;
    private javax.swing.JTextField txt_segundoApellidoCliente;
    private javax.swing.JTextField txt_segundoNombreCliente;
    private javax.swing.JTextField txt_telefonoCliente;
    // End of variables declaration//GEN-END:variables
}

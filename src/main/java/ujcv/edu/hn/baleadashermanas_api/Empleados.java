/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ujcv.edu.hn.baleadashermanas_api;

import baleadashermanas.BD.ConexionBD;
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
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author Carlos
 */
public class Empleados extends javax.swing.JFrame {
    int click = 0;
    Statement stmt = null;
    Connection con = null;
    /**
     * Creates new form Empleados
     * @param nombreUsuario
     * @throws java.sql.SQLException
     */
    public Empleados(String nombreUsuario) throws SQLException {
        initComponents();
        informacionGeneral();
        holders();
        lbl_nombreUsuario.setText(nombreUsuario);
        this.con = ConexionBD.obtenerConexion();
        lbl_Dni.setVisible(false);
    }
    
    public Empleados() {
      
    }
    
    public final void informacionGeneral(){
        this.setTitle("Empleados");
        this.setLocationRelativeTo(null);
        this.setIconImage(new ImageIcon(getClass().getResource("../Img/Titulo.png")).getImage());
    }
    
    public final void holders(){
        PlaceHolder holder;
        holder = new PlaceHolder(txt_primerNombreEmpleado,Color.gray,Color.black,"Ingrese su primer nombre",false,"Roboto",25);
        holder = new PlaceHolder(txt_segundoNombreEmpleado,Color.gray,Color.black,"Ingrese su segundo nombre",false,"Roboto",25);
        holder = new PlaceHolder(txt_primerApellidoEmpleado,Color.gray,Color.black,"Ingrese su primer apellido",false,"Roboto",25);
        holder = new PlaceHolder(txt_segundoApellidoEmpleado,Color.gray,Color.black,"Ingrese su segundo apellido",false,"Roboto",25);
        
        holder = new PlaceHolder(txt_telefonoEmpleado,Color.gray,Color.black,"Ingrese su número de teléfono",false,"Roboto",25);
        holder = new PlaceHolder(txt_emailEmpleado,Color.gray,Color.black,"Ingrese su correo eléctronico",false,"Roboto",25);
        holder = new PlaceHolder(txt_dniEmpleado,Color.gray,Color.black,"Ingrese su DNI",false,"Roboto",25);
        
        holder = new PlaceHolder(txt_usuarioEmpleado,Color.gray,Color.black,"Ingrese su nombre de usuario",false,"Roboto",25);
        holder = new PlaceHolder(txt_contraseñaEmpleado,Color.gray,Color.black,"Contraseña",false,"Roboto",25);
           
    }
    
    public void rellenar(){
        String input = "";
        input = JOptionPane.showInputDialog(this, "¿A quien desea buscar?","Consulta de empleado",JOptionPane.QUESTION_MESSAGE);
        if(input == null){
                JOptionPane.showMessageDialog(this,"La acción fue cancelada","¡AVISO!",JOptionPane.INFORMATION_MESSAGE);
        }
        else if(input.equals("")){ 
            JOptionPane.showMessageDialog(this,"Favor de ingresar los datos del empleado\n que desea buscar","¡AVISO!",JOptionPane.INFORMATION_MESSAGE);
        }
        else{
            String sql = "select * from empleado\n" +
            "where dniempleado = '"+input+"' or primer_nombre_empleado ='"+input+"' or primer_apellido_empleado ='"+input+"'";     
            try {
                stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                if(rs.next()){
                    Locale locale = new Locale("es", "HN"); 
                    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
                    txt_primerNombreEmpleado.setText(rs.getString("primer_nombre_empleado"));
                    txt_segundoNombreEmpleado.setText(rs.getString("segundo_nombre_empleado"));
                    txt_primerApellidoEmpleado.setText(rs.getString("primer_apellido_empleado"));
                    txt_segundoApellidoEmpleado.setText(rs.getString("segundo_apellido_empleado"));
                    txt_telefonoEmpleado.setText(rs.getString("telefono_empleado"));
                    txt_emailEmpleado.setText(rs.getString("email_empleado"));
                    txt_dniEmpleado.setText(rs.getString("dniempleado"));
                    txt_usuarioEmpleado.setText(rs.getString("usuario")); 
                    txt_contraseñaEmpleado.setText("");
                    lbl_Dni.setText(rs.getString("dniempleado"));
                    colorear();
                    habilitarAccionesBuscar();
                }
                else{
                    JOptionPane.showMessageDialog(null,"¡No se encuentra el empleado! Por favor verifique sí, lo escribio correctamente");
                }
            } catch (SQLException ex) {
                Logger.getLogger(Empleados.class.getName()).log(Level.SEVERE, null, ex);
            }   
            
              }
    }
    
    public void limpiar(){
        txt_primerNombreEmpleado.setText("");
        txt_segundoNombreEmpleado.setText("");
        txt_primerApellidoEmpleado.setText("");
        txt_segundoApellidoEmpleado.setText("");
        txt_telefonoEmpleado.setText("");
        txt_emailEmpleado.setText("");
        txt_dniEmpleado.setText("");
        txt_usuarioEmpleado.setText("");
        txt_contraseñaEmpleado.setText("");
    }
    
    public void colorear(){
        txt_primerNombreEmpleado.setForeground(Color.black);
        txt_segundoNombreEmpleado.setForeground(Color.black);
        txt_primerApellidoEmpleado.setForeground(Color.black);
        txt_segundoApellidoEmpleado.setForeground(Color.black);
        txt_telefonoEmpleado.setForeground(Color.black);
        txt_emailEmpleado.setForeground(Color.black);
        txt_dniEmpleado.setForeground(Color.black);
        txt_usuarioEmpleado.setForeground(Color.black);
        txt_contraseñaEmpleado.setForeground(Color.black);
    }
    
    public boolean estaVacio(){
        if(txt_primerNombreEmpleado.getText().equals("Ingrese su primer nombre")){
            JOptionPane.showMessageDialog(this,"Por favor ingrese el primer nombre del empleado","Ingrese el primer nombre",JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        if(txt_segundoNombreEmpleado.getText().equals("Ingrese su segundo nombre")){
            JOptionPane.showMessageDialog(this,"Por favor ingrese el segundo nombre del empleado","Ingrese el segundo nombre",JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        if(txt_primerApellidoEmpleado.getText().equals("Ingrese su primer apellido")){
            JOptionPane.showMessageDialog(this,"Por favor ingrese el primer apellido del empleado","Ingrese el primer apellido",JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        if(txt_primerApellidoEmpleado.getText().equals("Ingrese su segundo apellido")){
            JOptionPane.showMessageDialog(this,"Por favor ingrese el segundo apellido del empleado","Ingrese el segundo apellido",JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        if(txt_telefonoEmpleado.getText().equals("Ingrese su número de teléfono")){
            JOptionPane.showMessageDialog(this,"Por favor ingrese el teléfono del empleado","Ingrese el teléfono",JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        if(txt_emailEmpleado.getText().equals("Ingrese su correo eléctronico")){
            JOptionPane.showMessageDialog(this,"Por favor ingrese el email del empleado","Ingrese el email",JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        if(txt_dniEmpleado.getText().equals("Ingrese su DNI")){
            JOptionPane.showMessageDialog(this,"Por favor ingrese el DNI del empleado","Ingrese el DNI",JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        if(txt_usuarioEmpleado.getText().equals("Ingrese su nombre de usuario")){
            JOptionPane.showMessageDialog(this,"Por favor ingrese el nombre de usuario del empleado","Ingrese el nombre de usuario",JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        if(txt_contraseñaEmpleado.getText().equals("Contraseña")){
            JOptionPane.showMessageDialog(this,"Por favor ingrese la contraseña del empleado","Ingrese la contraseña del empleado",JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        
        return false;
    }
    
    public boolean estaVacioActualizar(){
        if(txt_primerNombreEmpleado.getText().equals("Ingrese su primer nombre")){
            JOptionPane.showMessageDialog(this,"Por favor ingrese el primer nombre del empleado","Ingrese el primer nombre",JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        if(txt_segundoNombreEmpleado.getText().equals("Ingrese su segundo nombre")){
            JOptionPane.showMessageDialog(this,"Por favor ingrese el segundo nombre del empleado","Ingrese el segundo nombre",JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        if(txt_primerApellidoEmpleado.getText().equals("Ingrese su primer apellido")){
            JOptionPane.showMessageDialog(this,"Por favor ingrese el primer apellido del empleado","Ingrese el primer apellido",JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        if(txt_primerApellidoEmpleado.getText().equals("Ingrese su segundo apellido")){
            JOptionPane.showMessageDialog(this,"Por favor ingrese el segundo apellido del empleado","Ingrese el segundo apellido",JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        if(txt_telefonoEmpleado.getText().equals("Ingrese su número de teléfono")){
            JOptionPane.showMessageDialog(this,"Por favor ingrese el teléfono del empleado","Ingrese el teléfono",JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        if(txt_emailEmpleado.getText().equals("Ingrese su correo eléctronico")){
            JOptionPane.showMessageDialog(this,"Por favor ingrese el email del empleado","Ingrese el email",JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        if(txt_dniEmpleado.getText().equals("Ingrese su DNI")){
            JOptionPane.showMessageDialog(this,"Por favor ingrese el DNI del empleado","Ingrese el DNI",JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        if(txt_usuarioEmpleado.getText().equals("Ingrese su nombre de usuario")){
            JOptionPane.showMessageDialog(this,"Por favor ingrese el nombre de usuario del empleado","Ingrese el nombre de usuario",JOptionPane.INFORMATION_MESSAGE);
            return true;
        }    
        return false;
    }
    
    public boolean existeUsuario(){
        try {
            Statement st = con.createStatement();
            String sql = "Select usuario from empleado where usuario = '"+txt_usuarioEmpleado.getText()+"'";
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
                JOptionPane.showMessageDialog(null, "Ya existe el nombre de usuario: "+txt_usuarioEmpleado.getText()+" ", "Nombre de usuario ¡Ya existe!", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
            else{
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Empleados.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean existeEmpleado(){
        try {
            Statement st = con.createStatement();
            String sql = "Select dniempleado from empleado where dniempleado = '"+txt_dniEmpleado.getText()+"'";
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
                JOptionPane.showMessageDialog(null, "El DNI: "+txt_dniEmpleado.getText()+" ya existe", "Este DNI ¡Ya existe!", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
            else{
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Empleados.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
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
     
     public void habilitarAccionesBuscar(){
         btn_agregar.setEnabled(false);
         btn_actualizar.setEnabled(true);
         btn_eliminar.setEnabled(true);
     }
     
     public void restablecer(){
         limpiar();
         btn_agregar.setEnabled(true);
         btn_buscar.setEnabled(true);
         btn_actualizar.setEnabled(false);
         btn_eliminar.setEnabled(false);
         txt_primerNombreEmpleado.requestFocus();
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
        txt_primerNombreEmpleado = new javax.swing.JTextField();
        txt_segundoNombreEmpleado = new javax.swing.JTextField();
        txt_primerApellidoEmpleado = new javax.swing.JTextField();
        txt_segundoApellidoEmpleado = new javax.swing.JTextField();
        txt_telefonoEmpleado = new javax.swing.JTextField();
        txt_emailEmpleado = new javax.swing.JTextField();
        txt_dniEmpleado = new javax.swing.JTextField();
        txt_usuarioEmpleado = new javax.swing.JTextField();
        lbl_nombreUsuario = new javax.swing.JLabel();
        btn_agregar = new javax.swing.JButton();
        btn_actualizar = new javax.swing.JButton();
        btn_buscar = new javax.swing.JButton();
        btn_eliminar = new javax.swing.JButton();
        lbl_home = new javax.swing.JLabel();
        lbl_usuario = new javax.swing.JLabel();
        lbl_datosInicioEmpleado = new javax.swing.JLabel();
        lbl_tituloEmpleados = new javax.swing.JLabel();
        txt_contraseñaEmpleado = new javax.swing.JPasswordField();
        lbl_segundoNombreEmpleado = new javax.swing.JLabel();
        lbl_contraseña = new javax.swing.JLabel();
        lbl_telefonoEmpleado = new javax.swing.JLabel();
        lbl_primerApellidoEmpleado = new javax.swing.JLabel();
        lbl_segundoApellidoEmpleado = new javax.swing.JLabel();
        lbl_primerNombreEmpleado = new javax.swing.JLabel();
        lbl_emailEmpleado = new javax.swing.JLabel();
        lbl_dniEmpleado = new javax.swing.JLabel();
        lbl_nombreUsuarioEmpleado = new javax.swing.JLabel();
        lbl_vercontraseña = new javax.swing.JLabel();
        lbl_Dni = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        kGradientPanel1.setkEndColor(new java.awt.Color(40, 74, 172));
        kGradientPanel1.setkStartColor(new java.awt.Color(205, 63, 145));
        kGradientPanel1.setkTransparentControls(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        txt_primerNombreEmpleado.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N
        txt_primerNombreEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_primerNombreEmpleadoActionPerformed(evt);
            }
        });
        txt_primerNombreEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_primerNombreEmpleadoKeyTyped(evt);
            }
        });

        txt_segundoNombreEmpleado.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N
        txt_segundoNombreEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_segundoNombreEmpleadoActionPerformed(evt);
            }
        });
        txt_segundoNombreEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_segundoNombreEmpleadoKeyTyped(evt);
            }
        });

        txt_primerApellidoEmpleado.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N
        txt_primerApellidoEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_primerApellidoEmpleadoActionPerformed(evt);
            }
        });
        txt_primerApellidoEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_primerApellidoEmpleadoKeyTyped(evt);
            }
        });

        txt_segundoApellidoEmpleado.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N
        txt_segundoApellidoEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_segundoApellidoEmpleadoActionPerformed(evt);
            }
        });
        txt_segundoApellidoEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_segundoApellidoEmpleadoKeyTyped(evt);
            }
        });

        txt_telefonoEmpleado.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N
        txt_telefonoEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_telefonoEmpleadoActionPerformed(evt);
            }
        });
        txt_telefonoEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_telefonoEmpleadoKeyTyped(evt);
            }
        });

        txt_emailEmpleado.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N
        txt_emailEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_emailEmpleadoActionPerformed(evt);
            }
        });
        txt_emailEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_emailEmpleadoKeyTyped(evt);
            }
        });

        txt_dniEmpleado.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N
        txt_dniEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_dniEmpleadoActionPerformed(evt);
            }
        });
        txt_dniEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_dniEmpleadoKeyTyped(evt);
            }
        });

        txt_usuarioEmpleado.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N
        txt_usuarioEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_usuarioEmpleadoActionPerformed(evt);
            }
        });
        txt_usuarioEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_usuarioEmpleadoKeyTyped(evt);
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

        lbl_home.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/home-icon-silhouette.png"))); // NOI18N
        lbl_home.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_home.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl_homeMousePressed(evt);
            }
        });

        lbl_usuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ujcv/edu/hn/Img/profile.png"))); // NOI18N
        lbl_usuario.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_datosInicioEmpleado.setFont(new java.awt.Font("Roboto Black", 0, 48)); // NOI18N
        lbl_datosInicioEmpleado.setText("Datos de Inicio");

        lbl_tituloEmpleados.setFont(new java.awt.Font("Roboto Black", 0, 48)); // NOI18N
        lbl_tituloEmpleados.setText("Empleados");

        txt_contraseñaEmpleado.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        txt_contraseñaEmpleado.setEchoChar('*');
        txt_contraseñaEmpleado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txt_contraseñaEmpleadoMousePressed(evt);
            }
        });
        txt_contraseñaEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_contraseñaEmpleadoKeyTyped(evt);
            }
        });

        lbl_segundoNombreEmpleado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/name.png"))); // NOI18N
        lbl_segundoNombreEmpleado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl_segundoNombreEmpleadoMousePressed(evt);
            }
        });

        lbl_contraseña.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/candado-cerrado.png"))); // NOI18N
        lbl_contraseña.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl_contraseñaMousePressed(evt);
            }
        });

        lbl_telefonoEmpleado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/phone-call.png"))); // NOI18N
        lbl_telefonoEmpleado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl_telefonoEmpleadoMousePressed(evt);
            }
        });

        lbl_primerApellidoEmpleado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/name2.png"))); // NOI18N
        lbl_primerApellidoEmpleado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl_primerApellidoEmpleadoMousePressed(evt);
            }
        });

        lbl_segundoApellidoEmpleado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/name2.png"))); // NOI18N
        lbl_segundoApellidoEmpleado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl_segundoApellidoEmpleadoMousePressed(evt);
            }
        });

        lbl_primerNombreEmpleado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/name.png"))); // NOI18N
        lbl_primerNombreEmpleado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl_primerNombreEmpleadoMousePressed(evt);
            }
        });

        lbl_emailEmpleado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/email.png"))); // NOI18N
        lbl_emailEmpleado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl_emailEmpleadoMousePressed(evt);
            }
        });

        lbl_dniEmpleado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/id-card.png"))); // NOI18N
        lbl_dniEmpleado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl_dniEmpleadoMousePressed(evt);
            }
        });

        lbl_nombreUsuarioEmpleado.setIcon(new javax.swing.ImageIcon("C:\\Users\\cmcha\\Documents\\NetBeansProjects\\BaleadasHermanas\\BaleadasHermanas\\src\\Img\\man-user.png")); // NOI18N
        lbl_nombreUsuarioEmpleado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl_nombreUsuarioEmpleadoMousePressed(evt);
            }
        });

        lbl_vercontraseña.setIcon(new javax.swing.ImageIcon("C:\\Users\\cmcha\\Documents\\NetBeansProjects\\BaleadasHermanas\\BaleadasHermanas\\src\\Img\\ojo-cerrado.png")); // NOI18N
        lbl_vercontraseña.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_vercontraseña.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_vercontraseñaMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_home)
                .addGap(21, 21, 21))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_usuario)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(lbl_nombreUsuario))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbl_Dni)
                        .addGap(198, 198, 198))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbl_segundoNombreEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_telefonoEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_primerApellidoEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_segundoApellidoEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_primerNombreEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_segundoApellidoEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_primerApellidoEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_telefonoEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_segundoNombreEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_primerNombreEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(123, 123, 123)
                                .addComponent(lbl_datosInicioEmpleado))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lbl_contraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl_emailEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl_dniEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl_nombreUsuarioEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txt_usuarioEmpleado, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
                                            .addComponent(txt_emailEmpleado)
                                            .addComponent(txt_contraseñaEmpleado))
                                        .addGap(18, 18, 18)
                                        .addComponent(lbl_vercontraseña))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addComponent(txt_dniEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(343, 343, 343)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_agregar, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btn_eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(61, 157, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(492, 492, 492)
                    .addComponent(lbl_tituloEmpleados)
                    .addContainerGap(498, Short.MAX_VALUE)))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btn_actualizar, btn_agregar, btn_buscar, btn_eliminar});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbl_nombreUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_home, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_Dni)
                            .addComponent(lbl_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_emailEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_emailEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(37, 37, 37)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_dniEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_dniEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(58, 58, 58)
                        .addComponent(lbl_datosInicioEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(64, 64, 64)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lbl_nombreUsuarioEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)
                                .addComponent(lbl_contraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txt_usuarioEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_primerNombreEmpleado)
                            .addComponent(lbl_primerNombreEmpleado, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE))
                        .addGap(37, 37, 37)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txt_segundoNombreEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_segundoNombreEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(50, 50, 50)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_primerApellidoEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_primerApellidoEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(53, 53, 53)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_segundoApellidoEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_segundoApellidoEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_telefonoEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txt_telefonoEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txt_contraseñaEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lbl_vercontraseña, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_agregar)
                    .addComponent(btn_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_eliminar))
                .addGap(19, 19, 19))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(48, 48, 48)
                    .addComponent(lbl_tituloEmpleados, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(702, Short.MAX_VALUE)))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btn_actualizar, btn_agregar, btn_buscar, btn_eliminar});

        javax.swing.GroupLayout kGradientPanel1Layout = new javax.swing.GroupLayout(kGradientPanel1);
        kGradientPanel1.setLayout(kGradientPanel1Layout);
        kGradientPanel1Layout.setHorizontalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        kGradientPanel1Layout.setVerticalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void lbl_vercontraseñaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_vercontraseñaMouseClicked
        click++;
        if(click%2 != 0){

            lbl_vercontraseña.setIcon(new javax.swing.ImageIcon("src\\Img\\ojo.png"));
            txt_contraseñaEmpleado.setEchoChar((char)0);
        }else{
            lbl_vercontraseña.setIcon(new javax.swing.ImageIcon("src\\Img\\ojo-cerrado.png"));
            txt_contraseñaEmpleado.setEchoChar('*');
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_vercontraseñaMouseClicked

    private void lbl_nombreUsuarioEmpleadoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_nombreUsuarioEmpleadoMousePressed
        txt_usuarioEmpleado.requestFocus();
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_nombreUsuarioEmpleadoMousePressed

    private void lbl_dniEmpleadoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_dniEmpleadoMousePressed
        txt_dniEmpleado.requestFocus();
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_dniEmpleadoMousePressed

    private void lbl_emailEmpleadoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_emailEmpleadoMousePressed
        txt_emailEmpleado.requestFocus();
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_emailEmpleadoMousePressed

    private void lbl_primerNombreEmpleadoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_primerNombreEmpleadoMousePressed
        txt_primerNombreEmpleado.requestFocus();
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_primerNombreEmpleadoMousePressed

    private void lbl_segundoApellidoEmpleadoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_segundoApellidoEmpleadoMousePressed
        txt_segundoApellidoEmpleado.requestFocus();
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_segundoApellidoEmpleadoMousePressed

    private void lbl_primerApellidoEmpleadoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_primerApellidoEmpleadoMousePressed
        txt_primerApellidoEmpleado.requestFocus();
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_primerApellidoEmpleadoMousePressed

    private void lbl_telefonoEmpleadoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_telefonoEmpleadoMousePressed
        txt_telefonoEmpleado.requestFocus();
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_telefonoEmpleadoMousePressed

    private void lbl_contraseñaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_contraseñaMousePressed
        txt_contraseñaEmpleado.requestFocus();
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_contraseñaMousePressed

    private void lbl_segundoNombreEmpleadoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_segundoNombreEmpleadoMousePressed
        txt_segundoNombreEmpleado.requestFocus();
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_segundoNombreEmpleadoMousePressed

    private void txt_contraseñaEmpleadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_contraseñaEmpleadoKeyTyped
        char a=evt.getKeyChar();
        if (evt.getKeyChar() == 8 || evt.getKeyChar() == 127 ||
            evt.getKeyChar() == 0 || evt.getKeyChar() == 3 || evt.getKeyChar() == 22
            || evt.getKeyChar() == 26 || evt.getKeyChar() == 24) {
            return;
        }

        if(evt.getKeyChar() == 32 || evt.getKeyChar() == 124){
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_contraseñaEmpleadoKeyTyped

    private void txt_contraseñaEmpleadoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_contraseñaEmpleadoMousePressed
        txt_contraseñaEmpleado.requestFocus();
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_contraseñaEmpleadoMousePressed

    private void lbl_homeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_homeMousePressed
        Principal principal = new Principal(lbl_nombreUsuario.getText());
        this.dispose();
        principal.setVisible(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_homeMousePressed

    private void btn_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminarActionPerformed
        btn_buscar.setBackground(new Color(40,74,172));
        String nombreEmpleado = txt_primerNombreEmpleado.getText() + " " + txt_primerApellidoEmpleado.getText();
        if(JOptionPane.showConfirmDialog(null,"¿Está seguro que desea actualizar el registro del empleado "+nombreEmpleado+"?","Confirmación de eliminación",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE
        )==JOptionPane.YES_OPTION){

            try{
                PreparedStatement ps;
                ResultSet rs;
                ps=con.prepareStatement("Delete empleado\n" +
                    "where dniempleado =?");
                ps.setString(1, lbl_Dni.getText());
                int res= ps.executeUpdate();
                if(res >0){
                    JOptionPane.showMessageDialog(this, "Empleado eliminado");
                    restablecer();
                }

            }catch(Exception e){

            }

        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_eliminarActionPerformed

    private void btn_eliminarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_eliminarMouseExited
        btn_eliminar.setBackground(new Color(205,63,145));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_eliminarMouseExited

    private void btn_eliminarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_eliminarMouseEntered
        btn_eliminar.setBackground(new Color(156,2,91));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_eliminarMouseEntered

    private void btn_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscarActionPerformed
        btn_buscar.setBackground(new Color(40,74,172));
        rellenar();
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_buscarActionPerformed

    private void btn_buscarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_buscarMouseExited
        btn_buscar.setBackground(new Color(205,63,145));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_buscarMouseExited

    private void btn_buscarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_buscarMouseEntered
        btn_buscar.setBackground(new Color(156,2,91));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_buscarMouseEntered

    private void btn_actualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_actualizarActionPerformed
        btn_actualizar.setBackground(new Color(40,74,172));

        String nombreEmpleado = txt_primerNombreEmpleado.getText() + " " + txt_primerApellidoEmpleado.getText();
        if(JOptionPane.showConfirmDialog(null,"¿Está seguro que desea actualizar el registro del empleado "+nombreEmpleado+"?","Confirmación de actualización",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE
        )==JOptionPane.YES_OPTION){
            try{
                if(estaVacioActualizar()){
                    return;
                }

                if(!validarLongitudTelefono(txt_telefonoEmpleado,8)){
                    return;
                }

                if(!validarDni(txt_dniEmpleado.getText())){
                    return;

                }

                char[] c = txt_contraseñaEmpleado.getPassword();
                String contraseñaFinal ="";
                for (int i = 0; i < c.length; i++) {
                    contraseñaFinal  += String.valueOf(c[i]);
                }

                String contraseñaEncriptada=DigestUtils.md5Hex(contraseñaFinal);

                PreparedStatement ps;
                ResultSet rs;
                int telefono = Integer.parseInt(txt_telefonoEmpleado.getText());
                if(contraseñaFinal.equals("")){
                    ps=con.prepareStatement("Update empleado\n" +
                        "Set primer_nombre_empleado = ?,\n" +
                        "segundo_nombre_empleado = ?,\n" +
                        "primer_apellido_empleado = ?,\n" +
                        "segundo_apellido_empleado = ?," +
                        "telefono_empleado =?,\n" +
                        "email_empleado = ?,\n" +
                        "dniempleado = ?,\n" +
                        "usuario =?\n" +
                        "where dniempleado =?");
                    ps.setString(1, txt_primerNombreEmpleado.getText());
                    ps.setString(2, txt_segundoNombreEmpleado.getText());
                    ps.setString(3, txt_primerApellidoEmpleado.getText());
                    ps.setString(4, txt_segundoApellidoEmpleado.getText());
                    ps.setInt(5, telefono);
                    ps.setString(6, txt_emailEmpleado.getText());
                    ps.setString(7, txt_dniEmpleado.getText());
                    ps.setString(8, txt_usuarioEmpleado.getText());
                    ps.setString(9, lbl_Dni.getText());
                    int res= ps.executeUpdate();
                    if(res >0){
                        JOptionPane.showMessageDialog(this, "Empleado actualizado");
                        restablecer();
                    }
                }
                else{

                    ps=con.prepareStatement("Update empleado\n" +
                        "Set primer_nombre_empleado = ?,\n" +
                        "segundo_nombre_empleado = ?,\n" +
                        "primer_apellido_empleado = ?,\n" +
                        "segundo_apellido_empleado = ?," +
                        "telefono_empleado = ?,\n" +
                        "email_empleado = ?,\n" +
                        "dniempleado = ?,\n" +
                        "usuario = ?,\n" +
                        "contraseña = ?\n" +
                        "where dniempleado =?");
                    ps.setString(1, txt_primerNombreEmpleado.getText());
                    ps.setString(2, txt_segundoNombreEmpleado.getText());
                    ps.setString(3, txt_primerApellidoEmpleado.getText());
                    ps.setString(4, txt_segundoApellidoEmpleado.getText());
                    ps.setInt(5, telefono);
                    ps.setString(6, txt_emailEmpleado.getText());
                    ps.setString(7, txt_dniEmpleado.getText());
                    ps.setString(8, txt_usuarioEmpleado.getText());
                    ps.setString(9, contraseñaEncriptada);
                    ps.setString(10, lbl_Dni.getText());
                    int res= ps.executeUpdate();
                    if(res >0){
                        JOptionPane.showMessageDialog(this, "Empleado actualizado");
                    }
                }

            }catch(HeadlessException | SQLException e){

            }
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_btn_actualizarActionPerformed

    private void btn_actualizarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_actualizarMouseExited
        btn_actualizar.setBackground(new Color(205,63,145));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_actualizarMouseExited

    private void btn_actualizarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_actualizarMouseEntered
        btn_actualizar.setBackground(new Color(156,2,91));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_actualizarMouseEntered

    private void btn_agregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_agregarActionPerformed
        btn_agregar.setBackground(new Color(40,74,172));

        try{
            if(estaVacio()){
                return;
            }

            if(existeEmpleado()){
                return;
            }

            if(existeUsuario()){
                return;
            }

            if(!validarLongitudTelefono(txt_telefonoEmpleado,8)){
                return;
            }

            if(!validarDni(txt_dniEmpleado.getText())){
                return;

            }

            char[] c = txt_contraseñaEmpleado.getPassword();
            String contraseñaFinal ="";
            for (int i = 0; i < c.length; i++) {
                contraseñaFinal  += String.valueOf(c[i]);
            }

            String contraseñaEncriptada=DigestUtils.md5Hex(contraseñaFinal);
            PreparedStatement ps;
            ResultSet rs;

            ps=con.prepareStatement("INSERT INTO empleado (primer_nombre_empleado, segundo_nombre_empleado, primer_apellido_empleado, segundo_apellido_empleado,"
                + "telefono_empleado, email_empleado,dniempleado,usuario,contraseña)"
                + "VALUES(?,?,?,?,?,?,?,?,?)");
            ps.setString(1, txt_primerNombreEmpleado.getText());
            ps.setString(2, txt_segundoNombreEmpleado.getText());
            ps.setString(3, txt_primerApellidoEmpleado.getText());
            ps.setString(4, txt_segundoApellidoEmpleado.getText());
            ps.setString(5, txt_telefonoEmpleado.getText());
            ps.setString(6, txt_emailEmpleado.getText());
            ps.setString(7, txt_dniEmpleado.getText());
            ps.setString(8, txt_usuarioEmpleado.getText());
            ps.setString(9, contraseñaEncriptada);
            int res= ps.executeUpdate();
            if(res >0){
                JOptionPane.showMessageDialog(this, "Empleado agregado");
                restablecer();
            }

        }catch(Exception e){

        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_agregarActionPerformed

    private void btn_agregarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_agregarMouseExited
        btn_agregar.setBackground(new Color(205,63,145));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_agregarMouseExited

    private void btn_agregarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_agregarMouseEntered
        btn_agregar.setBackground(new Color(156,2,91));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_agregarMouseEntered

    private void txt_usuarioEmpleadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_usuarioEmpleadoKeyTyped
        char a=evt.getKeyChar();
        if (evt.getKeyChar() == 8 || evt.getKeyChar() == 127 ||
            evt.getKeyChar() == 0 || evt.getKeyChar() == 3 || evt.getKeyChar() == 22
            || evt.getKeyChar() == 46 || evt.getKeyChar() == 26 || evt.getKeyChar() == 24) {
            return;
        }

        if(txt_usuarioEmpleado.getText().length() >=25){
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Número máximo de caracteres admitidos");
        }
        if(!Character.isLetterOrDigit(a) ){
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Sólo letras y numeros");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_usuarioEmpleadoKeyTyped

    private void txt_usuarioEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_usuarioEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_usuarioEmpleadoActionPerformed

    private void txt_dniEmpleadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_dniEmpleadoKeyTyped
        char a=evt.getKeyChar();
        if (evt.getKeyChar() == 8 || evt.getKeyChar() == 127 ||
            evt.getKeyChar() == 0 || evt.getKeyChar() == 3 || evt.getKeyChar() == 22
            || evt.getKeyChar() == 26 || evt.getKeyChar() == 24) {
            return;
        }
        if(txt_dniEmpleado.getText().length() >=13){
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
    }//GEN-LAST:event_txt_dniEmpleadoKeyTyped

    private void txt_dniEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_dniEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_dniEmpleadoActionPerformed

    private void txt_emailEmpleadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_emailEmpleadoKeyTyped
        char a=evt.getKeyChar();
        if (evt.getKeyChar() == 8 || evt.getKeyChar() == 127 ||
            evt.getKeyChar() == 0 || evt.getKeyChar() == 3 || evt.getKeyChar() == 22
            || evt.getKeyChar() == 26 || evt.getKeyChar() == 24) {
            return;
        }
        if(txt_emailEmpleado.getText().length() >=50){
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Número máximo de caracteres admitidos");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_emailEmpleadoKeyTyped

    private void txt_emailEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_emailEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_emailEmpleadoActionPerformed

    private void txt_telefonoEmpleadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_telefonoEmpleadoKeyTyped
        char a=evt.getKeyChar();
        if (evt.getKeyChar() == 8 || evt.getKeyChar() == 127 ||
            evt.getKeyChar() == 0 || evt.getKeyChar() == 3 || evt.getKeyChar() == 22
            || evt.getKeyChar() == 26 || evt.getKeyChar() == 24) {
            return;
        }
        if(txt_telefonoEmpleado.getText().length() >=8){
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
    }//GEN-LAST:event_txt_telefonoEmpleadoKeyTyped

    private void txt_telefonoEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_telefonoEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_telefonoEmpleadoActionPerformed

    private void txt_segundoApellidoEmpleadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_segundoApellidoEmpleadoKeyTyped
        char a=evt.getKeyChar();

        if (evt.getKeyChar() == 8 || evt.getKeyChar() == 127 ||
            evt.getKeyChar() == 0 || evt.getKeyChar() == 3 || evt.getKeyChar() == 22
            || evt.getKeyChar() == 26 || evt.getKeyChar() == 24) {
            return;
        }
        if(evt.getKeyChar() == 32){
            if(txt_segundoApellidoEmpleado.getText().length() == 0){
                evt.consume();
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            if(txt_segundoApellidoEmpleado.getText().substring(txt_segundoApellidoEmpleado.getText().length() - 1).equals(" ")){
                evt.consume();
                Toolkit.getDefaultToolkit().beep();
            }
            return;
        }
        if(txt_segundoApellidoEmpleado.getText().length() >=40){
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
    }//GEN-LAST:event_txt_segundoApellidoEmpleadoKeyTyped

    private void txt_segundoApellidoEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_segundoApellidoEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_segundoApellidoEmpleadoActionPerformed

    private void txt_primerApellidoEmpleadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_primerApellidoEmpleadoKeyTyped
        char a=evt.getKeyChar();

        if (evt.getKeyChar() == 8 || evt.getKeyChar() == 127 ||
            evt.getKeyChar() == 0 || evt.getKeyChar() == 3 || evt.getKeyChar() == 22
            || evt.getKeyChar() == 26 || evt.getKeyChar() == 24) {
            return;
        }
        if(evt.getKeyChar() == 32){
            if(txt_primerApellidoEmpleado.getText().length() == 0){
                evt.consume();
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            if(txt_primerApellidoEmpleado.getText().substring(txt_primerApellidoEmpleado.getText().length() - 1).equals(" ")){
                evt.consume();
                Toolkit.getDefaultToolkit().beep();
            }
            return;
        }
        if(txt_primerApellidoEmpleado.getText().length() >=40){
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
    }//GEN-LAST:event_txt_primerApellidoEmpleadoKeyTyped

    private void txt_primerApellidoEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_primerApellidoEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_primerApellidoEmpleadoActionPerformed

    private void txt_segundoNombreEmpleadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_segundoNombreEmpleadoKeyTyped
        char a=evt.getKeyChar();

        if (evt.getKeyChar() == 8 || evt.getKeyChar() == 127 ||
            evt.getKeyChar() == 0 || evt.getKeyChar() == 3 || evt.getKeyChar() == 22
            || evt.getKeyChar() == 26 || evt.getKeyChar() == 24) {
            return;
        }
        if(evt.getKeyChar() == 32){
            if(txt_segundoNombreEmpleado.getText().length() == 0){
                evt.consume();
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            if(txt_segundoNombreEmpleado.getText().substring(txt_segundoNombreEmpleado.getText().length() - 1).equals(" ")){
                evt.consume();
                Toolkit.getDefaultToolkit().beep();
            }
            return;
        }
        if(txt_segundoNombreEmpleado.getText().length() >=40){
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
    }//GEN-LAST:event_txt_segundoNombreEmpleadoKeyTyped

    private void txt_segundoNombreEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_segundoNombreEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_segundoNombreEmpleadoActionPerformed

    private void txt_primerNombreEmpleadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_primerNombreEmpleadoKeyTyped
        char a=evt.getKeyChar();

        if (evt.getKeyChar() == 8 || evt.getKeyChar() == 127 ||
            evt.getKeyChar() == 0 || evt.getKeyChar() == 3 || evt.getKeyChar() == 22
            || evt.getKeyChar() == 26 || evt.getKeyChar() == 24) {
            return;
        }
        if(evt.getKeyChar() == 32){
            if(txt_primerNombreEmpleado.getText().length() == 0){
                evt.consume();
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            if(txt_primerNombreEmpleado.getText().substring(txt_primerNombreEmpleado.getText().length() - 1).equals(" ")){
                evt.consume();
                Toolkit.getDefaultToolkit().beep();
            }
            return;
        }
        if(txt_primerNombreEmpleado.getText().length() >=40){
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
    }//GEN-LAST:event_txt_primerNombreEmpleadoKeyTyped

    private void txt_primerNombreEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_primerNombreEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_primerNombreEmpleadoActionPerformed

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
            java.util.logging.Logger.getLogger(Empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Empleados().setVisible(true);
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
    private javax.swing.JLabel lbl_contraseña;
    private javax.swing.JLabel lbl_datosInicioEmpleado;
    private javax.swing.JLabel lbl_dniEmpleado;
    private javax.swing.JLabel lbl_emailEmpleado;
    private javax.swing.JLabel lbl_home;
    private javax.swing.JLabel lbl_nombreUsuario;
    private javax.swing.JLabel lbl_nombreUsuarioEmpleado;
    private javax.swing.JLabel lbl_primerApellidoEmpleado;
    private javax.swing.JLabel lbl_primerNombreEmpleado;
    private javax.swing.JLabel lbl_segundoApellidoEmpleado;
    private javax.swing.JLabel lbl_segundoNombreEmpleado;
    private javax.swing.JLabel lbl_telefonoEmpleado;
    private javax.swing.JLabel lbl_tituloEmpleados;
    private javax.swing.JLabel lbl_usuario;
    private javax.swing.JLabel lbl_vercontraseña;
    private javax.swing.JPasswordField txt_contraseñaEmpleado;
    private javax.swing.JTextField txt_dniEmpleado;
    private javax.swing.JTextField txt_emailEmpleado;
    private javax.swing.JTextField txt_primerApellidoEmpleado;
    private javax.swing.JTextField txt_primerNombreEmpleado;
    private javax.swing.JTextField txt_segundoApellidoEmpleado;
    private javax.swing.JTextField txt_segundoNombreEmpleado;
    private javax.swing.JTextField txt_telefonoEmpleado;
    private javax.swing.JTextField txt_usuarioEmpleado;
    // End of variables declaration//GEN-END:variables
}

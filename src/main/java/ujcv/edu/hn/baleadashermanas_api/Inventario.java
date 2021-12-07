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
import java.util.Calendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Carlos
 */
public class Inventario extends javax.swing.JFrame {

    Statement stmt = null;
    Connection con = null;

    /**
     * Creates new form Inventario
     */
    public Inventario(String nombreUsuario) throws SQLException {
        initComponents();
        informacionGeneral();
        holders();
        lbl_nombreUsuario.setText(nombreUsuario);
        this.con = ConexionBD.obtenerConexion();
        lbl_idProducto.setVisible(false);
    }

    public Inventario() {

    }

    public void informacionGeneral() {
        this.setTitle("Inventario");
        this.setLocationRelativeTo(null);
        this.setIconImage(new ImageIcon(getClass().getResource("../Img/Titulo.png")).getImage());
    }

    public void holders() {
        PlaceHolder holder;
        holder = new PlaceHolder(txt_nombreProducto, Color.gray, Color.black, "Ingrese el nombre del producto", false, "Roboto", 25);
        holder = new PlaceHolder(txt_precioProducto, Color.gray, Color.black, "Ingrese el precio del producto", false, "Roboto", 25);
    }

    public void rellenar() {
        String input = "";
        input = JOptionPane.showInputDialog(this, "¿Qué producto desea buscar?", "Consulta de producto", JOptionPane.QUESTION_MESSAGE);
        if (input == null) {
            JOptionPane.showMessageDialog(this, "La acción fue cancelada", "¡AVISO!", JOptionPane.INFORMATION_MESSAGE);
        } else if (input.equals("")) {
            JOptionPane.showMessageDialog(this, "Favor de ingresar los datos del producto\n que desea buscar", "¡AVISO!", JOptionPane.INFORMATION_MESSAGE);
        } else {
            String sql = "select * from inventario\n"
                    + "where nombreproducto ='" + input + "'";
            try {
                stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    txt_nombreProducto.setText(rs.getString("nombreproducto"));
                    int cantidad = Integer.parseInt(rs.getString("cantidadstock"));
                    spi_cantidadProducto.setValue(cantidad);
                    String tipoMovimiento = rs.getString("tipomovimiento");
                    if (tipoMovimiento.equals("i")) {
                        cmb_tipoMovimiento.setSelectedItem("Ingreso");
                    }
                    if (tipoMovimiento.equals("r")) {
                        cmb_tipoMovimiento.setSelectedItem("Retiro");
                    }
                    txt_precioProducto.setText(rs.getString("precio"));
                    lbl_idProducto.setText(rs.getString("idproducto"));
                    colorear();
                    habilitarAccionesBuscar();
                } else {
                    JOptionPane.showMessageDialog(null, "¡No se encuentra el producto! Por favor verifique sí, lo escribio correctamente");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
                Logger.getLogger(Empleados.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void colorear() {
        txt_nombreProducto.setForeground(Color.black);
        spi_cantidadProducto.setForeground(Color.black);
        cmb_tipoMovimiento.setForeground(Color.black);
        txt_precioProducto.setForeground(Color.black);
    }

    public void habilitarAccionesBuscar() {
        btn_agregar.setEnabled(false);
        btn_actualizar.setEnabled(true);
        btn_eliminar.setEnabled(true);
    }

    public boolean estaVacio() {
        if (txt_nombreProducto.getText().equals("Ingrese el nombre del producto")) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese el nombre del producto", "Ingrese el nombre", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        if (spi_cantidadProducto.getValue().equals("0")) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese la cantidad del producto", "Ingrese la cantidad", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        if (cmb_tipoMovimiento.getSelectedItem().equals("Seleccione el movimiento")) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione el tipo de movimiento", "Seleccione el tipo de movimiento", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        if (txt_precioProducto.getText().equals("Ingrese el precio del producto")) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese el precio del producto", "Ingrese el precio", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        return false;
    }

    public void limpiar() {
        txt_nombreProducto.setText("");
        spi_cantidadProducto.setValue(0);
        cmb_tipoMovimiento.setSelectedItem("Seleccione el movimiento");
        txt_precioProducto.setText("");
    }

    public boolean existeProducto() {
        try {
            Statement st = con.createStatement();
            String sql = "Select nombreproducto from inventario where nombreproducto = '" + txt_nombreProducto.getText() + "'";
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "El Producto: " + txt_nombreProducto.getText() + " ya existe", "Este producto ¡Ya existe!", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Empleados.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void restablecer() {
        limpiar();
        btn_agregar.setEnabled(true);
        btn_buscar.setEnabled(true);
        btn_actualizar.setEnabled(false);
        btn_eliminar.setEnabled(false);
        txt_nombreProducto.requestFocus();
    }

    public String capturarIdEmpleado() {
        String idEmpleado = "";
        try {
            Statement st = con.createStatement();
            String sql = "Select idempleado from empleado where usuario = '" + lbl_nombreUsuario.getText() + "'";
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                idEmpleado = rs.getString("idempleado");
                return idEmpleado;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Empleados.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idEmpleado;
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
        txt_nombreProducto = new javax.swing.JTextField();
        txt_precioProducto = new javax.swing.JTextField();
        lbl_nombreUsuario = new javax.swing.JLabel();
        btn_agregar = new javax.swing.JButton();
        btn_actualizar = new javax.swing.JButton();
        btn_buscar = new javax.swing.JButton();
        btn_eliminar = new javax.swing.JButton();
        lbl_home = new javax.swing.JLabel();
        lbl_usuario = new javax.swing.JLabel();
        lbl_tituloInventario = new javax.swing.JLabel();
        lbl_precioProducto = new javax.swing.JLabel();
        lbl_cantidadProducto = new javax.swing.JLabel();
        lbl_tipoMovimiento = new javax.swing.JLabel();
        lbl_nombreProducto = new javax.swing.JLabel();
        spi_cantidadProducto = new javax.swing.JSpinner();
        cmb_tipoMovimiento = new javax.swing.JComboBox<>();
        lbl_idProducto = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        kGradientPanel1.setkEndColor(new java.awt.Color(40, 74, 172));
        kGradientPanel1.setkStartColor(new java.awt.Color(205, 63, 145));
        kGradientPanel1.setkTransparentControls(false);
        kGradientPanel1.setPreferredSize(new java.awt.Dimension(1319, 821));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        txt_nombreProducto.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N
        txt_nombreProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_nombreProductoActionPerformed(evt);
            }
        });
        txt_nombreProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_nombreProductoKeyTyped(evt);
            }
        });

        txt_precioProducto.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N
        txt_precioProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_precioProductoActionPerformed(evt);
            }
        });
        txt_precioProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_precioProductoKeyTyped(evt);
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
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_eliminarMousePressed(evt);
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

        lbl_usuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/profile.png"))); // NOI18N
        lbl_usuario.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_tituloInventario.setFont(new java.awt.Font("Roboto Black", 0, 48)); // NOI18N
        lbl_tituloInventario.setText("Inventario");

        lbl_precioProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pagar.png"))); // NOI18N
        lbl_precioProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl_precioProductoMousePressed(evt);
            }
        });

        lbl_cantidadProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/cantidad.png"))); // NOI18N
        lbl_cantidadProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl_cantidadProductoMousePressed(evt);
            }
        });

        lbl_tipoMovimiento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/inventario.png"))); // NOI18N
        lbl_tipoMovimiento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl_tipoMovimientoMousePressed(evt);
            }
        });

        lbl_nombreProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/nuevo-producto.png"))); // NOI18N
        lbl_nombreProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl_nombreProductoMousePressed(evt);
            }
        });

        spi_cantidadProducto.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N
        spi_cantidadProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        cmb_tipoMovimiento.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N
        cmb_tipoMovimiento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione el movimiento", "Ingreso", "Retiro" }));
        cmb_tipoMovimiento.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmb_tipoMovimiento.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        lbl_idProducto.setText("prueba");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_usuario)
                .addGap(18, 18, 18)
                .addComponent(lbl_nombreUsuario)
                .addGap(208, 208, 208)
                .addComponent(lbl_tituloInventario)
                .addGap(0, 501, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(143, 143, 143)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_nombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lbl_precioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbl_cantidadProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_tipoMovimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_nombreProducto, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
                    .addComponent(txt_precioProducto, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
                    .addComponent(cmb_tipoMovimiento, 0, 434, Short.MAX_VALUE)
                    .addComponent(spi_cantidadProducto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 179, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_agregar, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(176, 176, 176))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lbl_home)
                        .addGap(21, 21, 21))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lbl_idProducto)
                        .addGap(37, 37, 37))))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btn_actualizar, btn_agregar, btn_buscar, btn_eliminar});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(lbl_nombreUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_nombreProducto)
                            .addComponent(lbl_nombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lbl_home, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(21, 21, 21)
                                    .addComponent(lbl_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(lbl_tituloInventario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(114, 114, 114)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                        .addComponent(btn_agregar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_eliminar)
                        .addGap(227, 227, 227)
                        .addComponent(lbl_idProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(spi_cantidadProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(64, 64, 64)
                                .addComponent(cmb_tipoMovimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36)
                                .addComponent(txt_precioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lbl_cantidadProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(63, 63, 63)
                                .addComponent(lbl_tipoMovimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36)
                                .addComponent(lbl_precioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btn_actualizar, btn_agregar, btn_buscar, btn_eliminar});

        javax.swing.GroupLayout kGradientPanel1Layout = new javax.swing.GroupLayout(kGradientPanel1);
        kGradientPanel1.setLayout(kGradientPanel1Layout);
        kGradientPanel1Layout.setHorizontalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );
        kGradientPanel1Layout.setVerticalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
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
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_nombreProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_nombreProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_nombreProductoActionPerformed

    private void txt_precioProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_precioProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_precioProductoActionPerformed

    private void btn_agregarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_agregarMouseEntered
        btn_agregar.setBackground(new Color(156, 2, 91));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_agregarMouseEntered

    private void btn_agregarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_agregarMouseExited
        btn_agregar.setBackground(new Color(205, 63, 145));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_agregarMouseExited

    private void btn_actualizarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_actualizarMouseEntered
        btn_actualizar.setBackground(new Color(156, 2, 91));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_actualizarMouseEntered

    private void btn_actualizarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_actualizarMouseExited
        btn_actualizar.setBackground(new Color(205, 63, 145));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_actualizarMouseExited

    private void btn_buscarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_buscarMouseEntered
        btn_buscar.setBackground(new Color(156, 2, 91));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_buscarMouseEntered

    private void btn_buscarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_buscarMouseExited
        btn_buscar.setBackground(new Color(205, 63, 145));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_buscarMouseExited

    private void btn_eliminarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_eliminarMouseEntered
        btn_eliminar.setBackground(new Color(156, 2, 91));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_eliminarMouseEntered

    private void btn_eliminarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_eliminarMouseExited
        btn_eliminar.setBackground(new Color(205, 63, 145));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_eliminarMouseExited

    private void btn_eliminarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_eliminarMousePressed

        // TODO add your handling code here:
    }//GEN-LAST:event_btn_eliminarMousePressed

    private void lbl_homeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_homeMousePressed
        Principal principal = new Principal(lbl_nombreUsuario.getText());
        this.dispose();
        principal.setVisible(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_homeMousePressed

    private void lbl_precioProductoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_precioProductoMousePressed
        txt_precioProducto.requestFocus();
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_precioProductoMousePressed

    private void lbl_cantidadProductoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_cantidadProductoMousePressed

        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_cantidadProductoMousePressed

    private void lbl_tipoMovimientoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_tipoMovimientoMousePressed

        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_tipoMovimientoMousePressed

    private void lbl_nombreProductoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_nombreProductoMousePressed
        txt_nombreProducto.requestFocus();
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_nombreProductoMousePressed

    private void btn_agregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_agregarActionPerformed
        btn_agregar.setBackground(new Color(40, 74, 172));
        try {
            if (estaVacio()) {
                return;
            }

            if (existeProducto()) {
                return;
            }

            Calendar f;
            f = Calendar.getInstance();
            int d = f.get(Calendar.DATE), mes = 1 + (f.get(Calendar.MONTH)), año = f.get(Calendar.YEAR);
            String fecha = (año + "-" + mes + "-" + d);

            PreparedStatement ps;
            ResultSet rs;

            ps = con.prepareStatement("INSERT INTO inventario (nombreproducto, idempleado, cantidadstock, fechaintroduccion,"
                    + "tipomovimiento, precio)"
                    + "VALUES(?,?,?,?,?,?)");
            ps.setString(1, txt_nombreProducto.getText());
            String idEmpleado = capturarIdEmpleado();
            ps.setString(2, idEmpleado);
            ps.setString(3, spi_cantidadProducto.getValue().toString());
            ps.setString(4, fecha);
            ps.setString(5, cmb_tipoMovimiento.getSelectedItem().toString().substring(0, 1).toLowerCase());
            ps.setString(6, txt_precioProducto.getText());
            int res = ps.executeUpdate();
            if (res > 0) {
                JOptionPane.showMessageDialog(this, "Producto agregado");
                restablecer();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_btn_agregarActionPerformed

    private void btn_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscarActionPerformed
        btn_buscar.setBackground(new Color(40, 74, 172));
        rellenar();
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_buscarActionPerformed

    private void btn_actualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_actualizarActionPerformed
        btn_actualizar.setBackground(new Color(40, 74, 172));

        String nombreProducto = txt_nombreProducto.getText();
        if (JOptionPane.showConfirmDialog(null, "¿Está seguro que desea actualizar el registro del producto " + nombreProducto + "?", "Confirmación de actualización", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
        ) == JOptionPane.YES_OPTION) {
            try {
                if (estaVacio()) {
                    return;
                }

                Calendar f;
                f = Calendar.getInstance();
                int d = f.get(Calendar.DATE), mes = 1 + (f.get(Calendar.MONTH)), año = f.get(Calendar.YEAR);
                String fecha = (año + "-" + mes + "-" + d);

                String tipoMovimiento = cmb_tipoMovimiento.getSelectedItem().toString().substring(0, 1).toLowerCase();

                Statement st = con.createStatement();
                String sql = "Update inventario \n"
                        + "Set nombreproducto = '" + txt_nombreProducto.getText() + "',\n"
                        + "cantidadstock = '" + spi_cantidadProducto.getValue().toString() + "',\n"
                        + "fechaintroduccion = '" + fecha + "',\n"
                        + "tipomovimiento = '" + tipoMovimiento + "',"
                        + "precio = '" + txt_precioProducto.getText() + "'"
                        + "where idproducto = '" + lbl_idProducto.getText() + "'";
                int res = st.executeUpdate(sql);
                if (res > 0) {
                    JOptionPane.showMessageDialog(this, "Producto actualizado");
                    restablecer();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_actualizarActionPerformed

    private void btn_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminarActionPerformed
        btn_buscar.setBackground(new Color(40, 74, 172));
        String nombreProducto = txt_nombreProducto.getText();
        if (JOptionPane.showConfirmDialog(null, "¿Está seguro que desea actualizar el registro del producto " + nombreProducto + "?", "Confirmación de eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
        ) == JOptionPane.YES_OPTION) {

            try {
                PreparedStatement ps;
                ResultSet rs;
                ps = con.prepareStatement("Delete inventario\n"
                        + "where idproducto =?");
                ps.setString(1, lbl_idProducto.getText());
                int res = ps.executeUpdate();
                if (res > 0) {
                    JOptionPane.showMessageDialog(this, "Producto eliminado");
                    restablecer();
                }

            } catch (Exception e) {

            }

        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_eliminarActionPerformed

    private void txt_nombreProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nombreProductoKeyTyped
        char a=evt.getKeyChar();
   
            if (evt.getKeyChar() == 8 || evt.getKeyChar() == 127 || 
                 evt.getKeyChar() == 0 || evt.getKeyChar() == 3 || evt.getKeyChar() == 22 
                 || evt.getKeyChar() == 26 || evt.getKeyChar() == 24) {
        return;
        }
         if(evt.getKeyChar() == 32){
             if(txt_nombreProducto.getText().length() == 0){
                 evt.consume();
                 Toolkit.getDefaultToolkit().beep();
                 return;
             }
             if(txt_nombreProducto.getText().substring(txt_nombreProducto.getText().length() - 1).equals(" ")){
                 evt.consume();
                 Toolkit.getDefaultToolkit().beep();
             }
             return; 
         }
        if(txt_nombreProducto.getText().length() >=100){
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Número máximo de caracteres admitidos");
        }
        
        if(!Character.isLetterOrDigit(a) ){
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Sólo letras y números");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_nombreProductoKeyTyped

    private void txt_precioProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_precioProductoKeyTyped
        char a=evt.getKeyChar();
        if (evt.getKeyChar() == 8 || evt.getKeyChar() == 127 ||
            evt.getKeyChar() == 0 || evt.getKeyChar() == 3 || evt.getKeyChar() == 22
            || evt.getKeyChar() == 26 || evt.getKeyChar() == 24 || evt.getKeyChar() == 46 || evt.getKeyChar() == 44) {
            return;
        }

        if(txt_precioProducto.getText().length() >=10){
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Número máximo de dígitos admitidos");
        }
        if(Character.isLetter(a) || !Character.isLetterOrDigit(a)){
            evt.consume();
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Sólo numeros");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_precioProductoKeyTyped

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
            java.util.logging.Logger.getLogger(Inventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Inventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Inventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Inventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Inventario().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_actualizar;
    private javax.swing.JButton btn_agregar;
    private javax.swing.JButton btn_buscar;
    private javax.swing.JButton btn_eliminar;
    private javax.swing.JComboBox<String> cmb_tipoMovimiento;
    private javax.swing.JPanel jPanel1;
    private keeptoo.KGradientPanel kGradientPanel1;
    private javax.swing.JLabel lbl_cantidadProducto;
    private javax.swing.JLabel lbl_home;
    private javax.swing.JLabel lbl_idProducto;
    private javax.swing.JLabel lbl_nombreProducto;
    private javax.swing.JLabel lbl_nombreUsuario;
    private javax.swing.JLabel lbl_precioProducto;
    private javax.swing.JLabel lbl_tipoMovimiento;
    private javax.swing.JLabel lbl_tituloInventario;
    private javax.swing.JLabel lbl_usuario;
    private javax.swing.JSpinner spi_cantidadProducto;
    private javax.swing.JTextField txt_nombreProducto;
    private javax.swing.JTextField txt_precioProducto;
    // End of variables declaration//GEN-END:variables
}

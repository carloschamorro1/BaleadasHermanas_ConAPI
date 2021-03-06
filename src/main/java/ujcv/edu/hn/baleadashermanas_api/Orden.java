/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ujcv.edu.hn.baleadashermanas_api;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.placeholder.PlaceHolder;
import java.awt.Color;
import java.awt.Toolkit;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JRSaveContributor;
import net.sf.jasperreports.view.JRViewer;
import net.sf.jasperreports.view.JasperViewer;
import ujcv.edu.hn.baleadashermanas_api.model.Cliente;
import ujcv.edu.hn.baleadashermanas_api.model.Empleado;
import ujcv.edu.hn.baleadashermanas_api.model.FacturaDetalle;
import ujcv.edu.hn.baleadashermanas_api.model.FacturaEncabezado;
import ujcv.edu.hn.baleadashermanas_api.model.Producto;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;



/**
 *
 * @author Carlos
 */
public class Orden extends javax.swing.JFrame {
    ArrayList<String> clientes = new ArrayList<String>();
    int totalPrecioOrden;
    boolean hayDecimal = false;
    String URL_FACTURA_ENCABEZADO = "http://192.168.178.34:8080/api/v1/facturaencabezado";
    String URL_FACTURA_DETALLE = "http://192.168.178.34:8080/api/v1/facturadetalle";
    String URL_EMPLEADOS = "http://192.168.178.34:8080/api/v1/empleado";
    String URL_CLIENTES = "http://192.168.178.34:8080/api/v1/cliente";
    String URL_INVENTARIO = "http://192.168.178.34:8080/api/v1/inventario";
    String res = "";
    int filaSeleccionada;
    long idEmpleado = 0;
    String nombreEmpleado = "";
    String nombreCliente = "";
    String rtnCliente = "";
    String totalFactura = "";
    String cambioFactura = "";
    String pagoFactura="";
    Locale locale = new Locale("es", "HN");
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

    ArrayList<Producto> productoNuevo = new ArrayList<Producto>();
    /**
     * Creates new form Orden
     */
    public Orden(String nombreUsuario){
        initComponents();
        informacionGeneral();
        txt_subtotal.requestFocus();
        lbl_nombreUsuario.setText(nombreUsuario);
        lbl_nombreProducto.setVisible(false);
        lbl_idFactura.setVisible(false);
        lbl_idCliente.setVisible(false);
        lbl_idProducto.setVisible(false);
        lbl_idDetalle.setVisible(false);     
        buscarClientes();
    }

    public Orden() {

    }

    public void informacionGeneral() {
        this.setTitle("Ordenes");
        this.setLocationRelativeTo(null);
        this.setIconImage(new javax.swing.ImageIcon("src\\main\\java\\ujcv\\edu\\hn\\Img\\Titulo.png").getImage());
        PlaceHolder holder;
    }

    public void buscarClientes() {
        ArrayList<String> lista = new ArrayList<String>();
        try {
                   Client client= ClientBuilder.newClient();

                   WebTarget target = client.target(URL_CLIENTES + "");

                   //Invocation.Builder solicitud =target.queryParam("id",1).request();
                   Invocation.Builder solicitud =target.request();

                   Response get = solicitud.get();


                   String responseJson = get.readEntity(String.class);                   
                   
                   ArrayList<Cliente> data = new Gson().fromJson(responseJson,new TypeToken<ArrayList<Cliente>>(){}.getType());
                   
                   for (int i = 0; i < data.size(); i++) {
                        lista.add(data.get(i).getIdcliente().toString() + ". " + data.get(i).getPrimer_nombre_cliente() + " " + data.get(i).getPrimer_apellido_cliente());
                    }
                             
                   switch (get.getStatus()) {
                       case 200:
                           for (int i = 0; i < lista.size(); i++) {
                                cmb_cliente.addItem(lista.get(i));
                            }
                           break;
                       default:
                           res = "Error";
                           break;
                   }
        } catch (Exception ex) {
            Logger.getLogger(Orden.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   

    private void actualizarDatos() {
        try {
            String nombreProducto = lbl_nombreProducto.getText();
            double subtotal = 0;          
            Client client= ClientBuilder.newClient();
                   WebTarget target = client.target(URL_INVENTARIO+"/nombreproducto/"+nombreProducto);
                   Invocation.Builder solicitud =target.request();
                   Response get = solicitud.get();
                   String responseJson = get.readEntity(String.class);
                   Producto data = new Gson().fromJson(responseJson, Producto.class);
                   switch (get.getStatus()) {
                       case 200:   
                            DefaultTableModel model = (DefaultTableModel) tbl_orden.getModel();
                            String[] datos = new String[3];
                            datos[0] = data.getNombreproducto();
                            datos[1] = "1";
                            double precioSinFormato = Double.parseDouble(data.getPrecio());
                            String precio = currencyFormatter.format(precioSinFormato);
                            datos[2] = precio;       
                            model.addRow(datos);
                            subtotal = Double.parseDouble(data.getPrecio());
                            lbl_idProducto.setText(data.getIdproducto());

                            Producto producto  = new Producto();
                            producto.setNombreproducto(data.getNombreproducto());
                            producto.setCantidadstock("1");
                            producto.setPrecio(precio);
                            productoNuevo.add(producto);
                            
                            guardarDetalle();
                            int indiceUltimaFila = tbl_orden.getRowCount() - 1;
                            model.removeRow(indiceUltimaFila);
                            String[] datos2 = new String[4];
                            datos2[0] = data.getNombreproducto();
                            datos2[1] = "1";              
                            datos2[2] = precio;
                            datos2[3] = lbl_idDetalle.getText();
                            model.addRow(datos2);
                           break;
                       default:
                           res = "Error";
                           break;
                   }                 
            double totalAnterior = Double.parseDouble(txt_total.getText());
            double totalNuevo = totalAnterior + subtotal;

            double isv = totalNuevo * 0.15;
            double subtotalConImpuesto = totalNuevo - isv;

            double total = subtotalConImpuesto + isv;
            String subtotalFinal = currencyFormatter.format(subtotalConImpuesto).substring(1);
            String isvFinal = currencyFormatter.format(isv).substring(1);
            String totalFinal = currencyFormatter.format(total).substring(1);
            txt_subtotal.setText(subtotalFinal);
            txt_isv.setText(isvFinal);
            txt_total.setText(totalFinal);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void cambio() {
        Locale locale = new Locale("es", "HN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        String pagoCliente = txt_pago.getText();
        double pago = Double.parseDouble(pagoCliente);
        double total = Double.parseDouble(txt_total.getText());
        double cambio = pago - total;
        String cambioTotal = currencyFormatter.format(cambio);
        txt_cambio.setText(cambioTotal);
    }

    public void habilitarNumeros(Boolean accion) {
        btn_retroceso.setEnabled(accion);
        btn_punto.setEnabled(accion);
        btn_0.setEnabled(accion);
        btn_1.setEnabled(accion);
        btn_2.setEnabled(accion);
        btn_3.setEnabled(accion);
        btn_4.setEnabled(accion);
        btn_5.setEnabled(accion);
        btn_6.setEnabled(accion);
        btn_7.setEnabled(accion);
        btn_8.setEnabled(accion);
        btn_9.setEnabled(accion);
    }
    
    public void habilitarProductos(Boolean accion){
        btn_producto1.setEnabled(accion);
        btn_producto2.setEnabled(accion);
        btn_producto3.setEnabled(accion);
        btn_producto4.setEnabled(accion);
        btn_producto5.setEnabled(accion);
        btn_producto6.setEnabled(accion);
        btn_producto7.setEnabled(accion);
        btn_producto8.setEnabled(accion);
        btn_producto9.setEnabled(accion);
    }
    
    public void accionesIniciar(){
        btn_iniciarFactura.setEnabled(false);
        btn_cancelarFactura.setEnabled(true);
        btn_pagar.setEnabled(true);
        cmb_metodoPago.setEnabled(true);
        
    }
    
    public void botonesPorDefecto(){
        btn_iniciarFactura.setEnabled(true);
        btn_cancelarFactura.setEnabled(false);
        btn_eliminar.setEnabled(false);
        btn_pagar.setEnabled(false);
    }
    
    public void deleteAllRows(final DefaultTableModel model) {
    for( int i = model.getRowCount() - 1; i >= 0; i-- ) {
        model.removeRow(i);
    }
}
    
    public void accionesCancelar(){
        lbl_nombreProducto.setText("");
        lbl_idFactura.setText("");
        lbl_idCliente.setText("");
        lbl_idProducto.setText("");
        lbl_idDetalle.setText("");
        txt_subtotal.setText("0");
        txt_isv.setText("0");
        txt_total.setText("0");
        txt_pago.setText("");
        cmb_metodoPago.setSelectedItem("Seleccione el m??todo");
        cmb_metodoPago.setEnabled(false);
        txt_cambio.setText("");
        cmb_cliente.setEnabled(true);
        cmb_cliente.setSelectedItem("1. Consumidor Final");
        habilitarNumeros(false);
        habilitarProductos(false);
        botonesPorDefecto();  
        DefaultTableModel model = (DefaultTableModel) tbl_orden.getModel();
        deleteAllRows(model);
    }

    public void capturarIdEmpleado(String usuario){
        try{        
                   Client client= ClientBuilder.newClient();
                   
                   String path = URL_EMPLEADOS+"/"+usuario;
                   WebTarget target = client.target(path);

                   Invocation.Builder solicitud =target.request();

                   Response get = solicitud.get();

                   String responseJson = get.readEntity(String.class);
                   
                   Empleado data = new Gson().fromJson(responseJson, Empleado.class);       
                   switch (get.getStatus()) {
                       case 200:
                           res = "El usuario ya existe";
                           idEmpleado = data.getIdempleado();
                           nombreEmpleado = data.getPrimer_nombre_empleado() + " " + data.getPrimer_apellido_empleado();
                           break;
                       case 405:
                           res = "El usuario no existe";
                           break;
                   }
               }catch(Exception e){
                   res = e.getMessage();
               }
    }
    
    public void capturarIdFactura() {
        String idFactura = "";
        ArrayList<String> lista = new ArrayList<String>();
        try {   
                   Client client= ClientBuilder.newClient();

                   WebTarget target = client.target(URL_FACTURA_ENCABEZADO + "");

                   //Invocation.Builder solicitud =target.queryParam("id",1).request();
                   Invocation.Builder solicitud =target.request();

                   Response get = solicitud.get();


                   String responseJson = get.readEntity(String.class);                   
                   
                   ArrayList<FacturaEncabezado> data = new Gson().fromJson(responseJson,new TypeToken<ArrayList<FacturaEncabezado>>(){}.getType());
                   
                   for (int i = 0; i < data.size(); i++) {
                        lista.add(data.get(i).getIdfactura().toString());
                    }
                               
                   switch (get.getStatus()) {
                       case 200:
                           for (int i = 0; i < lista.size(); i++) {
                                if(i==(lista.size() - 1)){
                                   idFactura = lista.get(i);
                                }
                            }
                           break;
                       default:
                           res = "Error";
                           break;
                   }
        } catch (Exception ex) {
            Logger.getLogger(Empleados.class.getName()).log(Level.SEVERE, null, ex);
        }
        lbl_idFactura.setText(idFactura);
    }

    public String capturarNombreCliente(Long id) {
        String nombreCliente = "";
        try {
            Client client= ClientBuilder.newClient();
                   
                   String path = URL_CLIENTES+"/id/"+id;
                   WebTarget target = client.target(path);

                   Invocation.Builder solicitud =target.request();

                   Response get = solicitud.get();

                   String responseJson = get.readEntity(String.class);
                   
                    Cliente data = new Gson().fromJson(responseJson, Cliente.class);       
                   switch (get.getStatus()) {
                       case 200:
                           res = "El usuario ya existe";
                           nombreCliente = data.getPrimer_nombre_cliente() + " " + data.getPrimer_apellido_cliente();
                           rtnCliente = data.getRtncliente();
                           break;
                       case 405:
                           res = "El usuario no existe";
                           break;
                   }
           
        } catch (Exception ex) {
            Logger.getLogger(Empleados.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nombreCliente;
    }
    
    public void guardarDetalle(){
        try { 
            
                    Client client= ClientBuilder.newClient();
                    WebTarget target = client.target(URL_FACTURA_DETALLE + "/addfacturadetalle");
                    Invocation.Builder solicitud =target.request();
                    FacturaDetalle facturaDetalle = new FacturaDetalle();           
                    facturaDetalle.setIdfactura(Long.parseLong(lbl_idFactura.getText()));     
                    facturaDetalle.setIdproducto(Long.parseLong(lbl_idProducto.getText()));
                    int indiceUltimaFila = tbl_orden.getRowCount() - 1;
                    String cantidadProducto = tbl_orden.getValueAt(indiceUltimaFila, 1).toString();
                    String precio = tbl_orden.getValueAt(indiceUltimaFila, 2).toString().substring(1);
                    facturaDetalle.setCantidadfactura(cantidadProducto);
                    facturaDetalle.setPrecio(precio);
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(facturaDetalle);
                    Response post = solicitud.post(Entity.json(jsonString));
                    String responseJson = post.readEntity(String.class);
                    FacturaEncabezado data = new Gson().fromJson(responseJson, FacturaEncabezado.class);           
                    switch (post.getStatus()) {
                        case 201:
                             res = "Factura iniciada exitosamente";
                             capturarIdDetalle();
                             break;
                        default:
                            res = "Error factura";
                            JOptionPane.showMessageDialog(null,res);
                            break;
                    }
        
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    public void capturarIdDetalle() {
        String idDetalle = "";
        ArrayList<String> lista = new ArrayList<String>();
        try {   
                   Client client= ClientBuilder.newClient();

                   WebTarget target = client.target(URL_FACTURA_DETALLE + "");
                   //Invocation.Builder solicitud =target.queryParam("id",1).request();
                   Invocation.Builder solicitud =target.request();
                   Response get = solicitud.get();
                   String responseJson = get.readEntity(String.class);                   
                   
                   ArrayList<FacturaDetalle> data = new Gson().fromJson(responseJson,new TypeToken<ArrayList<FacturaDetalle>>(){}.getType());
                   
                   for (int i = 0; i < data.size(); i++) {
                        lista.add(data.get(i).getIddetalle().toString());
                    }
                               
                   switch (get.getStatus()) {
                       case 200:
                           for (int i = 0; i < lista.size(); i++) {
                                if(i==(lista.size() - 1)){
                                   idDetalle = lista.get(i);
                                }
                            }
                           break;
                       default:
                           res = "Error detalle";
                           JOptionPane.showMessageDialog(null,res);
                           break;
                   }
        } catch (Exception ex) {
            Logger.getLogger(Empleados.class.getName()).log(Level.SEVERE, null, ex);
        }
        lbl_idDetalle.setText(idDetalle);
    }
    
    public void validacionPago(){
        if(cmb_metodoPago.getSelectedItem().equals("Seleccione el m??todo")){
            JOptionPane.showMessageDialog(this, "Por favor seleccione el m??todo de pago", "Seleccione el m??todo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if(txt_pago.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Por favor seleccione la cantidad de pago con los botones num??ricos", "Pago en blanco", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if(txt_cambio.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Por favor seleccione la cantidad de pago con los botones num??ricos", "Cambio en blanco", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        double total = Double.parseDouble(txt_total.getText());
        double pago = Double.parseDouble(txt_pago.getText());
        
        if(total > pago){
            Locale locale = new Locale("es", "HN");
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
            double resto = total - pago;
            String restoTotal = currencyFormatter.format(resto);
            JOptionPane.showMessageDialog(this, "Pago insuficiente, faltan: L "+restoTotal+" ", "Pago Insuficiente", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    }
    
    public void imprimirFactura(){
        try { 
            JasperReport reporte = null;
            String path = "src\\main\\java\\ujcv\\edu\\hn\\reportes\\factura.jasper";
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("numeroFactura",lbl_idFactura.getText());
            parameters.put("cai","35BD6A-0195F4-B34BAA-8B7D13-37791A-2D");
            parameters.put("nombreEmpleado",nombreEmpleado);
            parameters.put("nombreCliente",nombreCliente);
            parameters.put("rtnCliente",rtnCliente);
            String totalConFormato = currencyFormatter.format(Double.parseDouble(totalFactura));
            String pagoConFormato = currencyFormatter.format(Double.parseDouble(pagoFactura));
            parameters.put("totalFactura",totalConFormato);
            parameters.put("cambioFactura",cambioFactura);
            parameters.put("pagoFactura",pagoConFormato);
            reporte = (JasperReport) JRLoader.loadObjectFromFile(path);
            JasperPrint jprint;
            
            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(productoNuevo);
            parameters.put("ds", ds);
            jprint=JasperFillManager.fillReport(reporte,parameters,new JREmptyDataSource());  
            JasperViewer view = new JasperViewer(jprint,false);
            final JRViewer viewer = new JRViewer(jprint);
            JRSaveContributor[] contrbs = viewer.getSaveContributors();

            for (JRSaveContributor saveContributor : contrbs)
            {
                if (!(saveContributor instanceof net.sf.jasperreports.view.save.JRDocxSaveContributor ||
                    saveContributor instanceof net.sf.jasperreports.view.save.JRSingleSheetXlsSaveContributor
                    || saveContributor instanceof net.sf.jasperreports.view.save.JRPdfSaveContributor))
            viewer.removeSaveContributor(saveContributor);
        }
        view.setContentPane(viewer);
        view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        view.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }
    
    public void actualizarTotal(){
       double isv = totalPrecioOrden * 0.15;
       double subtotal = totalPrecioOrden - isv;
       txt_total.setText(String.valueOf(totalPrecioOrden));
       txt_isv.setText(String.valueOf(isv));
       txt_subtotal.setText(String.valueOf(subtotal));
      
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
        jpn_principal = new javax.swing.JPanel();
        lbl_nombreUsuario = new javax.swing.JLabel();
        lbl_home = new javax.swing.JLabel();
        lbl_usuario = new javax.swing.JLabel();
        lbl_tituloClientes = new javax.swing.JLabel();
        jpn_numeros = new javax.swing.JPanel();
        btn_9 = new javax.swing.JButton();
        btn_7 = new javax.swing.JButton();
        btn_8 = new javax.swing.JButton();
        btn_4 = new javax.swing.JButton();
        btn_5 = new javax.swing.JButton();
        btn_6 = new javax.swing.JButton();
        btn_1 = new javax.swing.JButton();
        btn_2 = new javax.swing.JButton();
        btn_3 = new javax.swing.JButton();
        btn_0 = new javax.swing.JButton();
        btn_punto = new javax.swing.JButton();
        btn_retroceso = new javax.swing.JButton();
        jpn_productos = new javax.swing.JPanel();
        btn_producto1 = new javax.swing.JButton();
        btn_producto2 = new javax.swing.JButton();
        btn_producto3 = new javax.swing.JButton();
        btn_producto4 = new javax.swing.JButton();
        btn_producto5 = new javax.swing.JButton();
        btn_producto6 = new javax.swing.JButton();
        btn_producto7 = new javax.swing.JButton();
        btn_producto8 = new javax.swing.JButton();
        btn_producto9 = new javax.swing.JButton();
        jsp_tabla = new javax.swing.JScrollPane();
        tbl_orden = new javax.swing.JTable();
        jpn_pago = new javax.swing.JPanel();
        jpn_total = new javax.swing.JPanel();
        lbl_subtotal = new javax.swing.JLabel();
        lbl_isv = new javax.swing.JLabel();
        lbl_total = new javax.swing.JLabel();
        txt_subtotal = new javax.swing.JTextField();
        txt_total = new javax.swing.JTextField();
        txt_isv = new javax.swing.JTextField();
        jpn_metodoPago = new javax.swing.JPanel();
        lbl_metodoPago = new javax.swing.JLabel();
        lbl_pago = new javax.swing.JLabel();
        lbl_cambio = new javax.swing.JLabel();
        txt_cambio = new javax.swing.JTextField();
        txt_pago = new javax.swing.JTextField();
        cmb_metodoPago = new javax.swing.JComboBox<>();
        btn_cancelarFactura = new javax.swing.JButton();
        btn_iniciarFactura = new javax.swing.JButton();
        btn_pagar = new javax.swing.JButton();
        btn_eliminar = new javax.swing.JButton();
        lbl_cliente = new javax.swing.JLabel();
        lbl_nombreProducto = new javax.swing.JLabel();
        cmb_cliente = new javax.swing.JComboBox<>();
        lbl_idCliente = new javax.swing.JLabel();
        lbl_idFactura = new javax.swing.JLabel();
        lbl_idProducto = new javax.swing.JLabel();
        lbl_idDetalle = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        kGradientPanel1.setkEndColor(new java.awt.Color(40, 74, 172));
        kGradientPanel1.setkStartColor(new java.awt.Color(205, 63, 145));
        kGradientPanel1.setkTransparentControls(false);
        kGradientPanel1.setPreferredSize(new java.awt.Dimension(1319, 821));

        jpn_principal.setBackground(new java.awt.Color(255, 255, 255));

        lbl_nombreUsuario.setFont(new java.awt.Font("Roboto Black", 0, 24)); // NOI18N
        lbl_nombreUsuario.setText("Nombre de Usuario");

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
        lbl_tituloClientes.setText("Orden");

        jpn_numeros.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        btn_9.setFont(new java.awt.Font("Roboto", 1, 36)); // NOI18N
        btn_9.setText("9");
        btn_9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_9.setEnabled(false);
        btn_9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_9ActionPerformed(evt);
            }
        });

        btn_7.setFont(new java.awt.Font("Roboto", 1, 36)); // NOI18N
        btn_7.setText("7");
        btn_7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_7.setEnabled(false);
        btn_7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_7ActionPerformed(evt);
            }
        });

        btn_8.setFont(new java.awt.Font("Roboto", 1, 36)); // NOI18N
        btn_8.setText("8");
        btn_8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_8.setEnabled(false);
        btn_8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_8ActionPerformed(evt);
            }
        });

        btn_4.setFont(new java.awt.Font("Roboto", 1, 36)); // NOI18N
        btn_4.setText("4");
        btn_4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_4.setEnabled(false);
        btn_4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_4ActionPerformed(evt);
            }
        });

        btn_5.setFont(new java.awt.Font("Roboto", 1, 36)); // NOI18N
        btn_5.setText("5");
        btn_5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_5.setEnabled(false);
        btn_5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_5ActionPerformed(evt);
            }
        });

        btn_6.setFont(new java.awt.Font("Roboto", 1, 36)); // NOI18N
        btn_6.setText("6");
        btn_6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_6.setEnabled(false);
        btn_6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_6ActionPerformed(evt);
            }
        });

        btn_1.setFont(new java.awt.Font("Roboto", 1, 36)); // NOI18N
        btn_1.setText("1");
        btn_1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_1.setEnabled(false);
        btn_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_1ActionPerformed(evt);
            }
        });

        btn_2.setFont(new java.awt.Font("Roboto", 1, 36)); // NOI18N
        btn_2.setText("2");
        btn_2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_2.setEnabled(false);
        btn_2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_2ActionPerformed(evt);
            }
        });

        btn_3.setFont(new java.awt.Font("Roboto", 1, 36)); // NOI18N
        btn_3.setText("3");
        btn_3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_3.setEnabled(false);
        btn_3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_3ActionPerformed(evt);
            }
        });

        btn_0.setFont(new java.awt.Font("Roboto", 1, 36)); // NOI18N
        btn_0.setText("0");
        btn_0.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_0.setEnabled(false);
        btn_0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_0ActionPerformed(evt);
            }
        });

        btn_punto.setFont(new java.awt.Font("Roboto", 1, 36)); // NOI18N
        btn_punto.setText(".");
        btn_punto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_punto.setEnabled(false);
        btn_punto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_puntoActionPerformed(evt);
            }
        });

        btn_retroceso.setFont(new java.awt.Font("Roboto", 1, 36)); // NOI18N
        btn_retroceso.setText("C");
        btn_retroceso.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_retroceso.setEnabled(false);
        btn_retroceso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_retrocesoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpn_numerosLayout = new javax.swing.GroupLayout(jpn_numeros);
        jpn_numeros.setLayout(jpn_numerosLayout);
        jpn_numerosLayout.setHorizontalGroup(
            jpn_numerosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpn_numerosLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jpn_numerosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpn_numerosLayout.createSequentialGroup()
                        .addComponent(btn_7, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_8, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_9, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpn_numerosLayout.createSequentialGroup()
                        .addComponent(btn_4, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_5, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_6, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpn_numerosLayout.createSequentialGroup()
                        .addComponent(btn_1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_3, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpn_numerosLayout.createSequentialGroup()
                        .addComponent(btn_0, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_punto, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_retroceso, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jpn_numerosLayout.setVerticalGroup(
            jpn_numerosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpn_numerosLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jpn_numerosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_9, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_8, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_7, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpn_numerosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_6, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_5, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_4, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpn_numerosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_3, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpn_numerosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_retroceso, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_punto, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_0, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpn_productos.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        btn_producto1.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        btn_producto1.setIcon(new javax.swing.ImageIcon("src\\main\\java\\ujcv\\edu\\hn\\Img\\baleadasencilla.jpg"));
        btn_producto1.setText("Baleada Sencilla");
        btn_producto1.setToolTipText("");
        btn_producto1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_producto1.setEnabled(false);
        btn_producto1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_producto1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_producto1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_producto1ActionPerformed(evt);
            }
        });

        btn_producto2.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        btn_producto2.setIcon(new javax.swing.ImageIcon("src\\main\\java\\ujcv\\edu\\hn\\Img\\baleadacarne.jpg"));
        btn_producto2.setText("Baleada Carne");
        btn_producto2.setToolTipText("");
        btn_producto2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_producto2.setEnabled(false);
        btn_producto2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_producto2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_producto2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_producto2ActionPerformed(evt);
            }
        });

        btn_producto3.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        btn_producto3.setIcon(new javax.swing.ImageIcon("src\\main\\java\\ujcv\\edu\\hn\\Img\\baleada-con-todo.png"));
        btn_producto3.setText("Baleada con Todo");
        btn_producto3.setToolTipText("");
        btn_producto3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_producto3.setEnabled(false);
        btn_producto3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_producto3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_producto3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_producto3ActionPerformed(evt);
            }
        });

        btn_producto4.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        btn_producto4.setIcon(new javax.swing.ImageIcon("src\\main\\java\\ujcv\\edu\\hn\\Img\\carneasada.jpg"));
        btn_producto4.setText("Carne Asada");
        btn_producto4.setToolTipText("");
        btn_producto4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_producto4.setEnabled(false);
        btn_producto4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_producto4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_producto4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_producto4ActionPerformed(evt);
            }
        });

        btn_producto5.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        btn_producto5.setIcon(new javax.swing.ImageIcon("src\\main\\java\\ujcv\\edu\\hn\\Img\\pollo.png"));
        btn_producto5.setText("Pollo Chuco");
        btn_producto5.setToolTipText("");
        btn_producto5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_producto5.setEnabled(false);
        btn_producto5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_producto5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_producto5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_producto5ActionPerformed(evt);
            }
        });

        btn_producto6.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        btn_producto6.setIcon(new javax.swing.ImageIcon("src\\main\\java\\ujcv\\edu\\hn\\Img\\cena.jpg"));
        btn_producto6.setText("Cena T??pica");
        btn_producto6.setToolTipText("");
        btn_producto6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_producto6.setEnabled(false);
        btn_producto6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_producto6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_producto6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_producto6ActionPerformed(evt);
            }
        });

        btn_producto7.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        btn_producto7.setIcon(new javax.swing.ImageIcon("src\\main\\java\\ujcv\\edu\\hn\\Img\\refresconatural.jpeg"));
        btn_producto7.setText("Refresco Natural");
        btn_producto7.setToolTipText("");
        btn_producto7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_producto7.setEnabled(false);
        btn_producto7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_producto7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_producto7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_producto7ActionPerformed(evt);
            }
        });

        btn_producto8.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        btn_producto8.setIcon(new javax.swing.ImageIcon("src\\main\\java\\ujcv\\edu\\hn\\Img\\logococa.jpg"));
        btn_producto8.setText("Gaseosa");
        btn_producto8.setToolTipText("");
        btn_producto8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_producto8.setEnabled(false);
        btn_producto8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_producto8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_producto8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_producto8ActionPerformed(evt);
            }
        });

        btn_producto9.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        btn_producto9.setIcon(new javax.swing.ImageIcon("src\\main\\java\\ujcv\\edu\\hn\\Img\\desayunojpg.jpg"));
        btn_producto9.setText("Desayuno T??pico");
        btn_producto9.setToolTipText("");
        btn_producto9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_producto9.setEnabled(false);
        btn_producto9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_producto9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_producto9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_producto9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpn_productosLayout = new javax.swing.GroupLayout(jpn_productos);
        jpn_productos.setLayout(jpn_productosLayout);
        jpn_productosLayout.setHorizontalGroup(
            jpn_productosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpn_productosLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jpn_productosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_producto7)
                    .addComponent(btn_producto4)
                    .addComponent(btn_producto1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpn_productosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_producto8)
                    .addComponent(btn_producto5)
                    .addComponent(btn_producto2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpn_productosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_producto9)
                    .addComponent(btn_producto6)
                    .addComponent(btn_producto3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpn_productosLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btn_producto1, btn_producto2, btn_producto3, btn_producto4, btn_producto5, btn_producto6, btn_producto7, btn_producto8, btn_producto9});

        jpn_productosLayout.setVerticalGroup(
            jpn_productosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpn_productosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpn_productosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jpn_productosLayout.createSequentialGroup()
                        .addComponent(btn_producto3, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_producto6, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_producto9, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpn_productosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jpn_productosLayout.createSequentialGroup()
                            .addComponent(btn_producto2, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btn_producto5, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btn_producto8, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jpn_productosLayout.createSequentialGroup()
                            .addComponent(btn_producto1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btn_producto4, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btn_producto7, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpn_productosLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btn_producto1, btn_producto2, btn_producto3, btn_producto4, btn_producto5, btn_producto6, btn_producto7, btn_producto8, btn_producto9});

        tbl_orden.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre del Producto", "Cantidad", "Precio", "ID"
            }
        ));
        tbl_orden.setRowHeight(18);
        tbl_orden.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_ordenMouseClicked(evt);
            }
        });
        jsp_tabla.setViewportView(tbl_orden);

        jpn_pago.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jpn_total.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lbl_subtotal.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        lbl_subtotal.setText("Subtotal");

        lbl_isv.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        lbl_isv.setText("ISV");

        lbl_total.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        lbl_total.setText("Total");

        txt_subtotal.setEditable(false);
        txt_subtotal.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N
        txt_subtotal.setText("0");

        txt_total.setEditable(false);
        txt_total.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N
        txt_total.setText("0");

        txt_isv.setEditable(false);
        txt_isv.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N
        txt_isv.setText("0");

        javax.swing.GroupLayout jpn_totalLayout = new javax.swing.GroupLayout(jpn_total);
        jpn_total.setLayout(jpn_totalLayout);
        jpn_totalLayout.setHorizontalGroup(
            jpn_totalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpn_totalLayout.createSequentialGroup()
                .addGroup(jpn_totalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpn_totalLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(jpn_totalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_isv, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbl_total, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jpn_totalLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbl_subtotal)))
                .addGap(18, 18, 18)
                .addGroup(jpn_totalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_subtotal)
                    .addComponent(txt_isv)
                    .addComponent(txt_total, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jpn_totalLayout.setVerticalGroup(
            jpn_totalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpn_totalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpn_totalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_subtotal))
                .addGap(15, 15, 15)
                .addGroup(jpn_totalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_isv, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_isv))
                .addGap(18, 18, 18)
                .addGroup(jpn_totalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_total, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_total))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpn_metodoPago.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lbl_metodoPago.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        lbl_metodoPago.setText("M??todo de pago");

        lbl_pago.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        lbl_pago.setText("Pago");

        lbl_cambio.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        lbl_cambio.setText("Cambio");

        txt_cambio.setEditable(false);
        txt_cambio.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N

        txt_pago.setEditable(false);
        txt_pago.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N

        cmb_metodoPago.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N
        cmb_metodoPago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione el m??todo", "Efectivo", "Tarjeta" }));
        cmb_metodoPago.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmb_metodoPago.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cmb_metodoPago.setEnabled(false);
        cmb_metodoPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_metodoPagoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpn_metodoPagoLayout = new javax.swing.GroupLayout(jpn_metodoPago);
        jpn_metodoPago.setLayout(jpn_metodoPagoLayout);
        jpn_metodoPagoLayout.setHorizontalGroup(
            jpn_metodoPagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpn_metodoPagoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpn_metodoPagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_metodoPago)
                    .addComponent(lbl_pago, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_cambio, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jpn_metodoPagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_pago)
                    .addComponent(txt_cambio)
                    .addComponent(cmb_metodoPago, 0, 263, Short.MAX_VALUE))
                .addContainerGap(60, Short.MAX_VALUE))
        );
        jpn_metodoPagoLayout.setVerticalGroup(
            jpn_metodoPagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpn_metodoPagoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpn_metodoPagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_metodoPago)
                    .addComponent(cmb_metodoPago, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jpn_metodoPagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_pago, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_pago))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jpn_metodoPagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_cambio, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_cambio))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btn_cancelarFactura.setBackground(new java.awt.Color(205, 63, 145));
        btn_cancelarFactura.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        btn_cancelarFactura.setForeground(new java.awt.Color(255, 255, 255));
        btn_cancelarFactura.setText("Cancelar");
        btn_cancelarFactura.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_cancelarFactura.setEnabled(false);
        btn_cancelarFactura.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_cancelarFacturaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_cancelarFacturaMouseExited(evt);
            }
        });
        btn_cancelarFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelarFacturaActionPerformed(evt);
            }
        });

        btn_iniciarFactura.setBackground(new java.awt.Color(205, 63, 145));
        btn_iniciarFactura.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        btn_iniciarFactura.setForeground(new java.awt.Color(255, 255, 255));
        btn_iniciarFactura.setText("Iniciar");
        btn_iniciarFactura.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_iniciarFactura.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_iniciarFacturaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_iniciarFacturaMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_iniciarFacturaMousePressed(evt);
            }
        });
        btn_iniciarFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_iniciarFacturaActionPerformed(evt);
            }
        });

        btn_pagar.setBackground(new java.awt.Color(205, 63, 145));
        btn_pagar.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        btn_pagar.setForeground(new java.awt.Color(255, 255, 255));
        btn_pagar.setText("Pagar");
        btn_pagar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_pagar.setEnabled(false);
        btn_pagar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_pagarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_pagarMouseExited(evt);
            }
        });
        btn_pagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pagarActionPerformed(evt);
            }
        });

        btn_eliminar.setBackground(new java.awt.Color(205, 63, 145));
        btn_eliminar.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
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

        javax.swing.GroupLayout jpn_pagoLayout = new javax.swing.GroupLayout(jpn_pago);
        jpn_pago.setLayout(jpn_pagoLayout);
        jpn_pagoLayout.setHorizontalGroup(
            jpn_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpn_pagoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpn_total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jpn_metodoPago, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jpn_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_pagar, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_iniciarFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpn_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cancelarFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39))
        );
        jpn_pagoLayout.setVerticalGroup(
            jpn_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpn_pagoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jpn_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_iniciarFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cancelarFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jpn_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_pagar, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
            .addGroup(jpn_pagoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpn_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jpn_metodoPago, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpn_total, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lbl_cliente.setIcon(new javax.swing.ImageIcon("src\\main\\java\\ujcv\\edu\\hn\\Img\\cliente.png"));
        lbl_cliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl_clienteMousePressed(evt);
            }
        });

        lbl_nombreProducto.setText("nombreProducto");

        cmb_cliente.setFont(new java.awt.Font("Roboto", 0, 20)); // NOI18N
        cmb_cliente.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmb_cliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cmb_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_clienteActionPerformed(evt);
            }
        });

        lbl_idCliente.setText("idcliente");

        lbl_idFactura.setText("idFactura");

        lbl_idProducto.setText("idProducto");

        lbl_idDetalle.setText("idDetalle");

        javax.swing.GroupLayout jpn_principalLayout = new javax.swing.GroupLayout(jpn_principal);
        jpn_principal.setLayout(jpn_principalLayout);
        jpn_principalLayout.setHorizontalGroup(
            jpn_principalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpn_principalLayout.createSequentialGroup()
                .addGroup(jpn_principalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jpn_principalLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbl_usuario)
                        .addGap(18, 18, 18)
                        .addGroup(jpn_principalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_nombreUsuario)
                            .addGroup(jpn_principalLayout.createSequentialGroup()
                                .addComponent(lbl_idFactura)
                                .addGap(74, 74, 74)
                                .addComponent(lbl_idDetalle)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jpn_principalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpn_principalLayout.createSequentialGroup()
                                .addComponent(lbl_tituloClientes)
                                .addGap(164, 164, 164)
                                .addComponent(lbl_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cmb_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(lbl_home))
                            .addGroup(jpn_principalLayout.createSequentialGroup()
                                .addGap(194, 194, 194)
                                .addComponent(lbl_idProducto)
                                .addGap(308, 308, 308)
                                .addComponent(lbl_idCliente)
                                .addGap(140, 140, 140)
                                .addComponent(lbl_nombreProducto))))
                    .addGroup(jpn_principalLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jpn_principalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jpn_pago, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jpn_principalLayout.createSequentialGroup()
                                .addComponent(jpn_numeros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jsp_tabla, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jpn_productos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(21, 21, 21))
        );
        jpn_principalLayout.setVerticalGroup(
            jpn_principalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpn_principalLayout.createSequentialGroup()
                .addGroup(jpn_principalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpn_principalLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jpn_principalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_nombreProducto)
                            .addComponent(lbl_idCliente)
                            .addComponent(lbl_idProducto))
                        .addGap(18, 18, 18)
                        .addComponent(cmb_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpn_principalLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(lbl_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpn_principalLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jpn_principalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_home, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_cliente, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jpn_principalLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jpn_principalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_idFactura)
                            .addComponent(lbl_idDetalle))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jpn_principalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_nombreUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_tituloClientes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jpn_principalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jpn_productos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpn_numeros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jsp_tabla))
                .addGap(18, 18, 18)
                .addComponent(jpn_pago, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout kGradientPanel1Layout = new javax.swing.GroupLayout(kGradientPanel1);
        kGradientPanel1.setLayout(kGradientPanel1Layout);
        kGradientPanel1Layout.setHorizontalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addComponent(jpn_principal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        kGradientPanel1Layout.setVerticalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jpn_principal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1426, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(kGradientPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 928, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void lbl_homeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_homeMousePressed
        Principal principal = new Principal(lbl_nombreUsuario.getText());
        this.dispose();
        principal.setVisible(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_homeMousePressed

    private void btn_4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_4ActionPerformed
        String pago = "";
        pago = txt_pago.getText();
        pago += "4";
        txt_pago.setText(pago);
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_4ActionPerformed

    private void btn_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_1ActionPerformed
        String pago = "";
        pago = txt_pago.getText();
        pago += "1";
        txt_pago.setText(pago);
        cambio();
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_1ActionPerformed

    private void btn_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_2ActionPerformed
        String pago = "";
        pago = txt_pago.getText();
        pago += "2";
        txt_pago.setText(pago);
        cambio();
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_2ActionPerformed

    private void btn_retrocesoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_retrocesoActionPerformed
        int tama??o = txt_pago.getText().length();
        if (tama??o == 0) {
            return;
        }
        txt_pago.setText(txt_pago.getText().substring(0, (tama??o - 1)));

        if (tama??o >= 2) {
            cambio();
        }

        if (tama??o == 1) {
            txt_cambio.setText("-L" + txt_total.getText());
        }

        if (!txt_pago.getText().contains(".")) {
            hayDecimal = false;
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_retrocesoActionPerformed

    private void btn_0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_0ActionPerformed
        int tama??o = txt_pago.getText().length();
        if (tama??o == 0) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        String pago = "";
        pago = txt_pago.getText();
        pago += "0";
        txt_pago.setText(pago);
        cambio();
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_0ActionPerformed

    private void btn_3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_3ActionPerformed
        String pago = "";
        pago = txt_pago.getText();
        pago += "3";
        txt_pago.setText(pago);
        cambio();
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_3ActionPerformed

    private void btn_5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_5ActionPerformed
        String pago = "";
        pago = txt_pago.getText();
        pago += "5";
        txt_pago.setText(pago);
        cambio();
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_5ActionPerformed

    private void btn_6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_6ActionPerformed
        String pago = "";
        pago = txt_pago.getText();
        pago += "6";
        txt_pago.setText(pago);
        cambio();
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_6ActionPerformed

    private void btn_7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_7ActionPerformed
        String pago = "";
        pago = txt_pago.getText();
        pago += "7";
        txt_pago.setText(pago);
        cambio();
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_7ActionPerformed

    private void btn_8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_8ActionPerformed
        String pago = "";
        pago = txt_pago.getText();
        pago += "8";
        txt_pago.setText(pago);
        cambio();
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_8ActionPerformed

    private void btn_9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_9ActionPerformed
        String pago = "";
        pago = txt_pago.getText();
        pago += "9";
        txt_pago.setText(pago);
        cambio();
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_9ActionPerformed

    private void btn_puntoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_puntoActionPerformed
        int tama??o = txt_pago.getText().length();

        if (hayDecimal) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        if (tama??o == 0) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        String caracterAnterior = "";
        caracterAnterior = txt_pago.getText().substring((tama??o - 1));
        if (caracterAnterior.equals(".")) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        String pago = "";
        pago = txt_pago.getText();
        pago += ".";
        txt_pago.setText(pago);
        hayDecimal = true;
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_puntoActionPerformed

    private void btn_iniciarFacturaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_iniciarFacturaMouseEntered
        btn_iniciarFactura.setBackground(new Color(156, 2, 91));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_iniciarFacturaMouseEntered

    private void btn_iniciarFacturaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_iniciarFacturaMouseExited
        btn_iniciarFactura.setBackground(new Color(205, 63, 145));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_iniciarFacturaMouseExited

    private void btn_iniciarFacturaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_iniciarFacturaMousePressed

        // TODO add your handling code here:
    }//GEN-LAST:event_btn_iniciarFacturaMousePressed

    private void lbl_clienteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_clienteMousePressed
        cmb_cliente.requestFocus();
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_clienteMousePressed

    private void btn_iniciarFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_iniciarFacturaActionPerformed
        btn_iniciarFactura.setBackground(new Color(40, 74, 172));
        try {
            Calendar f;
            f = Calendar.getInstance();
            int d = f.get(Calendar.DATE), mes = 1 + (f.get(Calendar.MONTH)), a??o = f.get(Calendar.YEAR);
            String fecha = (a??o + "-" + mes + "-" + d);
            String cai = "35BD6A-0195F4-B34BAA-8B7D13-37791A-2D";
            int totalInicial = 0;
            int cambioInicial = 0;
            int pagoInicial = 0;
            
                    Client client= ClientBuilder.newClient();
                    WebTarget target = client.target(URL_FACTURA_ENCABEZADO + "/addfacturaencabezado");
                    Invocation.Builder solicitud =target.request();
                    FacturaEncabezado facturaEncabezado = new FacturaEncabezado();
                    facturaEncabezado.setCai(cai);
                    String idCliente = lbl_idCliente.getText();
                    capturarIdEmpleado(lbl_nombreUsuario.getText());           
                    facturaEncabezado.setIdempleado(String.valueOf(idEmpleado));
                    facturaEncabezado.setTotalfactura(String.valueOf(totalInicial));
                    facturaEncabezado.setIdcliente(Long.parseLong(idCliente));
                    facturaEncabezado.setFecha_factura(fecha);
                    facturaEncabezado.setCambio_factura(String.valueOf(cambioInicial));
                    facturaEncabezado.setPago_factura(String.valueOf(pagoInicial));
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(facturaEncabezado);
                    Response post = solicitud.post(Entity.json(jsonString));
                    String responseJson = post.readEntity(String.class);
                    FacturaEncabezado data = new Gson().fromJson(responseJson, FacturaEncabezado.class);           
                    switch (post.getStatus()) {
                        case 201:
                             res = "Factura iniciada exitosamente";
                             capturarIdFactura();
                             habilitarProductos(true);
                             cmb_cliente.setEnabled(false);
                             accionesIniciar();
                             break;
                        default:
                            res = "Error";
                            JOptionPane.showMessageDialog(null,res);
                            break;
                    }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_iniciarFacturaActionPerformed

    private void btn_pagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pagarActionPerformed
        btn_pagar.setBackground(new Color(40, 74, 172));
        try{
                    String cai = "35BD6A-0195F4-B34BAA-8B7D13-37791A-2D";
                    Calendar f;
                    f = Calendar.getInstance();
                    int d = f.get(Calendar.DATE), mes = 1 + (f.get(Calendar.MONTH)), a??o = f.get(Calendar.YEAR);
                    String fecha = (a??o + "-" + mes + "-" + d);
                    validacionPago();
                    
                    Client client= ClientBuilder.newClient();
                    WebTarget target = client.target(URL_FACTURA_ENCABEZADO + "/addfacturaencabezado");
                    Invocation.Builder solicitud =target.request();
                    FacturaEncabezado facturaEncabezado = new FacturaEncabezado();
                    facturaEncabezado.setIdfactura(Long.parseLong(lbl_idFactura.getText()));
                    facturaEncabezado.setCai(cai);              
                    String idCliente = lbl_idCliente.getText();
                    nombreCliente = capturarNombreCliente(Long.parseLong(idCliente));
                    capturarIdEmpleado(lbl_nombreUsuario.getText());           
                    facturaEncabezado.setIdempleado(String.valueOf(idEmpleado));
                    facturaEncabezado.setTotalfactura(txt_total.getText());
                    facturaEncabezado.setIdcliente(Long.parseLong(idCliente));
                    facturaEncabezado.setFecha_factura(fecha);
                    String cambio = txt_cambio.getText().substring(1);
                    facturaEncabezado.setCambio_factura(String.valueOf(cambio));
                    facturaEncabezado.setPago_factura(String.valueOf(txt_pago.getText()));
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(facturaEncabezado);
                    Response post = solicitud.post(Entity.json(jsonString));
                    String responseJson = post.readEntity(String.class);
                    FacturaEncabezado data = new Gson().fromJson(responseJson, FacturaEncabezado.class);           
                    switch (post.getStatus()) {
                        case 201:
                                JOptionPane.showMessageDialog(this, "Factura pagada");
                                totalFactura = txt_total.getText();
                                cambioFactura = txt_cambio.getText();
                                pagoFactura = txt_pago.getText();
                                imprimirFactura();
                                accionesCancelar();
                             break;
                        default:
                            res = "Error";
                            JOptionPane.showMessageDialog(null,res);
                            break;
                    }
        }catch(Exception e){
            
        }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_pagarActionPerformed

    private void btn_cancelarFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelarFacturaActionPerformed
        btn_cancelarFactura.setBackground(new Color(40, 74, 172));
        try {
                            Client client2= ClientBuilder.newClient();
                            WebTarget target2 = client2.target(URL_FACTURA_DETALLE + "/deleteAll/"+lbl_idFactura.getText());
                            Invocation.Builder solicitud2 =target2.request();
                            Response delete2 = solicitud2.delete();
                            String responseJson2 = delete2.readEntity(String.class);
                            switch(delete2.getStatus()){    
                                case 200:
                                Client client= ClientBuilder.newClient();
                                WebTarget target = client.target(URL_FACTURA_ENCABEZADO + "/delete/"+lbl_idFactura.getText());
                                Invocation.Builder solicitud =target.request();
                                Response delete = solicitud.delete();
                                String responseJson = delete.readEntity(String.class);
                                switch (delete.getStatus()) {
                                    case 200:
                                        res = "Factura cancelada exitosamente";
                                        JOptionPane.showMessageDialog(null,res);
                                        accionesCancelar();
                                        break;
                                    default:
                                        res = "Error";
                                        JOptionPane.showMessageDialog(null,res);
                                        break;
                                }            
                                break;
                                
                                default:
                                res = "Error: No se pudo eliminar la factura";
                                JOptionPane.showMessageDialog(null,res);
                                break;
                            }

            } catch (Exception e) {

            }
       
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_cancelarFacturaActionPerformed

    private void btn_pagarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_pagarMouseEntered
        btn_pagar.setBackground(new Color(156, 2, 91));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_pagarMouseEntered

    private void btn_pagarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_pagarMouseExited
        btn_pagar.setBackground(new Color(205, 63, 145));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_pagarMouseExited

    private void btn_cancelarFacturaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_cancelarFacturaMouseEntered
        btn_cancelarFactura.setBackground(new Color(156, 2, 91));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_cancelarFacturaMouseEntered

    private void btn_cancelarFacturaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_cancelarFacturaMouseExited
        btn_cancelarFactura.setBackground(new Color(205, 63, 145));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_cancelarFacturaMouseExited

    private void btn_eliminarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_eliminarMouseEntered
        btn_eliminar.setBackground(new Color(156, 2, 91));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_eliminarMouseEntered

    private void btn_eliminarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_eliminarMouseExited
        btn_eliminar.setBackground(new Color(205, 63, 145));
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_eliminarMouseExited

    private void btn_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminarActionPerformed
        btn_eliminar.setBackground(new Color(40, 74, 172));
            try {
                   Client client= ClientBuilder.newClient();
                   String idDetalle = tbl_orden.getValueAt(filaSeleccionada, 3).toString();
                   WebTarget target = client.target(URL_FACTURA_DETALLE + "/delete/"+idDetalle);
                   Invocation.Builder solicitud =target.request();
                   Response delete = solicitud.delete();
                   String responseJson = delete.readEntity(String.class);
                   switch (delete.getStatus()) {
                       case 200:
                           
                            productoNuevo.remove((filaSeleccionada));                         
                            JOptionPane.showMessageDialog(this, "Producto eliminado");
                            DefaultTableModel model = (DefaultTableModel) tbl_orden.getModel();
                            model.removeRow(filaSeleccionada);
                            int totalFilas = tbl_orden.getRowCount();
                            totalPrecioOrden = 0;
                            for (int i = 0; i < totalFilas; i++) {
                                totalPrecioOrden += Double.parseDouble(tbl_orden.getValueAt(i, 2).toString().substring(1));                            
                            }   
                            actualizarTotal();
                            btn_eliminar.setEnabled(false);
                           break;
                       default:
                           res = "Error";
                           JOptionPane.showMessageDialog(null,res);
                           break;
                   }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,e.getMessage());
            }

        // TODO add your handling code here:
    }//GEN-LAST:event_btn_eliminarActionPerformed

    private void btn_producto1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_producto1ActionPerformed
        lbl_nombreProducto.setText(btn_producto1.getText());
        actualizarDatos();
        // TODO add y;our handling code here:
    }//GEN-LAST:event_btn_producto1ActionPerformed

    private void btn_producto2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_producto2ActionPerformed
        lbl_nombreProducto.setText(btn_producto2.getText());
        actualizarDatos();
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_producto2ActionPerformed

    private void btn_producto3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_producto3ActionPerformed
        lbl_nombreProducto.setText(btn_producto3.getText());
        actualizarDatos();
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_producto3ActionPerformed

    private void btn_producto4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_producto4ActionPerformed
        lbl_nombreProducto.setText(btn_producto4.getText());
        actualizarDatos();
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_producto4ActionPerformed

    private void btn_producto5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_producto5ActionPerformed
        lbl_nombreProducto.setText(btn_producto5.getText());
        actualizarDatos();
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_producto5ActionPerformed

    private void btn_producto6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_producto6ActionPerformed
        lbl_nombreProducto.setText(btn_producto6.getText());
        actualizarDatos();
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_producto6ActionPerformed

    private void btn_producto7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_producto7ActionPerformed
        lbl_nombreProducto.setText(btn_producto7.getText());
        actualizarDatos();
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_producto7ActionPerformed

    private void btn_producto8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_producto8ActionPerformed
        lbl_nombreProducto.setText(btn_producto8.getText());
        actualizarDatos();
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_producto8ActionPerformed

    private void btn_producto9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_producto9ActionPerformed
        lbl_nombreProducto.setText(btn_producto9.getText());
        actualizarDatos();

        // TODO add your handling code here:
    }//GEN-LAST:event_btn_producto9ActionPerformed

    private void cmb_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_clienteActionPerformed
        String string = cmb_cliente.getSelectedItem().toString();
        String [] array =  string.split("\\.");
        lbl_idCliente.setText(array[0]);
        // TODO add your handling code here:
    }//GEN-LAST:event_cmb_clienteActionPerformed

    private void tbl_ordenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_ordenMouseClicked
        btn_eliminar.setEnabled(true);
        filaSeleccionada = tbl_orden.getSelectedRow();
        // TODO add your handling code here:
    }//GEN-LAST:event_tbl_ordenMouseClicked

    private void cmb_metodoPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_metodoPagoActionPerformed
        if(cmb_metodoPago.getSelectedItem().equals("Seleccione el m??todo")){
            habilitarNumeros(false);
        }else{
             habilitarNumeros(true);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_cmb_metodoPagoActionPerformed

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
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
            UIManager.put("Button.arc", 20);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Orden().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_0;
    private javax.swing.JButton btn_1;
    private javax.swing.JButton btn_2;
    private javax.swing.JButton btn_3;
    private javax.swing.JButton btn_4;
    private javax.swing.JButton btn_5;
    private javax.swing.JButton btn_6;
    private javax.swing.JButton btn_7;
    private javax.swing.JButton btn_8;
    private javax.swing.JButton btn_9;
    private javax.swing.JButton btn_cancelarFactura;
    private javax.swing.JButton btn_eliminar;
    private javax.swing.JButton btn_iniciarFactura;
    private javax.swing.JButton btn_pagar;
    private javax.swing.JButton btn_producto1;
    private javax.swing.JButton btn_producto2;
    private javax.swing.JButton btn_producto3;
    private javax.swing.JButton btn_producto4;
    private javax.swing.JButton btn_producto5;
    private javax.swing.JButton btn_producto6;
    private javax.swing.JButton btn_producto7;
    private javax.swing.JButton btn_producto8;
    private javax.swing.JButton btn_producto9;
    private javax.swing.JButton btn_punto;
    private javax.swing.JButton btn_retroceso;
    private javax.swing.JComboBox<String> cmb_cliente;
    private javax.swing.JComboBox<String> cmb_metodoPago;
    private javax.swing.JPanel jpn_metodoPago;
    private javax.swing.JPanel jpn_numeros;
    private javax.swing.JPanel jpn_pago;
    private javax.swing.JPanel jpn_principal;
    private javax.swing.JPanel jpn_productos;
    private javax.swing.JPanel jpn_total;
    private javax.swing.JScrollPane jsp_tabla;
    private keeptoo.KGradientPanel kGradientPanel1;
    private javax.swing.JLabel lbl_cambio;
    private javax.swing.JLabel lbl_cliente;
    private javax.swing.JLabel lbl_home;
    private javax.swing.JLabel lbl_idCliente;
    private javax.swing.JLabel lbl_idDetalle;
    private javax.swing.JLabel lbl_idFactura;
    private javax.swing.JLabel lbl_idProducto;
    private javax.swing.JLabel lbl_isv;
    private javax.swing.JLabel lbl_metodoPago;
    private javax.swing.JLabel lbl_nombreProducto;
    private javax.swing.JLabel lbl_nombreUsuario;
    private javax.swing.JLabel lbl_pago;
    private javax.swing.JLabel lbl_subtotal;
    private javax.swing.JLabel lbl_tituloClientes;
    private javax.swing.JLabel lbl_total;
    private javax.swing.JLabel lbl_usuario;
    private javax.swing.JTable tbl_orden;
    private javax.swing.JTextField txt_cambio;
    private javax.swing.JTextField txt_isv;
    private javax.swing.JTextField txt_pago;
    private javax.swing.JTextField txt_subtotal;
    private javax.swing.JTextField txt_total;
    // End of variables declaration//GEN-END:variables
}

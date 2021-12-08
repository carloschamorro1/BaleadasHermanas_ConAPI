/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ujcv.edu.hn.baleadashermanas_api.model;

/**
 *
 * @author Carlos
 */
public class Producto {
    
    private String idproducto;
    private String nombreproducto; 
    private Long idempleado; 
    private String cantidadstock; 
    private String fechaintroduccion;
    private String tipomovimiento; 
    private String precio;

    //constructor

    public Producto() {
        super();
    }

    public String getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(String idproducto) {
        this.idproducto = idproducto;
    }

    public String getNombreproducto() {
        return nombreproducto;
    }

    public void setNombreproducto(String nombreproducto) {
        this.nombreproducto = nombreproducto;
    }

    public Long getIdempleado() {
        return idempleado;
    }

    public void setIdempleado(Long idempleado) {
        this.idempleado = idempleado;
    }

    public String getCantidadstock() {
        return cantidadstock;
    }

    public void setCantidadstock(String cantidadstock) {
        this.cantidadstock = cantidadstock;
    }

    public String getFechaintroduccion() {
        return fechaintroduccion;
    }

    public void setFechaintroduccion(String fechaintroduccion) {
        this.fechaintroduccion = fechaintroduccion;
    }

    public String getTipomovimiento() {
        return tipomovimiento;
    }

    public void setTipomovimiento(String tipomovimiento) {
        this.tipomovimiento = tipomovimiento;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public void setVisible(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

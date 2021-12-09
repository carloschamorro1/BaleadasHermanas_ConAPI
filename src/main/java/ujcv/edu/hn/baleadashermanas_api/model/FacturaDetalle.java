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
public class FacturaDetalle {

    private Long iddetalle;
    private Long idfactura;
    private Long idproducto;
    private String cantidadfactura;
    private String precio;

    //Constructor
    public FacturaDetalle(){
        super();
    }

    public Long getIddetalle() {
        return iddetalle;
    }

    public void setIddetalle(Long iddetalle) {
        this.iddetalle = iddetalle;
    }

    public Long getIdfactura() {
        return idfactura;
    }

    public void setIdfactura(Long idfactura) {
        this.idfactura = idfactura;
    }

    public Long getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(Long idproducto) {
        this.idproducto = idproducto;
    }

    public String getCantidadfactura() {
        return cantidadfactura;
    }

    public void setCantidadfactura(String cantidadfactura) {
        this.cantidadfactura = cantidadfactura;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}

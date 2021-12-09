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
public class FacturaEncabezado {
    
    private Long idfactura;
    private String cai; 
    private String idempleado; 
    private String totalfactura; 
    private Long idcliente; 
    private String fecha_factura; 
    private String cambio_factura; 
    private String pago_factura;

    //Constructor
    public FacturaEncabezado(){
        super();
    }

    public Long getIdfactura() {
        return idfactura;
    }

    public void setIdfactura(Long idfactura) {
        this.idfactura = idfactura;
    }

    public String getCai() {
        return cai;
    }

    public void setCai(String cai) {
        this.cai = cai;
    }

    public String getIdempleado() {
        return idempleado;
    }

    public void setIdempleado(String idempleado) {
        this.idempleado = idempleado;
    }

    public String getTotalfactura() {
        return totalfactura;
    }

    public void setTotalfactura(String totalfactura) {
        this.totalfactura = totalfactura;
    }

    public Long getIdcliente() {
        return idcliente;
    }

    public void setIdcliente(Long idcliente) {
        this.idcliente = idcliente;
    }

    public String getFecha_factura() {
        return fecha_factura;
    }

    public void setFecha_factura(String fecha_factura) {
        this.fecha_factura = fecha_factura;
    }

    public String getCambio_factura() {
        return cambio_factura;
    }

    public void setCambio_factura(String cambio_factura) {
        this.cambio_factura = cambio_factura;
    }

    public String getPago_factura() {
        return pago_factura;
    }

    public void setPago_factura(String pago_factura) {
        this.pago_factura = pago_factura;
    }
}

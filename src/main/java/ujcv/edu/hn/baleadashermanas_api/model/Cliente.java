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
public class Cliente {
    private Long idcliente;
    private String primer_nombre_cliente ;
    private String segundo_nombre_cliente;
    private String primer_apellido_cliente;
    private String segundo_apellido_cliente;
    private String telefono_cliente;
    private String email_cliente;
    private String dnicliente;
    private String rtncliente;
   

    //Constructor
    public Cliente(){
        super();
    }

    //getters and setters

    public Long getIdcliente() {
        return idcliente;
    }

    public void setIdcliente(Long idcliente) {
        this.idcliente = idcliente;
    }


    public String getPrimer_nombre_cliente() {
        return primer_nombre_cliente;
    }

    public void setPrimer_nombre_cliente(String primer_nombre_cliente) {
        this.primer_nombre_cliente = primer_nombre_cliente;
    }

    public String getSegundo_nombre_cliente() {
        return segundo_nombre_cliente;
    }

    public void setSegundo_nombre_cliente(String segundo_nombre_cliente) {
        this.segundo_nombre_cliente = segundo_nombre_cliente;
    }

    public String getPrimer_apellido_cliente() {
        return primer_apellido_cliente;
    }

    public void setPrimer_apellido_cliente(String primer_apellido_cliente) {
        this.primer_apellido_cliente = primer_apellido_cliente;
    }

    public String getSegundo_apellido_cliente() {
        return segundo_apellido_cliente;
    }

    public void setSegundo_apellido_cliente(String segundo_apellido_cliente) {
        this.segundo_apellido_cliente = segundo_apellido_cliente;
    }

    public String getTelefono_cliente() {
        return telefono_cliente;
    }

    public void setTelefono_cliente(String telefono_cliente) {
        this.telefono_cliente = telefono_cliente;
    }

    public String getEmail_cliente() {
        return email_cliente;
    }

    public void setEmail_cliente(String email_cliente) {
        this.email_cliente = email_cliente;
    }

    public String getRtncliente() {
        return rtncliente;
    }

    public void setRtncliente(String rtncliente) {
        this.rtncliente = rtncliente;
    }

    public String getDnicliente() {
        return dnicliente;
    }

    public void setDnicliente(String dnicliente) {
        this.dnicliente = dnicliente;
    }
}

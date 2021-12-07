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
public class Empleado {

    private Long idempleado;
    private String primer_nombre_empleado ;
    private String segundo_nombre_empleado;
    private String primer_apellido_empleado;
    private String segundo_apellido_empleado;
    private String telefono_empleado;
    private String email_empleado;
    private String dniempleado;
    private String usuario;
    private String contraseña;

    public Empleado(){
        super();
    }

    public Long getIdempleado() {
        return idempleado;
    }

    public void setIdempleado(Long idempleado) {
        this.idempleado = idempleado;
    }

    public String getPrimer_nombre_empleado() {
        return primer_nombre_empleado;
    }

    public void setPrimer_nombre_empleado(String primer_nombre_empleado) {
        this.primer_nombre_empleado = primer_nombre_empleado;
    }

    public String getSegundo_nombre_empleado() {
        return segundo_nombre_empleado;
    }

    public void setSegundo_nombre_empleado(String segundo_nombre_empleado) {
        this.segundo_nombre_empleado = segundo_nombre_empleado;
    }

    public String getPrimer_apellido_empleado() {
        return primer_apellido_empleado;
    }

    public void setPrimer_apellido_empleado(String primer_apellido_empleado) {
        this.primer_apellido_empleado = primer_apellido_empleado;
    }

    public String getSegundo_apellido_empleado() {
        return segundo_apellido_empleado;
    }

    public void setSegundo_apellido_empleado(String segundo_apellido_empleado) {
        this.segundo_apellido_empleado = segundo_apellido_empleado;
    }

    public String getTelefono_empleado() {
        return telefono_empleado;
    }

    public void setTelefono_empleado(String telefono_empleado) {
        this.telefono_empleado = telefono_empleado;
    }

    public String getEmail_empleado() {
        return email_empleado;
    }

    public void setEmail_empleado(String email_empleado) {
        this.email_empleado = email_empleado;
    }

    public String getDniempleado() {
        return dniempleado;
    }

    public void setDniempleado(String dniempleado) {
        this.dniempleado = dniempleado;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
}

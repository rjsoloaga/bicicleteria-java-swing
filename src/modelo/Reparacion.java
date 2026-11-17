package modelo;
import java.sql.Date;
public class Reparacion {
    private int idReparacion;
    private String codigo;
    private String descripcion;
    private String bicicleta;
    private int costo;
    private String cliente;
    private Date fechaIngreso;
    private String estado;
    public int getIdReparacion() { return idReparacion; }
    public void setIdReparacion(int idReparacion) { this.idReparacion = idReparacion; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getBicicleta() { return bicicleta; }
    public void setBicicleta(String bicicleta) { this.bicicleta = bicicleta; }
    public int getCosto() { return costo; }
    public void setCosto(int costo) { this.costo = costo; }
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    public Date getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(Date fechaIngreso) { this.fechaIngreso = fechaIngreso; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}

package modelo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
public class ConsultasReparacion extends Conexion {
    PreparedStatement ps = null;
    ResultSet rs = null;
    public boolean insertar(Reparacion reparacion) {
        Connection conexion = getConnection();
        if (conexion == null) { return false; }
        try {
            ps = conexion.prepareStatement("INSERT INTO reparacion (codigo,descripcion,bicicleta,costo,cliente,fecha_ingreso,estado) VALUES (?,?,?,?,?,?,?)");
            ps.setString(1, reparacion.getCodigo());
            ps.setString(2, reparacion.getDescripcion());
            ps.setString(3, reparacion.getBicicleta());
            ps.setInt(4, reparacion.getCosto());
            ps.setString(5, reparacion.getCliente());
            ps.setDate(6, reparacion.getFechaIngreso());
            ps.setString(7, reparacion.getEstado());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            System.err.println("Error en insertar: " + ex);
            return false;
        } finally { cerrar(conexion); }
    }
    public boolean buscar(Reparacion reparacion) {
        Connection conexion = getConnection();
        if (conexion == null) { return false; }
        try {
            ps = conexion.prepareStatement("SELECT * FROM reparacion WHERE codigo=?");
            ps.setString(1, reparacion.getCodigo());
            rs = ps.executeQuery();
            if (rs.next()) {
                reparacion.setIdReparacion(rs.getInt("idReparacion"));
                reparacion.setCodigo(rs.getString("codigo"));
                reparacion.setDescripcion(rs.getString("descripcion"));
                reparacion.setBicicleta(rs.getString("bicicleta"));
                reparacion.setCosto(rs.getInt("costo"));
                reparacion.setCliente(rs.getString("cliente"));
                reparacion.setFechaIngreso(rs.getDate("fecha_ingreso"));
                reparacion.setEstado(rs.getString("estado"));
                return true;
            }
            return false;
        } catch (Exception ex) {
            System.err.println("Error en buscar: " + ex);
            return false;
        } finally { cerrar(conexion); }
    }
    public boolean modificar(Reparacion reparacion) {
        Connection conexion = getConnection();
        if (conexion == null) { return false; }
        try {
            ps = conexion.prepareStatement("UPDATE reparacion SET codigo=?,descripcion=?,bicicleta=?,costo=?,cliente=?,fecha_ingreso=?,estado=? WHERE idReparacion=?");
            ps.setString(1, reparacion.getCodigo());
            ps.setString(2, reparacion.getDescripcion());
            ps.setString(3, reparacion.getBicicleta());
            ps.setInt(4, reparacion.getCosto());
            ps.setString(5, reparacion.getCliente());
            ps.setDate(6, reparacion.getFechaIngreso());
            ps.setString(7, reparacion.getEstado());
            ps.setInt(8, reparacion.getIdReparacion());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            System.err.println("Error en modificar: " + ex);
            return false;
        } finally { cerrar(conexion); }
    }
    public boolean eliminar(Reparacion reparacion) {
        Connection conexion = getConnection();
        if (conexion == null) { return false; }
        try {
            ps = conexion.prepareStatement("DELETE FROM reparacion WHERE idReparacion=?");
            ps.setInt(1, reparacion.getIdReparacion());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            System.err.println("Error en eliminar: " + ex);
            return false;
        } finally { cerrar(conexion); }
    }
    public List<Reparacion> listar() {
        List<Reparacion> lista = new ArrayList<Reparacion>();
        Connection conexion = getConnection();
        if (conexion == null) { return lista; }
        try {
            //ps = conexion.prepareStatement("SELECT * FROM reparacoines");
            ps = conexion.prepareStatement("SELECT * FROM reparacion");
            rs = ps.executeQuery();
            while (rs.next()) {
                Reparacion item = new Reparacion();
                item.setIdReparacion(rs.getInt("idReparacion"));
                item.setCodigo(rs.getString("codigo"));
                item.setDescripcion(rs.getString("descripcion"));
                item.setBicicleta(rs.getString("bicicleta"));
                item.setCosto(rs.getInt("costo"));
                item.setCliente(rs.getString("cliente"));
                item.setFechaIngreso(rs.getDate("fecha_ingreso"));
                item.setEstado(rs.getString("estado"));
                lista.add(item);
            }
        } catch (Exception ex) {
            System.err.println("Error al listar reparacion: " + ex);
        } finally { cerrar(conexion); }
        return lista;
    }
    private void cerrar(Connection conexion) {
        try { conexion.close(); } catch (Exception ex) { System.err.println("Error al cerrar: " + ex); }
    }
}

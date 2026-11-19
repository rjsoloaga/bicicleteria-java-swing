package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ConsultasTipo extends Conexion {

    public boolean insertar(TipoProducto tipo) {
        String sql = "INSERT INTO tipo_producto (nombre) VALUES (?)";
        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, tipo.getNombre());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            System.err.println("Error al insertar tipo de producto: " + ex.getMessage());
            return false;
        }
    }

    public boolean modificar(TipoProducto tipo) {
        String sql = "UPDATE tipo_producto SET nombre = ? WHERE id_tipo = ?";
        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, tipo.getNombre());
            ps.setInt(2, tipo.getIdTipo());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            System.err.println("Error al modificar tipo de producto: " + ex.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idTipo) {
        String sql = "DELETE FROM tipo_producto WHERE id_tipo = ?";
        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idTipo);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            System.err.println("Error al eliminar tipo de producto: " + ex.getMessage());
            return false;
        }
    }

    public boolean eliminar(TipoProducto tipo) {
        return tipo != null && eliminar(tipo.getIdTipo());
    }

    public List<TipoProducto> listar() {
        List<TipoProducto> lista = new ArrayList<>();
        String sql = "SELECT id_tipo, nombre FROM tipo_producto ORDER BY id_tipo ASC";
        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new TipoProducto(rs.getInt("id_tipo"), rs.getString("nombre")));
            }
        } catch (Exception ex) {
            System.err.println("Error al listar tipos de producto: " + ex.getMessage());
        }
        return lista;
    }

    public boolean existeNombre(String nombre, int idExcluir) {
        String sql = "SELECT COUNT(*) FROM tipo_producto WHERE LOWER(nombre) = LOWER(?) AND id_tipo <> ?";
        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setInt(2, idExcluir);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (Exception ex) {
            System.err.println("Error al validar nombre de tipo duplicado: " + ex.getMessage());
            return false;
        }
    }

    public boolean tieneCategoriasAsociadas(int idTipo) {
        String sql = "SELECT COUNT(*) FROM categoria_producto WHERE id_tipo = ?";
        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idTipo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (Exception ex) {
            System.err.println("Error al verificar categorías asociadas al tipo: " + ex.getMessage());
            return true;
        }
    }
}

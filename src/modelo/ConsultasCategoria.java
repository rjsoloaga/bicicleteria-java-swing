package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ConsultasCategoria extends Conexion {

    public boolean insertar(CategoriaProducto categoria) {
        String sql = "INSERT INTO categoria_producto (nombre, id_tipo) VALUES (?, ?)";
        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, categoria.getNombre());
            ps.setInt(2, categoria.getTipo().getIdTipo());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            System.err.println("Error al insertar categoría: " + ex.getMessage());
            return false;
        }
    }

    public boolean modificar(CategoriaProducto categoria) {
        String sql = "UPDATE categoria_producto SET nombre = ?, id_tipo = ? WHERE id_categoria = ?";
        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, categoria.getNombre());
            ps.setInt(2, categoria.getTipo().getIdTipo());
            ps.setInt(3, categoria.getIdCategoria());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            System.err.println("Error al modificar categoría: " + ex.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idCategoria) {
        String sql = "DELETE FROM categoria_producto WHERE id_categoria = ?";
        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idCategoria);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            System.err.println("Error al eliminar categoría: " + ex.getMessage());
            return false;
        }
    }

    public boolean eliminar(CategoriaProducto categoria) {
        return categoria != null && eliminar(categoria.getIdCategoria());
    }

    public List<CategoriaProducto> listar() {
        List<CategoriaProducto> lista = new ArrayList<>();
        String sql = "SELECT c.id_categoria, c.nombre, t.id_tipo, t.nombre AS nombre_tipo "
                + "FROM categoria_producto c "
                + "INNER JOIN tipo_producto t ON c.id_tipo = t.id_tipo "
                + "ORDER BY c.nombre ASC";
        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TipoProducto tipo = new TipoProducto(rs.getInt("id_tipo"), rs.getString("nombre_tipo"));
                CategoriaProducto cat = new CategoriaProducto(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre"),
                        tipo
                );
                lista.add(cat);
            }
        } catch (Exception ex) {
            System.err.println("Error al listar categorías: " + ex.getMessage());
        }
        return lista;
    }

    public List<TipoProducto> listarTipos() {
        List<TipoProducto> tipos = new ArrayList<>();
        String sql = "SELECT id_tipo, nombre FROM tipo_producto ORDER BY nombre ASC";
        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tipos.add(new TipoProducto(rs.getInt("id_tipo"), rs.getString("nombre")));
            }
        } catch (Exception ex) {
            System.err.println("Error al listar tipos para combo: " + ex.getMessage());
        }
        return tipos;
    }

    public boolean existeNombre(String nombre, int idExcluir) {
        String sql = "SELECT COUNT(*) FROM categoria_producto WHERE LOWER(nombre) = LOWER(?) AND id_categoria <> ?";
        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setInt(2, idExcluir);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (Exception ex) {
            System.err.println("Error al validar nombre de categoría duplicado: " + ex.getMessage());
            return false;
        }
    }

    public boolean tieneProductosAsociados(int idCategoria) {
        String sql = "SELECT COUNT(*) FROM producto WHERE id_categoria = ?";
        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (Exception ex) {
            System.err.println("Error al verificar productos asociados a la categoría: " + ex.getMessage());
            return true;
        }
    }
}

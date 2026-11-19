package modelo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Producto.OpcionCategoria;

public class ConsultasProducto extends Conexion {

    public boolean insertar(Producto p) {
        String sql = "INSERT INTO producto (codigo, nombre, precio, stock_actual, activo, id_categoria) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setBigDecimal(3, p.getPrecio());
            ps.setInt(4, p.getStockActual());
            ps.setBoolean(5, p.isActivo());
            ps.setInt(6, p.getIdCategoria());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            System.err.println("Error al insertar producto: " + ex.getMessage());
            return false;
        }
    }

    public boolean modificar(Producto p) {
        String sql = "UPDATE producto SET codigo = ?, nombre = ?, precio = ?, stock_actual = ?, id_categoria = ? "
                + "WHERE id_producto = ?";
        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setBigDecimal(3, p.getPrecio());
            ps.setInt(4, p.getStockActual());
            ps.setInt(5, p.getIdCategoria());
            ps.setInt(6, p.getIdProducto());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            System.err.println("Error al modificar producto: " + ex.getMessage());
            return false;
        }
    }

    public boolean cambiarEstado(int idProducto, boolean activo) {
        String sql = "UPDATE producto SET activo = ? WHERE id_producto = ?";
        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setBoolean(1, activo);
            ps.setInt(2, idProducto);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            System.err.println("Error al cambiar estado del producto: " + ex.getMessage());
            return false;
        }
    }

    public List<Producto> listar(boolean soloActivos, String filtroTexto) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.id_producto, p.codigo, p.nombre, p.precio, p.stock_actual, p.activo, "
                + "c.id_categoria, c.nombre AS nombre_categoria, "
                + "t.nombre AS nombre_tipo "
                + "FROM producto p "
                + "INNER JOIN categoria_producto c ON p.id_categoria = c.id_categoria "
                + "INNER JOIN tipo_producto t ON c.id_tipo = t.id_tipo "
                + "WHERE (p.codigo LIKE ? OR p.nombre LIKE ? OR c.nombre LIKE ?) "
                + (soloActivos ? "AND p.activo = 1 " : "")
                + "ORDER BY p.id_producto ASC";

        String filtro = "%" + (filtroTexto != null ? filtroTexto.trim() : "") + "%";

        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, filtro);
            ps.setString(2, filtro);
            ps.setString(3, filtro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearProducto(rs));
                }
            }
        } catch (Exception ex) {
            System.err.println("Error al listar productos: " + ex.getMessage());
        }
        return lista;
    }

    public boolean existeCodigo(String codigo, int idExcluir) {
        String sql = "SELECT COUNT(*) FROM producto WHERE codigo = ? AND id_producto <> ?";
        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, codigo);
            ps.setInt(2, idExcluir);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (Exception ex) {
            System.err.println("Error al validar código único: " + ex.getMessage());
            return false;
        }
    }

    public List<OpcionCategoria> listarCategoriasCombo() {
        List<OpcionCategoria> categorias = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre FROM categoria_producto ORDER BY nombre ASC";
        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                categorias.add(new OpcionCategoria(rs.getInt("id_categoria"), rs.getString("nombre")));
            }
        } catch (Exception ex) {
            System.err.println("Error al listar categorías para combo: " + ex.getMessage());
        }
        return categorias;
    }

    public int obtenerTotalProductosActivos() {
        String sql = "SELECT COUNT(*) FROM producto WHERE activo = 1";
        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception ex) {
            System.err.println("Error al obtener total de productos activos: " + ex.getMessage());
        }
        return 0;
    }

    public int obtenerStockTotalActivo() {
        String sql = "SELECT COALESCE(SUM(stock_actual), 0) FROM producto WHERE activo = 1";
        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception ex) {
            System.err.println("Error al obtener stock total activo: " + ex.getMessage());
        }
        return 0;
    }

    public BigDecimal obtenerValorizacionTotalActiva() {
        String sql = "SELECT COALESCE(SUM(precio * stock_actual), 0) FROM producto WHERE activo = 1";
        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                BigDecimal valor = rs.getBigDecimal(1);
                return valor != null ? valor : BigDecimal.ZERO;
            }
        } catch (Exception ex) {
            System.err.println("Error al obtener valorización total activa: " + ex.getMessage());
        }
        return BigDecimal.ZERO;
    }

    public List<Producto> listarStockBajo() {
        return listarStockBajo(5);
    }

    public List<Producto> listarStockBajo(int limiteStock) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.id_producto, p.codigo, p.nombre, p.precio, p.stock_actual, p.activo, "
                + "c.id_categoria, c.nombre AS nombre_categoria, "
                + "t.nombre AS nombre_tipo "
                + "FROM producto p "
                + "INNER JOIN categoria_producto c ON p.id_categoria = c.id_categoria "
                + "INNER JOIN tipo_producto t ON c.id_tipo = t.id_tipo "
                + "WHERE p.activo = 1 AND p.stock_actual <= ? "
                + "ORDER BY p.stock_actual ASC, p.nombre ASC";

        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, limiteStock);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearProducto(rs));
                }
            }
        } catch (Exception ex) {
            System.err.println("Error al listar productos con stock bajo: " + ex.getMessage());
        }
        return lista;
    }

    public List<EstadisticaTipo> obtenerEstadisticasPorTipo() {
        List<EstadisticaTipo> lista = new ArrayList<>();
        String sql = "SELECT t.nombre AS nombre_tipo, "
                + "COUNT(p.id_producto) AS cantidad_productos, "
                + "COALESCE(SUM(p.stock_actual), 0) AS stock_total, "
                + "COALESCE(MIN(p.precio), 0) AS precio_minimo, "
                + "COALESCE(MAX(p.precio), 0) AS precio_maximo "
                + "FROM tipo_producto t "
                + "LEFT JOIN categoria_producto c ON c.id_tipo = t.id_tipo "
                + "LEFT JOIN producto p ON p.id_categoria = c.id_categoria AND p.activo = 1 "
                + "GROUP BY t.id_tipo, t.nombre "
                + "ORDER BY t.nombre ASC";

        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                EstadisticaTipo est = new EstadisticaTipo(
                        rs.getString("nombre_tipo"),
                        rs.getInt("cantidad_productos"),
                        rs.getInt("stock_total"),
                        rs.getBigDecimal("precio_minimo"),
                        rs.getBigDecimal("precio_maximo")
                );
                lista.add(est);
            }
        } catch (Exception ex) {
            System.err.println("Error al obtener estadísticas por tipo: " + ex.getMessage());
        }
        return lista;
    }

    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setIdProducto(rs.getInt("id_producto"));
        p.setCodigo(rs.getString("codigo"));
        p.setNombre(rs.getString("nombre"));
        p.setPrecio(rs.getBigDecimal("precio"));
        p.setStockActual(rs.getInt("stock_actual"));
        p.setActivo(rs.getBoolean("activo"));
        p.setIdCategoria(rs.getInt("id_categoria"));
        p.setNombreCategoria(rs.getString("nombre_categoria"));
        p.setNombreTipo(rs.getString("nombre_tipo"));
        return p;
    }
}

package modelo;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ConsultasUsuario extends Conexion {

    public Usuario autenticar(String nombreUsuario, char[] clave) {
        String sql = "SELECT u.id_usuario, u.nombre_usuario, u.clave_hash, u.activo, "
                + "r.id_rol, r.nombre AS nombre_rol, "
                + "p.id_persona, p.nombre, p.apellido, p.dni, p.telefono, p.email "
                + "FROM usuario u "
                + "INNER JOIN rol r ON u.id_rol = r.id_rol "
                + "INNER JOIN persona p ON u.id_persona = p.id_persona "
                + "WHERE u.nombre_usuario = ?";

        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next() || !rs.getBoolean("activo")) {
                    return null;
                }
                if (!calcularHash(new String(clave)).equalsIgnoreCase(rs.getString("clave_hash"))) {
                    return null;
                }
                return mapearUsuario(rs);
            }
        } catch (Exception ex) {
            System.err.println("Error al autenticar: " + ex.getMessage());
            return null;
        }
    }

    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT u.id_usuario, u.nombre_usuario, u.activo, "
                + "r.id_rol, r.nombre AS nombre_rol, "
                + "p.id_persona, p.nombre, p.apellido, p.dni, p.telefono, p.email "
                + "FROM usuario u "
                + "INNER JOIN rol r ON u.id_rol = r.id_rol "
                + "INNER JOIN persona p ON u.id_persona = p.id_persona "
                + "ORDER BY u.id_usuario ASC";

        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
        } catch (Exception ex) {
            System.err.println("Error al listar usuarios: " + ex.getMessage());
        }
        return lista;
    }

    public List<Rol> listarRoles() {
        List<Rol> roles = new ArrayList<>();
        String sql = "SELECT id_rol, nombre FROM rol ORDER BY id_rol ASC";
        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                roles.add(new Rol(rs.getInt("id_rol"), rs.getString("nombre")));
            }
        } catch (Exception ex) {
            System.err.println("Error al listar roles: " + ex.getMessage());
        }
        return roles;
    }

    public boolean existeNombreUsuario(String nombreUsuario, int idUsuarioExcluir) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE nombre_usuario = ? AND id_usuario <> ?";
        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            ps.setInt(2, idUsuarioExcluir);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean existeDni(String dni, int idPersonaExcluir) {
        String sql = "SELECT COUNT(*) FROM persona WHERE dni = ? AND id_persona <> ?";
        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, dni);
            ps.setInt(2, idPersonaExcluir);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean insertarConPersona(Usuario usuario, String clavePlana) {
        String sqlPersona = "INSERT INTO persona (nombre, apellido, dni, telefono, email) VALUES (?, ?, ?, ?, ?)";
        String sqlUsuario = "INSERT INTO usuario (nombre_usuario, clave_hash, activo, id_rol, id_persona) VALUES (?, ?, ?, ?, ?)";
        Connection conexion = getConnection();
        if (conexion == null) return false;

        try {
            conexion.setAutoCommit(false);

            int idPersonaGenerado;
            try (PreparedStatement psPersona = conexion.prepareStatement(sqlPersona, Statement.RETURN_GENERATED_KEYS)) {
                Persona p = usuario.getPersona();
                psPersona.setString(1, p.getNombre());
                psPersona.setString(2, p.getApellido());
                psPersona.setString(3, p.getDni());
                psPersona.setString(4, p.getTelefono());
                psPersona.setString(5, p.getEmail());
                psPersona.executeUpdate();

                try (ResultSet keys = psPersona.getGeneratedKeys()) {
                    if (!keys.next()) throw new SQLException("No se generó ID de persona.");
                    idPersonaGenerado = keys.getInt(1);
                }
            }

            try (PreparedStatement psUsuario = conexion.prepareStatement(sqlUsuario)) {
                psUsuario.setString(1, usuario.getNombreUsuario());
                psUsuario.setString(2, calcularHash(clavePlana));
                psUsuario.setBoolean(3, usuario.isActivo());
                psUsuario.setInt(4, usuario.getRol().getIdRol());
                psUsuario.setInt(5, idPersonaGenerado);
                psUsuario.executeUpdate();
            }

            conexion.commit();
            return true;
        } catch (Exception ex) {
            try { conexion.rollback(); } catch (SQLException ignored) {}
            System.err.println("Error transaccional al insertar usuario: " + ex.getMessage());
            return false;
        } finally {
            try {
                conexion.setAutoCommit(true);
                conexion.close();
            } catch (SQLException ignored) {}
        }
    }

    public boolean modificarConPersona(Usuario usuario, String nuevaClavePlana) {
        String sqlPersona = "UPDATE persona SET nombre=?, apellido=?, dni=?, telefono=?, email=? WHERE id_persona=?";
        boolean cambiaClave = nuevaClavePlana != null && !nuevaClavePlana.trim().isEmpty();
        String sqlUsuario = cambiaClave
                ? "UPDATE usuario SET nombre_usuario=?, clave_hash=?, id_rol=? WHERE id_usuario=?"
                : "UPDATE usuario SET nombre_usuario=?, id_rol=? WHERE id_usuario=?";

        Connection conexion = getConnection();
        if (conexion == null) return false;

        try {
            conexion.setAutoCommit(false);

            try (PreparedStatement psPersona = conexion.prepareStatement(sqlPersona)) {
                Persona p = usuario.getPersona();
                psPersona.setString(1, p.getNombre());
                psPersona.setString(2, p.getApellido());
                psPersona.setString(3, p.getDni());
                psPersona.setString(4, p.getTelefono());
                psPersona.setString(5, p.getEmail());
                psPersona.setInt(6, p.getIdPersona());
                psPersona.executeUpdate();
            }

            try (PreparedStatement psUsuario = conexion.prepareStatement(sqlUsuario)) {
                psUsuario.setString(1, usuario.getNombreUsuario());
                if (cambiaClave) {
                    psUsuario.setString(2, calcularHash(nuevaClavePlana));
                    psUsuario.setInt(3, usuario.getRol().getIdRol());
                    psUsuario.setInt(4, usuario.getIdUsuario());
                } else {
                    psUsuario.setInt(2, usuario.getRol().getIdRol());
                    psUsuario.setInt(3, usuario.getIdUsuario());
                }
                psUsuario.executeUpdate();
            }

            conexion.commit();
            return true;
        } catch (Exception ex) {
            try { conexion.rollback(); } catch (SQLException ignored) {}
            System.err.println("Error transaccional al modificar usuario: " + ex.getMessage());
            return false;
        } finally {
            try {
                conexion.setAutoCommit(true);
                conexion.close();
            } catch (SQLException ignored) {}
        }
    }

    public boolean cambiarEstado(int idUsuario, boolean nuevoEstado) {
        String sql = "UPDATE usuario SET activo = ? WHERE id_usuario = ?";
        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setBoolean(1, nuevoEstado);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            System.err.println("Error al cambiar estado: " + ex.getMessage());
            return false;
        }
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Rol rol = new Rol(rs.getInt("id_rol"), rs.getString("nombre_rol"));

        Persona persona = new Persona();
        persona.setIdPersona(rs.getInt("id_persona"));
        persona.setNombre(rs.getString("nombre"));
        persona.setApellido(rs.getString("apellido"));
        persona.setDni(rs.getString("dni"));
        persona.setTelefono(rs.getString("telefono"));
        persona.setEmail(rs.getString("email"));

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("id_usuario"));
        usuario.setNombreUsuario(rs.getString("nombre_usuario"));
        usuario.setActivo(rs.getBoolean("activo"));
        usuario.setRol(rol);
        usuario.setPersona(persona);
        return usuario;
    }

    public String calcularHash(String texto) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = digest.digest(texto.getBytes(StandardCharsets.UTF_8));
        StringBuilder resultado = new StringBuilder();
        for (byte valor : bytes) {
            resultado.append(String.format("%02x", valor));
        }
        return resultado.toString();
    }
}
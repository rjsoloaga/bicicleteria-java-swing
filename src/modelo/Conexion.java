package modelo;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {
    public static final String URL = "jdbc:mysql://localhost:3306/bicicleteria_db?useSSL=false&serverTimezone=America/Argentina/Buenos_Aires";
    public static final String USUARIO = "root";
    public static final String CONTRASENA = "Ciro2014";

    public Connection getConnection() {
        Connection conexion = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
        } catch (Exception ex) {
            System.err.println("Error de conexión: " + ex.getMessage());
        }
        return conexion;
    }
}
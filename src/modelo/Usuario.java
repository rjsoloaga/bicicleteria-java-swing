package modelo;

public class Usuario {
    private int idUsuario;
    private String nombreUsuario;
    private Rol rol;
    private Persona persona;
    private boolean activo;

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
    public Persona getPersona() { return persona; }
    public void setPersona(Persona persona) { this.persona = persona; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public boolean esAdministrador() {
        return rol != null && "ADMINISTRADOR".equalsIgnoreCase(rol.getNombre());
    }
}
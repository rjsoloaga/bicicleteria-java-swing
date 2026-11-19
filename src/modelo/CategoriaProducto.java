package modelo;

public class CategoriaProducto {
    private int idCategoria;
    private String nombre;
    private TipoProducto tipo;

    public CategoriaProducto() {
    }

    public CategoriaProducto(int idCategoria, String nombre, TipoProducto tipo) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public TipoProducto getTipo() { return tipo; }
    public void setTipo(TipoProducto tipo) { this.tipo = tipo; }

    @Override
    public String toString() {
        return nombre;
    }
}

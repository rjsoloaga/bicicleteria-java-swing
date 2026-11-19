package modelo;

import java.math.BigDecimal;

public class EstadisticaTipo {
    private String nombreTipo;
    private int cantidadProductos;
    private int stockTotal;
    private BigDecimal precioMinimo;
    private BigDecimal precioMaximo;

    public EstadisticaTipo() {
    }

    public EstadisticaTipo(String nombreTipo, int cantidadProductos, int stockTotal, BigDecimal precioMinimo, BigDecimal precioMaximo) {
        this.nombreTipo = nombreTipo;
        this.cantidadProductos = cantidadProductos;
        this.stockTotal = stockTotal;
        this.precioMinimo = precioMinimo;
        this.precioMaximo = precioMaximo;
    }

    public String getNombreTipo() { return nombreTipo; }
    public void setNombreTipo(String nombreTipo) { this.nombreTipo = nombreTipo; }

    public int getCantidadProductos() { return cantidadProductos; }
    public void setCantidadProductos(int cantidadProductos) { this.cantidadProductos = cantidadProductos; }

    public int getStockTotal() { return stockTotal; }
    public void setStockTotal(int stockTotal) { this.stockTotal = stockTotal; }

    public BigDecimal getPrecioMinimo() { return precioMinimo; }
    public void setPrecioMinimo(BigDecimal precioMinimo) { this.precioMinimo = precioMinimo; }

    public BigDecimal getPrecioMaximo() { return precioMaximo; }
    public void setPrecioMaximo(BigDecimal precioMaximo) { this.precioMaximo = precioMaximo; }
}

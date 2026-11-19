package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;
import modelo.ConsultasProducto;
import modelo.EstadisticaTipo;
import modelo.Producto;
import modelo.Usuario;
import vista.PanelReportes;

public class ControladorReporte implements ActionListener {
    private PanelReportes vista;
    private ConsultasProducto modelo;

    public ControladorReporte(PanelReportes vista, ConsultasProducto modelo) {
        this.vista = vista;
        this.modelo = modelo;
        this.vista.botonActualizar.addActionListener(this);
    }

    public ControladorReporte(PanelReportes vista, ConsultasProducto modelo, Usuario usuarioLogueado) {
        this(vista, modelo);
    }

    public void iniciar() {
        cargarReporte();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.botonActualizar) {
            cargarReporte();
        }
    }

    public void cargarReporte() {
        // 1. Indicadores resumen en tiempo real (RF-11)
        int totalActivos = modelo.obtenerTotalProductosActivos();
        int stockTotal = modelo.obtenerStockTotalActivo();
        BigDecimal valorizacionTotal = modelo.obtenerValorizacionTotalActiva();

        vista.lblTotalProductosActivos.setText(String.valueOf(totalActivos));
        vista.lblStockTotalAcumulado.setText(String.valueOf(stockTotal));
        vista.lblValorizacionTotal.setText(String.format("$ %,.2f", valorizacionTotal));

        // 2. Tabla de alerta de productos activos con stock bajo (stock_actual <= 5)
        vista.modeloStockBajo.setRowCount(0);
        List<Producto> stockBajo = modelo.listarStockBajo();
        for (Producto p : stockBajo) {
            vista.modeloStockBajo.addRow(new Object[]{
                p.getCodigo(),
                p.getNombre(),
                p.getNombreCategoria(),
                p.getNombreTipo(),
                p.getPrecio(),
                p.getStockActual()
            });
        }

        // 3. Tabla estadística agrupada por Tipo de Producto
        vista.modeloEstadisticaTipo.setRowCount(0);
        List<EstadisticaTipo> estadisticas = modelo.obtenerEstadisticasPorTipo();
        for (EstadisticaTipo est : estadisticas) {
            vista.modeloEstadisticaTipo.addRow(new Object[]{
                est.getNombreTipo(),
                est.getCantidadProductos(),
                est.getStockTotal(),
                est.getPrecioMinimo(),
                est.getPrecioMaximo()
            });
        }
    }
}

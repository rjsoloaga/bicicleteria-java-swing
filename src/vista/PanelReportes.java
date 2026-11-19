package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class PanelReportes extends JPanel {
    public JLabel lblTotalProductosActivos;
    public JLabel lblStockTotalAcumulado;
    public JLabel lblValorizacionTotal;

    public JTable tablaStockBajo;
    public DefaultTableModel modeloStockBajo;

    public JTable tablaEstadisticaTipo;
    public DefaultTableModel modeloEstadisticaTipo;

    public JButton botonActualizar;

    public PanelReportes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior: Tarjetas de indicadores y botón Actualizar Reporte
        JPanel panelNorte = new JPanel(new BorderLayout(10, 10));

        JPanel panelIndicadores = new JPanel(new GridLayout(1, 3, 15, 0));
        lblTotalProductosActivos = new JLabel("0", SwingConstants.CENTER);
        lblStockTotalAcumulado = new JLabel("0", SwingConstants.CENTER);
        lblValorizacionTotal = new JLabel("$ 0.00", SwingConstants.CENTER);

        panelIndicadores.add(crearTarjetaIndicador("Total Productos Activos", lblTotalProductosActivos));
        panelIndicadores.add(crearTarjetaIndicador("Stock Total Acumulado", lblStockTotalAcumulado));
        panelIndicadores.add(crearTarjetaIndicador("Valorización Total Inventario", lblValorizacionTotal));

        panelNorte.add(panelIndicadores, BorderLayout.CENTER);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        botonActualizar = new JButton("Actualizar Reporte");
        panelAcciones.add(botonActualizar);
        panelNorte.add(panelAcciones, BorderLayout.SOUTH);

        add(panelNorte, BorderLayout.NORTH);

        // Panel central: las 2 tablas de reportes
        JPanel panelTablas = new JPanel(new GridLayout(2, 1, 10, 10));

        // 1. Tabla de alerta de productos activos con stock bajo (stock_actual <= 5)
        JPanel panelStockBajo = new JPanel(new BorderLayout(5, 5));
        panelStockBajo.setBorder(BorderFactory.createTitledBorder("Alerta de Stock Bajo (Productos Activos con Stock <= 5)"));
        String[] colStockBajo = {"Código", "Nombre", "Categoría", "Tipo", "Precio ($)", "Stock Actual"};
        modeloStockBajo = new DefaultTableModel(colStockBajo, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaStockBajo = new JTable(modeloStockBajo);
        tablaStockBajo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelStockBajo.add(new JScrollPane(tablaStockBajo), BorderLayout.CENTER);

        // 2. Tabla estadística agrupada por Tipo de Producto
        JPanel panelPorTipo = new JPanel(new BorderLayout(5, 5));
        panelPorTipo.setBorder(BorderFactory.createTitledBorder("Estadísticas de Inventario Agrupadas por Tipo de Producto"));
        String[] colEstadistica = {
            "Tipo de Producto",
            "Cant. Productos Activos",
            "Stock Acumulado",
            "Precio Mínimo ($)",
            "Precio Máximo ($)"
        };
        modeloEstadisticaTipo = new DefaultTableModel(colEstadistica, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaEstadisticaTipo = new JTable(modeloEstadisticaTipo);
        tablaEstadisticaTipo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelPorTipo.add(new JScrollPane(tablaEstadisticaTipo), BorderLayout.CENTER);

        panelTablas.add(panelStockBajo);
        panelTablas.add(panelPorTipo);

        add(panelTablas, BorderLayout.CENTER);
    }

    private JPanel crearTarjetaIndicador(String titulo, JLabel labelValor) {
        JPanel tarjeta = new JPanel(new BorderLayout(5, 5));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(titulo),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        labelValor.setFont(new Font("SansSerif", Font.BOLD, 20));
        tarjeta.add(labelValor, BorderLayout.CENTER);
        return tarjeta;
    }
}

package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import modelo.Producto.OpcionCategoria;

public class PanelProductos extends JPanel {
    public JPanel panelBusqueda;
    public JTextField cajaBuscar;
    public JButton botonBuscar;
    public JButton botonLimpiarFiltro;

    public JPanel panelFormulario;
    public JPanel panelBotones;

    public JTextField cajaIdProducto;
    public JTextField cajaCodigo;
    public JTextField cajaNombre;
    public JTextField cajaPrecio;
    public JTextField cajaStock;
    public JComboBox<OpcionCategoria> comboCategoria;

    public JButton botonInsertar;
    public JButton botonModificar;
    public JButton botonCambiarEstado;
    public JButton botonLimpiar;

    public JTable tablaProductos;
    public DefaultTableModel modeloTabla;

    public PanelProductos() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        cajaIdProducto = new JTextField();
        cajaIdProducto.setVisible(false);

        // Barra superior de búsqueda
        panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelBusqueda.setBorder(BorderFactory.createTitledBorder("Búsqueda por Código, Nombre o Categoría"));
        panelBusqueda.add(new JLabel("Filtrar:"));
        cajaBuscar = new JTextField(25);
        panelBusqueda.add(cajaBuscar);
        botonBuscar = new JButton("Filtrar");
        panelBusqueda.add(botonBuscar);
        botonLimpiarFiltro = new JButton("Ver Todos");
        panelBusqueda.add(botonLimpiarFiltro);

        // Panel de formulario (ABM para ADMINISTRADOR)
        panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Gestión de Producto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 0: Código y Nombre
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1;
        panelFormulario.add(new JLabel("Código:*"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.4;
        cajaCodigo = new JTextField();
        panelFormulario.add(cajaCodigo, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.1;
        panelFormulario.add(new JLabel("Nombre:*"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.4;
        cajaNombre = new JTextField();
        panelFormulario.add(cajaNombre, gbc);

        // Fila 1: Precio y Stock
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.1;
        panelFormulario.add(new JLabel("Precio ($):*"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.4;
        cajaPrecio = new JTextField();
        panelFormulario.add(cajaPrecio, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0.1;
        panelFormulario.add(new JLabel("Stock:*"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.4;
        cajaStock = new JTextField();
        panelFormulario.add(cajaStock, gbc);

        // Fila 2: Categoría
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.1;
        panelFormulario.add(new JLabel("Categoría:*"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.4;
        comboCategoria = new JComboBox<>();
        panelFormulario.add(comboCategoria, gbc);

        // Fila 3: Botones de acción
        panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        botonInsertar = new JButton("Guardar Nuevo");
        botonModificar = new JButton("Modificar");
        botonCambiarEstado = new JButton("Desactivar / Activar");
        botonLimpiar = new JButton("Limpiar");

        panelBotones.add(botonInsertar);
        panelBotones.add(botonModificar);
        panelBotones.add(botonCambiarEstado);
        panelBotones.add(botonLimpiar);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        panelFormulario.add(panelBotones, gbc);

        // Contenedor norte que agrupa búsqueda y formulario
        JPanel contenedorNorte = new JPanel(new BorderLayout(5, 5));
        contenedorNorte.add(panelBusqueda, BorderLayout.NORTH);
        contenedorNorte.add(panelFormulario, BorderLayout.CENTER);
        add(contenedorNorte, BorderLayout.NORTH);

        // Tabla inferior no editable
        String[] columnas = {"ID", "ID_CAT", "Código", "Nombre", "Categoría", "Tipo", "Precio ($)", "Stock", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Ocultar columna ID_CAT visualmente
        tablaProductos.getColumnModel().getColumn(1).setMinWidth(0);
        tablaProductos.getColumnModel().getColumn(1).setMaxWidth(0);
        tablaProductos.getColumnModel().getColumn(1).setWidth(0);

        add(new JScrollPane(tablaProductos), BorderLayout.CENTER);
    }
}

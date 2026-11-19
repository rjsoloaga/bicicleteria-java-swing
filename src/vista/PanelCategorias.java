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
import modelo.TipoProducto;

public class PanelCategorias extends JPanel {
    public JTextField cajaIdCategoria;
    public JTextField cajaNombre;
    public JComboBox<TipoProducto> comboTipo;

    public JButton botonInsertar;
    public JButton botonModificar;
    public JButton botonEliminar;
    public JButton botonLimpiar;

    public JTable tablaCategorias;
    public DefaultTableModel modeloTabla;

    public PanelCategorias() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        cajaIdCategoria = new JTextField();
        cajaIdCategoria.setVisible(false);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Gestión de Categorías de Producto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 0: Nombre de la Categoría y Tipo de Producto
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.15;
        panelFormulario.add(new JLabel("Nombre Categoría:*"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.35;
        cajaNombre = new JTextField(20);
        panelFormulario.add(cajaNombre, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.15;
        panelFormulario.add(new JLabel("Tipo de Producto:*"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.35;
        comboTipo = new JComboBox<>();
        panelFormulario.add(comboTipo, gbc);

        // Fila 1: Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        botonInsertar = new JButton("Guardar Nuevo");
        botonModificar = new JButton("Modificar");
        botonEliminar = new JButton("Eliminar");
        botonLimpiar = new JButton("Limpiar");

        panelBotones.add(botonInsertar);
        panelBotones.add(botonModificar);
        panelBotones.add(botonEliminar);
        panelBotones.add(botonLimpiar);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 4;
        panelFormulario.add(panelBotones, gbc);

        add(panelFormulario, BorderLayout.NORTH);

        // Tabla inferior no editable mostrando ID, Categoría y Tipo asociado
        String[] columnas = {"ID", "Categoría", "Tipo Asociado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaCategorias = new JTable(modeloTabla);
        tablaCategorias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(new JScrollPane(tablaCategorias), BorderLayout.CENTER);
    }
}

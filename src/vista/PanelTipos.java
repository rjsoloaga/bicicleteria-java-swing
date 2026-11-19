package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class PanelTipos extends JPanel {
    public JTextField cajaIdTipo;
    public JTextField cajaNombre;

    public JButton botonInsertar;
    public JButton botonModificar;
    public JButton botonEliminar;
    public JButton botonLimpiar;

    public JTable tablaTipos;
    public DefaultTableModel modeloTabla;

    public PanelTipos() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        cajaIdTipo = new JTextField();
        cajaIdTipo.setVisible(false);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Gestión de Tipos de Producto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 0: Nombre del Tipo
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
        panelFormulario.add(new JLabel("Nombre del Tipo:*"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.8;
        cajaNombre = new JTextField(25);
        panelFormulario.add(cajaNombre, gbc);

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

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        panelFormulario.add(panelBotones, gbc);

        add(panelFormulario, BorderLayout.NORTH);

        // Tabla inferior no editable
        String[] columnas = {"ID", "Nombre del Tipo"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaTipos = new JTable(modeloTabla);
        tablaTipos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(new JScrollPane(tablaTipos), BorderLayout.CENTER);
    }
}

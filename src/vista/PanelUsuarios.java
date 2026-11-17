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
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import modelo.Rol;

public class PanelUsuarios extends JPanel {
    public JTextField cajaIdUsuario;
    public JTextField cajaIdPersona;
    public JTextField cajaNombre;
    public JTextField cajaApellido;
    public JTextField cajaDni;
    public JTextField cajaTelefono;
    public JTextField cajaEmail;
    public JTextField cajaNombreUsuario;
    public JPasswordField cajaClave;
    public JComboBox<Rol> comboRol;

    public JButton botonInsertar;
    public JButton botonModificar;
    public JButton botonCambiarEstado;
    public JButton botonLimpiar;

    public JTable tablaUsuarios;
    public DefaultTableModel modeloTabla;

    public PanelUsuarios() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        cajaIdUsuario = new JTextField();
        cajaIdPersona = new JTextField();
        cajaIdUsuario.setVisible(false);
        cajaIdPersona.setVisible(false);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos de Persona y Cuenta de Usuario"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 0: Nombre y Apellido
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1;
        panelFormulario.add(new JLabel("Nombre:*"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.4;
        cajaNombre = new JTextField();
        panelFormulario.add(cajaNombre, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.1;
        panelFormulario.add(new JLabel("Apellido:*"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.4;
        cajaApellido = new JTextField();
        panelFormulario.add(cajaApellido, gbc);

        // Fila 1: DNI y Teléfono
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.1;
        panelFormulario.add(new JLabel("DNI:*"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.4;
        cajaDni = new JTextField();
        panelFormulario.add(cajaDni, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0.1;
        panelFormulario.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.4;
        cajaTelefono = new JTextField();
        panelFormulario.add(cajaTelefono, gbc);

        // Fila 2: Email y Usuario
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.1;
        panelFormulario.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.4;
        cajaEmail = new JTextField();
        panelFormulario.add(cajaEmail, gbc);

        gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0.1;
        panelFormulario.add(new JLabel("Usuario:*"), gbc);
        gbc.gridx = 3; gbc.gridy = 2; gbc.weightx = 0.4;
        cajaNombreUsuario = new JTextField();
        panelFormulario.add(cajaNombreUsuario, gbc);

        // Fila 3: Contraseña y Rol
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.1;
        panelFormulario.add(new JLabel("Clave:*"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 0.4;
        cajaClave = new JPasswordField();
        cajaClave.setToolTipText("Obligatoria al crear. Dejar vacía al modificar si no desea cambiarla.");
        panelFormulario.add(cajaClave, gbc);

        gbc.gridx = 2; gbc.gridy = 3; gbc.weightx = 0.1;
        panelFormulario.add(new JLabel("Rol:*"), gbc);
        gbc.gridx = 3; gbc.gridy = 3; gbc.weightx = 0.4;
        comboRol = new JComboBox<>();
        panelFormulario.add(comboRol, gbc);

        // Fila 4: Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        botonInsertar = new JButton("Guardar Nuevo");
        botonModificar = new JButton("Modificar");
        botonCambiarEstado = new JButton("Activar / Desactivar");
        botonLimpiar = new JButton("Limpiar");

        panelBotones.add(botonInsertar);
        panelBotones.add(botonModificar);
        panelBotones.add(botonCambiarEstado);
        panelBotones.add(botonLimpiar);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4;
        panelFormulario.add(panelBotones, gbc);

        add(panelFormulario, BorderLayout.NORTH);

        // Tabla inferior
        String[] columnas = {"ID", "ID_PER", "Usuario", "Nombre", "Apellido", "DNI", "Teléfono", "Email", "Rol", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Ocultar columna ID_PER visualmente
        tablaUsuarios.getColumnModel().getColumn(1).setMinWidth(0);
        tablaUsuarios.getColumnModel().getColumn(1).setMaxWidth(0);
        tablaUsuarios.getColumnModel().getColumn(1).setWidth(0);

        add(new JScrollPane(tablaUsuarios), BorderLayout.CENTER);
    }
}
package vista;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class VistaLogin extends JFrame {
    public JTextField cajaUsuario;
    public JPasswordField cajaClave;
    public JButton botonIngresar;

    public VistaLogin() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(380, 240);
        setResizable(false);
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("Sistema Bicicletería - Acceso", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 10));
        add(titulo, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        panelCentro.add(new JLabel("Usuario:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.7;
        cajaUsuario = new JTextField(15);
        panelCentro.add(cajaUsuario, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        panelCentro.add(new JLabel("Contraseña:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.7;
        cajaClave = new JPasswordField(15);
        panelCentro.add(cajaClave, gbc);

        add(panelCentro, BorderLayout.CENTER);

        JPanel panelSur = new JPanel();
        panelSur.setBorder(BorderFactory.createEmptyBorder(0, 10, 15, 10));
        botonIngresar = new JButton("Ingresar");
        panelSur.add(botonIngresar);
        add(panelSur, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(botonIngresar);
    }
}
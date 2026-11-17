package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.JOptionPane;
import modelo.ConsultasUsuario;
import modelo.Usuario;
import vista.PanelUsuarios;
import vista.VistaLogin;
import vista.VistaMenuPrincipal;

public class ControladorLogin implements ActionListener {
    private VistaLogin vista;
    private ConsultasUsuario modelo;

    public ControladorLogin(VistaLogin vista, ConsultasUsuario modelo) {
        this.vista = vista;
        this.modelo = modelo;
        vista.botonIngresar.addActionListener(this);
    }

    public void iniciar() {
        vista.setTitle("Ingreso - Sistema Bicicletería");
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        if (evento.getSource() == vista.botonIngresar) {
            ingresar();
        }
    }

    private void ingresar() {
        String nombreUsuario = vista.cajaUsuario.getText().trim();
        char[] clave = vista.cajaClave.getPassword();
        try {
            if (nombreUsuario.isEmpty() || clave.length == 0) {
                JOptionPane.showMessageDialog(vista, "Debe ingresar usuario y contraseña.");
                return;
            }
            Usuario usuario = modelo.autenticar(nombreUsuario, clave);
            if (usuario == null) {
                JOptionPane.showMessageDialog(vista, "Usuario, contraseña o estado de cuenta incorrectos.");
                vista.cajaClave.setText("");
                return;
            }

            PanelUsuarios panelUsuarios = new PanelUsuarios();
            if (usuario.esAdministrador()) {
                ControladorUsuario ctrlUsuario = new ControladorUsuario(panelUsuarios, modelo, usuario);
                ctrlUsuario.iniciar();
            }

            VistaMenuPrincipal menu = new VistaMenuPrincipal(usuario, panelUsuarios);
            menu.botonCerrarSesion.addActionListener(e -> {
                menu.dispose();
                VistaLogin nuevoLogin = new VistaLogin();
                new ControladorLogin(nuevoLogin, modelo).iniciar();
            });

            vista.dispose();
            menu.setVisible(true);
        } finally {
            Arrays.fill(clave, '\0');
        }
    }
}
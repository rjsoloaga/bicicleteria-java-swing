package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import modelo.Usuario;

public class VistaMenuPrincipal extends JFrame {
    public JTabbedPane pestanas;
    public JButton botonCerrarSesion;

    public VistaMenuPrincipal(Usuario usuarioLogueado, PanelUsuarios panelUsuarios) {
        setTitle("Bicicletería - Sesión: " + usuarioLogueado.getNombreUsuario() + " [" + usuarioLogueado.getRol().getNombre() + "]");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Barra superior con datos de sesión y botón Salir
        JPanel barraSuperior = new JPanel(new BorderLayout());
        barraSuperior.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JLabel lblBienvenida = new JLabel("Usuario: " + usuarioLogueado.getPersona().getNombre() + " "
                + usuarioLogueado.getPersona().getApellido() + " (" + usuarioLogueado.getRol().getNombre() + ")");
        lblBienvenida.setFont(new Font("SansSerif", Font.BOLD, 13));
        barraSuperior.add(lblBienvenida, BorderLayout.WEST);

        JPanel panelDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        botonCerrarSesion = new JButton("Cerrar Sesión");
        panelDerecha.add(botonCerrarSesion);
        barraSuperior.add(panelDerecha, BorderLayout.EAST);

        add(barraSuperior, BorderLayout.NORTH);

        // Pestañas de módulos
        pestanas = new JTabbedPane();

        // Módulos visibles para todos los roles (Juan Pablo los reemplazará por sus paneles reales)
        pestanas.addTab("Catálogo de Productos", crearPlaceholder("Módulo Productos (Juan Pablo Katona)"));
        pestanas.addTab("Reportes e Inventario", crearPlaceholder("Módulo Reportes (Juan Pablo Katona)"));

        // Módulos exclusivos de ADMINISTRADOR
        if (usuarioLogueado.esAdministrador()) {
            pestanas.addTab("Categorías", crearPlaceholder("Módulo Categorías (Macarena Rico)"));
            pestanas.addTab("Tipos de Producto", crearPlaceholder("Módulo Tipos (Macarena Rico)"));
            pestanas.addTab("Gestión de Usuarios", panelUsuarios);
        }

        add(pestanas, BorderLayout.CENTER);
    }

    private JPanel crearPlaceholder(String texto) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(texto, JLabel.CENTER);
        label.setFont(new Font("SansSerif", Font.PLAIN, 15));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}
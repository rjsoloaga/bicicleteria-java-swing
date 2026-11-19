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

    public VistaMenuPrincipal(
            Usuario usuarioLogueado,
            PanelProductos panelProductos,
            PanelReportes panelReportes,
            PanelCategorias panelCategorias,
            PanelTipos panelTipos,
            PanelUsuarios panelUsuarios) {

        setTitle("Bicicletería - Sesión: " + usuarioLogueado.getNombreUsuario() + " [" + usuarioLogueado.getRol().getNombre() + "]");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 680);
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

        // Módulos visibles para todos los roles (ADMINISTRADOR y VENDEDOR)
        pestanas.addTab("Catálogo de Productos", panelProductos);
        pestanas.addTab("Reportes e Inventario", panelReportes);

        // Módulos exclusivos de ADMINISTRADOR
        if (usuarioLogueado.esAdministrador()) {
            pestanas.addTab("Categorías", panelCategorias);
            pestanas.addTab("Tipos de Producto", panelTipos);
            pestanas.addTab("Gestión de Usuarios", panelUsuarios);
        }

        add(pestanas, BorderLayout.CENTER);
    }
}
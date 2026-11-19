package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.JOptionPane;
import modelo.ConsultasCategoria;
import modelo.ConsultasProducto;
import modelo.ConsultasTipo;
import modelo.ConsultasUsuario;
import modelo.Usuario;
import vista.PanelCategorias;
import vista.PanelProductos;
import vista.PanelReportes;
import vista.PanelTipos;
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

            // 1. Módulos visibles para todos los roles (Productos y Reportes)
            ConsultasProducto consultasProducto = new ConsultasProducto();
            PanelProductos panelProductos = new PanelProductos();
            ControladorProducto ctrlProducto = new ControladorProducto(panelProductos, consultasProducto, usuario);
            ctrlProducto.iniciar();

            PanelReportes panelReportes = new PanelReportes();
            ControladorReporte ctrlReporte = new ControladorReporte(panelReportes, consultasProducto);
            ctrlReporte.iniciar();

            // 2. Módulos exclusivos de ADMINISTRADOR (Tipos, Categorías y Usuarios)
            PanelTipos panelTipos = new PanelTipos();
            PanelCategorias panelCategorias = new PanelCategorias();
            PanelUsuarios panelUsuarios = new PanelUsuarios();

            if (usuario.esAdministrador()) {
                ConsultasTipo consultasTipo = new ConsultasTipo();
                ControladorTipo ctrlTipo = new ControladorTipo(panelTipos, consultasTipo);
                ctrlTipo.iniciar();

                ConsultasCategoria consultasCategoria = new ConsultasCategoria();
                ControladorCategoria ctrlCategoria = new ControladorCategoria(panelCategorias, consultasCategoria, consultasTipo);
                ctrlCategoria.iniciar();

                ControladorUsuario ctrlUsuario = new ControladorUsuario(panelUsuarios, modelo, usuario);
                ctrlUsuario.iniciar();
            }

            // 3. Armar ventana principal e integrar pestañas
            VistaMenuPrincipal menu = new VistaMenuPrincipal(
                    usuario,
                    panelProductos,
                    panelReportes,
                    panelCategorias,
                    panelTipos,
                    panelUsuarios
            );

            // Actualizar datos dinámicamente al cambiar de pestaña
            menu.pestanas.addChangeListener(e -> {
                java.awt.Component seleccionada = menu.pestanas.getSelectedComponent();
                if (seleccionada == panelProductos) {
                    if (usuario.esAdministrador()) {
                        ctrlProducto.cargarCategorias();
                    }
                    ctrlProducto.listarProductos();
                } else if (seleccionada == panelReportes) {
                    ctrlReporte.cargarReporte();
                }
            });

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
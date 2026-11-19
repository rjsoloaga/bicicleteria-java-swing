package vista;

import modelo.Usuario;

public class VistaMenuPrincipal extends javax.swing.JFrame {

    public VistaMenuPrincipal() {
        initComponents();
        setLocationRelativeTo(null);
    }

    public VistaMenuPrincipal(Usuario usuario,
                              PanelProductos panelProductos,
                              PanelReportes panelReportes,
                              PanelCategorias panelCategorias,
                              PanelTipos panelTipos,
                              PanelUsuarios panelUsuarios) {
        initComponents();
        setLocationRelativeTo(null);

        boolean esAdmin = usuario.esAdministrador();
        etiquetaBienvenida.setText("Usuario: " + usuario.getPersona().getNombre() + " " + usuario.getPersona().getApellido() + " (" + usuario.getNombreUsuario() + ")");
        etiquetaRol.setText("Rol: " + usuario.getRol().getNombre());

        pestanas.addTab("Catálogo de Productos", panelProductos);
        pestanas.addTab("Reportes e Inventario", panelReportes);

        if (esAdmin) {
            pestanas.addTab("Tipos de Producto", panelTipos);
            pestanas.addTab("Categorías", panelCategorias);
            pestanas.addTab("Gestión de Usuarios", panelUsuarios);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        etiquetaBienvenida = new javax.swing.JLabel();
        etiquetaRol = new javax.swing.JLabel();
        botonCerrarSesion = new javax.swing.JButton();
        pestanas = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gestión de Bicicletería - Menú Principal");

        etiquetaBienvenida.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        etiquetaBienvenida.setText("Usuario: Administrador");

        etiquetaRol.setText("Rol: ADMINISTRADOR");

        botonCerrarSesion.setText("Cerrar Sesión");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pestanas, javax.swing.GroupLayout.DEFAULT_SIZE, 980, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(etiquetaBienvenida)
                        .addGap(24, 24, 24)
                        .addComponent(etiquetaRol)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(botonCerrarSesion)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(etiquetaBienvenida)
                    .addComponent(etiquetaRol)
                    .addComponent(botonCerrarSesion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pestanas, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton botonCerrarSesion;
    public javax.swing.JLabel etiquetaBienvenida;
    public javax.swing.JLabel etiquetaRol;
    public javax.swing.JTabbedPane pestanas;
    // End of variables declaration//GEN-END:variables
}

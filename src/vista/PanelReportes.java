package vista;

import javax.swing.table.DefaultTableModel;

public class PanelReportes extends javax.swing.JPanel {

    public DefaultTableModel modeloEstadisticaTipo;
    public DefaultTableModel modeloStockBajo;

    public PanelReportes() {
        initComponents();
        modeloEstadisticaTipo = new DefaultTableModel(new Object[]{"Tipo de Producto", "Cant. Productos", "Stock Total", "Precio Mínimo", "Precio Máximo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaEstadisticas.setModel(modeloEstadisticaTipo);

        modeloStockBajo = new DefaultTableModel(new Object[]{"Código", "Producto", "Categoría", "Stock Actual"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaStockBajo.setModel(modeloStockBajo);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelP = new javax.swing.JLabel();
        lblTotalProductosActivos = new javax.swing.JLabel();
        jLabelS = new javax.swing.JLabel();
        lblStockTotalAcumulado = new javax.swing.JLabel();
        jLabelV = new javax.swing.JLabel();
        lblValorizacionTotal = new javax.swing.JLabel();
        botonActualizar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaEstadisticas = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaStockBajo = new javax.swing.JTable();

        jLabelP.setText("Productos Activos:");

        lblTotalProductosActivos.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        lblTotalProductosActivos.setText("0");

        jLabelS.setText("Stock Total:");

        lblStockTotalAcumulado.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        lblStockTotalAcumulado.setText("0");

        jLabelV.setText("Valorización:");

        lblValorizacionTotal.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        lblValorizacionTotal.setText("$ 0.00");

        botonActualizar.setText("Actualizar Reportes");

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel1.setText("Estadísticas por Tipo de Producto (COUNT, SUM, MIN, MAX):");

        tablaEstadisticas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"Tipo de Producto", "Cant. Productos", "Stock Total", "Precio Mínimo", "Precio Máximo"}
        ));
        jScrollPane1.setViewportView(tablaEstadisticas);

        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel2.setText("Productos Activos con Stock Bajo (<= 5 unidades):");

        tablaStockBajo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"Código", "Producto", "Categoría", "Stock Actual"}
        ));
        jScrollPane2.setViewportView(tablaStockBajo);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotalProductosActivos)
                        .addGap(30, 30, 30)
                        .addComponent(jLabelS)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblStockTotalAcumulado)
                        .addGap(30, 30, 30)
                        .addComponent(jLabelV)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblValorizacionTotal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 120, Short.MAX_VALUE)
                        .addComponent(botonActualizar))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelP)
                    .addComponent(lblTotalProductosActivos)
                    .addComponent(jLabelS)
                    .addComponent(lblStockTotalAcumulado)
                    .addComponent(jLabelV)
                    .addComponent(lblValorizacionTotal)
                    .addComponent(botonActualizar))
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton botonActualizar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelP;
    private javax.swing.JLabel jLabelS;
    private javax.swing.JLabel jLabelV;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JLabel lblStockTotalAcumulado;
    public javax.swing.JLabel lblTotalProductosActivos;
    public javax.swing.JLabel lblValorizacionTotal;
    public javax.swing.JTable tablaEstadisticas;
    public javax.swing.JTable tablaStockBajo;
    // End of variables declaration//GEN-END:variables
}

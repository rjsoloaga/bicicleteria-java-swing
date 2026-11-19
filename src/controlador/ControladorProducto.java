package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.JOptionPane;
import modelo.ConsultasProducto;
import modelo.Producto;
import modelo.Producto.OpcionCategoria;
import modelo.Usuario;
import vista.PanelProductos;

public class ControladorProducto implements ActionListener {
    private PanelProductos vista;
    private ConsultasProducto modelo;
    private Usuario usuarioLogueado;

    public ControladorProducto(PanelProductos vista, ConsultasProducto modelo, Usuario usuarioLogueado) {
        this.vista = vista;
        this.modelo = modelo;
        this.usuarioLogueado = usuarioLogueado;

        this.vista.botonInsertar.addActionListener(this);
        this.vista.botonModificar.addActionListener(this);
        this.vista.botonCambiarEstado.addActionListener(this);
        this.vista.botonLimpiar.addActionListener(this);
        this.vista.botonBuscar.addActionListener(this);
        this.vista.botonLimpiarFiltro.addActionListener(this);
        this.vista.cajaBuscar.addActionListener(this);

        this.vista.cajaBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                listarProductos();
            }
        });

        this.vista.tablaProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && esAdmin()) {
                cargarSeleccionado();
            }
        });
    }

    private boolean esAdmin() {
        return usuarioLogueado != null && usuarioLogueado.esAdministrador();
    }

    public void iniciar() {
        if (esAdmin()) {
            vista.panelFormulario.setVisible(true);
            vista.panelBotones.setVisible(true);
            cargarCategorias();
            limpiarCampos();
        } else {
            // RF-08 / AC-05: VENDEDOR solo visualiza productos activos sin controles de ABM
            vista.panelFormulario.setVisible(false);
            vista.panelBotones.setVisible(false);
            vista.botonInsertar.setEnabled(false);
            vista.botonModificar.setEnabled(false);
            vista.botonCambiarEstado.setEnabled(false);
            vista.botonLimpiar.setEnabled(false);
        }
        listarProductos();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object fuente = e.getSource();
        if (fuente == vista.botonInsertar) {
            insertar();
        } else if (fuente == vista.botonModificar) {
            modificar();
        } else if (fuente == vista.botonCambiarEstado) {
            cambiarEstado();
        } else if (fuente == vista.botonLimpiar) {
            limpiarCampos();
        } else if (fuente == vista.botonBuscar || fuente == vista.cajaBuscar) {
            listarProductos();
        } else if (fuente == vista.botonLimpiarFiltro) {
            vista.cajaBuscar.setText("");
            listarProductos();
        }
    }

    public void cargarCategorias() {
        vista.comboCategoria.removeAllItems();
        for (OpcionCategoria cat : modelo.listarCategoriasCombo()) {
            vista.comboCategoria.addItem(cat);
        }
    }

    public void listarProductos() {
        vista.modeloTabla.setRowCount(0);
        boolean soloActivos = !esAdmin();
        String filtro = vista.cajaBuscar.getText().trim();

        List<Producto> lista = modelo.listar(soloActivos, filtro);
        for (Producto p : lista) {
            vista.modeloTabla.addRow(new Object[]{
                p.getIdProducto(),
                p.getIdCategoria(),
                p.getCodigo(),
                p.getNombre(),
                p.getNombreCategoria(),
                p.getNombreTipo(),
                p.getPrecio(),
                p.getStockActual(),
                p.isActivo() ? "ACTIVO" : "INACTIVO"
            });
        }
    }

    private void insertar() {
        if (!esAdmin()) return;

        Producto nuevo = construirDesdeFormulario(0);
        if (nuevo == null) return;
        nuevo.setActivo(true);

        if (modelo.insertar(nuevo)) {
            JOptionPane.showMessageDialog(vista, "Producto registrado correctamente.");
            limpiarCampos();
            listarProductos();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al registrar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificar() {
        if (!esAdmin()) return;

        if (vista.cajaIdProducto.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Seleccione un producto de la tabla para modificar.");
            return;
        }
        int idProducto = Integer.parseInt(vista.cajaIdProducto.getText());
        Producto editado = construirDesdeFormulario(idProducto);
        if (editado == null) return;

        if (modelo.modificar(editado)) {
            JOptionPane.showMessageDialog(vista, "Producto actualizado correctamente.");
            limpiarCampos();
            listarProductos();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al actualizar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cambiarEstado() {
        if (!esAdmin()) return;

        int fila = vista.tablaProductos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(vista, "Seleccione un producto de la tabla.");
            return;
        }
        int idProducto = Integer.parseInt(vista.modeloTabla.getValueAt(fila, 0).toString());
        String estadoActual = vista.modeloTabla.getValueAt(fila, 8).toString();
        boolean nuevoEstado = !"ACTIVO".equals(estadoActual);

        if (modelo.cambiarEstado(idProducto, nuevoEstado)) {
            JOptionPane.showMessageDialog(vista,
                    "Estado del producto actualizado a " + (nuevoEstado ? "ACTIVO" : "INACTIVO") + ".");
            limpiarCampos();
            listarProductos();
        } else {
            JOptionPane.showMessageDialog(vista, "No se pudo cambiar el estado del producto.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Producto construirDesdeFormulario(int idProducto) {
        String codigo = vista.cajaCodigo.getText().trim();
        String nombre = vista.cajaNombre.getText().trim();
        String precioTexto = vista.cajaPrecio.getText().trim().replace(",", ".");
        String stockTexto = vista.cajaStock.getText().trim();
        OpcionCategoria categoriaSeleccionada = (OpcionCategoria) vista.comboCategoria.getSelectedItem();

        if (codigo.isEmpty() || nombre.isEmpty() || precioTexto.isEmpty() || stockTexto.isEmpty() || categoriaSeleccionada == null) {
            JOptionPane.showMessageDialog(vista, "Complete todos los campos obligatorios (*).");
            return null;
        }

        BigDecimal precio;
        try {
            precio = new BigDecimal(precioTexto);
            if (precio.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(vista, "El precio debe ser un valor decimal mayor o igual a 0.",
                        "Precio inválido", JOptionPane.WARNING_MESSAGE);
                return null;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "Ingrese un precio numérico válido (ej. 15000.50).",
                    "Precio inválido", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        int stock;
        try {
            stock = Integer.parseInt(stockTexto);
            if (stock < 0) {
                JOptionPane.showMessageDialog(vista, "El stock debe ser un número entero mayor o igual a 0.",
                        "Stock inválido", JOptionPane.WARNING_MESSAGE);
                return null;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "Ingrese un stock entero válido (ej. 10).",
                    "Stock inválido", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        if (modelo.existeCodigo(codigo, idProducto)) {
            JOptionPane.showMessageDialog(vista, "El código '" + codigo + "' ya pertenece a otro producto.",
                    "Código duplicado", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        Producto p = new Producto();
        p.setIdProducto(idProducto);
        p.setCodigo(codigo);
        p.setNombre(nombre);
        p.setPrecio(precio);
        p.setStockActual(stock);
        p.setIdCategoria(categoriaSeleccionada.getId());
        p.setNombreCategoria(categoriaSeleccionada.getNombre());
        return p;
    }

    private void cargarSeleccionado() {
        int fila = vista.tablaProductos.getSelectedRow();
        if (fila < 0) return;

        vista.cajaIdProducto.setText(vista.modeloTabla.getValueAt(fila, 0).toString());
        int idCategoria = Integer.parseInt(vista.modeloTabla.getValueAt(fila, 1).toString());
        vista.cajaCodigo.setText(vista.modeloTabla.getValueAt(fila, 2).toString());
        vista.cajaNombre.setText(vista.modeloTabla.getValueAt(fila, 3).toString());
        vista.cajaPrecio.setText(vista.modeloTabla.getValueAt(fila, 6).toString());
        vista.cajaStock.setText(vista.modeloTabla.getValueAt(fila, 7).toString());

        for (int i = 0; i < vista.comboCategoria.getItemCount(); i++) {
            OpcionCategoria item = vista.comboCategoria.getItemAt(i);
            if (item.getId() == idCategoria) {
                vista.comboCategoria.setSelectedIndex(i);
                break;
            }
        }

        vista.botonInsertar.setEnabled(false);
        vista.botonModificar.setEnabled(true);
        vista.botonCambiarEstado.setEnabled(true);
    }

    public void limpiarCampos() {
        vista.cajaIdProducto.setText("");
        vista.cajaCodigo.setText("");
        vista.cajaNombre.setText("");
        vista.cajaPrecio.setText("");
        vista.cajaStock.setText("");
        if (vista.comboCategoria.getItemCount() > 0) {
            vista.comboCategoria.setSelectedIndex(0);
        }
        vista.tablaProductos.clearSelection();
        vista.botonInsertar.setEnabled(true);
        vista.botonModificar.setEnabled(false);
        vista.botonCambiarEstado.setEnabled(false);
    }
}

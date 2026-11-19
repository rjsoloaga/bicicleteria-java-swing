package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import javax.swing.JOptionPane;
import modelo.CategoriaProducto;
import modelo.ConsultasCategoria;
import modelo.ConsultasTipo;
import modelo.TipoProducto;
import modelo.Usuario;
import vista.PanelCategorias;

public class ControladorCategoria implements ActionListener {
    private PanelCategorias vista;
    private ConsultasCategoria modelo;
    private ConsultasTipo modeloTipo;

    public ControladorCategoria(PanelCategorias vista, ConsultasCategoria modelo, ConsultasTipo modeloTipo) {
        this.vista = vista;
        this.modelo = modelo;
        this.modeloTipo = modeloTipo != null ? modeloTipo : new ConsultasTipo();

        this.vista.botonInsertar.addActionListener(this);
        this.vista.botonModificar.addActionListener(this);
        this.vista.botonEliminar.addActionListener(this);
        this.vista.botonLimpiar.addActionListener(this);

        this.vista.tablaCategorias.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccionado();
            }
        });

        // Recargar combo de Tipos dinámicamente al mostrar la pestaña
        this.vista.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                cargarTipos();
            }
        });
    }

    public ControladorCategoria(PanelCategorias vista, ConsultasCategoria modelo) {
        this(vista, modelo, new ConsultasTipo());
    }

    public ControladorCategoria(PanelCategorias vista, ConsultasCategoria modelo, Usuario usuarioLogueado) {
        this(vista, modelo, new ConsultasTipo());
    }

    public ControladorCategoria(PanelCategorias vista, ConsultasCategoria modelo, ConsultasTipo modeloTipo, Usuario usuarioLogueado) {
        this(vista, modelo, modeloTipo);
    }

    public void iniciar() {
        cargarTipos();
        listarCategorias();
        limpiarCampos();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.botonInsertar) {
            insertar();
        } else if (e.getSource() == vista.botonModificar) {
            modificar();
        } else if (e.getSource() == vista.botonEliminar) {
            eliminar();
        } else if (e.getSource() == vista.botonLimpiar) {
            limpiarCampos();
        }
    }

    public void cargarTipos() {
        TipoProducto seleccionadoAnterior = (TipoProducto) vista.comboTipo.getSelectedItem();
        vista.comboTipo.removeAllItems();
        List<TipoProducto> tipos = modeloTipo.listar();
        for (TipoProducto t : tipos) {
            vista.comboTipo.addItem(t);
            if (seleccionadoAnterior != null && t.getIdTipo() == seleccionadoAnterior.getIdTipo()) {
                vista.comboTipo.setSelectedItem(t);
            }
        }
    }

    public void listarCategorias() {
        vista.modeloTabla.setRowCount(0);
        List<CategoriaProducto> lista = modelo.listar();
        for (CategoriaProducto c : lista) {
            vista.modeloTabla.addRow(new Object[]{
                c.getIdCategoria(),
                c.getNombre(),
                c.getTipo() != null ? c.getTipo().getNombre() : ""
            });
        }
    }

    private void insertar() {
        CategoriaProducto nueva = construirDesdeFormulario(0);
        if (nueva == null) return;

        if (modelo.insertar(nueva)) {
            JOptionPane.showMessageDialog(vista, "Categoría registrada correctamente.");
            limpiarCampos();
            listarCategorias();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al registrar la categoría.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificar() {
        if (vista.cajaIdCategoria.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Seleccione una categoría de la tabla para modificar.");
            return;
        }
        int idCategoria = Integer.parseInt(vista.cajaIdCategoria.getText());
        CategoriaProducto editada = construirDesdeFormulario(idCategoria);
        if (editada == null) return;

        if (modelo.modificar(editada)) {
            JOptionPane.showMessageDialog(vista, "Categoría actualizada correctamente.");
            limpiarCampos();
            listarCategorias();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al actualizar la categoría.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        if (vista.cajaIdCategoria.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Seleccione una categoría de la tabla para eliminar.");
            return;
        }
        int idCategoria = Integer.parseInt(vista.cajaIdCategoria.getText());

        // RF-05 / AC-06: Validar integridad referencial antes de eliminar
        if (modelo.tieneProductosAsociados(idCategoria)) {
            JOptionPane.showMessageDialog(vista,
                    "No se puede eliminar la Categoría porque tiene Productos asociados.",
                    "Error de Integridad Referencial",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (modelo.eliminar(idCategoria)) {
            JOptionPane.showMessageDialog(vista, "Categoría eliminada correctamente.");
            limpiarCampos();
            listarCategorias();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al eliminar la categoría.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private CategoriaProducto construirDesdeFormulario(int idCategoria) {
        String nombre = vista.cajaNombre.getText().trim();
        TipoProducto tipoSeleccionado = (TipoProducto) vista.comboTipo.getSelectedItem();

        // RF-05 / AC-02: Validar nombre no vacío, Tipo válido seleccionado y nombre no duplicado
        if (nombre.isEmpty() || tipoSeleccionado == null) {
            JOptionPane.showMessageDialog(vista, "Complete el nombre de la categoría y seleccione un Tipo de Producto (*).");
            return null;
        }
        if (modelo.existeNombre(nombre, idCategoria)) {
            JOptionPane.showMessageDialog(vista,
                    "Ya existe una Categoría con el nombre '" + nombre + "'.",
                    "Nombre duplicado",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }

        return new CategoriaProducto(idCategoria, nombre, tipoSeleccionado);
    }

    private void cargarSeleccionado() {
        int fila = vista.tablaCategorias.getSelectedRow();
        if (fila < 0) return;

        vista.cajaIdCategoria.setText(vista.modeloTabla.getValueAt(fila, 0).toString());
        vista.cajaNombre.setText(vista.modeloTabla.getValueAt(fila, 1).toString());
        String nombreTipo = vista.modeloTabla.getValueAt(fila, 2).toString();

        for (int i = 0; i < vista.comboTipo.getItemCount(); i++) {
            TipoProducto item = vista.comboTipo.getItemAt(i);
            if (item.getNombre().equalsIgnoreCase(nombreTipo)) {
                vista.comboTipo.setSelectedIndex(i);
                break;
            }
        }

        vista.botonInsertar.setEnabled(false);
        vista.botonModificar.setEnabled(true);
        vista.botonEliminar.setEnabled(true);
    }

    public void limpiarCampos() {
        vista.cajaIdCategoria.setText("");
        vista.cajaNombre.setText("");
        if (vista.comboTipo.getItemCount() > 0) {
            vista.comboTipo.setSelectedIndex(0);
        }
        vista.tablaCategorias.clearSelection();
        vista.botonInsertar.setEnabled(true);
        vista.botonModificar.setEnabled(false);
        vista.botonEliminar.setEnabled(false);
    }
}

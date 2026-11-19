package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import modelo.ConsultasTipo;
import modelo.TipoProducto;
import modelo.Usuario;
import vista.PanelTipos;

public class ControladorTipo implements ActionListener {
    private PanelTipos vista;
    private ConsultasTipo modelo;

    public ControladorTipo(PanelTipos vista, ConsultasTipo modelo) {
        this.vista = vista;
        this.modelo = modelo;

        this.vista.botonInsertar.addActionListener(this);
        this.vista.botonModificar.addActionListener(this);
        this.vista.botonEliminar.addActionListener(this);
        this.vista.botonLimpiar.addActionListener(this);

        this.vista.tablaTipos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccionado();
            }
        });
    }

    public ControladorTipo(PanelTipos vista, ConsultasTipo modelo, Usuario usuarioLogueado) {
        this(vista, modelo);
    }

    public void iniciar() {
        listarTipos();
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

    public void listarTipos() {
        vista.modeloTabla.setRowCount(0);
        List<TipoProducto> lista = modelo.listar();
        for (TipoProducto t : lista) {
            vista.modeloTabla.addRow(new Object[]{
                t.getIdTipo(),
                t.getNombre()
            });
        }
    }

    private void insertar() {
        TipoProducto nuevo = construirDesdeFormulario(0);
        if (nuevo == null) return;

        if (modelo.insertar(nuevo)) {
            JOptionPane.showMessageDialog(vista, "Tipo de producto registrado correctamente.");
            limpiarCampos();
            listarTipos();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al registrar el tipo de producto.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificar() {
        if (vista.cajaIdTipo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Seleccione un tipo de producto de la tabla para modificar.");
            return;
        }
        int idTipo = Integer.parseInt(vista.cajaIdTipo.getText());
        TipoProducto editado = construirDesdeFormulario(idTipo);
        if (editado == null) return;

        if (modelo.modificar(editado)) {
            JOptionPane.showMessageDialog(vista, "Tipo de producto actualizado correctamente.");
            limpiarCampos();
            listarTipos();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al actualizar el tipo de producto.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        if (vista.cajaIdTipo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Seleccione un tipo de producto de la tabla para eliminar.");
            return;
        }
        int idTipo = Integer.parseInt(vista.cajaIdTipo.getText());

        // RF-04 / AC-06: Validar integridad referencial antes de eliminar
        if (modelo.tieneCategoriasAsociadas(idTipo)) {
            JOptionPane.showMessageDialog(vista,
                    "No se puede eliminar el Tipo de Producto porque tiene Categorías asociadas.",
                    "Error de Integridad Referencial",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (modelo.eliminar(idTipo)) {
            JOptionPane.showMessageDialog(vista, "Tipo de producto eliminado correctamente.");
            limpiarCampos();
            listarTipos();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al eliminar el tipo de producto.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private TipoProducto construirDesdeFormulario(int idTipo) {
        String nombre = vista.cajaNombre.getText().trim();

        // RF-04 / AC-01: Validar nombre no vacío ni duplicado (case-insensitive)
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El nombre del Tipo de Producto es obligatorio (*).");
            return null;
        }
        if (modelo.existeNombre(nombre, idTipo)) {
            JOptionPane.showMessageDialog(vista,
                    "Ya existe un Tipo de Producto con el nombre '" + nombre + "'.",
                    "Nombre duplicado",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }

        return new TipoProducto(idTipo, nombre);
    }

    private void cargarSeleccionado() {
        int fila = vista.tablaTipos.getSelectedRow();
        if (fila < 0) return;

        vista.cajaIdTipo.setText(vista.modeloTabla.getValueAt(fila, 0).toString());
        vista.cajaNombre.setText(vista.modeloTabla.getValueAt(fila, 1).toString());

        vista.botonInsertar.setEnabled(false);
        vista.botonModificar.setEnabled(true);
        vista.botonEliminar.setEnabled(true);
    }

    public void limpiarCampos() {
        vista.cajaIdTipo.setText("");
        vista.cajaNombre.setText("");
        vista.tablaTipos.clearSelection();
        vista.botonInsertar.setEnabled(true);
        vista.botonModificar.setEnabled(false);
        vista.botonEliminar.setEnabled(false);
    }
}

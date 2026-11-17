package controlador;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.List;
import javax.swing.JOptionPane;
import modelo.Reparacion;
import modelo.ConsultasReparacion;
import modelo.Usuario;
import vista.VistaReparacion;
public class ControladorReparacion implements ActionListener {
    private VistaReparacion vista;
    private Reparacion reparacion;
    private ConsultasReparacion modelo;
    private Usuario usuario;

    public ControladorReparacion(VistaReparacion vista, Reparacion reparacion, ConsultasReparacion modelo, Usuario usuario) {
        this.vista = vista;
        this.reparacion = reparacion;
        this.modelo = modelo;
        this.usuario = usuario;
        vista.botonInsertar.addActionListener(this);
        vista.botonBuscar.addActionListener(this);
        vista.botonModificar.addActionListener(this);
        vista.botonEliminar.addActionListener(this);
        vista.botonLimpiar.addActionListener(this);
        vista.botonListar.addActionListener(this);
        vista.tablaReparaciones.getSelectionModel().addListSelectionListener(
            new javax.swing.event.ListSelectionListener() {
                @Override
                public void valueChanged(javax.swing.event.ListSelectionEvent evento) {
                    if (!evento.getValueIsAdjusting()) { cargarSeleccionada(); }
                }
        });
    }
    public void iniciar() {
        vista.setTitle("Taller - " + usuario.getNombreUsuario() + " [" + usuario.getRol().getNombre() + "]");
        vista.setLocationRelativeTo(null);
        vista.cajaID.setVisible(false);
        vista.botonModificar.setEnabled(false);
        //vista.botonEliminar.setEnabled(!usuario.esAdministrador());
        vista.botonEliminar.setEnabled(usuario.esAdministrador());
        vista.botonInsertar.setEnabled(usuario.esAdministrador());
        // Un objeto de dominio se recibe desde ControladorLogin.
        reparacion.setCodigo("");
    }
    @Override
    public void actionPerformed(ActionEvent evento) {
        if (evento.getSource() == vista.botonInsertar) { insertar(); }
        else if (evento.getSource() == vista.botonBuscar) { buscar(); }
        else if (evento.getSource() == vista.botonModificar) { modificar(); }
        else if (evento.getSource() == vista.botonEliminar) { eliminar(); }
        else if (evento.getSource() == vista.botonLimpiar) { limpiarCajas(); }
        else if (evento.getSource() == vista.botonListar) { listarReparaciones(); }
    }
    private void insertar() {
        if (!usuario.esAdministrador()) { return; }
        if (!cargarYValidarReparacion()) { return; }
        if (modelo.insertar(reparacion)) {
            JOptionPane.showMessageDialog(vista, "Registro insertado correctamente");
            limpiarCajas(); listarReparaciones();
        } else { JOptionPane.showMessageDialog(vista, "No se pudo insertar"); }
    }
    private void buscar() {
        String codigo = vista.cajaBuscar.getText().trim();
        deshabilitarEdicion();
        if (codigo.isEmpty()) { JOptionPane.showMessageDialog(vista, "Ingresá un código de reparación"); return; }
        reparacion.setCodigo(codigo);
        if (modelo.buscar(reparacion)) {
            mostrarReparacion();
            mostrarEncontrada();
            if (usuario.esAdministrador()) {
                vista.botonModificar.setEnabled(true);
                vista.botonEliminar.setEnabled(true);
            }
        } else { JOptionPane.showMessageDialog(vista, "No existe esa reparación"); }
    }
    private void modificar() {
        if (!usuario.esAdministrador() || vista.cajaID.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Primero seleccioná un registro válido"); return;
        }
        if (!cargarYValidarReparacion()) { return; }
        reparacion.setIdReparacion(Integer.parseInt(vista.cajaID.getText()));
        if (modelo.modificar(reparacion)) { limpiarCajas(); listarReparaciones(); }
    }
    private void eliminar() {
        if (!usuario.esAdministrador()){
            JOptionPane.showMessageDialog(vista, "Accion no permitida");
            return;//Esto me falto que me indico el profe.
        }
        
        if (vista.cajaID.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Primero seleccioná un registro autorizado"); return;
        }
        int respuesta = JOptionPane.showConfirmDialog(vista, "¿Eliminar el registro?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (respuesta != JOptionPane.YES_OPTION) { return; }
        reparacion.setIdReparacion(Integer.parseInt(vista.cajaID.getText()));
        if (modelo.eliminar(reparacion)) { limpiarCajas(); listarReparaciones(); }
    }
    private boolean cargarYValidarReparacion() {
        String codigo = vista.cajaCodigo.getText().trim();
        String principal = vista.cajaDescripcion.getText().trim();
        String otro = vista.cajaBicicleta.getText().trim();
        String costoTexto = vista.cajaCosto.getText().trim();
        String persona = vista.cajaCliente.getText().trim();
        String fechaTexto = vista.cajaFechaIngreso.getText().trim();
        String estado = vista.comboEstado.getSelectedItem().toString();
        if (codigo.isEmpty() || principal.isEmpty() || otro.isEmpty() || persona.isEmpty() || fechaTexto.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Completá todos los campos"); return false;
        }
        // ERROR INTENCIONAL: esta conversión puede provocar NumberFormatException.
        
        //int numero = Integer.parseInt(costoTexto);
        if (costoTexto.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El costo es obligatorio.");
            return false;
        }
        
        int numero;
        
        try {
            numero = Integer.parseInt(costoTexto);
            if (numero < 0) {
                JOptionPane.showMessageDialog(vista, "El costo no puede ser negativo.");
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "El costo debe ser un número entero válido.");
            return false;
        }
        
        if ("Seleccionar".equals(estado)) { JOptionPane.showMessageDialog(vista, "Seleccioná estado"); return false; }
        Date fecha;
        try { fecha = Date.valueOf(fechaTexto); }
        catch (IllegalArgumentException ex) { JOptionPane.showMessageDialog(vista, "Fecha: AAAA-MM-DD"); return false; }
        reparacion.setCodigo(codigo);
        reparacion.setDescripcion(principal);
        reparacion.setBicicleta(otro);
        reparacion.setCosto(numero);
        reparacion.setCliente(persona);
        reparacion.setFechaIngreso(fecha);
        reparacion.setEstado(estado);
        return true;
    }
    private void mostrarReparacion() {
        vista.cajaID.setText(String.valueOf(reparacion.getIdReparacion()));
        vista.cajaCodigo.setText(String.valueOf(reparacion.getCodigo()));
        vista.cajaDescripcion.setText(String.valueOf(reparacion.getDescripcion()));
        vista.cajaBicicleta.setText(String.valueOf(reparacion.getBicicleta()));
        vista.cajaCosto.setText(String.valueOf(reparacion.getCosto()));
        vista.cajaCliente.setText(String.valueOf(reparacion.getCliente()));
        vista.cajaFechaIngreso.setText(String.valueOf(reparacion.getFechaIngreso()));
        vista.comboEstado.setSelectedItem(reparacion.getEstado());
    }
    private void listarReparaciones() {
        List<Reparacion> lista = modelo.listar();
        mostrarListado(lista);
    }
    private void mostrarListado(List<Reparacion> lista) {
        vista.modeloTabla.setRowCount(0);
        deshabilitarEdicion();
        for (Reparacion item : lista) {
            vista.modeloTabla.addRow(new Object[]{item.getIdReparacion(),
                item.getCodigo(), item.getDescripcion(), item.getBicicleta(), item.getCosto(), item.getCliente(), item.getFechaIngreso(), item.getEstado()});
        }
    }
    private void cargarSeleccionada() {
        int fila = vista.tablaReparaciones.getSelectedRow();
        if (fila < 0) { return; }
        reparacion.setIdReparacion(Integer.parseInt(vista.modeloTabla.getValueAt(fila, 0).toString()));
        reparacion.setCodigo(vista.modeloTabla.getValueAt(fila, 1).toString());
        reparacion.setDescripcion(vista.modeloTabla.getValueAt(fila, 2).toString());
        reparacion.setBicicleta(vista.modeloTabla.getValueAt(fila, 3).toString());
        reparacion.setCosto(Integer.parseInt(vista.modeloTabla.getValueAt(fila, 4).toString()));
        reparacion.setCliente(vista.modeloTabla.getValueAt(fila, 5).toString());
        reparacion.setFechaIngreso(Date.valueOf(vista.modeloTabla.getValueAt(fila, 6).toString()));
        reparacion.setEstado(vista.modeloTabla.getValueAt(fila, 7).toString());
        mostrarReparacion();
        if (usuario.esAdministrador()) {
        vista.botonModificar.setEnabled(true);
        vista.botonEliminar.setEnabled(true);
        }
    }
    private void mostrarEncontrada() {
        vista.modeloTabla.setRowCount(0);
        vista.modeloTabla.addRow(new Object[]{reparacion.getIdReparacion(),
            reparacion.getCodigo(), reparacion.getDescripcion(), reparacion.getBicicleta(), reparacion.getCosto(), reparacion.getCliente(), reparacion.getFechaIngreso(), reparacion.getEstado()});
    }
    private void deshabilitarEdicion() {
        vista.botonModificar.setEnabled(false);
        vista.botonEliminar.setEnabled(false);
    }
    public void limpiarCajas() {
        vista.cajaID.setText("");
        vista.cajaBuscar.setText("");
        vista.cajaCodigo.setText("");
        vista.cajaDescripcion.setText("");
        vista.cajaBicicleta.setText("");
        vista.cajaCosto.setText("");
        vista.cajaCliente.setText("");
        vista.cajaFechaIngreso.setText("");
        vista.comboEstado.setSelectedIndex(0);
        vista.modeloTabla.setRowCount(0);
        deshabilitarEdicion();
    }
}

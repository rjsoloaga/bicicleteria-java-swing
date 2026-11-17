package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;
import modelo.ConsultasUsuario;
import modelo.Persona;
import modelo.Rol;
import modelo.Usuario;
import vista.PanelUsuarios;

public class ControladorUsuario implements ActionListener {
    private PanelUsuarios vista;
    private ConsultasUsuario modelo;
    private Usuario usuarioLogueado;

    public ControladorUsuario(PanelUsuarios vista, ConsultasUsuario modelo, Usuario usuarioLogueado) {
        this.vista = vista;
        this.modelo = modelo;
        this.usuarioLogueado = usuarioLogueado;

        this.vista.botonInsertar.addActionListener(this);
        this.vista.botonModificar.addActionListener(this);
        this.vista.botonCambiarEstado.addActionListener(this);
        this.vista.botonLimpiar.addActionListener(this);

        this.vista.tablaUsuarios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccionado();
            }
        });
    }

    public void iniciar() {
        cargarRoles();
        listarUsuarios();
        limpiarCampos();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.botonInsertar) {
            insertar();
        } else if (e.getSource() == vista.botonModificar) {
            modificar();
        } else if (e.getSource() == vista.botonCambiarEstado) {
            cambiarEstado();
        } else if (e.getSource() == vista.botonLimpiar) {
            limpiarCampos();
        }
    }

    private void cargarRoles() {
        vista.comboRol.removeAllItems();
        for (Rol r : modelo.listarRoles()) {
            vista.comboRol.addItem(r);
        }
    }

    private void listarUsuarios() {
        vista.modeloTabla.setRowCount(0);
        List<Usuario> lista = modelo.listar();
        for (Usuario u : lista) {
            Persona p = u.getPersona();
            vista.modeloTabla.addRow(new Object[]{
                u.getIdUsuario(),
                p.getIdPersona(),
                u.getNombreUsuario(),
                p.getNombre(),
                p.getApellido(),
                p.getDni(),
                p.getTelefono(),
                p.getEmail(),
                u.getRol().getNombre(),
                u.isActivo() ? "ACTIVO" : "INACTIVO"
            });
        }
    }

    private void insertar() {
        char[] claveChars = vista.cajaClave.getPassword();
        try {
            if (claveChars.length < 4) {
                JOptionPane.showMessageDialog(vista, "La contraseña debe tener al menos 4 caracteres.");
                return;
            }
            Usuario nuevo = construirDesdeFormulario(0, 0);
            if (nuevo == null) return;
            nuevo.setActivo(true);

            if (modelo.insertarConPersona(nuevo, new String(claveChars))) {
                JOptionPane.showMessageDialog(vista, "Usuario y persona registrados correctamente.");
                limpiarCampos();
                listarUsuarios();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al registrar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } finally {
            Arrays.fill(claveChars, '\0');
        }
    }

    private void modificar() {
        if (vista.cajaIdUsuario.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Seleccione un usuario de la tabla para modificar.");
            return;
        }
        int idUsuario = Integer.parseInt(vista.cajaIdUsuario.getText());
        int idPersona = Integer.parseInt(vista.cajaIdPersona.getText());
        char[] claveChars = vista.cajaClave.getPassword();

        try {
            if (claveChars.length > 0 && claveChars.length < 4) {
                JOptionPane.showMessageDialog(vista, "Si ingresa una nueva clave, debe tener al menos 4 caracteres.");
                return;
            }
            Usuario editado = construirDesdeFormulario(idUsuario, idPersona);
            if (editado == null) return;

            String nuevaClave = claveChars.length > 0 ? new String(claveChars) : null;
            if (modelo.modificarConPersona(editado, nuevaClave)) {
                JOptionPane.showMessageDialog(vista, "Usuario actualizado correctamente.");
                limpiarCampos();
                listarUsuarios();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al actualizar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } finally {
            Arrays.fill(claveChars, '\0');
        }
    }

    private void cambiarEstado() {
        int fila = vista.tablaUsuarios.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(vista, "Seleccione un usuario de la tabla.");
            return;
        }
        int idUsuario = Integer.parseInt(vista.modeloTabla.getValueAt(fila, 0).toString());
        String estadoActual = vista.modeloTabla.getValueAt(fila, 9).toString();
        boolean nuevoEstado = !"ACTIVO".equals(estadoActual);

        // Regla RF-10 / AC-08: Bloquear auto-desactivación del propio ADMIN
        if (idUsuario == usuarioLogueado.getIdUsuario() && !nuevoEstado) {
            JOptionPane.showMessageDialog(vista,
                    "No puede desactivar su propia cuenta de administrador mientras tiene la sesión iniciada.",
                    "Acción bloqueada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (modelo.cambiarEstado(idUsuario, nuevoEstado)) {
            JOptionPane.showMessageDialog(vista, "Estado actualizado a " + (nuevoEstado ? "ACTIVO" : "INACTIVO") + ".");
            limpiarCampos();
            listarUsuarios();
        }
    }

    private Usuario construirDesdeFormulario(int idUsuario, int idPersona) {
        String nombre = vista.cajaNombre.getText().trim();
        String apellido = vista.cajaApellido.getText().trim();
        String dni = vista.cajaDni.getText().trim();
        String telefono = vista.cajaTelefono.getText().trim();
        String email = vista.cajaEmail.getText().trim();
        String nombreUsuario = vista.cajaNombreUsuario.getText().trim();
        Rol rolSeleccionado = (Rol) vista.comboRol.getSelectedItem();

        if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || nombreUsuario.isEmpty() || rolSeleccionado == null) {
            JOptionPane.showMessageDialog(vista, "Complete todos los campos obligatorios (*).");
            return null;
        }
        if (modelo.existeNombreUsuario(nombreUsuario, idUsuario)) {
            JOptionPane.showMessageDialog(vista, "El nombre de usuario '" + nombreUsuario + "' ya existe.", "Duplicado", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        if (modelo.existeDni(dni, idPersona)) {
            JOptionPane.showMessageDialog(vista, "El DNI '" + dni + "' ya pertenece a otra persona.", "Duplicado", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        Persona p = new Persona();
        p.setIdPersona(idPersona);
        p.setNombre(nombre);
        p.setApellido(apellido);
        p.setDni(dni);
        p.setTelefono(telefono);
        p.setEmail(email);

        Usuario u = new Usuario();
        u.setIdUsuario(idUsuario);
        u.setNombreUsuario(nombreUsuario);
        u.setRol(rolSeleccionado);
        u.setPersona(p);
        return u;
    }

    private void cargarSeleccionado() {
        int fila = vista.tablaUsuarios.getSelectedRow();
        if (fila < 0) return;

        vista.cajaIdUsuario.setText(vista.modeloTabla.getValueAt(fila, 0).toString());
        vista.cajaIdPersona.setText(vista.modeloTabla.getValueAt(fila, 1).toString());
        vista.cajaNombreUsuario.setText(vista.modeloTabla.getValueAt(fila, 2).toString());
        vista.cajaNombre.setText(vista.modeloTabla.getValueAt(fila, 3).toString());
        vista.cajaApellido.setText(vista.modeloTabla.getValueAt(fila, 4).toString());
        vista.cajaDni.setText(vista.modeloTabla.getValueAt(fila, 5).toString());
        Object tel = vista.modeloTabla.getValueAt(fila, 6);
        vista.cajaTelefono.setText(tel != null ? tel.toString() : "");
        Object mail = vista.modeloTabla.getValueAt(fila, 7);
        vista.cajaEmail.setText(mail != null ? mail.toString() : "");

        String rolNombre = vista.modeloTabla.getValueAt(fila, 8).toString();
        for (int i = 0; i < vista.comboRol.getItemCount(); i++) {
            if (vista.comboRol.getItemAt(i).getNombre().equals(rolNombre)) {
                vista.comboRol.setSelectedIndex(i);
                break;
            }
        }

        vista.cajaClave.setText("");
        vista.botonInsertar.setEnabled(false);
        vista.botonModificar.setEnabled(true);
        vista.botonCambiarEstado.setEnabled(true);
    }

    public void limpiarCampos() {
        vista.cajaIdUsuario.setText("");
        vista.cajaIdPersona.setText("");
        vista.cajaNombre.setText("");
        vista.cajaApellido.setText("");
        vista.cajaDni.setText("");
        vista.cajaTelefono.setText("");
        vista.cajaEmail.setText("");
        vista.cajaNombreUsuario.setText("");
        vista.cajaClave.setText("");
        if (vista.comboRol.getItemCount() > 0) vista.comboRol.setSelectedIndex(0);
        vista.tablaUsuarios.clearSelection();
        vista.botonInsertar.setEnabled(true);
        vista.botonModificar.setEnabled(false);
        vista.botonCambiarEstado.setEnabled(false);
    }
}
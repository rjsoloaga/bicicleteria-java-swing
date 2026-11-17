package crud_mvc;

import controlador.ControladorLogin;
import modelo.ConsultasUsuario;
import vista.VistaLogin;

public class CRUD_MVC {
    public static void main(String[] args) {
        VistaLogin vista = new VistaLogin();
        ConsultasUsuario modelo = new ConsultasUsuario();
        ControladorLogin controlador = new ControladorLogin(vista, modelo);
        controlador.iniciar();
    }
}
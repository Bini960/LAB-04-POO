package controlador;

/**
 * Controlador para que la Vista pueda
 * establecer/consultar permisos del usuario actual.
 */
public class ControladorUsuario {

    private boolean esAdmin;

    public ControladorUsuario() {
        this.esAdmin = false;
    }

    public void iniciarSesionComoAdmin() {
        this.esAdmin = true;
    }

    public void iniciarSesionComoEditor() {
        this.esAdmin = false;
    }

    public boolean esAdmin() {
        return esAdmin;
    }
}

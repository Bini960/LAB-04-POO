package controlador;

import modelo.sistema.SistemaCMS;
import modelo.usuarios.Usuario;

public class ControladorUsuario {
    private final SistemaCMS sistema;
    private Usuario usuarioActual; // opcional por ahora

    public ControladorUsuario(SistemaCMS sistema) {
        this.sistema = sistema;
    }

    public Usuario getUsuarioActual() { return usuarioActual; }
}

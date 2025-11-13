package modelo.usuarios;

public class Editor extends Usuario {

    public Editor(String nombre, String correo, String contrasena) {
        super(nombre, correo, contrasena);
    }

    @Override
    public boolean esAdmin() {
        return false;
    }
    
}

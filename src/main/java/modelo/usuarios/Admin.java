package modelo.usuarios;

public class Admin extends Usuario {

    public Admin(String nombre, String correo, String contrasena) {
        super(nombre, correo, contrasena);
    }

    @Override
    public boolean esAdmin() {
        return true;
    }
    
}

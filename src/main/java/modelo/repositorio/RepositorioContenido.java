package modelo.repositorio;

import java.util.ArrayList;

import modelo.contenidos.Contenido;

public class RepositorioContenido {

    private final ArrayList<Contenido> datos = new ArrayList<>();
    private int secuenciaId = 1;

    public int agregar(Contenido c) {
        if (c == null) throw new IllegalArgumentException("Contenido nulo");
        c.setId(secuenciaId++);
        datos.add(c);
        return c.getId();
    }

    public ArrayList<Contenido> obtenerTodos() {
        return new ArrayList<>(datos);
    }

    public Contenido buscarPorId(int id) {
        for (Contenido c : datos) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    public boolean eliminar(int id) {
        return datos.removeIf(c -> c.getId() == id);
    }
}

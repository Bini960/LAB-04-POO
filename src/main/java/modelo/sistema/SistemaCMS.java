package modelo.sistema;

import java.util.ArrayList;

import modelo.contenidos.Articulo;
import modelo.contenidos.Contenido;
import modelo.contenidos.Estado;
import modelo.contenidos.Imagen;
import modelo.contenidos.Video;
import modelo.reporte.IReportable;
import modelo.repositorio.RepositorioContenido;

public class SistemaCMS {

    private final RepositorioContenido repo;

    public SistemaCMS(RepositorioContenido repo) {
        this.repo = repo;
    }

    // ---------- Crear contenido ----------
    public int crearArticulo(String titulo, String cuerpo, int autorId, String fechaCreacion,
                             ArrayList<String> etiquetas, String categoria) {
        validarNoVacio(titulo, "titulo");
        validarNoVacio(cuerpo, "cuerpo");
        validarNoVacio(fechaCreacion, "fechaCreacion");

        Articulo a = new Articulo();
        a.setTitulo(titulo);
        a.setCuerpo(cuerpo);
        a.setAutorId(autorId);
        a.setFechaCreacion(fechaCreacion);
        a.setEtiquetas(etiquetas != null ? etiquetas : new ArrayList<>());
        a.setCategoria(categoria);
        a.setEstado(Estado.BORRADOR);

        return repo.agregar(a);
    }

    public int crearVideo(String titulo, String url, int duracionSeg, int autorId, String fechaCreacion,
                          ArrayList<String> etiquetas, String categoria) {
        validarNoVacio(titulo, "titulo");
        validarNoVacio(url, "url");
        if (duracionSeg < 0) throw new IllegalArgumentException("duracionSeg no puede ser negativa");
        validarNoVacio(fechaCreacion, "fechaCreacion");

        Video v = new Video();
        v.setTitulo(titulo);
        v.setUrl(url);
        v.setDuracionSeg(duracionSeg);
        v.setAutorId(autorId);
        v.setFechaCreacion(fechaCreacion);
        v.setEtiquetas(etiquetas != null ? etiquetas : new ArrayList<>());
        v.setCategoria(categoria);
        v.setEstado(Estado.BORRADOR);

        return repo.agregar(v);
    }

    public int crearImagen(String titulo, String url, int ancho, int alto, int autorId, String fechaCreacion,
                           ArrayList<String> etiquetas, String categoria) {
        validarNoVacio(titulo, "titulo");
        validarNoVacio(url, "url");
        if (ancho <= 0 || alto <= 0) throw new IllegalArgumentException("Dimensiones inválidas");
        validarNoVacio(fechaCreacion, "fechaCreacion");

        Imagen i = new Imagen();
        i.setTitulo(titulo);
        i.setUrl(url);
        i.setAncho(ancho);
        i.setAlto(alto);
        i.setAutorId(autorId);
        i.setFechaCreacion(fechaCreacion);
        i.setEtiquetas(etiquetas != null ? etiquetas : new ArrayList<>());
        i.setCategoria(categoria);
        i.setEstado(Estado.BORRADOR);

        return repo.agregar(i);
    }

    // ---------- Consultas ----------
    public ArrayList<Contenido> listarTodos() {
        return repo.obtenerTodos();
    }

    public ArrayList<Contenido> listarPorEstado(Estado estado) {
        ArrayList<Contenido> out = new ArrayList<>();
        for (Contenido c : repo.obtenerTodos()) {
            if (c.getEstado() == estado) out.add(c);
        }
        return out;
    }

    public Contenido buscarPorId(int id) {
        return repo.buscarPorId(id);
    }

    // ---------- Reglas ----------
    public boolean publicar(int id, boolean esAdmin) {
        Contenido c = repo.buscarPorId(id);
        if (c == null || !esAdmin) return false;
        c.setEstado(Estado.PUBLICADO);
        return true;
    }

    public boolean despublicar(int id, boolean esAdmin) {
        Contenido c = repo.buscarPorId(id);
        if (c == null || !esAdmin) return false;
        c.setEstado(Estado.BORRADOR);
        return true;
    }

    public boolean eliminar(int id, boolean esAdmin) {
        if (!esAdmin) return false;
        return repo.eliminar(id);
    }

    // ---------- Reporte ----------
    public String generarReporte(IReportable reportador) {
        ArrayList<Contenido> datos = repo.obtenerTodos();
        return reportador.generar(datos);
    }

    // ---------- Helpers ----------
    private void validarNoVacio(String s, String campo) {
        if (s == null || s.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo '" + campo + "' es obligatorio");
        }
    }
}

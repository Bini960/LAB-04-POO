package modelo.contenidos;

import java.util.ArrayList;
import modelo.interfaces.IPublicable;

public abstract class Contenido implements IPublicable {

    private int id;
    private String titulo;
    private int autorId;
    private String fechaCreacion;
    private ArrayList<String> etiquetas = new ArrayList<>();
    private String categoria;
    private Estado estado = Estado.BORRADOR;

    // Getters/Setters básicos
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public int getAutorId() { return autorId; }
    public void setAutorId(int autorId) { this.autorId = autorId; }

    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public ArrayList<String> getEtiquetas() { return etiquetas; }
    public void setEtiquetas(ArrayList<String> etiquetas) { this.etiquetas = etiquetas; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    // IPublicable
    @Override public void publicar()    { this.estado = Estado.PUBLICADO; }
    @Override public void despublicar() { this.estado = Estado.BORRADOR; }
}

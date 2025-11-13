package controlador;

import java.util.ArrayList;

import modelo.contenidos.Contenido;
import modelo.contenidos.Estado;
import modelo.reporte.IReportable;
import modelo.sistema.SistemaCMS;

public class ControladorContenido {

    private final SistemaCMS sistema;
    private boolean esAdminActual;

    public ControladorContenido(SistemaCMS sistema) {
        this.sistema = sistema;
        this.esAdminActual = false;
    }

    public void setEsAdminActual(boolean esAdmin) { 
        this.esAdminActual = esAdmin; 
    }

    public int crearArticulo(String titulo, String cuerpo, int autorId, String fecha,
                             ArrayList<String> etiquetas, String categoria) {
        return sistema.crearArticulo(titulo, cuerpo, autorId, fecha, etiquetas, categoria);
    }

    public int crearVideo(String titulo, String url, int duracionSeg, int autorId, String fecha,
                          ArrayList<String> etiquetas, String categoria) {
        return sistema.crearVideo(titulo, url, duracionSeg, autorId, fecha, etiquetas, categoria);
    }

    public int crearImagen(String titulo, String url, int ancho, int alto, int autorId, String fecha,
                           ArrayList<String> etiquetas, String categoria) {
        return sistema.crearImagen(titulo, url, ancho, alto, autorId, fecha, etiquetas, categoria);
    }

    public ArrayList<Contenido> listarTodos() { 
        return sistema.listarTodos(); 
    }

    public ArrayList<Contenido> listarPorEstado(Estado e) { 
        return sistema.listarPorEstado(e); 
    }

    public boolean publicar(int id) { 
        return sistema.publicar(id, esAdminActual); 
    }

    public boolean despublicar(int id) { 
        return sistema.despublicar(id, esAdminActual); 
    }

    public boolean eliminar(int id) { 
        return sistema.eliminar(id, esAdminActual); 
    }

    public String generarReporte(IReportable reportador) { 
        return sistema.generarReporte(reportador); 
    }
}

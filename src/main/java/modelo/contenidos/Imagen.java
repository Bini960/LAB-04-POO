package modelo.contenidos;

public class Imagen extends Contenido {
    private String url;
    private int ancho;
    private int alto;

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public int getAncho() { return ancho; }
    public void setAncho(int ancho) { this.ancho = ancho; }

    public int getAlto() { return alto; }
    public void setAlto(int alto) { this.alto = alto; }
}

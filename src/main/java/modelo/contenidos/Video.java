package modelo.contenidos;

public class Video extends Contenido {
    private String url;
    private int duracionSeg;

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public int getDuracionSeg() { return duracionSeg; }
    public void setDuracionSeg(int duracionSeg) { this.duracionSeg = duracionSeg; }
}

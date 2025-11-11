package modelo.reporte;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import modelo.contenidos.Contenido;

public class ReporteTexto implements IReportable {

    private final String rutaSalida;

    public ReporteTexto(String rutaSalida) {
        this.rutaSalida = (rutaSalida == null || rutaSalida.trim().isEmpty())
                ? "reporte_contenidos.txt" : rutaSalida;
    }

    @Override
    public String generar(ArrayList<Contenido> contenidos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaSalida))) {
            bw.write("REPORTE DE CONTENIDOS\n");
            bw.write("======================\n\n");
            if (contenidos == null || contenidos.isEmpty()) {
                bw.write("(sin contenidos)\n");
            } else {
                for (Contenido c : contenidos) {
                    bw.write("ID: " + c.getId() + "\n");
                    bw.write("Título: " + c.getTitulo() + "\n");
                    bw.write("Categoría: " + c.getCategoria() + "\n");
                    bw.write("Estado: " + c.getEstado() + "\n");
                    bw.write("AutorId: " + c.getAutorId() + "\n");
                    bw.write("Fecha: " + c.getFechaCreacion() + "\n");
                    bw.write("-----\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al escribir reporte: " + e.getMessage(), e);
        }
        return rutaSalida;
    }
}

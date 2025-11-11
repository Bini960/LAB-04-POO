package modelo.reporte;

import java.util.ArrayList;
import modelo.contenidos.Contenido;

public interface IReportable {
    String generar(ArrayList<Contenido> contenidos);
}

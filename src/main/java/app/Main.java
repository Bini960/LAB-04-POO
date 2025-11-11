package app;

import javafx.application.Application;
import javafx.stage.Stage;
import vista.VistaJavaFX;
import controlador.ControladorContenido;
import controlador.ControladorUsuario;
import modelo.sistema.SistemaCMS;
import modelo.repositorio.RepositorioContenido;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        RepositorioContenido repo = new RepositorioContenido();
        SistemaCMS sistema = new SistemaCMS(repo);

        ControladorContenido cCont = new ControladorContenido(sistema);
        ControladorUsuario cUser = new ControladorUsuario();

        VistaJavaFX vista = new VistaJavaFX();
        vista.setControladores(cCont, cUser);
        vista.initUI(stage);

        stage.setTitle("CMS");
        stage.show();
    }

    public static void main(String[] args) { launch(args); }
}

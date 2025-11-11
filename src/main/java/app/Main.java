package app;

import javafx.application.Application;
import javafx.stage.Stage;
import vista.VistaJavaFX;
import controlador.ControladorContenido;
import controlador.ControladorUsuario;
import modelo.sistema.SistemaCMS;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        SistemaCMS sistema = new SistemaCMS();
        ControladorContenido cCont = new ControladorContenido(sistema);
        ControladorUsuario cUser = new ControladorUsuario(sistema);

        VistaJavaFX vista = new VistaJavaFX();
        vista.setControladores(cCont, cUser);
        vista.initUI(stage);

        stage.setTitle("CMS");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

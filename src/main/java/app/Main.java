//Andrés Castro Morales - 25039
//Carlos Pozuelos Mendizábal - 25104
//Mauricio Corado - 25218

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
        // Inicializar repositorio y sistema
        RepositorioContenido repo = new RepositorioContenido();
        SistemaCMS sistema = new SistemaCMS(repo);

        // Inicializar controladores
        ControladorContenido cCont = new ControladorContenido(sistema);
        ControladorUsuario cUser = new ControladorUsuario();
        
        // Por defecto, iniciar como Admin (para testing)
        cUser.iniciarSesionComoAdmin();
        cCont.setEsAdminActual(true);

        // Inicializar vista
        VistaJavaFX vista = new VistaJavaFX();
        vista.setControladores(cCont, cUser);
        vista.initUI(stage);

        // Configurar ventana principal
        stage.setTitle("CMS - Sistema de Gestión de Contenidos");
        stage.setResizable(true);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        
        // Mostrar ventana
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
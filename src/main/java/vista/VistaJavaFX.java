package vista;

import controlador.ControladorContenido;
import controlador.ControladorUsuario;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VistaJavaFX {
    private ControladorContenido cCont;
    private ControladorUsuario cUser;
    private Stage stage;        // guarda la referencia
    private Label lbl;          // mensaje visible

    public void setControladores(ControladorContenido cCont, ControladorUsuario cUser) {
        this.cCont = cCont; this.cUser = cUser;
    }

    public void initUI(Stage stage) {
        this.stage = stage;

        lbl = new Label("Bienvenido");
        Button btnCambiar = new Button("Cambiar mensaje");
        btnCambiar.setOnAction(e -> lbl.setText("Mensaje actualizado")); // <-- aquí cambias el texto

        Button btnCerrar = new Button("Cerrar ventana");
        btnCerrar.setOnAction(e -> this.stage.hide());  // cierra (oculta) la ventana

        Button btnAbrir = new Button("Abrir ventana");
        btnAbrir.setOnAction(e -> this.stage.show());   // vuelve a abrirla

        VBox root = new VBox(10, btnCambiar, btnCerrar, btnAbrir, lbl);
        stage.setScene(new Scene(root, 800, 500));
        stage.setTitle("CMS");
    }

    // Extra: pop-up rápido
    public void mostrarMensaje(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }
}

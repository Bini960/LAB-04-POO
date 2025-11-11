package vista;

import controlador.ControladorContenido;
import controlador.ControladorUsuario;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class VistaJavaFX {
    private ControladorContenido cCont;
    private ControladorUsuario cUser;
    private Stage stage;
    
    // Componentes principales
    private BorderPane root;
    private VBox menuLateral;
    private VBox contenidoPrincipal;
    private Label lblUsuarioActual;
    private Label lblTipoUsuario;

    public void setControladores(ControladorContenido cCont, ControladorUsuario cUser) {
        this.cCont = cCont;
        this.cUser = cUser;
    }

    public void initUI(Stage stage) {
        this.stage = stage;
        
        // Layout principal
        root = new BorderPane();
        
        // Crear menú lateral
        crearMenuLateral();
        
        // Crear contenido principal
        contenidoPrincipal = new VBox(20);
        contenidoPrincipal.setPadding(new Insets(20));
        contenidoPrincipal.setStyle("-fx-background-color: #f0f0f0;");
        
        Label lblBienvenida = new Label("Bienvenido al Sistema CMS");
        lblBienvenida.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        contenidoPrincipal.getChildren().add(lblBienvenida);
        
        // Ensamblar
        root.setLeft(menuLateral);
        root.setCenter(contenidoPrincipal);
        
        Scene scene = new Scene(root, 1000, 600);
        stage.setScene(scene);
        stage.setTitle("CMS - Sistema de Gestión de Contenidos");
    }

    private void crearMenuLateral() {
        menuLateral = new VBox(15);
        menuLateral.setPrefWidth(200);
        menuLateral.setPadding(new Insets(20));
        menuLateral.setStyle("-fx-background-color: #d3d3d3;");
        
        // Sección de usuario
        VBox perfilUsuario = new VBox(10);
        perfilUsuario.setAlignment(Pos.CENTER);
        perfilUsuario.setPadding(new Insets(10));
        perfilUsuario.setStyle("-fx-background-color: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        
        // Icono de usuario (simulado con label)
        Label iconoUsuario = new Label("👤");
        iconoUsuario.setFont(Font.font(40));
        
        lblTipoUsuario = new Label("ADMIN");
        lblTipoUsuario.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        lblUsuarioActual = new Label("usuario@cms.com");
        lblUsuarioActual.setFont(Font.font("Arial", 12));
        
        perfilUsuario.getChildren().addAll(iconoUsuario, lblTipoUsuario, lblUsuarioActual);
        
        // Separador
        Separator sep1 = new Separator();
        
        // Título del menú
        Label lblMenu = new Label("Menú");
        lblMenu.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        // Opciones del menú
        Button btnContenido = new Button("Contenido");
        Button btnCategorias = new Button("Categorías");
        Button btnReportes = new Button("Reportes");
        
        // Estilo de botones del menú
        String estiloBoton = "-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 14px; " +
                           "-fx-alignment: center-left; -fx-padding: 10; -fx-cursor: hand;";
        btnContenido.setStyle(estiloBoton);
        btnCategorias.setStyle(estiloBoton);
        btnReportes.setStyle(estiloBoton);
        
        btnContenido.setMaxWidth(Double.MAX_VALUE);
        btnCategorias.setMaxWidth(Double.MAX_VALUE);
        btnReportes.setMaxWidth(Double.MAX_VALUE);
        
        // Eventos del menú (por ahora solo mensajes)
        btnContenido.setOnAction(e -> mostrarMensaje("Vista de Contenido"));
        btnCategorias.setOnAction(e -> mostrarMensaje("Vista de Categorías (próximamente)"));
        btnReportes.setOnAction(e -> mostrarMensaje("Vista de Reportes (próximamente)"));
        
        // Ensamblar menú
        menuLateral.getChildren().addAll(
            perfilUsuario,
            sep1,
            lblMenu,
            btnContenido,
            btnCategorias,
            btnReportes
        );
    }

    public void mostrarMensaje(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    
    private void actualizarVistaUsuario() {
        if (cUser.esAdmin()) {
            lblTipoUsuario.setText("ADMIN");
            lblTipoUsuario.setStyle("-fx-text-fill: #2c3e50;");
        } else {
            lblTipoUsuario.setText("EDITOR");
            lblTipoUsuario.setStyle("-fx-text-fill: #7f8c8d;");
        }
    }
}
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
import javafx.stage.Modality;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.scene.control.ButtonType;
import modelo.contenidos.*;
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

    // Componentes de búsqueda
    private ComboBox<String> cmbFiltroCategoria;
    private TextField txtBusqueda;
    
    // Componentes de contenido
    private ScrollPane scrollContenidos;
    private VBox listaContenidos;

    public void initUI(Stage stage) {
        this.stage = stage;
        
        // Layout principal
        root = new BorderPane();
        
        // Crear menú lateral
        crearMenuLateral();
        
        // Crear barra superior
        VBox topSection = new VBox(10);
        topSection.setPadding(new Insets(15));
        topSection.setStyle("-fx-background-color: white;");
        
        HBox barraSuperior = crearBarraSuperior();
        topSection.getChildren().add(barraSuperior);
        
        // Crear área de contenidos
        crearAreaContenidos();
        
        // Ensamblar
        root.setTop(topSection);
        root.setLeft(menuLateral);
        root.setCenter(scrollContenidos);
        
        Scene scene = new Scene(root, 1000, 600);
        stage.setScene(scene);
        stage.setTitle("CMS - Sistema de Gestión de Contenidos");
        
        // Sincronizar permisos
        cCont.setEsAdminActual(cUser.esAdmin());
        actualizarVistaUsuario();
        
        // Cargar contenidos iniciales
        cargarContenidos();
    }

    private void crearAreaContenidos() {
        listaContenidos = new VBox(15);
        listaContenidos.setPadding(new Insets(20));
        
        scrollContenidos = new ScrollPane(listaContenidos);
        scrollContenidos.setFitToWidth(true);
        scrollContenidos.setStyle("-fx-background: #f0f0f0; -fx-background-color: #f0f0f0;");
    }

    private void cargarContenidos() {
        listaContenidos.getChildren().clear();
        ArrayList<Contenido> contenidos = cCont.listarTodos();
        
        if (contenidos.isEmpty()) {
            Label lblVacio = new Label("No hay contenidos disponibles. Crea uno nuevo usando los botones superiores.");
            lblVacio.setFont(Font.font("Arial", 14));
            lblVacio.setStyle("-fx-text-fill: #7f8c8d;");
            listaContenidos.getChildren().add(lblVacio);
        } else {
            for (Contenido contenido : contenidos) {
                VBox card = crearCardContenido(contenido);
                listaContenidos.getChildren().add(card);
            }
        }
    }

    private VBox crearCardContenido(Contenido contenido) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; " +
                     "-fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5;");
        
        HBox headerCard = new HBox(15);
        headerCard.setAlignment(Pos.CENTER_LEFT);
        
        // Icono según tipo
        String icono = "📄";
        if (contenido instanceof Video) {
            icono = "▶️";
        } else if (contenido instanceof Imagen) {
            icono = "🖼️";
        }
        
        Label lblIcono = new Label(icono);
        lblIcono.setFont(Font.font(30));
        
        // Información del contenido
        VBox infoContenido = new VBox(5);
        
        Label lblTitulo = new Label(contenido.getTitulo());
        lblTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        // Resumen según tipo
        String resumen = "";
        if (contenido instanceof Articulo) {
            Articulo art = (Articulo) contenido;
            String cuerpo = art.getCuerpo();
            resumen = cuerpo != null && cuerpo.length() > 60 
                     ? cuerpo.substring(0, 60) + "..." 
                     : (cuerpo != null ? cuerpo : "Sin contenido");
        } else if (contenido instanceof Video) {
            Video vid = (Video) contenido;
            int minutos = vid.getDuracionSeg() / 60;
            int segundos = vid.getDuracionSeg() % 60;
            resumen = String.format("Duración: %d:%02d | URL: %s", minutos, segundos, vid.getUrl());
        } else if (contenido instanceof Imagen) {
            Imagen img = (Imagen) contenido;
            resumen = String.format("%dx%d px | URL: %s", img.getAncho(), img.getAlto(), img.getUrl());
        }
        
        Label lblResumen = new Label(resumen);
        lblResumen.setFont(Font.font("Arial", 12));
        lblResumen.setStyle("-fx-text-fill: #7f8c8d;");
        
        infoContenido.getChildren().addAll(lblTitulo, lblResumen);
        
        // Separador
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Categoría
        Label lblCategoria = new Label("Categoría");
        lblCategoria.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lblCategoria.setStyle("-fx-text-fill: #2c3e50;");
        
        Label lblCategoriaValor = new Label(contenido.getCategoria() != null ? contenido.getCategoria() : "Sin categoría");
        lblCategoriaValor.setFont(Font.font("Arial", 12));
        
        VBox categoriaBox = new VBox(3, lblCategoria, lblCategoriaValor);
        
        headerCard.getChildren().addAll(lblIcono, infoContenido, spacer, categoriaBox);
        
        HBox botonesAccion = new HBox(10);
        botonesAccion.setAlignment(Pos.CENTER_RIGHT);
        botonesAccion.setPadding(new Insets(10, 0, 0, 0));
        
        // Estado del contenido
        Label lblEstado = new Label("Estado: " + contenido.getEstado());
        lblEstado.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        if (contenido.getEstado() == Estado.PUBLICADO) {
            lblEstado.setStyle("-fx-text-fill: #27ae60;");
        } else {
            lblEstado.setStyle("-fx-text-fill: #e67e22;");
        }
        
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        
        // Botón Publicar/Despublicar
        Button btnPublicar = new Button(
            contenido.getEstado() == Estado.PUBLICADO ? "Despublicar" : "Publicar"
        );
        
        if (contenido.getEstado() == Estado.PUBLICADO) {
            btnPublicar.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-padding: 5 15;");
        } else {
            btnPublicar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-padding: 5 15;");
        }
        
        btnPublicar.setOnAction(e -> {
            if (!cUser.esAdmin()) {
                mostrarError("Solo los administradores pueden publicar/despublicar contenidos");
                return;
            }
            
            boolean exito;
            if (contenido.getEstado() == Estado.PUBLICADO) {
                exito = cCont.despublicar(contenido.getId());
            } else {
                exito = cCont.publicar(contenido.getId());
            }
            
            if (exito) {
                mostrarMensaje("Operación realizada exitosamente");
                cargarContenidos();
            } else {
                mostrarError("No se pudo realizar la operación");
            }
        });
        
        // Botón Eliminar
        Button btnEliminar = new Button("Eliminar");
        btnEliminar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 5 15;");
        
        btnEliminar.setOnAction(e -> {
            if (!cUser.esAdmin()) {
                mostrarError("Solo los administradores pueden eliminar contenidos");
                return;
            }
            
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Está seguro de eliminar este contenido?");
            confirmacion.setContentText("Esta acción no se puede deshacer");
            
            confirmacion.showAndWait().ifPresent(respuesta -> {
                if (respuesta == ButtonType.OK) {
                    boolean exito = cCont.eliminar(contenido.getId());
                    if (exito) {
                        mostrarMensaje("Contenido eliminado exitosamente");
                        cargarContenidos();
                    } else {
                        mostrarError("No se pudo eliminar el contenido");
                    }
                }
            });
        });
        
        // Solo mostrar botones si hay permisos
        if (cUser.esAdmin()) {
            botonesAccion.getChildren().addAll(lblEstado, spacer2, btnPublicar, btnEliminar);
        } else {
            botonesAccion.getChildren().addAll(lblEstado, spacer2);
        }
        
        card.getChildren().addAll(headerCard, botonesAccion);
        
        return card;
    }

    private HBox crearBarraSuperior() {
        HBox barra = new HBox(15);
        barra.setAlignment(Pos.CENTER_LEFT);
        barra.setPadding(new Insets(10));
        
        // Título CMS
        Label lblCMS = new Label("CMS");
        lblCMS.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        
        // Separador flexible
        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        
        // ComboBox de filtro
        cmbFiltroCategoria = new ComboBox<>();
        cmbFiltroCategoria.getItems().addAll("ALL", "Tecnología", "Ciencia", "Arte", "Educación", "Deportes");
        cmbFiltroCategoria.setValue("ALL");
        cmbFiltroCategoria.setPrefWidth(150);
        cmbFiltroCategoria.setStyle("-fx-font-size: 14px;");
        
        // Botón buscar
        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; " +
                          "-fx-padding: 8 20 8 20; -fx-cursor: hand;");
        btnBuscar.setOnAction(e -> buscarContenidos());
        
        // Separador
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        
        // Botones de creación
        Button btnNuevoArticulo = new Button("Nuevo Artículo");
        Button btnNuevoVideo = new Button("Nuevo Video");
        Button btnNuevaImagen = new Button("Nueva Imagen");
        
        String estiloBtnCrear = "-fx-background-color: #5dade2; -fx-text-fill: white; -fx-font-size: 13px; " +
                               "-fx-padding: 8 15 8 15; -fx-cursor: hand;";
        btnNuevoArticulo.setStyle(estiloBtnCrear);
        btnNuevoVideo.setStyle(estiloBtnCrear);
        btnNuevaImagen.setStyle(estiloBtnCrear);
        
        // Eventos de creación (temporales)
        btnNuevoArticulo.setOnAction(e -> abrirModalCrearArticulo());
        btnNuevoVideo.setOnAction(e -> abrirModalCrearVideo());
        btnNuevaImagen.setOnAction(e -> abrirModalCrearImagen());
        
        barra.getChildren().addAll(
            lblCMS,
            spacer1,
            cmbFiltroCategoria,
            btnBuscar,
            spacer2,
            btnNuevoArticulo,
            btnNuevoVideo,
            btnNuevaImagen
        );
        
        return barra;
    }

    private void buscarContenidos() {
        String categoriaSeleccionada = cmbFiltroCategoria.getValue();
        
        listaContenidos.getChildren().clear();
        ArrayList<Contenido> contenidos = cCont.listarTodos();
        
        // Filtrar por categoría si no es "ALL"
        ArrayList<Contenido> filtrados = new ArrayList<>();
        for (Contenido c : contenidos) {
            if (categoriaSeleccionada.equals("ALL") || 
                (c.getCategoria() != null && c.getCategoria().equals(categoriaSeleccionada))) {
                filtrados.add(c);
            }
        }
        
        if (filtrados.isEmpty()) {
            Label lblVacio = new Label("No se encontraron contenidos con los filtros seleccionados.");
            lblVacio.setFont(Font.font("Arial", 14));
            lblVacio.setStyle("-fx-text-fill: #7f8c8d;");
            listaContenidos.getChildren().add(lblVacio);
        } else {
            for (Contenido c : filtrados) {
                VBox card = crearCardContenido(c);
                listaContenidos.getChildren().add(card);
            }
        }
        
        mostrarMensaje("Se encontraron " + filtrados.size() + " contenido(s)");
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
        btnContenido.setOnAction(e -> {
            cargarContenidos();
            mostrarMensaje("Vista de Contenido actualizada");
        });
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
    
    private void abrirModalCrearArticulo() {
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.setTitle("Crear Nuevo Artículo");
        
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        
        Label lblTitulo = new Label("Nuevo Artículo");
        lblTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        // Campos del formulario
        TextField txtTitulo = new TextField();
        txtTitulo.setPromptText("Título del artículo");
        
        TextArea txtCuerpo = new TextArea();
        txtCuerpo.setPromptText("Contenido del artículo");
        txtCuerpo.setPrefRowCount(5);
        
        TextField txtCategoria = new TextField();
        txtCategoria.setPromptText("Categoría (ej: Tecnología)");
        
        TextField txtEtiquetas = new TextField();
        txtEtiquetas.setPromptText("Etiquetas separadas por comas");
        
        // Botones
        HBox botones = new HBox(10);
        botones.setAlignment(Pos.CENTER_RIGHT);
        
        Button btnGuardar = new Button("Guardar");
        btnGuardar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-padding: 8 20;");
        
        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-padding: 8 20;");
        
        btnGuardar.setOnAction(e -> {
            try {
                String titulo = txtTitulo.getText().trim();
                String cuerpo = txtCuerpo.getText().trim();
                String categoria = txtCategoria.getText().trim();
                String etiquetasStr = txtEtiquetas.getText().trim();
                
                if (titulo.isEmpty() || cuerpo.isEmpty()) {
                    mostrarError("El título y el cuerpo son obligatorios");
                    return;
                }
                
                ArrayList<String> etiquetas = new ArrayList<>();
                if (!etiquetasStr.isEmpty()) {
                    String[] etiqArray = etiquetasStr.split(",");
                    for (String etiq : etiqArray) {
                        etiquetas.add(etiq.trim());
                    }
                }
                
                String fecha = LocalDate.now().toString();
                int id = cCont.crearArticulo(titulo, cuerpo, 1, fecha, etiquetas, categoria);
                
                mostrarMensaje("Artículo creado exitosamente con ID: " + id);
                cargarContenidos();
                modal.close();
            } catch (Exception ex) {
                mostrarError("Error al crear artículo: " + ex.getMessage());
            }
        });
        
        btnCancelar.setOnAction(e -> modal.close());
        
        botones.getChildren().addAll(btnCancelar, btnGuardar);
        
        layout.getChildren().addAll(
            lblTitulo,
            new Label("Título:"), txtTitulo,
            new Label("Contenido:"), txtCuerpo,
            new Label("Categoría:"), txtCategoria,
            new Label("Etiquetas:"), txtEtiquetas,
            botones
        );
        
        Scene scene = new Scene(layout, 500, 450);
        modal.setScene(scene);
        modal.showAndWait();
    }
    
    private void abrirModalCrearVideo() {
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.setTitle("Crear Nuevo Video");
        
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        
        Label lblTitulo = new Label("Nuevo Video");
        lblTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        // Campos del formulario
        TextField txtTitulo = new TextField();
        txtTitulo.setPromptText("Título del video");
        
        TextField txtUrl = new TextField();
        txtUrl.setPromptText("URL del video");
        
        TextField txtDuracion = new TextField();
        txtDuracion.setPromptText("Duración en segundos");
        
        TextField txtCategoria = new TextField();
        txtCategoria.setPromptText("Categoría (ej: Educación)");
        
        TextField txtEtiquetas = new TextField();
        txtEtiquetas.setPromptText("Etiquetas separadas por comas");
        
        // Botones
        HBox botones = new HBox(10);
        botones.setAlignment(Pos.CENTER_RIGHT);
        
        Button btnGuardar = new Button("Guardar");
        btnGuardar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-padding: 8 20;");
        
        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-padding: 8 20;");
        
        btnGuardar.setOnAction(e -> {
            try {
                String titulo = txtTitulo.getText().trim();
                String url = txtUrl.getText().trim();
                String duracionStr = txtDuracion.getText().trim();
                String categoria = txtCategoria.getText().trim();
                String etiquetasStr = txtEtiquetas.getText().trim();
                
                if (titulo.isEmpty() || url.isEmpty() || duracionStr.isEmpty()) {
                    mostrarError("El título, URL y duración son obligatorios");
                    return;
                }
                
                int duracion = Integer.parseInt(duracionStr);
                
                ArrayList<String> etiquetas = new ArrayList<>();
                if (!etiquetasStr.isEmpty()) {
                    String[] etiqArray = etiquetasStr.split(",");
                    for (String etiq : etiqArray) {
                        etiquetas.add(etiq.trim());
                    }
                }
                
                String fecha = LocalDate.now().toString();
                int id = cCont.crearVideo(titulo, url, duracion, 1, fecha, etiquetas, categoria);
                
                mostrarMensaje("Video creado exitosamente con ID: " + id);
                cargarContenidos();
                modal.close();
            } catch (NumberFormatException ex) {
                mostrarError("La duración debe ser un número válido");
            } catch (Exception ex) {
                mostrarError("Error al crear video: " + ex.getMessage());
            }
        });
        
        btnCancelar.setOnAction(e -> modal.close());
        
        botones.getChildren().addAll(btnCancelar, btnGuardar);
        
        layout.getChildren().addAll(
            lblTitulo,
            new Label("Título:"), txtTitulo,
            new Label("URL:"), txtUrl,
            new Label("Duración (segundos):"), txtDuracion,
            new Label("Categoría:"), txtCategoria,
            new Label("Etiquetas:"), txtEtiquetas,
            botones
        );
        
        Scene scene = new Scene(layout, 500, 400);
        modal.setScene(scene);
        modal.showAndWait();
    }
    
    private void abrirModalCrearImagen() {
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.setTitle("Crear Nueva Imagen");
        
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        
        Label lblTitulo = new Label("Nueva Imagen");
        lblTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        // Campos del formulario
        TextField txtTitulo = new TextField();
        txtTitulo.setPromptText("Título de la imagen");
        
        TextField txtUrl = new TextField();
        txtUrl.setPromptText("URL de la imagen");
        
        TextField txtAncho = new TextField();
        txtAncho.setPromptText("Ancho en píxeles");
        
        TextField txtAlto = new TextField();
        txtAlto.setPromptText("Alto en píxeles");
        
        TextField txtCategoria = new TextField();
        txtCategoria.setPromptText("Categoría (ej: Arte)");
        
        TextField txtEtiquetas = new TextField();
        txtEtiquetas.setPromptText("Etiquetas separadas por comas");
        
        // Botones
        HBox botones = new HBox(10);
        botones.setAlignment(Pos.CENTER_RIGHT);
        
        Button btnGuardar = new Button("Guardar");
        btnGuardar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-padding: 8 20;");
        
        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-padding: 8 20;");
        
        btnGuardar.setOnAction(e -> {
            try {
                String titulo = txtTitulo.getText().trim();
                String url = txtUrl.getText().trim();
                String anchoStr = txtAncho.getText().trim();
                String altoStr = txtAlto.getText().trim();
                String categoria = txtCategoria.getText().trim();
                String etiquetasStr = txtEtiquetas.getText().trim();
                
                if (titulo.isEmpty() || url.isEmpty() || anchoStr.isEmpty() || altoStr.isEmpty()) {
                    mostrarError("Todos los campos principales son obligatorios");
                    return;
                }
                
                int ancho = Integer.parseInt(anchoStr);
                int alto = Integer.parseInt(altoStr);
                
                ArrayList<String> etiquetas = new ArrayList<>();
                if (!etiquetasStr.isEmpty()) {
                    String[] etiqArray = etiquetasStr.split(",");
                    for (String etiq : etiqArray) {
                        etiquetas.add(etiq.trim());
                    }
                }
                
                String fecha = LocalDate.now().toString();
                int id = cCont.crearImagen(titulo, url, ancho, alto, 1, fecha, etiquetas, categoria);
                
                mostrarMensaje("Imagen creada exitosamente con ID: " + id);
                cargarContenidos();
                modal.close();
            } catch (NumberFormatException ex) {
                mostrarError("El ancho y alto deben ser números válidos");
            } catch (Exception ex) {
                mostrarError("Error al crear imagen: " + ex.getMessage());
            }
        });
        
        btnCancelar.setOnAction(e -> modal.close());
        
        botones.getChildren().addAll(btnCancelar, btnGuardar);
        
        layout.getChildren().addAll(
            lblTitulo,
            new Label("Título:"), txtTitulo,
            new Label("URL:"), txtUrl,
            new Label("Ancho (px):"), txtAncho,
            new Label("Alto (px):"), txtAlto,
            new Label("Categoría:"), txtCategoria,
            new Label("Etiquetas:"), txtEtiquetas,
            botones
        );
        
        Scene scene = new Scene(layout, 500, 450);
        modal.setScene(scene);
        modal.showAndWait();
    }
    
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
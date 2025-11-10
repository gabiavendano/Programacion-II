package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/InicioSesion.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);
        stage.setTitle("Inicio de Sesion");
        stage.setScene(scene);
        stage.setResizable(false); // Hace que la ventana no sea redimensionable
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
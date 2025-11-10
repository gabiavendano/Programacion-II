package app.controller;

import app.model.Usuario;
import app.model.UsuarioDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.regex.Pattern;

public class RegisterController {
    @FXML private TextField IngreseNombre;

    @FXML private TextField IngreseEmail;

    @FXML private PasswordField IngreseContraseña;
    @FXML private PasswordField ConfirmarContraseña;

    @FXML private TextField VerContraseña;
    @FXML private TextField VerReconfirmar;

    @FXML private Button Ojo1;
    @FXML private Button Ojo2;
    @FXML private Button RegistrarBtn;

    private boolean mostrandoContra = false;
    private boolean mostrandoRecontra = false;

    @FXML
    private void guardarUsuario() {
        String nombre = IngreseNombre.getText();
        String email = IngreseEmail.getText();
        String contraseña = IngreseContraseña.getText();
        String confirmar = ConfirmarContraseña.getText();

        String VerContra = VerContraseña.getText();
        String VerReconf = VerReconfirmar.getText();

        if (nombre.trim().isEmpty() || email.trim().isEmpty() || contraseña.trim().isEmpty() || confirmar.trim().isEmpty()) {
            mostrarError("Error", "Todos los campos son obligatorios");
            return;
        }

        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!Pattern.matches(regex, email)) {
            mostrarError("Error", "El email ingresado no es válido");
            return;
        }

        if (UsuarioDAO.ValidarEmail(email)) {
            mostrarError("Error", "El email ya está registrado");
            return;
        }

        if (!contraseña.equals(confirmar)) {
            mostrarError("Error", "Las contraseñas no coinciden");
            return;
        }

        if (IngreseContraseña.getText().length() < 8) {
            mostrarError("Error", "La contraseña debe tener al menos 8 caracteres");
            return;
        }

        Usuario usuario = new Usuario(nombre, email, contraseña);

        if (UsuarioDAO.insertar(usuario)) {
            mostrarAlerta("Éxito", "Usuario registrado correctamente");

            // Limpiar campos
            IngreseNombre.clear();
            IngreseEmail.clear();
            IngreseContraseña.clear();
            ConfirmarContraseña.clear();

            Stage stageRegister = (Stage) IngreseNombre.getScene().getWindow();
            stageRegister.close();

        } else {
            mostrarError("Error", "No se pudo registrar el usuario");
        }
    }

    public void initialize() {
        // Ocultar los TextField en el arranque
        VerContraseña.setManaged(false);
        VerReconfirmar.setManaged(false);

        // Primer ojito 👁
        Ojo1.setOnAction(e -> {
            mostrandoContra = !mostrandoContra;
            if (mostrandoContra) {
                VerContraseña.setText(IngreseContraseña.getText());
                VerContraseña.setVisible(true);
                VerContraseña.setManaged(true);

                IngreseContraseña.setVisible(false);
                IngreseContraseña.setManaged(false);

                Ojo1.setText("🙈");
            } else {
                IngreseContraseña.setText(VerContraseña.getText());
                IngreseContraseña.setVisible(true);
                IngreseContraseña.setManaged(true);

                VerContraseña.setVisible(false);
                VerContraseña.setManaged(false);

                Ojo1.setText("👁");
            }
        });

        // Segundo ojito 👁
        Ojo2.setOnAction(e -> {
            mostrandoRecontra = !mostrandoRecontra;
            if (mostrandoRecontra) {
                VerReconfirmar.setText(ConfirmarContraseña.getText());
                VerReconfirmar.setVisible(true);
                VerReconfirmar.setManaged(true);

                ConfirmarContraseña.setVisible(false);
                ConfirmarContraseña.setManaged(false);

                Ojo2.setText("🙈");
            } else {
                ConfirmarContraseña.setText(VerReconfirmar.getText());
                ConfirmarContraseña.setVisible(true);
                ConfirmarContraseña.setManaged(true);

                VerReconfirmar.setVisible(false);
                VerReconfirmar.setManaged(false);

                Ojo2.setText("👁");
            }
        });
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
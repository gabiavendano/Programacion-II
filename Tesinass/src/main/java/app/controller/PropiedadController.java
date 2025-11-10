package app.controller;

import app.model.Propiedad;
import app.model.PropiedadDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

public class PropiedadController {

    // Formularios
    @FXML private TextField txtDireccion;
    @FXML private ComboBox<String> cbTipo;
    @FXML private TextField txtPrecio;
    @FXML private ComboBox<String> cbEstado;
    @FXML private TextField txtDescripcion;

    // Nuevos campos
    @FXML private TextField txtMetrosLote;
    @FXML private TextField txtMetrosCubiertos;
    @FXML private TextField txtAntiguedad;
    @FXML private TextField txtDormitorios;
    @FXML private TextField txtBanos;
    @FXML private CheckBox cbPileta;
    @FXML private CheckBox cbCochera;
    @FXML private TextField txtExpensas;
    @FXML private ComboBox<String> cbMoneda;
    @FXML private TextField txtOtros;
    @FXML private TextField txtFoto;

    // Paneles
    @FXML private AnchorPane cRegistrarPropiedad;
    @FXML private AnchorPane verPropiedad;

    // Tabla
    @FXML private TableView<Propiedad> tablaPropiedad;
    @FXML private TableColumn<Propiedad, String> colDireccion;
    @FXML private TableColumn<Propiedad, String> colTipo;
    @FXML private TableColumn<Propiedad, String> colDescripcion;
    @FXML private TableColumn<Propiedad, Double> colPrecio;
    @FXML private TableColumn<Propiedad, String> colEstado;

    private ObservableList<Propiedad> listaPropiedades = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Llenar combos
        cbTipo.getItems().addAll("Casa", "Departamento", "Terreno", "Local", "Otro");
        cbEstado.getItems().addAll("Disponible", "Reservada", "Vendida", "Alquilada", "Inactiva");
        cbMoneda.getItems().addAll("USD", "ARS", "EUR", "BRL");

        // Inicializar columnas
        colDireccion.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getDireccion()));
        colTipo.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getTipo()));
        colDescripcion.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getDescripcion()));
        colPrecio.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getPrecio()));
        colEstado.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getEstado()));

        cargarPropiedades();
    }

    // ===== MÉTODOS PRINCIPALES =====
    public void cargarPropiedades() {
        listaPropiedades.clear();
        listaPropiedades.addAll(PropiedadDAO.obtenerTodas());
        tablaPropiedad.setItems(listaPropiedades);
    }

    @FXML
    private void registrarPropiedad() {
        try {
            String direccion = txtDireccion.getText();
            String tipo = cbTipo.getValue();
            String descripcion = txtDescripcion.getText();
            String estado = cbEstado.getValue();
            String precioStr = txtPrecio.getText();
            String moneda = cbMoneda.getValue();
            String otros = txtOtros.getText();
            String foto = txtFoto.getText();

            double precio = Double.parseDouble(precioStr);
            double metrosLote = txtMetrosLote.getText().isEmpty() ? 0 : Double.parseDouble(txtMetrosLote.getText());
            double metrosCubiertos = txtMetrosCubiertos.getText().isEmpty() ? 0 : Double.parseDouble(txtMetrosCubiertos.getText());
            int antiguedad = txtAntiguedad.getText().isEmpty() ? 0 : Integer.parseInt(txtAntiguedad.getText());
            int dormitorios = txtDormitorios.getText().isEmpty() ? 0 : Integer.parseInt(txtDormitorios.getText());
            int banos = txtBanos.getText().isEmpty() ? 0 : Integer.parseInt(txtBanos.getText());
            double expensas = txtExpensas.getText().isEmpty() ? 0 : Double.parseDouble(txtExpensas.getText());
            boolean pileta = cbPileta.isSelected();
            boolean cochera = cbCochera.isSelected();

            if (direccion.isEmpty() || tipo == null || descripcion.isEmpty() || estado == null || precioStr.isEmpty() || moneda == null) {
                mostrarError("Todos los campos obligatorios deben completarse");
                return;
            }

            // Aquí debes pasar el idUsuario actual (ejemplo: 1)
            int idUsuario = 1;

            Propiedad propiedad = new Propiedad(direccion, tipo, metrosLote, metrosCubiertos, antiguedad,
                    dormitorios, banos, pileta, cochera, expensas, descripcion, precio, moneda, otros, foto, estado, idUsuario);

            if (PropiedadDAO.insertar(propiedad)) {
                mostrarAlerta("Propiedad registrada correctamente");
                limpiarFormulario();
                cargarPropiedades();
                verRegistrarPropiedad(false);
            } else {
                mostrarError("No se pudo registrar la propiedad");
            }
        } catch (NumberFormatException e) {
            mostrarError("Los valores numéricos son inválidos");
        }
    }

    @FXML
    private void cancelarRegistroPropiedad() {
        limpiarFormulario();
        verRegistrarPropiedad(false);
    }

    @FXML
    private void seleccionarPropiedadParaEditar() {
        Propiedad seleccionado = tablaPropiedad.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Selecciona una propiedad para editar");
            return;
        }

        txtDireccion.setText(seleccionado.getDireccion());
        cbTipo.setValue(seleccionado.getTipo());
        txtDescripcion.setText(seleccionado.getDescripcion());
        txtPrecio.setText(String.valueOf(seleccionado.getPrecio()));
        cbEstado.setValue(seleccionado.getEstado());
        cbMoneda.setValue(seleccionado.getMoneda());
        txtMetrosLote.setText(String.valueOf(seleccionado.getMetrosLote()));
        txtMetrosCubiertos.setText(String.valueOf(seleccionado.getMetrosCubiertos()));
        txtAntiguedad.setText(String.valueOf(seleccionado.getAntiguedad()));
        txtDormitorios.setText(String.valueOf(seleccionado.getDormitorios()));
        txtBanos.setText(String.valueOf(seleccionado.getBanos()));
        txtExpensas.setText(String.valueOf(seleccionado.getExpensas()));
        cbPileta.setSelected(seleccionado.isPileta());
        cbCochera.setSelected(seleccionado.isCochera());
        txtOtros.setText(seleccionado.getOtros());
        txtFoto.setText(seleccionado.getFoto());

        verRegistrarPropiedad(true);
    }

    @FXML
    private void desactivarPropiedad() {
        Propiedad seleccionado = tablaPropiedad.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Selecciona una propiedad para desactivar");
            return;
        }

        // Cambio: Usar desactivar() en lugar de eliminar()
        if (PropiedadDAO.desactivar(seleccionado.getIdPropiedad())) {
            mostrarAlerta("Propiedad desactivada correctamente");
            cargarPropiedades(); // Recargar la lista para reflejar el cambio de estado
        } else {
            mostrarError("No se pudo desactivar la propiedad");
        }
    }

    @FXML
    private void activarPropiedad() {
        Propiedad seleccionado = tablaPropiedad.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Selecciona una propiedad para activar");
            return;
        }

        // Opcional: Método para reactivar propiedades
        if (PropiedadDAO.activar(seleccionado.getIdPropiedad())) {
            mostrarAlerta("Propiedad activada correctamente");
            cargarPropiedades();
        } else {
            mostrarError("No se pudo activar la propiedad");
        }
    }

    // ===== MÉTODOS AUXILIARES =====
    private void limpiarFormulario() {
        txtDireccion.clear();
        cbTipo.getSelectionModel().clearSelection();
        txtDescripcion.clear();
        txtPrecio.clear();
        cbEstado.getSelectionModel().clearSelection();
        cbMoneda.getSelectionModel().clearSelection();
        txtMetrosLote.clear();
        txtMetrosCubiertos.clear();
        txtAntiguedad.clear();
        txtDormitorios.clear();
        txtBanos.clear();
        txtExpensas.clear();
        cbPileta.setSelected(false);
        cbCochera.setSelected(false);
        txtOtros.clear();
        txtFoto.clear();
    }

    private void verPropiedad(boolean mostrar) {
        verPropiedad.setVisible(mostrar);
    }

    private void verRegistrarPropiedad(boolean mostrar) {
        cRegistrarPropiedad.setVisible(mostrar);
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
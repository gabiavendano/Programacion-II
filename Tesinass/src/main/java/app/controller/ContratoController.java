package app.controller;

import app.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ContratoController {

    // ========================= Formulario nuevo contrato =========================
    @FXML private ComboBox<Propiedad> cbPropiedad;
    @FXML private ComboBox<Cliente> cbCliente;
    @FXML private ComboBox<String> cbTipoContrato;
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;
    @FXML private TextField txtMonto;
    @FXML private ComboBox<String> cbTipoAlquiler;
    @FXML private TextField txtPorcentajeAumento;
    @FXML private ComboBox<String> cbFrecuenciaAumento;
    @FXML private ComboBox<String> cbIndiceAumento;
    @FXML private TextArea txtServiciosIncluidos;

    // ========================= Tabla de contratos =========================
    @FXML private TableView<Contrato> tablaContratos;
    @FXML private TableColumn<Contrato, String> colDireccion;
    @FXML private TableColumn<Contrato, String> colCliente;
    @FXML private TableColumn<Contrato, String> colTipo;
    @FXML private TableColumn<Contrato, String> colFechaInicio;
    @FXML private TableColumn<Contrato, String> colFechaFin;
    @FXML private TableColumn<Contrato, Double> colMonto;

    // ========================= Paneles =========================
    @FXML private AnchorPane cNuevoContrato;
    @FXML private AnchorPane verContratos;

    private ObservableList<Contrato> listaContratos = FXCollections.observableArrayList();

    // ========================= Referencia a Menu =========================
    private Menu menuController;

    public void setMenuController(Menu menuController) {
        this.menuController = menuController;
    }

    // ========================= Inicialización =========================
    @FXML
    public void initialize() {
        System.out.println("=== DEBUG: Inicializando ContratoController ===");

        // Debug de componentes
        debugComponent("cbPropiedad", cbPropiedad);
        debugComponent("cbCliente", cbCliente);
        debugComponent("cbTipoContrato", cbTipoContrato);
        debugComponent("cbTipoAlquiler", cbTipoAlquiler);
        debugComponent("cbFrecuenciaAumento", cbFrecuenciaAumento);
        debugComponent("cbIndiceAumento", cbIndiceAumento);
        debugComponent("dpFechaInicio", dpFechaInicio);
        debugComponent("dpFechaFin", dpFechaFin);
        debugComponent("txtMonto", txtMonto);

        // Inicializar ComboBox con opciones
        inicializarCombos();

        if (tablaContratos != null && colDireccion != null) {
            configurarTablaContratos();
            cargarContratos();
        }

        if (cbPropiedad != null && cbCliente != null) {
            cargarPropiedades();
            cargarClientes();
        }

        System.out.println("ContratoController inicializado correctamente");
    }

    private void debugComponent(String name, Object component) {
        if (component == null) {
            System.out.println("DEBUG: " + name + " es NULL");
        } else {
            System.out.println("DEBUG: " + name + " se inyectó correctamente");
        }
    }

    private void inicializarCombos() {
        // Inicializar ComboBox con opciones fijas
        if (cbTipoContrato != null) {
            cbTipoContrato.getItems().clear();
            cbTipoContrato.getItems().addAll("Alquiler", "Venta", "Permuta");
            System.out.println("cbTipoContrato items: " + cbTipoContrato.getItems().size());
        }

        if (cbTipoAlquiler != null) {
            cbTipoAlquiler.getItems().clear();
            cbTipoAlquiler.getItems().addAll("Vivienda Familiar", "Comercio", "Otro");
            System.out.println("cbTipoAlquiler items: " + cbTipoAlquiler.getItems().size());
        }

        if (cbFrecuenciaAumento != null) {
            cbFrecuenciaAumento.getItems().clear();
            cbFrecuenciaAumento.getItems().addAll("3 meses", "6 meses", "12 meses");
            System.out.println("cbFrecuenciaAumento items: " + cbFrecuenciaAumento.getItems().size());
        }

        if (cbIndiceAumento != null) {
            cbIndiceAumento.getItems().clear();
            cbIndiceAumento.getItems().addAll("IPC", "Contractual", "Otro");
            System.out.println("cbIndiceAumento items: " + cbIndiceAumento.getItems().size());
        }
    }

    // ========================= Configurar tabla =========================
    private void configurarTablaContratos() {
        try {
            colDireccion.setCellValueFactory(cell -> {
                Propiedad propiedad = PropiedadDAO.obtenerPorId(cell.getValue().getIdPropiedad());
                return new javafx.beans.property.SimpleStringProperty(propiedad != null ? propiedad.getDireccion() : "");
            });

            colCliente.setCellValueFactory(cell -> {
                Cliente cliente = ClienteDAO.obtenerPorId(cell.getValue().getIdCliente());
                return new javafx.beans.property.SimpleStringProperty(cliente != null ?
                        cliente.getNombre() + " " + cliente.getApellido() : "");
            });

            colTipo.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getTipo()));

            colFechaInicio.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                    cell.getValue().getFechaInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

            colFechaFin.setCellValueFactory(cell -> {
                LocalDate fechaFin = cell.getValue().getFechaFin();
                return new javafx.beans.property.SimpleStringProperty(
                        fechaFin != null ? fechaFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "");
            });

            colMonto.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getMonto()));
        } catch (Exception e) {
            System.out.println("Error configurando tabla de contratos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ========================= Cargar ComboBox =========================
    public void cargarPropiedades() {
        if (cbPropiedad != null) {
            try {
                List<Propiedad> propiedades = PropiedadDAO.obtenerTodas();
                System.out.println("Propiedades cargadas: " + propiedades.size());

                cbPropiedad.getItems().clear();
                cbPropiedad.getItems().addAll(propiedades);

                cbPropiedad.setConverter(new StringConverter<Propiedad>() {
                    @Override
                    public String toString(Propiedad propiedad) {
                        if (propiedad == null) return "Seleccionar propiedad";
                        return propiedad.getDireccion() + " - " + propiedad.getTipo() + " (" + propiedad.getEstado() + ")";
                    }
                    @Override
                    public Propiedad fromString(String string) { return null; }
                });
            } catch (Exception e) {
                System.out.println("Error cargando propiedades: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("cbPropiedad es null en cargarPropiedades()");
        }
    }

    public void cargarClientes() {
        if (cbCliente != null) {
            try {
                List<Cliente> clientes = ClienteDAO.obtenerTodos();
                System.out.println("Clientes cargados: " + clientes.size());

                cbCliente.getItems().clear();
                cbCliente.getItems().addAll(clientes);

                cbCliente.setConverter(new StringConverter<Cliente>() {
                    @Override
                    public String toString(Cliente cliente) {
                        if (cliente == null) return "Seleccionar cliente";
                        return cliente.getNombre() + " " + cliente.getApellido() + " - " + cliente.getDni();
                    }
                    @Override
                    public Cliente fromString(String string) { return null; }
                });
            } catch (Exception e) {
                System.out.println("Error cargando clientes: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("cbCliente es null en cargarClientes()");
        }
    }

    private void cargarContratos() {
        if (tablaContratos != null) {
            try {
                listaContratos.clear();
                List<Contrato> contratos = ContratoDAO.obtenerTodos();
                System.out.println("Contratos cargados: " + contratos.size());
                listaContratos.addAll(contratos);
                tablaContratos.setItems(listaContratos);
            } catch (Exception e) {
                System.out.println("Error cargando contratos: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // ========================= Registrar contrato =========================
    @FXML
    private void registrarContrato() {
        try {
            Propiedad propiedad = cbPropiedad.getValue();
            Cliente cliente = cbCliente.getValue();
            String tipo = cbTipoContrato.getValue();
            LocalDate fechaInicio = dpFechaInicio.getValue();
            LocalDate fechaFin = dpFechaFin.getValue();

            // Validaciones básicas
            if (propiedad == null) {
                mostrarError("Error", "Debe seleccionar una propiedad");
                return;
            }
            if (cliente == null) {
                mostrarError("Error", "Debe seleccionar un cliente");
                return;
            }
            if (tipo == null) {
                mostrarError("Error", "Debe seleccionar un tipo de contrato");
                return;
            }
            if (fechaInicio == null) {
                mostrarError("Error", "Debe seleccionar una fecha de inicio");
                return;
            }

            double monto = txtMonto != null && !txtMonto.getText().isEmpty() ?
                    Double.parseDouble(txtMonto.getText()) : 0;
            String tipoAlquiler = cbTipoAlquiler != null ? cbTipoAlquiler.getValue() : "Vivienda Familiar";
            double porcentajeAumento = txtPorcentajeAumento != null && !txtPorcentajeAumento.getText().isEmpty() ?
                    Double.parseDouble(txtPorcentajeAumento.getText()) : 0;

            int frecuenciaAumento = 12;
            if (cbFrecuenciaAumento != null && cbFrecuenciaAumento.getValue() != null) {
                String frecuenciaStr = cbFrecuenciaAumento.getValue().split(" ")[0];
                frecuenciaAumento = Integer.parseInt(frecuenciaStr);
            }

            String indiceAumento = cbIndiceAumento != null ? cbIndiceAumento.getValue() : "IPC";
            String serviciosIncluidos = txtServiciosIncluidos != null ? txtServiciosIncluidos.getText() : "";

            Contrato contrato = new Contrato(0, tipo, fechaInicio, fechaFin, monto,
                    cliente.getIdCliente(), propiedad.getIdPropiedad(),
                    porcentajeAumento, frecuenciaAumento, indiceAumento,
                    serviciosIncluidos, tipoAlquiler);

            if (ContratoDAO.insertar(contrato)) {
                if ("Alquiler".equals(tipo) || "Venta".equals(tipo)) {
                    propiedad.setEstado("Alquiler".equals(tipo) ? "Alquilada" : "Vendida");
                    PropiedadDAO.actualizar(propiedad);
                }
                mostrarAlerta("Éxito", "Contrato registrado correctamente");
                limpiarFormulario();
                cargarContratos();
                verNuevoContrato(false);
            } else {
                mostrarError("Error", "No se pudo registrar el contrato");
            }

        } catch (NumberFormatException e) {
            mostrarError("Error", "Los valores numéricos son inválidos");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error", "Error inesperado: " + e.getMessage());
        }
    }

    // ========================= Generar recibo =========================
    @FXML
    private void generarRecibo() {
        if (tablaContratos != null) {
            Contrato contratoSeleccionado = tablaContratos.getSelectionModel().getSelectedItem();
            if (contratoSeleccionado == null) {
                mostrarError("Error", "Selecciona un contrato");
                return;
            }
            if (!"Alquiler".equals(contratoSeleccionado.getTipo())) {
                mostrarError("Error", "Solo se generan recibos para contratos de alquiler");
                return;
            }
            generarReciboAlquiler(contratoSeleccionado);
        }
    }

    private void generarReciboAlquiler(Contrato contrato) {
        try {
            Cliente cliente = ClienteDAO.obtenerPorId(contrato.getIdCliente());
            Propiedad propiedad = PropiedadDAO.obtenerPorId(contrato.getIdPropiedad());
            if (cliente == null || propiedad == null) {
                mostrarError("Error", "No se pudo obtener información");
                return;
            }

            LocalDate hoy = LocalDate.now();
            String mesReferencia = obtenerMesReferencia(hoy);
            LocalDate vencimiento = hoy.plusDays(10);

            double servicios = 0;
            double total = contrato.getMonto() + servicios;

            Recibo recibo = new Recibo(0, contrato.getIdContrato(), hoy, mesReferencia,
                    contrato.getMonto(), servicios, total, vencimiento, false);

            if (ReciboDAO.insertar(recibo)) {
                mostrarRecibo(recibo, cliente, propiedad, contrato);
                mostrarAlerta("Éxito", "Recibo generado correctamente");
            } else {
                mostrarError("Error", "No se pudo generar el recibo");
            }
        } catch (Exception e) {
            mostrarError("Error", "Error al generar recibo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String obtenerMesReferencia(LocalDate fecha) {
        String[] meses = {"Enero","Febrero","Marzo","Abril","Mayo","Junio",
                "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
        return meses[fecha.getMonthValue()-1] + " " + fecha.getYear();
    }

    private void mostrarRecibo(Recibo recibo, Cliente cliente, Propiedad propiedad, Contrato contrato) {
        String reciboTexto =
                "Fecha: " + recibo.getFechaEmision().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n\n" +
                        "Recibí del señor " + cliente.getNombre() + " " + cliente.getApellido() + "\n" +
                        "La cantidad de $" + String.format("%,.2f", recibo.getMontoAlquiler()) + "\n" +
                        "Importe del alquiler de " + contrato.getTipoAlquiler() + "\n" +
                        "Que ocupa en la calle " + propiedad.getDireccion() + "\n" +
                        "Correspondiente al mes de " + recibo.getMesReferencia() + "\n" +
                        "Servicios: $" + String.format("%,.2f", recibo.getServicios()) + "\n" +
                        "Que vence el día " + recibo.getFechaVencimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n" +
                        "Según liquidación al pie.\n\n" +
                        "Son: $" + String.format("%,.2f", recibo.getTotal()) + "\n";

        TextArea textArea = new TextArea(reciboTexto);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefSize(400, 300);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Recibo de Alquiler");
        alert.setHeaderText("Recibo generado");
        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }

    // ========================= Cancelar y limpiar formulario =========================
    @FXML
    private void cancelarRegistro() {
        limpiarFormulario();
        verNuevoContrato(false);
    }

    private void limpiarFormulario() {
        if (cbPropiedad != null) cbPropiedad.getSelectionModel().clearSelection();
        if (cbCliente != null) cbCliente.getSelectionModel().clearSelection();
        if (cbTipoContrato != null) cbTipoContrato.getSelectionModel().clearSelection();
        if (dpFechaInicio != null) dpFechaInicio.setValue(null);
        if (dpFechaFin != null) dpFechaFin.setValue(null);
        if (txtMonto != null) txtMonto.clear();
        if (cbTipoAlquiler != null) cbTipoAlquiler.getSelectionModel().clearSelection();
        if (txtPorcentajeAumento != null) txtPorcentajeAumento.clear();
        if (cbFrecuenciaAumento != null) cbFrecuenciaAumento.getSelectionModel().clearSelection();
        if (cbIndiceAumento != null) cbIndiceAumento.getSelectionModel().clearSelection();
        if (txtServiciosIncluidos != null) txtServiciosIncluidos.clear();
    }

    private void verNuevoContrato(boolean mostrar) {
        if (cNuevoContrato != null && verContratos != null) {
            cNuevoContrato.setVisible(mostrar);
            verContratos.setVisible(!mostrar);
        }
    }

    // ========================= Alertas =========================
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

    // ========================= Botones "+" =========================
    @FXML
    private void abrirRegistrarPropiedad() {
        if (menuController != null) {
            menuController.abrirRegistrarPropiedad();
        } else {
            mostrarError("Error", "No se puede abrir registrar propiedad");
        }
    }

    @FXML
    private void abrirRegistrarCliente() {
        if (menuController != null) {
            menuController.abrirRegistrarCliente();
        } else {
            mostrarError("Error", "No se puede abrir registrar cliente");
        }
    }

    // ========================= Métodos públicos para ser llamados desde Menu =========================
    public void refrescarDatos() {
        cargarPropiedades();
        cargarClientes();
        cargarContratos();
        inicializarCombos();
    }
}
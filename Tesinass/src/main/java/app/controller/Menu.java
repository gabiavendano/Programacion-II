package app.controller;

import app.model.*;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Menu {

    @FXML private AnchorPane BienvenidaPane;

    // SideBar
    @FXML private VBox SideBar;
    @FXML private VBox SubMenuPropiedades;
    @FXML private Button BtnVerPropiedades;
    @FXML private Button BtnAgregarPropiedad;

    @FXML private VBox SubMenuCliente;
    @FXML private Button BtnVerClientes;
    @FXML private Button BtnAgregarCliente;

    @FXML private VBox SubMenuContratos;
    @FXML private Button BtnVerContrato;
    @FXML private Button BtnNuevoContrato;

    @FXML private Button BtnSalir;

    // ===== CLIENTES =====
    @FXML private AnchorPane cRegistrar;
    @FXML private TextField txtNombre, txtApellido, txtDni, txtTelefono, txtEmail;
    @FXML private ComboBox<TipoCliente> comboRol;
    @FXML private AnchorPane verCliente;
    @FXML private TableView<Cliente> tablaCliente;
    @FXML private TableColumn<Cliente, String> colNombre, colApellido, colDNI, colTelefono, colEmail, colTipoCliente, colEstadoCliente;

    // Nuevos campos para asignar propiedad a propietario
    @FXML private Label lblPropiedadAsignar;
    @FXML private ComboBox<Propiedad> cbPropiedadAsignar;

    private ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();

    // Ventana Modificar Cliente
    @FXML private AnchorPane modCliente;
    @FXML private TextField txtNombre1;
    @FXML private TextField txtApellido1;
    @FXML private TextField txtDni1;
    @FXML private TextField txtTelefono1;
    @FXML private TextField txtEmail1;
    @FXML private ComboBox<TipoCliente> comboRol1;

    // ===== PROPIEDADES =====
    @FXML private AnchorPane cRegistrarPropiedad;
    @FXML private TextField txtDireccion, txtDescripcion, txtPrecio, txtMetrosLote, txtMetrosCubiertos, txtAntiguedad,
            txtDormitorios, txtBanos, txtExpensas, txtOtros, txtFoto;
    @FXML private ComboBox<String> cbTipo, cbEstado, cbMoneda;
    @FXML private CheckBox cbPileta, cbCochera;
    @FXML private AnchorPane verPropiedad;
    @FXML private TableView<Propiedad> tablaPropiedad;
    @FXML private TableColumn<Propiedad, String> colDireccion, colTipo, colDescripcion, colEstado, colMoneda, colOtros, colFoto;
    @FXML private TableColumn<Propiedad, Double> colPrecio, colMetrosLote, colMetrosCubiertos, colExpensas;
    @FXML private TableColumn<Propiedad, Integer> colDormitorios, colBanos;
    @FXML private TableColumn<Propiedad, Boolean> colPileta, colCochera;
    @FXML private Button btnAgregarPropiedad, btnModificarPropiedad, btnDesactivarPropiedad, btnActivarPropiedad, btnEliminarPropiedad, btnGestionarEstadoPropiedad;
    @FXML private ComboBox<String> cbFiltroEstado, cbFiltroTipo;
    private ObservableList<Propiedad> listaPropiedades = FXCollections.observableArrayList();
    private Propiedad propiedadSeleccionada;

    // ===== CONTRATOS =====
    @FXML private AnchorPane verContratos;
    @FXML private AnchorPane cNuevoContrato;

    // Componentes del panel de nuevo contrato
    @FXML private ComboBox<Propiedad> cbPropiedad;
    @FXML private ComboBox<Cliente> cbCliente;
    @FXML private ComboBox<String> cbTipoContrato;
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;
    @FXML private TextField txtMonto;

    // Campos de alquiler (se mostrarán solo cuando se seleccione "Alquiler")
    @FXML private Label lblTipoAlquiler, lblPorcentajeAumento, lblFrecuenciaAumento, lblIndiceAumento, lblServiciosIncluidos;
    @FXML private ComboBox<String> cbTipoAlquiler;
    @FXML private TextField txtPorcentajeAumento;
    @FXML private ComboBox<String> cbFrecuenciaAumento;
    @FXML private ComboBox<String> cbIndiceAumento;
    @FXML private TextArea txtServiciosIncluidos;

    @FXML private TableView<Contrato> tablaContratos;
    @FXML private TableColumn<Contrato, String> colDireccionContrato, colCliente, colTipoContrato, colFechaInicio, colFechaFin;
    @FXML private TableColumn<Contrato, Double> colMonto;
    @FXML private Button btnGenerarRecibo, btnGestionarEstadoContrato;

    private clienteController clienteController = new clienteController();
    private ObservableList<Contrato> listaContratos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        System.out.println("=== INICIALIZANDO MENU PRINCIPAL ===");

        // SideBar
        BtnVerPropiedades.setOnAction(e -> {
            abrirTablaPropiedad();
            cargarPropiedades();
        });

        BtnAgregarPropiedad.setOnAction(e -> abrirRegistrarPropiedad());

        BtnVerClientes.setOnAction(e -> {
            abrirTablaCliente();
        });

        BtnAgregarCliente.setOnAction(e -> {
            abrirRegistrarCliente();
        });

        BtnVerContrato.setOnAction(e -> {
            abrirVerContratos();
        });

        BtnNuevoContrato.setOnAction(e -> {
            abrirNuevoContrato();
        });

        BtnSalir.setOnAction(e -> salirAplicacion());

        // Botones de propiedades
        btnAgregarPropiedad.setOnAction(e -> abrirRegistrarPropiedad());
        btnModificarPropiedad.setOnAction(e -> seleccionarPropiedadParaEditar());
        btnDesactivarPropiedad.setOnAction(e -> desactivarPropiedad());
        btnActivarPropiedad.setOnAction(e -> activarPropiedad());
        btnEliminarPropiedad.setOnAction(e -> eliminarPropiedad());
        // Botón generar recibo
        btnGenerarRecibo.setOnAction(e -> generarRecibo());
        // Filtros de propiedades
        cbFiltroEstado.getItems().addAll("Todos", "Disponible", "Reservada", "Vendida", "Alquilada", "Inactiva");
        cbFiltroTipo.getItems().addAll("Todos", "Casa", "Departamento", "Terreno", "Local", "Otro");
        cbFiltroEstado.setOnAction(e -> filtrarPropiedades());
        cbFiltroTipo.setOnAction(e -> filtrarPropiedades());

        // Inicializar tablas y combos
        initializeClientes();
        initializePropiedades();
        initializeContratos();
        initializeCombos();
        initializeContratoPanel();

        // Configurar visibilidad de campos de alquiler
        configurarVisibilidadCamposAlquiler();

        // Configurar visibilidad de campo de propiedad para propietarios
        configurarCampoPropiedadPropietario();

        System.out.println("Menu inicializado correctamente");
    }

    // ===== BOTÓN SALIR =====
    @FXML
    private void salirAplicacion() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Salir");
        confirmacion.setHeaderText("¿Está seguro de que desea salir?");
        confirmacion.setContentText("Se cerrará la aplicación.");

        if (confirmacion.showAndWait().get() == ButtonType.OK) {
            Stage stage = (Stage) BtnSalir.getScene().getWindow();
            stage.close();
        }
    }

    // ===== CONFIGURACIÓN DE CAMPOS DINÁMICOS =====

    private void configurarVisibilidadCamposAlquiler() {
        cbTipoContrato.valueProperty().addListener((observable, oldValue, newValue) -> {
            boolean esAlquiler = "Alquiler".equals(newValue);

            // Controlar visibilidad de campos de alquiler
            controlarVisibilidadComponente(lblTipoAlquiler, cbTipoAlquiler, esAlquiler);
            controlarVisibilidadComponente(lblPorcentajeAumento, txtPorcentajeAumento, esAlquiler);
            controlarVisibilidadComponente(lblFrecuenciaAumento, cbFrecuenciaAumento, esAlquiler);
            controlarVisibilidadComponente(lblIndiceAumento, cbIndiceAumento, esAlquiler);
            controlarVisibilidadComponente(lblServiciosIncluidos, txtServiciosIncluidos, esAlquiler);

            // Limpiar campos si se cambia de Alquiler a otro tipo
            if (!esAlquiler && "Alquiler".equals(oldValue)) {
                limpiarCamposAlquiler();
            }
        });

        // Configurar visibilidad inicial
        boolean esAlquilerInicial = "Alquiler".equals(cbTipoContrato.getValue());
        controlarVisibilidadComponente(lblTipoAlquiler, cbTipoAlquiler, esAlquilerInicial);
        controlarVisibilidadComponente(lblPorcentajeAumento, txtPorcentajeAumento, esAlquilerInicial);
        controlarVisibilidadComponente(lblFrecuenciaAumento, cbFrecuenciaAumento, esAlquilerInicial);
        controlarVisibilidadComponente(lblIndiceAumento, cbIndiceAumento, esAlquilerInicial);
        controlarVisibilidadComponente(lblServiciosIncluidos, txtServiciosIncluidos, esAlquilerInicial);
    }

    private void configurarCampoPropiedadPropietario() {
        comboRol.valueProperty().addListener((observable, oldValue, newValue) -> {
            boolean esPropietario = newValue != null && "Propietario".equals(newValue.getTipo());

            // Mostrar/ocultar campo de propiedad para propietarios
            controlarVisibilidadComponente(lblPropiedadAsignar, cbPropiedadAsignar, esPropietario);

            if (esPropietario) {
                cargarPropiedadesDisponiblesEnCombo();
            }
        });

        // Configurar convertidor para el ComboBox de propiedades
        if (cbPropiedadAsignar != null) {
            cbPropiedadAsignar.setConverter(new StringConverter<Propiedad>() {
                @Override
                public String toString(Propiedad propiedad) {
                    return propiedad != null ? propiedad.getDireccion() + " - " + propiedad.getTipo() : "";
                }
                @Override
                public Propiedad fromString(String string) { return null; }
            });
        }
    }

    private void controlarVisibilidadComponente(Label label, Control control, boolean visible) {
        if (label != null) {
            label.setVisible(visible);
            label.setManaged(visible);
        }
        if (control != null) {
            control.setVisible(visible);
            control.setManaged(visible);
        }
    }

    private void limpiarCamposAlquiler() {
        if (cbTipoAlquiler != null) cbTipoAlquiler.getSelectionModel().clearSelection();
        if (txtPorcentajeAumento != null) txtPorcentajeAumento.clear();
        if (cbFrecuenciaAumento != null) cbFrecuenciaAumento.getSelectionModel().clearSelection();
        if (cbIndiceAumento != null) cbIndiceAumento.getSelectionModel().clearSelection();
        if (txtServiciosIncluidos != null) txtServiciosIncluidos.clear();
    }

    private void cargarPropiedadesDisponiblesEnCombo() {
        if (cbPropiedadAsignar != null) {
            try {
                List<Propiedad> propiedades = PropiedadDAO.obtenerPorEstado("Disponible");
                cbPropiedadAsignar.getItems().clear();
                cbPropiedadAsignar.getItems().addAll(propiedades);
                System.out.println("Propiedades disponibles cargadas: " + propiedades.size());
            } catch (Exception e) {
                System.out.println("Error cargando propiedades disponibles: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void initializeClientes() {
        try {
            colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
            colDNI.setCellValueFactory(new PropertyValueFactory<>("dni"));
            colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
            colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            colTipoCliente.setCellValueFactory(new PropertyValueFactory<>("tipo"));
            colEstadoCliente.setCellValueFactory(new PropertyValueFactory<>("estado"));
            cargarClientes();
        } catch (Exception e) {
            mostrarError("Error", "Error inicializando tabla de clientes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initializePropiedades() {
        try {
            colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
            colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
            colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
            colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
            colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
            colMoneda.setCellValueFactory(new PropertyValueFactory<>("moneda"));
            colMetrosLote.setCellValueFactory(new PropertyValueFactory<>("metrosLote"));
            colMetrosCubiertos.setCellValueFactory(new PropertyValueFactory<>("metrosCubiertos"));
            colDormitorios.setCellValueFactory(new PropertyValueFactory<>("dormitorios"));
            colBanos.setCellValueFactory(new PropertyValueFactory<>("banos"));
            colExpensas.setCellValueFactory(new PropertyValueFactory<>("expensas"));
            colPileta.setCellValueFactory(new PropertyValueFactory<>("pileta"));
            colCochera.setCellValueFactory(new PropertyValueFactory<>("cochera"));
            colOtros.setCellValueFactory(new PropertyValueFactory<>("otros"));
            colFoto.setCellValueFactory(new PropertyValueFactory<>("foto"));
        } catch (Exception e) {
            mostrarError("Error", "Error inicializando tabla de propiedades: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initializeContratos() {
        try {
            colDireccionContrato.setCellValueFactory(cell -> {
                try {
                    Propiedad propiedad = PropiedadDAO.obtenerPorId(cell.getValue().getIdPropiedad());
                    return new javafx.beans.property.SimpleStringProperty(
                            propiedad != null ? propiedad.getDireccion() : "Propiedad no encontrada"
                    );
                } catch (Exception e) {
                    return new javafx.beans.property.SimpleStringProperty("Error al cargar");
                }
            });

            colCliente.setCellValueFactory(cell -> {
                try {
                    Cliente cliente = ClienteDAO.obtenerPorId(cell.getValue().getIdCliente());
                    return new javafx.beans.property.SimpleStringProperty(
                            cliente != null ?
                                    cliente.getNombre() + " " + cliente.getApellido() :
                                    "Cliente no encontrado"
                    );
                } catch (Exception e) {
                    return new javafx.beans.property.SimpleStringProperty("Error al cargar");
                }
            });

            colTipoContrato.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getTipo()));

            colFechaInicio.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                    cell.getValue().getFechaInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

            colFechaFin.setCellValueFactory(cell -> {
                LocalDate fechaFin = cell.getValue().getFechaFin();
                return new javafx.beans.property.SimpleStringProperty(
                        fechaFin != null ? fechaFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "");
            });

            colMonto.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getMonto()));

        } catch (Exception e) {
            mostrarError("Error", "Error inicializando tabla de contratos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initializeCombos() {
        try {
            cbTipo.getItems().addAll("Casa", "Departamento", "Terreno", "Local", "Otro");
            cbEstado.getItems().addAll("Disponible", "Reservada", "Vendida", "Alquilada", "Inactiva");
            cbMoneda.getItems().addAll("USD", "ARS", "EUR", "BRL");
            comboRol.getItems().addAll(TipoClienteDAO.obtenerTipoC());
            comboRol1.getItems().addAll(TipoClienteDAO.obtenerTipoC());
        } catch (Exception e) {
            mostrarError("Error", "Error inicializando combos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initializeContratoPanel() {
        try {
            System.out.println("Inicializando panel de contrato...");

            // Inicializar ComboBox con opciones fijas
            if (cbTipoContrato != null) {
                cbTipoContrato.getItems().addAll("Alquiler", "Venta", "Permuta");
                System.out.println("cbTipoContrato inicializado");
            }

            if (cbTipoAlquiler != null) {
                cbTipoAlquiler.getItems().addAll("Vivienda Familiar", "Comercio", "Otro");
                System.out.println("cbTipoAlquiler inicializado");
            }

            if (cbFrecuenciaAumento != null) {
                cbFrecuenciaAumento.getItems().addAll("3 meses", "6 meses", "12 meses");
                System.out.println("cbFrecuenciaAumento inicializado");
            }

            if (cbIndiceAumento != null) {
                cbIndiceAumento.getItems().addAll("IPC", "Contractual", "Otro");
                System.out.println("cbIndiceAumento inicializado");
            }

            // Configurar convertidores para los ComboBox de datos
            if (cbPropiedad != null) {
                cbPropiedad.setConverter(new StringConverter<Propiedad>() {
                    @Override
                    public String toString(Propiedad propiedad) {
                        return propiedad != null ? propiedad.getDireccion() + " - " + propiedad.getTipo() : "";
                    }
                    @Override
                    public Propiedad fromString(String string) { return null; }
                });
            }

            if (cbCliente != null) {
                cbCliente.setConverter(new StringConverter<Cliente>() {
                    @Override
                    public String toString(Cliente cliente) {
                        return cliente != null ? cliente.getNombre() + " " + cliente.getApellido() + " - " + cliente.getDni() : "";
                    }
                    @Override
                    public Cliente fromString(String string) { return null; }
                });
            }

        } catch (Exception e) {
            System.out.println("Error inicializando panel de contrato: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void ocultarVentanas() {
        BienvenidaPane.setVisible(false);
        cRegistrar.setVisible(false);
        modCliente.setVisible(false);
        verCliente.setVisible(false);
        cRegistrarPropiedad.setVisible(false);
        verPropiedad.setVisible(false);
        cNuevoContrato.setVisible(false);
        verContratos.setVisible(false);
    }

    // ===== CLIENTES =====
    private void cargarClientes() {
        try {
            listaClientes.clear();
            List<Cliente> clientes = ClienteDAO.obtenerTodos();
            listaClientes.addAll(clientes);
            tablaCliente.setItems(listaClientes);
            System.out.println("Clientes cargados: " + clientes.size());
        } catch (Exception e) {
            mostrarError("Error", "No se pudieron cargar los Clientes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void abrirRegistrarCliente() {
        ocultarVentanas();
        cRegistrar.setVisible(true);
        // Limpiar campos al abrir
        limpiarFormularioCliente();
    }

    public void abrirTablaCliente() {
        ocultarVentanas();
        verCliente.setVisible(true);
        cargarClientes();
    }

    public void limpiarRegistrarC() {
        limpiarFormularioCliente();
        ocultarVentanas();
        verCliente.setVisible(true);
    }

    private void limpiarFormularioCliente() {
        txtNombre.clear();
        txtApellido.clear();
        txtDni.clear();
        txtTelefono.clear();
        txtEmail.clear();
        comboRol.getSelectionModel().clearSelection();
        if (cbPropiedadAsignar != null) {
            cbPropiedadAsignar.getSelectionModel().clearSelection();
        }
    }

    // VALIDACIONES MEJORADAS PARA CLIENTES
    public void registrarCliente() {
        try {
            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            String dni = txtDni.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String email = txtEmail.getText().trim();
            String estado = "Activo";

            // Validaciones completas
            if(nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
                mostrarError("Error", "Todos los campos son obligatorios");
                return;
            }

            // Validar nombre y apellido (solo letras y espacios)
            String nombreApellidoRegex = "^[A-Za-zÁáÉéÍíÓóÚúÑñ\\s]+$";
            if (!nombre.matches(nombreApellidoRegex)) {
                mostrarError("Error", "El nombre solo puede contener letras y espacios");
                return;
            }

            if (!apellido.matches(nombreApellidoRegex)) {
                mostrarError("Error", "El apellido solo puede contener letras y espacios");
                return;
            }

            // Validar DNI (solo números, 7-8 dígitos)
            String dniRegex = "^\\d{7,8}$";
            if (!dni.matches(dniRegex)) {
                mostrarError("Error", "El DNI debe contener 7 u 8 números");
                return;
            }

            // Validar teléfono (números, pueden tener +, espacios, paréntesis)
            String telRegex = "^[+]?[\\d\\s\\-\\(\\)]{7,15}$";
            if (!telefono.matches(telRegex)) {
                mostrarError("Error", "El número de teléfono no es válido");
                return;
            }

            // Validar email
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
            if (!email.matches(emailRegex)) {
                mostrarError("Error", "El correo electrónico no es válido");
                return;
            }

            // Verificar si el DNI ya existe
            if (ClienteDAO.existeDni(dni)) {
                mostrarError("Error", "El DNI ya está registrado");
                return;
            }

            // Verificar si el email ya existe
            if (ClienteDAO.existeEmail(email)) {
                mostrarError("Error", "El email ya está registrado");
                return;
            }

            TipoCliente tipoCliente = comboRol.getValue();
            if (tipoCliente == null) {
                mostrarError("Error", "Debe seleccionar un tipo de cliente");
                return;
            }

            Cliente cliente = new Cliente(nombre, apellido, dni, telefono, email, tipoCliente.getTipo(), estado);

            // Si es propietario y se seleccionó una propiedad, asignarla
            if ("Propietario".equals(tipoCliente.getTipo()) && cbPropiedadAsignar.getValue() != null) {
                Propiedad propiedadSeleccionada = cbPropiedadAsignar.getValue();
                // Aquí podrías implementar la lógica para asignar la propiedad al propietario
                // Por ejemplo, actualizar el idUsuario de la propiedad
                propiedadSeleccionada.setIdUsuario(1); // ID del usuario actual
                if (PropiedadDAO.actualizar(propiedadSeleccionada)) {
                    System.out.println("Propiedad asignada al propietario: " + propiedadSeleccionada.getDireccion());
                }
            }

            if (ClienteDAO.insertar(cliente, tipoCliente.getIdTipo())) {
                cargarClientes();
                limpiarRegistrarC();
                mostrarAlerta("Registro", "Cliente registrado correctamente");
            } else {
                mostrarError("Error", "No se pudo registrar el cliente");
            }
        } catch (Exception e) {
            mostrarError("Error", "Error al registrar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void seleccionarClienteParaEditar() {
        try {
            Cliente seleccionado = tablaCliente.getSelectionModel().getSelectedItem();
            if (seleccionado == null) {
                mostrarError("Error", "Debes seleccionar un cliente");
                return;
            }

            Cliente cliente = clienteController.seleccionarCliente(seleccionado);

            txtNombre1.setText(cliente.getNombre());
            txtApellido1.setText(cliente.getApellido());
            txtDni1.setText(cliente.getDni());
            txtTelefono1.setText(cliente.getTelefono());
            txtEmail1.setText(cliente.getEmail());

            for (TipoCliente tc : comboRol1.getItems()) {
                if (tc.getTipo().equals(cliente.getTipo())) {
                    comboRol1.setValue(tc);
                    break;
                }
            }

            ocultarVentanas();
            modCliente.setVisible(true);
        } catch (Exception e) {
            mostrarError("Error", "Error al seleccionar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void guardarEdicionCliente() {
        String nombre = txtNombre1.getText().trim();
        String apellido = txtApellido1.getText().trim();
        String dni = txtDni1.getText().trim();
        String telefono = txtTelefono1.getText().trim();
        String email = txtEmail1.getText().trim();
        TipoCliente tipoCliente = comboRol1.getValue();

        if (tipoCliente == null) {
            mostrarError("Error", "Debe seleccionar un tipo de cliente");
            return;
        }

        if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
            mostrarError("Error", "Todos los campos son obligatorios");
            return;
        }

        // Validaciones
        String nombreApellidoRegex = "^[A-Za-zÁáÉéÍíÓóÚúÑñ\\s]+$";
        if (!nombre.matches(nombreApellidoRegex)) {
            mostrarError("Error", "El nombre solo puede contener letras y espacios");
            return;
        }

        if (!apellido.matches(nombreApellidoRegex)) {
            mostrarError("Error", "El apellido solo puede contener letras y espacios");
            return;
        }

        String dniRegex = "^\\d{7,8}$";
        if (!dni.matches(dniRegex)) {
            mostrarError("Error", "El DNI debe contener 7 u 8 números");
            return;
        }

        String telRegex = "^[+]?[\\d\\s\\-\\(\\)]{7,15}$";
        if (!telefono.matches(telRegex)) {
            mostrarError("Error", "El número de teléfono no es válido");
            return;
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!email.matches(emailRegex)) {
            mostrarError("Error", "El correo electrónico no es válido");
            return;
        }

        try {
            boolean exito = clienteController.actualizarCliente(
                    nombre,
                    apellido,
                    dni,
                    telefono,
                    email,
                    tipoCliente
            );

            if (exito) {
                mostrarAlerta("Edición", "Cliente actualizado correctamente");
                cargarClientes();
                ocultarVentanas();
                verCliente.setVisible(true);
            } else mostrarError("Error", "No se pudo actualizar el cliente");
        } catch (Exception e) {
            mostrarError("Error", "Error al guardar edición: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void cancelarEdicionCliente() {
        try {
            clienteController.cancelarEdicion();
            txtNombre1.clear();
            txtApellido1.clear();
            txtDni1.clear();
            txtTelefono1.clear();
            txtEmail1.clear();
            ocultarVentanas();
            verCliente.setVisible(true);
        } catch (Exception e) {
            mostrarError("Error", "Error al cancelar edición: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void eliminarCliente() {
        try {
            Cliente seleccionado = tablaCliente.getSelectionModel().getSelectedItem();
            if (seleccionado == null) {
                mostrarError("Error", "Selecciona un cliente para eliminar");
                return;
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Estás seguro de eliminar este cliente?");
            confirmacion.setContentText("Esta acción no se puede deshacer.");

            if (confirmacion.showAndWait().get() == ButtonType.OK) {
                if (ClienteDAO.eliminar(seleccionado)) {
                    mostrarAlerta("Éxito", "Cliente eliminado correctamente");
                    cargarClientes();
                } else mostrarError("Error", "No se pudo eliminar el cliente");
            }
        } catch (Exception e) {
            mostrarError("Error", "Error al eliminar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void desactivarCliente() {
        Cliente seleccionado = tablaCliente.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Error", "Debe seleccionar un cliente");
            return;
        }

        if (ClienteDAO.desactivar(seleccionado.getIdCliente())) {
            mostrarAlerta("Desactivación", "Cliente desactivado correctamente");
            cargarClientes();
        } else {
            mostrarError("Error", "No se pudo desactivar el cliente");
        }
    }

    @FXML
    private void activarCliente() {
        Cliente seleccionado = tablaCliente.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Error", "Debe seleccionar un cliente");
            return;
        }

        if (ClienteDAO.activar(seleccionado.getIdCliente())) {
            mostrarAlerta("Activación", "Cliente activado correctamente");
            cargarClientes();
        } else {
            mostrarError("Error", "No se pudo activar el cliente");
        }
    }

    // NUEVO MÉTODO: Gestionar estado del cliente
    @FXML
    private void gestionarEstadoCliente() {
        try {
            Cliente seleccionado = tablaCliente.getSelectionModel().getSelectedItem();
            if (seleccionado == null) {
                mostrarError("Error", "Selecciona un cliente");
                return;
            }

            String estadoActual = seleccionado.getEstado();
            String nuevoEstado = "Inactivo".equals(estadoActual) ? "Activo" : "Inactivo";
            String mensaje = "Inactivo".equals(estadoActual) ? "activar" : "desactivar";

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar cambio de estado");
            confirmacion.setHeaderText("¿Estás seguro de " + mensaje + " este cliente?");
            confirmacion.setContentText("El cliente será marcado como: " + nuevoEstado);

            if (confirmacion.showAndWait().get() == ButtonType.OK) {
                if (ClienteDAO.cambiarEstado(seleccionado.getIdCliente(), nuevoEstado)) {
                    mostrarAlerta("Éxito", "Cliente " + mensaje + "do correctamente");
                    cargarClientes();
                } else {
                    mostrarError("Error", "No se pudo cambiar el estado del cliente");
                }
            }
        } catch (Exception e) {
            mostrarError("Error", "Error al cambiar estado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===== PROPIEDADES =====
    private void cargarPropiedades() {
        try {
            listaPropiedades.clear();
            listaPropiedades.addAll(PropiedadDAO.obtenerTodas());
            tablaPropiedad.setItems(listaPropiedades);
            System.out.println("Propiedades cargadas: " + listaPropiedades.size());
        } catch (Exception e) {
            mostrarError("Error", "Error cargando propiedades: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void filtrarPropiedades() {
        try {
            String estado = cbFiltroEstado.getValue();
            String tipo = cbFiltroTipo.getValue();

            List<Propiedad> propiedadesFiltradas = PropiedadDAO.obtenerTodas();

            if (estado != null && !"Todos".equals(estado)) {
                propiedadesFiltradas = propiedadesFiltradas.stream()
                        .filter(p -> p.getEstado().equals(estado))
                        .collect(Collectors.toList());
            }

            if (tipo != null && !"Todos".equals(tipo)) {
                propiedadesFiltradas = propiedadesFiltradas.stream()
                        .filter(p -> p.getTipo().equals(tipo))
                        .collect(Collectors.toList());
            }

            listaPropiedades.setAll(propiedadesFiltradas);
        } catch (Exception e) {
            mostrarError("Error", "Error al filtrar propiedades: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void abrirRegistrarPropiedad() {
        propiedadSeleccionada = null;
        limpiarFormularioPropiedad();
        ocultarVentanas();
        cRegistrarPropiedad.setVisible(true);
    }

    private void abrirTablaPropiedad() {
        ocultarVentanas();
        verPropiedad.setVisible(true);
        cargarPropiedades();
    }

    @FXML
    private void seleccionarPropiedadParaEditar() {
        try {
            Propiedad seleccionada = tablaPropiedad.getSelectionModel().getSelectedItem();
            if (seleccionada == null) {
                mostrarError("Error", "Selecciona una propiedad para editar");
                return;
            }

            // Llenar el formulario con los datos de la propiedad seleccionada
            txtDireccion.setText(seleccionada.getDireccion());
            cbTipo.setValue(seleccionada.getTipo());
            txtPrecio.setText(String.valueOf(seleccionada.getPrecio()));
            cbEstado.setValue(seleccionada.getEstado());
            cbMoneda.setValue(seleccionada.getMoneda());
            txtMetrosLote.setText(String.valueOf(seleccionada.getMetrosLote()));
            txtMetrosCubiertos.setText(String.valueOf(seleccionada.getMetrosCubiertos()));
            txtAntiguedad.setText(String.valueOf(seleccionada.getAntiguedad()));
            txtDormitorios.setText(String.valueOf(seleccionada.getDormitorios()));
            txtBanos.setText(String.valueOf(seleccionada.getBanos()));
            txtExpensas.setText(String.valueOf(seleccionada.getExpensas()));
            cbPileta.setSelected(seleccionada.isPileta());
            cbCochera.setSelected(seleccionada.isCochera());
            txtDescripcion.setText(seleccionada.getDescripcion());
            txtOtros.setText(seleccionada.getOtros());
            txtFoto.setText(seleccionada.getFoto());

            // Guardar la propiedad seleccionada para la edición
            propiedadSeleccionada = seleccionada;

            // Cambiar a la vista de registro para editar
            ocultarVentanas();
            cRegistrarPropiedad.setVisible(true);

        } catch (Exception e) {
            mostrarError("Error", "Error al seleccionar propiedad: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void desactivarPropiedad() {
        try {
            Propiedad seleccionada = tablaPropiedad.getSelectionModel().getSelectedItem();
            if (seleccionada == null) {
                mostrarError("Error", "Selecciona una propiedad para desactivar");
                return;
            }

            if (PropiedadDAO.desactivar(seleccionada.getIdPropiedad())) {
                mostrarAlerta("Éxito", "Propiedad desactivada correctamente");
                cargarPropiedades();
            } else {
                mostrarError("Error", "No se pudo desactivar la propiedad");
            }
        } catch (Exception e) {
            mostrarError("Error", "Error al desactivar propiedad: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void activarPropiedad() {
        try {
            Propiedad seleccionada = tablaPropiedad.getSelectionModel().getSelectedItem();
            if (seleccionada == null) {
                mostrarError("Error", "Selecciona una propiedad para activar");
                return;
            }

            if (PropiedadDAO.activar(seleccionada.getIdPropiedad())) {
                mostrarAlerta("Éxito", "Propiedad activada correctamente");
                cargarPropiedades();
            } else {
                mostrarError("Error", "No se pudo activar la propiedad");
            }
        } catch (Exception e) {
            mostrarError("Error", "Error al activar propiedad: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // NUEVO MÉTODO: Gestionar estado de propiedad
    @FXML
    private void gestionarEstadoPropiedad() {
        try {
            Propiedad seleccionada = tablaPropiedad.getSelectionModel().getSelectedItem();
            if (seleccionada == null) {
                mostrarError("Error", "Selecciona una propiedad");
                return;
            }

            String estadoActual = seleccionada.getEstado();
            String nuevoEstado = "Inactiva".equals(estadoActual) ? "Disponible" : "Inactiva";
            String mensaje = "Inactiva".equals(estadoActual) ? "activar" : "desactivar";

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar cambio de estado");
            confirmacion.setHeaderText("¿Estás seguro de " + mensaje + " esta propiedad?");
            confirmacion.setContentText("La propiedad será marcada como: " + nuevoEstado);

            if (confirmacion.showAndWait().get() == ButtonType.OK) {
                if (PropiedadDAO.cambiarEstado(seleccionada.getIdPropiedad(), nuevoEstado)) {
                    mostrarAlerta("Éxito", "Propiedad " + mensaje + "da correctamente");
                    cargarPropiedades();
                } else {
                    mostrarError("Error", "No se pudo cambiar el estado de la propiedad");
                }
            }
        } catch (Exception e) {
            mostrarError("Error", "Error al cambiar estado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void eliminarPropiedad() {
        try {
            Propiedad seleccionada = tablaPropiedad.getSelectionModel().getSelectedItem();
            if (seleccionada == null) {
                mostrarError("Error", "Selecciona una propiedad para eliminar");
                return;
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Estás seguro de eliminar esta propiedad?");
            confirmacion.setContentText("Esta acción no se puede deshacer.");

            if (confirmacion.showAndWait().get() == ButtonType.OK) {
                if (PropiedadDAO.eliminar(seleccionada.getIdPropiedad())) {
                    mostrarAlerta("Éxito", "Propiedad eliminada permanentemente");
                    cargarPropiedades();
                } else {
                    mostrarError("Error", "No se pudo eliminar la propiedad");
                }
            }
        } catch (Exception e) {
            mostrarError("Error", "Error al eliminar propiedad: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void limpiarRegistrarP() {
        limpiarFormularioPropiedad();
        ocultarVentanas();
        verPropiedad.setVisible(true);
    }

    private void limpiarFormularioPropiedad() {
        if (txtDireccion != null) txtDireccion.clear();
        if (cbTipo != null) cbTipo.getSelectionModel().clearSelection();
        if (cbEstado != null) cbEstado.getSelectionModel().clearSelection();
        if (cbMoneda != null) cbMoneda.getSelectionModel().clearSelection();
        if (txtDescripcion != null) txtDescripcion.clear();
        if (txtPrecio != null) txtPrecio.clear();
        if (txtMetrosLote != null) txtMetrosLote.clear();
        if (txtMetrosCubiertos != null) txtMetrosCubiertos.clear();
        if (txtAntiguedad != null) txtAntiguedad.clear();
        if (txtDormitorios != null) txtDormitorios.clear();
        if (txtBanos != null) txtBanos.clear();
        if (txtExpensas != null) txtExpensas.clear();
        if (txtOtros != null) txtOtros.clear();
        if (txtFoto != null) txtFoto.clear();
        if (cbPileta != null) cbPileta.setSelected(false);
        if (cbCochera != null) cbCochera.setSelected(false);
        propiedadSeleccionada = null;
    }

    // VALIDACIONES MEJORADAS PARA PROPIEDADES
    public void registrarPropiedad() {
        try {
            // Validar campos obligatorios
            if (txtDireccion.getText().trim().isEmpty() || cbTipo.getValue() == null ||
                    cbEstado.getValue() == null || cbMoneda.getValue() == null ||
                    txtDescripcion.getText().trim().isEmpty() || txtPrecio.getText().trim().isEmpty()) {
                mostrarError("Error", "Todos los campos obligatorios deben completarse");
                return;
            }

            // Validar precio (número positivo)
            double precio;
            try {
                precio = Double.parseDouble(txtPrecio.getText());
                if (precio <= 0) {
                    mostrarError("Error", "El precio debe ser mayor a 0");
                    return;
                }
            } catch (NumberFormatException e) {
                mostrarError("Error", "El precio debe ser un número válido");
                return;
            }

            // Validar metros (si se ingresan)
            if (!txtMetrosLote.getText().isEmpty()) {
                try {
                    double metrosLote = Double.parseDouble(txtMetrosLote.getText());
                    if (metrosLote < 0) {
                        mostrarError("Error", "Los metros de lote no pueden ser negativos");
                        return;
                    }
                } catch (NumberFormatException e) {
                    mostrarError("Error", "Los metros de lote deben ser un número válido");
                    return;
                }
            }

            if (!txtMetrosCubiertos.getText().isEmpty()) {
                try {
                    double metrosCubiertos = Double.parseDouble(txtMetrosCubiertos.getText());
                    if (metrosCubiertos < 0) {
                        mostrarError("Error", "Los metros cubiertos no pueden ser negativos");
                        return;
                    }
                } catch (NumberFormatException e) {
                    mostrarError("Error", "Los metros cubiertos deben ser un número válido");
                    return;
                }
            }

            // Validar números enteros positivos
            if (!txtDormitorios.getText().isEmpty()) {
                try {
                    int dormitorios = Integer.parseInt(txtDormitorios.getText());
                    if (dormitorios < 0) {
                        mostrarError("Error", "El número de dormitorios no puede ser negativo");
                        return;
                    }
                } catch (NumberFormatException e) {
                    mostrarError("Error", "El número de dormitorios debe ser un número entero");
                    return;
                }
            }

            if (!txtBanos.getText().isEmpty()) {
                try {
                    int banos = Integer.parseInt(txtBanos.getText());
                    if (banos < 0) {
                        mostrarError("Error", "El número de baños no puede ser negativo");
                        return;
                    }
                } catch (NumberFormatException e) {
                    mostrarError("Error", "El número de baños debe ser un número entero");
                    return;
                }
            }

            if (!txtAntiguedad.getText().isEmpty()) {
                try {
                    int antiguedad = Integer.parseInt(txtAntiguedad.getText());
                    if (antiguedad < 0) {
                        mostrarError("Error", "La antigüedad no puede ser negativa");
                        return;
                    }
                } catch (NumberFormatException e) {
                    mostrarError("Error", "La antigüedad debe ser un número entero");
                    return;
                }
            }

            // Validar expensas
            if (!txtExpensas.getText().isEmpty()) {
                try {
                    double expensas = Double.parseDouble(txtExpensas.getText());
                    if (expensas < 0) {
                        mostrarError("Error", "Las expensas no pueden ser negativas");
                        return;
                    }
                } catch (NumberFormatException e) {
                    mostrarError("Error", "Las expensas deben ser un número válido");
                    return;
                }
            }

            // Obtener valores del formulario
            String direccion = txtDireccion.getText();
            String tipo = cbTipo.getValue();
            String estado = cbEstado.getValue();
            String moneda = cbMoneda.getValue();
            boolean pileta = cbPileta.isSelected();
            boolean cochera = cbCochera.isSelected();
            String descripcion = txtDescripcion.getText();
            double metrosLote = txtMetrosLote.getText().isEmpty() ? 0 : Double.parseDouble(txtMetrosLote.getText());
            double metrosCubiertos = txtMetrosCubiertos.getText().isEmpty() ? 0 : Double.parseDouble(txtMetrosCubiertos.getText());
            int antiguedad = txtAntiguedad.getText().isEmpty() ? 0 : Integer.parseInt(txtAntiguedad.getText());
            int dormitorios = txtDormitorios.getText().isEmpty() ? 0 : Integer.parseInt(txtDormitorios.getText());
            int banos = txtBanos.getText().isEmpty() ? 0 : Integer.parseInt(txtBanos.getText());
            double expensas = txtExpensas.getText().isEmpty() ? 0 : Double.parseDouble(txtExpensas.getText());
            String otros = txtOtros.getText();
            String foto = txtFoto.getText();
            int idUsuario = 1; // ID del usuario actual

            if (propiedadSeleccionada != null) {
                // Modo edición - Actualizar propiedad existente
                propiedadSeleccionada.setDireccion(direccion);
                propiedadSeleccionada.setTipo(tipo);
                propiedadSeleccionada.setEstado(estado);
                propiedadSeleccionada.setMoneda(moneda);
                propiedadSeleccionada.setPileta(pileta);
                propiedadSeleccionada.setCochera(cochera);
                propiedadSeleccionada.setDescripcion(descripcion);
                propiedadSeleccionada.setPrecio(precio);
                propiedadSeleccionada.setMetrosLote(metrosLote);
                propiedadSeleccionada.setMetrosCubiertos(metrosCubiertos);
                propiedadSeleccionada.setAntiguedad(antiguedad);
                propiedadSeleccionada.setDormitorios(dormitorios);
                propiedadSeleccionada.setBanos(banos);
                propiedadSeleccionada.setExpensas(expensas);
                propiedadSeleccionada.setOtros(otros);
                propiedadSeleccionada.setFoto(foto);

                if (PropiedadDAO.actualizar(propiedadSeleccionada)) {
                    mostrarAlerta("Éxito", "Propiedad actualizada correctamente");
                    limpiarFormularioPropiedad();
                    cargarPropiedades();
                    ocultarVentanas();
                    verPropiedad.setVisible(true);
                } else {
                    mostrarError("Error", "No se pudo actualizar la propiedad");
                }
            } else {
                // Modo registro - Insertar nueva propiedad
                Propiedad propiedad = new Propiedad(direccion, tipo, metrosLote, metrosCubiertos, antiguedad,
                        dormitorios, banos, pileta, cochera, expensas, descripcion, precio, moneda, otros, foto, estado, idUsuario);

                if (PropiedadDAO.insertar(propiedad)) {
                    mostrarAlerta("Éxito", "Propiedad registrada correctamente");
                    limpiarFormularioPropiedad();
                    cargarPropiedades();
                    ocultarVentanas();
                    verPropiedad.setVisible(true);
                } else {
                    mostrarError("Error", "No se pudo registrar la propiedad");
                }
            }
        } catch (NumberFormatException e) {
            mostrarError("Error", "Los valores numéricos deben contener números válidos");
            e.printStackTrace();
        } catch (Exception e) {
            mostrarError("Error", "Error al procesar la propiedad: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===== CONTRATOS =====
    @FXML
    private void abrirVerContratos() {
        ocultarVentanas();
        verContratos.setVisible(true);
        cargarContratos();
    }

    // Métodos para cargar datos en los ComboBox
    private void cargarPropiedadesEnCombo() {
        if (cbPropiedad != null) {
            try {
                List<Propiedad> propiedades = PropiedadDAO.obtenerDisponiblesParaContrato();
                cbPropiedad.getItems().clear();
                cbPropiedad.getItems().addAll(propiedades);
                System.out.println("Propiedades cargadas en combo: " + propiedades.size());
            } catch (Exception e) {
                System.out.println("Error cargando propiedades en combo: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void cargarClientesEnCombo() {
        if (cbCliente != null) {
            try {
                List<Cliente> clientes = ClienteDAO.obtenerTodos();
                cbCliente.getItems().clear();
                cbCliente.getItems().addAll(clientes);
                System.out.println("Clientes cargados en combo: " + clientes.size());
            } catch (Exception e) {
                System.out.println("Error cargando clientes en combo: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void abrirNuevoContrato() {
        ocultarVentanas();
        cNuevoContrato.setVisible(true);
        limpiarFormularioContrato();

        // Cargar datos en los ComboBox
        cargarPropiedadesEnCombo();
        cargarClientesEnCombo();

        System.out.println("Panel de nuevo contrato abierto");
    }

    // Método para seleccionar contrato para editar
    @FXML
    private void seleccionarContratoParaEditar() {
        try {
            Contrato contratoSeleccionado = tablaContratos.getSelectionModel().getSelectedItem();
            if (contratoSeleccionado == null) {
                mostrarError("Error", "Selecciona un contrato para editar");
                return;
            }

            // Cargar datos del contrato en el formulario
            cargarDatosContratoEnFormulario(contratoSeleccionado);

            // Cambiar a la vista de edición
            ocultarVentanas();
            cNuevoContrato.setVisible(true);

        } catch (Exception e) {
            mostrarError("Error", "Error al seleccionar contrato: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarDatosContratoEnFormulario(Contrato contrato) {
        try {
            // Cargar propiedades y clientes primero
            cargarPropiedadesEnCombo();
            cargarClientesEnCombo();

            // Seleccionar la propiedad y cliente correspondientes
            Propiedad propiedad = PropiedadDAO.obtenerPorId(contrato.getIdPropiedad());
            Cliente cliente = ClienteDAO.obtenerPorId(contrato.getIdCliente());

            if (propiedad != null) {
                cbPropiedad.getSelectionModel().select(propiedad);
            }
            if (cliente != null) {
                cbCliente.getSelectionModel().select(cliente);
            }

            // Cargar el resto de los datos
            cbTipoContrato.getSelectionModel().select(contrato.getTipo());
            dpFechaInicio.setValue(contrato.getFechaInicio());
            dpFechaFin.setValue(contrato.getFechaFin());
            txtMonto.setText(String.valueOf(contrato.getMonto()));

            if (contrato.getTipoAlquiler() != null) {
                cbTipoAlquiler.getSelectionModel().select(contrato.getTipoAlquiler());
            }

            txtPorcentajeAumento.setText(String.valueOf(contrato.getPorcentajeAumento()));

            // Configurar frecuencia de aumento
            String frecuencia = contrato.getFrecuenciaAumento() + " meses";
            cbFrecuenciaAumento.getSelectionModel().select(frecuencia);

            if (contrato.getIndiceAumento() != null) {
                cbIndiceAumento.getSelectionModel().select(contrato.getIndiceAumento());
            }

            if (contrato.getServiciosIncluidos() != null) {
                txtServiciosIncluidos.setText(contrato.getServiciosIncluidos());
            }

        } catch (Exception e) {
            System.out.println("Error cargando datos del contrato en formulario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Agregar manejadores de eventos para los botones del panel de contrato
    @FXML
    private void abrirRegistrarPropiedadDesdeContrato() {
        abrirRegistrarPropiedad();
    }

    @FXML
    private void abrirRegistrarClienteDesdeContrato() {
        abrirRegistrarCliente();
    }

    // VALIDACIONES MEJORADAS PARA CONTRATOS
    @FXML
    private void registrarContrato() {
        try {
            // Validar campos obligatorios
            if (cbPropiedad.getValue() == null) {
                mostrarError("Error", "Seleccione una propiedad");
                return;
            }
            if (cbCliente.getValue() == null) {
                mostrarError("Error", "Seleccione un cliente");
                return;
            }
            if (cbTipoContrato.getValue() == null) {
                mostrarError("Error", "Seleccione un tipo de contrato");
                return;
            }
            if (dpFechaInicio.getValue() == null) {
                mostrarError("Error", "Seleccione una fecha de inicio");
                return;
            }

            Propiedad propiedad = cbPropiedad.getValue();
            Cliente cliente = cbCliente.getValue();
            String tipo = cbTipoContrato.getValue();
            LocalDate fechaInicio = dpFechaInicio.getValue();
            LocalDate fechaFin = dpFechaFin.getValue();

            // Validar monto
            double monto = 0;
            if (!txtMonto.getText().isEmpty()) {
                try {
                    monto = Double.parseDouble(txtMonto.getText());
                    if (monto < 0) {
                        mostrarError("Error", "El monto no puede ser negativo");
                        return;
                    }
                } catch (NumberFormatException e) {
                    mostrarError("Error", "El monto debe ser un número válido");
                    return;
                }
            }

            // Validar campos específicos de alquiler
            String tipoAlquiler = null;
            double porcentajeAumento = 0;
            int frecuenciaAumento = 12;
            String indiceAumento = null;
            String serviciosIncluidos = null;

            boolean esAlquiler = "Alquiler".equals(tipo);
            if (esAlquiler) {
                if (cbTipoAlquiler.getValue() == null) {
                    mostrarError("Error", "Seleccione el tipo de alquiler");
                    return;
                }

                // VALIDACIÓN DEL PORCENTAJE DE AUMENTO
                if (!txtPorcentajeAumento.getText().isEmpty()) {
                    try {
                        porcentajeAumento = Double.parseDouble(txtPorcentajeAumento.getText());

                        // Validar que el porcentaje no sea mayor a 100
                        if (porcentajeAumento > 100) {
                            mostrarError("Error", "El porcentaje de aumento no puede ser mayor a 100%");
                            return;
                        }

                        // Validar que el porcentaje no sea negativo
                        if (porcentajeAumento < 0) {
                            mostrarError("Error", "El porcentaje de aumento no puede ser negativo");
                            return;
                        }
                    } catch (NumberFormatException e) {
                        mostrarError("Error", "El porcentaje de aumento debe ser un número válido");
                        return;
                    }
                }

                tipoAlquiler = cbTipoAlquiler.getValue();

                if (cbFrecuenciaAumento.getValue() != null) {
                    try {
                        String[] partes = cbFrecuenciaAumento.getValue().split(" ");
                        frecuenciaAumento = Integer.parseInt(partes[0]);
                    } catch (NumberFormatException e) {
                        mostrarError("Error", "Frecuencia de aumento inválida");
                        return;
                    }
                }

                indiceAumento = cbIndiceAumento.getValue();
                serviciosIncluidos = txtServiciosIncluidos.getText();
            }

            // Verificar si estamos editando o creando nuevo
            Contrato contratoSeleccionado = tablaContratos.getSelectionModel().getSelectedItem();
            Contrato contrato;
            boolean esEdicion = (contratoSeleccionado != null);

            if (esEdicion) {
                // Modo edición
                contrato = contratoSeleccionado;
                contrato.setTipo(tipo);
                contrato.setFechaInicio(fechaInicio);
                contrato.setFechaFin(fechaFin);
                contrato.setMonto(monto);
                contrato.setIdCliente(cliente.getIdCliente());
                contrato.setIdPropiedad(propiedad.getIdPropiedad());
                if (esAlquiler) {
                    contrato.setPorcentajeAumento(porcentajeAumento);
                    contrato.setFrecuenciaAumento(frecuenciaAumento);
                    contrato.setIndiceAumento(indiceAumento);
                    contrato.setServiciosIncluidos(serviciosIncluidos);
                    contrato.setTipoAlquiler(tipoAlquiler);
                }
            } else {
                // Modo nuevo registro
                contrato = new Contrato(0, tipo, fechaInicio, fechaFin, monto,
                        cliente.getIdCliente(), propiedad.getIdPropiedad(),
                        porcentajeAumento, frecuenciaAumento, indiceAumento,
                        serviciosIncluidos, tipoAlquiler);
            }

            boolean exito;
            if (esEdicion) {
                exito = ContratoDAO.actualizar(contrato);
            } else {
                exito = ContratoDAO.insertar(contrato);
            }

            if (exito) {
                String mensaje = esEdicion ? "actualizado" : "registrado";
                mostrarAlerta("Éxito", "Contrato " + mensaje + " correctamente");

                // Actualizar estado de la propiedad si es alquiler o venta
                if ("Alquiler".equals(tipo)) {
                    propiedad.setEstado("Alquilada");
                    PropiedadDAO.actualizar(propiedad);
                } else if ("Venta".equals(tipo)) {
                    propiedad.setEstado("Vendida");
                    PropiedadDAO.actualizar(propiedad);
                }

                limpiarFormularioContrato();
                cargarContratos();
                ocultarVentanas();
                verContratos.setVisible(true);
            } else {
                mostrarError("Error", "No se pudo " + (esEdicion ? "actualizar" : "registrar") + " el contrato");
            }

        } catch (NumberFormatException e) {
            mostrarError("Error", "Los valores numéricos son inválidos");
            e.printStackTrace();
        } catch (Exception e) {
            mostrarError("Error", "Error al registrar contrato: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para desactivar contrato
    @FXML
    private void desactivarContrato() {
        try {
            Contrato contratoSeleccionado = tablaContratos.getSelectionModel().getSelectedItem();
            if (contratoSeleccionado == null) {
                mostrarError("Error", "Selecciona un contrato para desactivar");
                return;
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar desactivación");
            confirmacion.setHeaderText("¿Estás seguro de desactivar este contrato?");
            confirmacion.setContentText("El contrato se marcará como inactivo pero no se eliminará.");

            if (confirmacion.showAndWait().get() == ButtonType.OK) {
                if (ContratoDAO.desactivar(contratoSeleccionado.getIdContrato())) {
                    mostrarAlerta("Éxito", "Contrato desactivado correctamente");
                    cargarContratos();

                    // Si era un contrato de alquiler, liberar la propiedad
                    if ("Alquiler".equals(contratoSeleccionado.getTipo())) {
                        Propiedad propiedad = PropiedadDAO.obtenerPorId(contratoSeleccionado.getIdPropiedad());
                        if (propiedad != null && "Alquilada".equals(propiedad.getEstado())) {
                            propiedad.setEstado("Disponible");
                            PropiedadDAO.actualizar(propiedad);
                        }
                    }
                } else {
                    mostrarError("Error", "No se pudo desactivar el contrato");
                }
            }
        } catch (Exception e) {
            mostrarError("Error", "Error al desactivar contrato: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para activar contrato
    @FXML
    private void activarContrato() {
        try {
            Contrato contratoSeleccionado = tablaContratos.getSelectionModel().getSelectedItem();
            if (contratoSeleccionado == null) {
                mostrarError("Error", "Selecciona un contrato para activar");
                return;
            }

            // Verificar si la propiedad está disponible
            Propiedad propiedad = PropiedadDAO.obtenerPorId(contratoSeleccionado.getIdPropiedad());
            if (propiedad != null && !"Disponible".equals(propiedad.getEstado())) {
                mostrarError("Error", "La propiedad no está disponible para activar el contrato");
                return;
            }

            if (ContratoDAO.activar(contratoSeleccionado.getIdContrato())) {
                mostrarAlerta("Éxito", "Contrato activado correctamente");
                cargarContratos();

                // Si es contrato de alquiler, marcar propiedad como alquilada
                if ("Alquiler".equals(contratoSeleccionado.getTipo())) {
                    propiedad.setEstado("Alquilada");
                    PropiedadDAO.actualizar(propiedad);
                }
            } else {
                mostrarError("Error", "No se pudo activar el contrato");
            }
        } catch (Exception e) {
            mostrarError("Error", "Error al activar contrato: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // NUEVO MÉTODO: Gestionar estado del contrato
    @FXML
    private void gestionarEstadoContrato() {
        try {
            Contrato contratoSeleccionado = tablaContratos.getSelectionModel().getSelectedItem();
            if (contratoSeleccionado == null) {
                mostrarError("Error", "Selecciona un contrato");
                return;
            }

            // Para contratos, necesitamos verificar el estado activo en la base de datos
            Contrato contratoCompleto = ContratoDAO.obtenerPorId(contratoSeleccionado.getIdContrato());
            if (contratoCompleto == null) {
                mostrarError("Error", "No se pudo obtener información del contrato");
                return;
            }

            // Asumimos que si está en la tabla principal, está activo
            boolean estaActivo = true;
            String mensaje = estaActivo ? "desactivar" : "activar";
            String nuevoEstado = estaActivo ? "inactivo" : "activo";

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar cambio de estado");
            confirmacion.setHeaderText("¿Estás seguro de " + mensaje + " este contrato?");
            confirmacion.setContentText("El contrato será marcado como: " + nuevoEstado);

            if (confirmacion.showAndWait().get() == ButtonType.OK) {
                boolean exito;
                if (estaActivo) {
                    exito = ContratoDAO.desactivar(contratoSeleccionado.getIdContrato());
                    // Liberar propiedad si es alquiler
                    if (exito && "Alquiler".equals(contratoSeleccionado.getTipo())) {
                        Propiedad propiedad = PropiedadDAO.obtenerPorId(contratoSeleccionado.getIdPropiedad());
                        if (propiedad != null) {
                            propiedad.setEstado("Disponible");
                            PropiedadDAO.actualizar(propiedad);
                        }
                    }
                } else {
                    exito = ContratoDAO.activar(contratoSeleccionado.getIdContrato());
                    // Ocupar propiedad si es alquiler
                    if (exito && "Alquiler".equals(contratoSeleccionado.getTipo())) {
                        Propiedad propiedad = PropiedadDAO.obtenerPorId(contratoSeleccionado.getIdPropiedad());
                        if (propiedad != null) {
                            propiedad.setEstado("Alquilada");
                            PropiedadDAO.actualizar(propiedad);
                        }
                    }
                }

                if (exito) {
                    mostrarAlerta("Éxito", "Contrato " + mensaje + "do correctamente");
                    cargarContratos();
                } else {
                    mostrarError("Error", "No se pudo cambiar el estado del contrato");
                }
            }
        } catch (Exception e) {
            mostrarError("Error", "Error al cambiar estado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para eliminar contrato permanentemente
    @FXML
    private void eliminarContrato() {
        try {
            Contrato contratoSeleccionado = tablaContratos.getSelectionModel().getSelectedItem();
            if (contratoSeleccionado == null) {
                mostrarError("Error", "Selecciona un contrato para eliminar");
                return;
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Estás seguro de eliminar este contrato permanentemente?");
            confirmacion.setContentText("Esta acción no se puede deshacer.");

            if (confirmacion.showAndWait().get() == ButtonType.OK) {
                if (ContratoDAO.eliminar(contratoSeleccionado.getIdContrato())) {
                    mostrarAlerta("Éxito", "Contrato eliminado permanentemente");
                    cargarContratos();
                } else {
                    mostrarError("Error", "No se pudo eliminar el contrato");
                }
            }
        } catch (Exception e) {
            mostrarError("Error", "Error al eliminar contrato: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void cancelarRegistroContrato() {
        limpiarFormularioContrato();
        ocultarVentanas();
        verContratos.setVisible(true);
    }

    private void limpiarFormularioContrato() {
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

    private void cargarContratos() {
        try {
            listaContratos.clear();
            List<Contrato> contratos = ContratoDAO.obtenerTodos();
            listaContratos.addAll(contratos);
            tablaContratos.setItems(listaContratos);
            System.out.println("Contratos cargados: " + contratos.size());
        } catch (Exception e) {
            mostrarError("Error", "Error cargando contratos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===== GENERAR RECIBO =====
    @FXML
    private void generarRecibo() {
        try {
            Contrato contratoSeleccionado = tablaContratos.getSelectionModel().getSelectedItem();

            if (contratoSeleccionado == null) {
                mostrarError("Error", "Selecciona un contrato de la tabla");
                return;
            }

            if (!"Alquiler".equals(contratoSeleccionado.getTipo())) {
                mostrarError("Error", "Solo se generan recibos para contratos de alquiler");
                return;
            }

            // Obtener información del cliente y propiedad
            Cliente cliente = ClienteDAO.obtenerPorId(contratoSeleccionado.getIdCliente());
            Propiedad propiedad = PropiedadDAO.obtenerPorId(contratoSeleccionado.getIdPropiedad());

            if (cliente == null || propiedad == null) {
                mostrarError("Error", "No se pudo obtener información del cliente o propiedad");
                return;
            }

            LocalDate hoy = LocalDate.now();
            String mesReferencia = obtenerMesReferencia(hoy);
            LocalDate vencimiento = hoy.plusDays(10);

            double servicios = 0;
            double total = contratoSeleccionado.getMonto() + servicios;

            Recibo recibo = new Recibo(0, contratoSeleccionado.getIdContrato(), hoy, mesReferencia,
                    contratoSeleccionado.getMonto(), servicios, total, vencimiento, false);

            if (ReciboDAO.insertar(recibo)) {
                // Generar PDF del recibo
                generarPDFRecibo(recibo, cliente, propiedad, contratoSeleccionado);

                mostrarAlerta("Éxito", "Recibo generado correctamente y guardado como archivo de texto");
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

    // ===== GENERACIÓN DE ARCHIVO DE TEXTO =====
    private void generarPDFRecibo(Recibo recibo, Cliente cliente, Propiedad propiedad, Contrato contrato) {
        try {
            // Crear nombre del archivo: "NombrePropiedad_NombreCliente_MesAño.txt"
            String nombreArchivo = generarNombreArchivoPDF(propiedad, cliente, recibo);

            // Usar PDFGenerator para crear el archivo de texto
            PDFGenerator.generarReciboPDF(recibo, cliente, propiedad, contrato, nombreArchivo);

            System.out.println("Recibo generado: " + nombreArchivo);

            // Mostrar mensaje de éxito
            mostrarAlerta("Éxito", "Recibo generado exitosamente:\n" + nombreArchivo);

        } catch (Exception e) {
            System.out.println("Error al generar recibo: " + e.getMessage());
            e.printStackTrace();
            // Mostrar recibo en texto si falla la generación del archivo
            mostrarRecibo(recibo, cliente, propiedad, contrato);
        }
    }

    private String generarNombreArchivoPDF(Propiedad propiedad, Cliente cliente, Recibo recibo) {
        // Limpiar caracteres especiales para el nombre del archivo
        String nombrePropiedad = propiedad.getDireccion()
                .replaceAll("[^a-zA-Z0-9]", "_")
                .replaceAll("_+", "_");

        String nombreCliente = (cliente.getNombre() + "_" + cliente.getApellido())
                .replaceAll("[^a-zA-Z0-9]", "_")
                .replaceAll("_+", "_");

        String mesAño = recibo.getMesReferencia()
                .replace(" ", "_")
                .replaceAll("[^a-zA-Z0-9_]", "");

        return nombrePropiedad + "_" + nombreCliente + "_" + mesAño + ".txt";
    }

    // ===== ANIMACIONES SIDEBAR =====
    private boolean isOpen = false;

    @FXML
    private void SideBarPropiedades () {
        SubMenuPropiedades.setVisible(!SubMenuPropiedades.isVisible());
        TranslateTransition tt = new TranslateTransition(Duration.millis(200), SubMenuPropiedades);
        tt.setFromY(-SubMenuPropiedades.getHeight());
        tt.setToY(0);
        tt.play();
    }

    @FXML
    private void SideBarClientes () {
        SubMenuCliente.setVisible(!SubMenuCliente.isVisible());
        TranslateTransition tt = new TranslateTransition(Duration.millis(200), SubMenuCliente);
        tt.setFromY(-SubMenuCliente.getHeight());
        tt.setToY(0);
        tt.play();
    }

    @FXML
    private void SideBarContratos () {
        SubMenuContratos.setVisible(!SubMenuContratos.isVisible());
        TranslateTransition tt = new TranslateTransition(Duration.millis(200), SubMenuContratos);
        tt.setFromY(-SubMenuContratos.getHeight());
        tt.setToY(0);
        tt.play();
    }

    // ===== UTILITARIOS =====
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
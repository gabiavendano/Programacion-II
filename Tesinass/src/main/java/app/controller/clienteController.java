package app.controller;

import app.model.Cliente;
import app.model.ClienteDAO;
import app.model.TipoCliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class clienteController {

    private ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
    private Cliente clienteSeleccionado; // guarda el cliente actual en edición

    public ObservableList<Cliente> getListaClientes() {
        listaClientes.setAll(ClienteDAO.obtenerTodos());
        return listaClientes;
    }

    public boolean registrarCliente(String nombre, String apellido, String dni, String telefono, String email, int idTipoRol, String rol, String estado) {
        if (nombre.trim().isEmpty() || apellido.trim().isEmpty() || dni.trim().isEmpty()
                || telefono.trim().isEmpty() || email.trim().isEmpty()) {
            return false;
        }

        Cliente cliente = new Cliente(nombre, apellido, dni, telefono, email, rol, estado);
        return ClienteDAO.insertar(cliente, idTipoRol);
    }

    public Cliente seleccionarCliente(Cliente cliente) {
        this.clienteSeleccionado = cliente;
        return clienteSeleccionado;
    }

    public boolean actualizarCliente(String nombre, String apellido, String dni, String telefono, String email, TipoCliente tipoCliente) {
        if (clienteSeleccionado == null) return false;

        clienteSeleccionado.setNombre(nombre);
        clienteSeleccionado.setApellido(apellido);
        clienteSeleccionado.setDni(dni);
        clienteSeleccionado.setTelefono(telefono);
        clienteSeleccionado.setEmail(email);
        clienteSeleccionado.setTipoCliente(tipoCliente.getTipo());

        return ClienteDAO.actualizar(clienteSeleccionado, tipoCliente.getIdTipo());
    }

    public void cancelarEdicion() {
        this.clienteSeleccionado = null;
    }

    public boolean eliminarCliente(Cliente cliente) {
        return ClienteDAO.eliminar(cliente);
    }
}
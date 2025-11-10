package app.model;

public class Cliente {
    private int idCliente;
    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;
    private String email;
    private String tipo;
    private String estado;

    // Constructor completo (para cuando traes datos desde la DB)
    public Cliente(int idCliente, String nombre, String apellido, String dni, String telefono, String email, String tipo, String estado) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
        this.tipo = tipo;
        this.estado = estado;
    }

    // Constructor sin id (para insertar nuevos clientes)
    public Cliente(String nombre, String apellido, String dni, String telefono, String email, String tipo, String estado) {
        this(0, nombre, apellido, dni, telefono, email, tipo, estado);
    }

    // Constructor corregido - agregar estado
    public Cliente(int idCliente, String nombre, String apellido, String dni, String telefono, String email, String tipo) {
        this(idCliente, nombre, apellido, dni, telefono, email, tipo, "Activo");
    }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getDni() { return dni; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }
    public String getTipo() { return tipo; }
    public String getEstado() { return estado; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setDni(String dni) { this.dni = dni; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setEmail(String email) { this.email = email; }
    public void setTipoCliente(String tipo) { this.tipo = tipo; }
    public void setEstado(String estado) { this.estado = estado; }
}
package app.model;

public class Propiedad {
    private int idPropiedad;
    private String direccion;
    private String tipo;
    private double metrosLote;
    private double metrosCubiertos;
    private int antiguedad;
    private int dormitorios;
    private int banos;
    private boolean pileta;
    private boolean cochera;
    private double expensas;
    private String descripcion;
    private double precio;
    private String moneda;
    private String otros;
    private String foto;
    private String estado;
    private int idUsuario;

    // Constructor sin idPropiedad (para registrar desde la app)
    public Propiedad(String direccion, String tipo, double metrosLote, double metrosCubiertos,
                     int antiguedad, int dormitorios, int banos, boolean pileta, boolean cochera,
                     double expensas, String descripcion, double precio, String moneda,
                     String otros, String foto, String estado, int idUsuario) {
        this.direccion = direccion;
        this.tipo = tipo;
        this.metrosLote = metrosLote;
        this.metrosCubiertos = metrosCubiertos;
        this.antiguedad = antiguedad;
        this.dormitorios = dormitorios;
        this.banos = banos;
        this.pileta = pileta;
        this.cochera = cochera;
        this.expensas = expensas;
        this.descripcion = descripcion;
        this.precio = precio;
        this.moneda = moneda;
        this.otros = otros;
        this.foto = foto;
        this.estado = estado;
        this.idUsuario = idUsuario;
    }

    // Constructor completo (para consultas desde la DB)
    public Propiedad(int idPropiedad, String direccion, String tipo, double metrosLote, double metrosCubiertos,
                     int antiguedad, int dormitorios, int banos, boolean pileta, boolean cochera,
                     double expensas, String descripcion, double precio, String moneda,
                     String otros, String foto, String estado, int idUsuario) {
        this(direccion, tipo, metrosLote, metrosCubiertos, antiguedad, dormitorios, banos,
                pileta, cochera, expensas, descripcion, precio, moneda, otros, foto, estado, idUsuario);
        this.idPropiedad = idPropiedad;
    }

    // ===== GETTERS Y SETTERS =====
    public int getIdPropiedad() { return idPropiedad; }
    public void setIdPropiedad(int idPropiedad) { this.idPropiedad = idPropiedad; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public double getMetrosLote() { return metrosLote; }
    public void setMetrosLote(double metrosLote) { this.metrosLote = metrosLote; }

    public double getMetrosCubiertos() { return metrosCubiertos; }
    public void setMetrosCubiertos(double metrosCubiertos) { this.metrosCubiertos = metrosCubiertos; }

    public int getAntiguedad() { return antiguedad; }
    public void setAntiguedad(int antiguedad) { this.antiguedad = antiguedad; }

    public int getDormitorios() { return dormitorios; }
    public void setDormitorios(int dormitorios) { this.dormitorios = dormitorios; }

    public int getBanos() { return banos; }
    public void setBanos(int banos) { this.banos = banos; }

    public boolean isPileta() { return pileta; }
    public void setPileta(boolean pileta) { this.pileta = pileta; }

    public boolean isCochera() { return cochera; }
    public void setCochera(boolean cochera) { this.cochera = cochera; }

    public double getExpensas() { return expensas; }
    public void setExpensas(double expensas) { this.expensas = expensas; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }

    public String getOtros() { return otros; }
    public void setOtros(String otros) { this.otros = otros; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
}
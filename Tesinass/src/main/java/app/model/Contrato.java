package app.model;

import java.time.LocalDate;

public class Contrato {
    private int idContrato;
    private String tipo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double monto;
    private int idCliente;
    private int idPropiedad;
    private double porcentajeAumento;
    private int frecuenciaAumento;
    private String indiceAumento;
    private String serviciosIncluidos;
    private String tipoAlquiler;

    // Constructores
    public Contrato() {}

    public Contrato(int idContrato, String tipo, LocalDate fechaInicio, LocalDate fechaFin,
                    double monto, int idCliente, int idPropiedad, double porcentajeAumento,
                    int frecuenciaAumento, String indiceAumento, String serviciosIncluidos,
                    String tipoAlquiler) {
        this.idContrato = idContrato;
        this.tipo = tipo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.monto = monto;
        this.idCliente = idCliente;
        this.idPropiedad = idPropiedad;
        this.porcentajeAumento = porcentajeAumento;
        this.frecuenciaAumento = frecuenciaAumento;
        this.indiceAumento = indiceAumento;
        this.serviciosIncluidos = serviciosIncluidos;
        this.tipoAlquiler = tipoAlquiler;
    }

    // Getters y Setters
    public int getIdContrato() { return idContrato; }
    public void setIdContrato(int idContrato) { this.idContrato = idContrato; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public int getIdPropiedad() { return idPropiedad; }
    public void setIdPropiedad(int idPropiedad) { this.idPropiedad = idPropiedad; }

    public double getPorcentajeAumento() { return porcentajeAumento; }
    public void setPorcentajeAumento(double porcentajeAumento) { this.porcentajeAumento = porcentajeAumento; }

    public int getFrecuenciaAumento() { return frecuenciaAumento; }
    public void setFrecuenciaAumento(int frecuenciaAumento) { this.frecuenciaAumento = frecuenciaAumento; }

    public String getIndiceAumento() { return indiceAumento; }
    public void setIndiceAumento(String indiceAumento) { this.indiceAumento = indiceAumento; }

    public String getServiciosIncluidos() { return serviciosIncluidos; }
    public void setServiciosIncluidos(String serviciosIncluidos) { this.serviciosIncluidos = serviciosIncluidos; }

    public String getTipoAlquiler() { return tipoAlquiler; }
    public void setTipoAlquiler(String tipoAlquiler) { this.tipoAlquiler = tipoAlquiler; }
}
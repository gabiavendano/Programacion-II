package app.model;

import java.time.LocalDate;

public class Recibo {
    private int idRecibo;
    private int idContrato;
    private LocalDate fechaEmision;
    private String mesReferencia;
    private double montoAlquiler;
    private double servicios;
    private double total;
    private LocalDate fechaVencimiento;
    private boolean pagado;

    // Constructores
    public Recibo() {}

    public Recibo(int idRecibo, int idContrato, LocalDate fechaEmision, String mesReferencia,
                  double montoAlquiler, double servicios, double total, LocalDate fechaVencimiento,
                  boolean pagado) {
        this.idRecibo = idRecibo;
        this.idContrato = idContrato;
        this.fechaEmision = fechaEmision;
        this.mesReferencia = mesReferencia;
        this.montoAlquiler = montoAlquiler;
        this.servicios = servicios;
        this.total = total;
        this.fechaVencimiento = fechaVencimiento;
        this.pagado = pagado;
    }

    // Getters y Setters
    public int getIdRecibo() { return idRecibo; }
    public void setIdRecibo(int idRecibo) { this.idRecibo = idRecibo; }

    public int getIdContrato() { return idContrato; }
    public void setIdContrato(int idContrato) { this.idContrato = idContrato; }

    public LocalDate getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDate fechaEmision) { this.fechaEmision = fechaEmision; }

    public String getMesReferencia() { return mesReferencia; }
    public void setMesReferencia(String mesReferencia) { this.mesReferencia = mesReferencia; }

    public double getMontoAlquiler() { return montoAlquiler; }
    public void setMontoAlquiler(double montoAlquiler) { this.montoAlquiler = montoAlquiler; }

    public double getServicios() { return servicios; }
    public void setServicios(double servicios) { this.servicios = servicios; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public boolean isPagado() { return pagado; }
    public void setPagado(boolean pagado) { this.pagado = pagado; }
}
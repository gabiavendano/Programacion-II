package app.model;

public class TipoCliente {

    private int idTipo;
    private String tipo;

    public TipoCliente(int idTipo, String tipo) {
        this.idTipo = idTipo;
        this.tipo = tipo;
    }

    public int getIdTipo() { return idTipo; }
    public String getTipo() { return tipo; }

    @Override
    public String toString() {
        return tipo;
    }
}

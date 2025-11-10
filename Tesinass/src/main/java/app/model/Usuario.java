package app.model;


public class Usuario {
    private String nombre;
    private String contraseña;
    private String email;

    public Usuario(String nombre, String email, String contraseña) {
        this.nombre = nombre;
        this.email = email;
        this.contraseña = contraseña;
    }

    public Usuario(String nombreOEmail, String contraseña) {
        this.nombre = nombreOEmail;
        this.contraseña = contraseña;
    }

    public String getNombre() {return nombre; }
    public String getContraseña() { return contraseña; }
    public String getEmail() { return email; }
}
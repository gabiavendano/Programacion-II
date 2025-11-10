package app.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.*;

public class UsuarioDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/inmobiliaria";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static boolean insertar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (usuario, contraseña, email) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getContraseña());
            stmt.setString(3, usuario.getEmail());
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean checkDB(Usuario usuario) {
        String sql = "SELECT * FROM usuarios WHERE (usuario = ? OR email = ?) AND contraseña = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {


            stmt.setString(1, usuario.getNombre()); // nombre
            stmt.setString(2, usuario.getNombre().trim()); // email
            stmt.setString(3, usuario.getContraseña()); // contraseña


            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Si existe al menos un registro, el usuario es válido
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean ValidarEmail(String email) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email.trim());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
        }
    }

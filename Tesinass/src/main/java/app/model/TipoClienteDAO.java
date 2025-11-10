package app.model;

import java.sql.*;
import java.util.*;

public class TipoClienteDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/inmobiliaria";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static List<TipoCliente> obtenerTipoC() {
        List<TipoCliente> tipos = new ArrayList<>();
        String SQL = "Select * from tipos_cliente";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            while (rs.next()) {
                tipos.add(new TipoCliente(
                        rs.getInt("idTipo"),
                        rs.getString("tipo")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tipos;
    }
}

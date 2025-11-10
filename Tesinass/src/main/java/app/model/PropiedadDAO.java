package app.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PropiedadDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/inmobiliaria";
    private static final String USER = "root";
    private static final String PASS = "";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // Insertar nueva propiedad
    public static boolean insertar(Propiedad p) {
        // Validaciones antes de insertar
        if (p.getPrecio() <= 0) {
            System.out.println("Precio debe ser mayor a 0");
            return false;
        }

        if (p.getMetrosLote() < 0 || p.getMetrosCubiertos() < 0) {
            System.out.println("Los metros no pueden ser negativos");
            return false;
        }

        String sql = "INSERT INTO propiedades (direccion, tipo, metros_lote, metros_cubiertos, antiguedad, "
                + "dormitorios, banos, pileta, cochera, expensas, descripcion, precio, moneda, otros, foto, estado, idUsuario) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getDireccion());
            stmt.setString(2, p.getTipo());
            stmt.setDouble(3, p.getMetrosLote());
            stmt.setDouble(4, p.getMetrosCubiertos());
            stmt.setInt(5, p.getAntiguedad());
            stmt.setInt(6, p.getDormitorios());
            stmt.setInt(7, p.getBanos());
            stmt.setBoolean(8, p.isPileta());
            stmt.setBoolean(9, p.isCochera());
            stmt.setDouble(10, p.getExpensas());
            stmt.setString(11, p.getDescripcion());
            stmt.setDouble(12, p.getPrecio());
            stmt.setString(13, p.getMoneda());
            stmt.setString(14, p.getOtros());
            stmt.setString(15, p.getFoto());
            stmt.setString(16, p.getEstado());
            stmt.setInt(17, p.getIdUsuario());
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Obtener todas las propiedades
    public static List<Propiedad> obtenerTodas() {
        List<Propiedad> lista = new ArrayList<>();
        String sql = "SELECT * FROM propiedades";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Propiedad(
                        rs.getInt("idPropiedad"),
                        rs.getString("direccion"),
                        rs.getString("tipo"),
                        rs.getDouble("metros_lote"),
                        rs.getDouble("metros_cubiertos"),
                        rs.getInt("antiguedad"),
                        rs.getInt("dormitorios"),
                        rs.getInt("banos"),
                        rs.getBoolean("pileta"),
                        rs.getBoolean("cochera"),
                        rs.getDouble("expensas"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getString("moneda"),
                        rs.getString("otros"),
                        rs.getString("foto"),
                        rs.getString("estado"),
                        rs.getInt("idUsuario")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Obtener propiedad por ID
    public static Propiedad obtenerPorId(int idPropiedad) {
        String sql = "SELECT * FROM propiedades WHERE idPropiedad = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPropiedad);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Propiedad(
                        rs.getInt("idPropiedad"),
                        rs.getString("direccion"),
                        rs.getString("tipo"),
                        rs.getDouble("metros_lote"),
                        rs.getDouble("metros_cubiertos"),
                        rs.getInt("antiguedad"),
                        rs.getInt("dormitorios"),
                        rs.getInt("banos"),
                        rs.getBoolean("pileta"),
                        rs.getBoolean("cochera"),
                        rs.getDouble("expensas"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getString("moneda"),
                        rs.getString("otros"),
                        rs.getString("foto"),
                        rs.getString("estado"),
                        rs.getInt("idUsuario")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Actualizar propiedad existente
    public static boolean actualizar(Propiedad p) {
        // Validaciones antes de actualizar
        if (p.getPrecio() <= 0) {
            System.out.println("Precio debe ser mayor a 0");
            return false;
        }

        String sql = "UPDATE propiedades SET direccion=?, tipo=?, metros_lote=?, metros_cubiertos=?, antiguedad=?, "
                + "dormitorios=?, banos=?, pileta=?, cochera=?, expensas=?, descripcion=?, precio=?, moneda=?, otros=?, foto=?, estado=?, idUsuario=? "
                + "WHERE idPropiedad=?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getDireccion());
            stmt.setString(2, p.getTipo());
            stmt.setDouble(3, p.getMetrosLote());
            stmt.setDouble(4, p.getMetrosCubiertos());
            stmt.setInt(5, p.getAntiguedad());
            stmt.setInt(6, p.getDormitorios());
            stmt.setInt(7, p.getBanos());
            stmt.setBoolean(8, p.isPileta());
            stmt.setBoolean(9, p.isCochera());
            stmt.setDouble(10, p.getExpensas());
            stmt.setString(11, p.getDescripcion());
            stmt.setDouble(12, p.getPrecio());
            stmt.setString(13, p.getMoneda());
            stmt.setString(14, p.getOtros());
            stmt.setString(15, p.getFoto());
            stmt.setString(16, p.getEstado());
            stmt.setInt(17, p.getIdUsuario());
            stmt.setInt(18, p.getIdPropiedad());
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Desactivar propiedad (cambiar estado a "Inactiva")
    public static boolean desactivar(int idPropiedad) {
        String sql = "UPDATE propiedades SET estado = 'Inactiva' WHERE idPropiedad = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPropiedad);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Activar propiedad (cambiar estado a "Disponible")
    public static boolean activar(int idPropiedad) {
        String sql = "UPDATE propiedades SET estado = 'Disponible' WHERE idPropiedad = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPropiedad);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // NUEVO MÉTODO: Cambiar estado de propiedad
    public static boolean cambiarEstado(int idPropiedad, String estado) {
        String sql = "UPDATE propiedades SET estado = ? WHERE idPropiedad = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, estado);
            stmt.setInt(2, idPropiedad);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar propiedad permanentemente
    public static boolean eliminar(int idPropiedad) {
        String sql = "DELETE FROM propiedades WHERE idPropiedad = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPropiedad);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Obtener propiedades por estado
    public static List<Propiedad> obtenerPorEstado(String estado) {
        List<Propiedad> lista = new ArrayList<>();
        String sql = "SELECT * FROM propiedades WHERE estado = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, estado);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Propiedad(
                        rs.getInt("idPropiedad"),
                        rs.getString("direccion"),
                        rs.getString("tipo"),
                        rs.getDouble("metros_lote"),
                        rs.getDouble("metros_cubiertos"),
                        rs.getInt("antiguedad"),
                        rs.getInt("dormitorios"),
                        rs.getInt("banos"),
                        rs.getBoolean("pileta"),
                        rs.getBoolean("cochera"),
                        rs.getDouble("expensas"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getString("moneda"),
                        rs.getString("otros"),
                        rs.getString("foto"),
                        rs.getString("estado"),
                        rs.getInt("idUsuario")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Obtener propiedades por tipo
    public static List<Propiedad> obtenerPorTipo(String tipo) {
        List<Propiedad> lista = new ArrayList<>();
        String sql = "SELECT * FROM propiedades WHERE tipo = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipo);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Propiedad(
                        rs.getInt("idPropiedad"),
                        rs.getString("direccion"),
                        rs.getString("tipo"),
                        rs.getDouble("metros_lote"),
                        rs.getDouble("metros_cubiertos"),
                        rs.getInt("antiguedad"),
                        rs.getInt("dormitorios"),
                        rs.getInt("banos"),
                        rs.getBoolean("pileta"),
                        rs.getBoolean("cochera"),
                        rs.getDouble("expensas"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getString("moneda"),
                        rs.getString("otros"),
                        rs.getString("foto"),
                        rs.getString("estado"),
                        rs.getInt("idUsuario")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Obtener propiedades activas
    public static List<Propiedad> obtenerActivas() {
        List<Propiedad> lista = new ArrayList<>();
        String sql = "SELECT * FROM propiedades WHERE estado IN ('Disponible', 'Reservada', 'Vendida', 'Alquilada')";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Propiedad(
                        rs.getInt("idPropiedad"),
                        rs.getString("direccion"),
                        rs.getString("tipo"),
                        rs.getDouble("metros_lote"),
                        rs.getDouble("metros_cubiertos"),
                        rs.getInt("antiguedad"),
                        rs.getInt("dormitorios"),
                        rs.getInt("banos"),
                        rs.getBoolean("pileta"),
                        rs.getBoolean("cochera"),
                        rs.getDouble("expensas"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getString("moneda"),
                        rs.getString("otros"),
                        rs.getString("foto"),
                        rs.getString("estado"),
                        rs.getInt("idUsuario")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // NUEVO MÉTODO: Obtener propiedades disponibles para contratos
    public static List<Propiedad> obtenerDisponiblesParaContrato() {
        List<Propiedad> lista = new ArrayList<>();
        String sql = "SELECT * FROM propiedades WHERE estado IN ('Disponible', 'Reservada')";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Propiedad(
                        rs.getInt("idPropiedad"),
                        rs.getString("direccion"),
                        rs.getString("tipo"),
                        rs.getDouble("metros_lote"),
                        rs.getDouble("metros_cubiertos"),
                        rs.getInt("antiguedad"),
                        rs.getInt("dormitorios"),
                        rs.getInt("banos"),
                        rs.getBoolean("pileta"),
                        rs.getBoolean("cochera"),
                        rs.getDouble("expensas"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getString("moneda"),
                        rs.getString("otros"),
                        rs.getString("foto"),
                        rs.getString("estado"),
                        rs.getInt("idUsuario")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
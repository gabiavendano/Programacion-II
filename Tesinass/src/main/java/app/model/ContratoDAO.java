package app.model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ContratoDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/inmobiliaria";
    private static final String USER = "root";
    private static final String PASS = "";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static boolean insertar(Contrato contrato) {
        // Validaciones antes de insertar
        if (contrato.getMonto() < 0) {
            System.out.println("El monto no puede ser negativo");
            return false;
        }

        if (contrato.getPorcentajeAumento() < 0 || contrato.getPorcentajeAumento() > 100) {
            System.out.println("El porcentaje de aumento debe estar entre 0 y 100");
            return false;
        }

        String sql = "INSERT INTO contratos (tipo, fechaInicio, fechaFin, monto, idCliente, idPropiedad, " +
                "porcentaje_aumento, frecuencia_aumento, indice_aumento, servicios_incluidos, tipo_alquiler, activo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, contrato.getTipo());
            stmt.setDate(2, Date.valueOf(contrato.getFechaInicio()));
            stmt.setDate(3, contrato.getFechaFin() != null ? Date.valueOf(contrato.getFechaFin()) : null);
            stmt.setDouble(4, contrato.getMonto());
            stmt.setInt(5, contrato.getIdCliente());
            stmt.setInt(6, contrato.getIdPropiedad());
            stmt.setDouble(7, contrato.getPorcentajeAumento());
            stmt.setInt(8, contrato.getFrecuenciaAumento());
            stmt.setString(9, contrato.getIndiceAumento());
            stmt.setString(10, contrato.getServiciosIncluidos());
            stmt.setString(11, contrato.getTipoAlquiler());
            stmt.setBoolean(12, true); // Contrato activo por defecto

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Contrato> obtenerTodos() {
        List<Contrato> lista = new ArrayList<>();
        String sql = "SELECT * FROM contratos WHERE activo = true";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Contrato contrato = new Contrato(
                        rs.getInt("idContrato"),
                        rs.getString("tipo"),
                        rs.getDate("fechaInicio").toLocalDate(),
                        rs.getDate("fechaFin") != null ? rs.getDate("fechaFin").toLocalDate() : null,
                        rs.getDouble("monto"),
                        rs.getInt("idCliente"),
                        rs.getInt("idPropiedad"),
                        rs.getDouble("porcentaje_aumento"),
                        rs.getInt("frecuencia_aumento"),
                        rs.getString("indice_aumento"),
                        rs.getString("servicios_incluidos"),
                        rs.getString("tipo_alquiler")
                );
                lista.add(contrato);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static List<Contrato> obtenerTodosIncluyendoInactivos() {
        List<Contrato> lista = new ArrayList<>();
        String sql = "SELECT * FROM contratos";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Contrato contrato = new Contrato(
                        rs.getInt("idContrato"),
                        rs.getString("tipo"),
                        rs.getDate("fechaInicio").toLocalDate(),
                        rs.getDate("fechaFin") != null ? rs.getDate("fechaFin").toLocalDate() : null,
                        rs.getDouble("monto"),
                        rs.getInt("idCliente"),
                        rs.getInt("idPropiedad"),
                        rs.getDouble("porcentaje_aumento"),
                        rs.getInt("frecuencia_aumento"),
                        rs.getString("indice_aumento"),
                        rs.getString("servicios_incluidos"),
                        rs.getString("tipo_alquiler")
                );
                lista.add(contrato);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static Contrato obtenerPorId(int idContrato) {
        String sql = "SELECT * FROM contratos WHERE idContrato = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idContrato);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Contrato(
                        rs.getInt("idContrato"),
                        rs.getString("tipo"),
                        rs.getDate("fechaInicio").toLocalDate(),
                        rs.getDate("fechaFin") != null ? rs.getDate("fechaFin").toLocalDate() : null,
                        rs.getDouble("monto"),
                        rs.getInt("idCliente"),
                        rs.getInt("idPropiedad"),
                        rs.getDouble("porcentaje_aumento"),
                        rs.getInt("frecuencia_aumento"),
                        rs.getString("indice_aumento"),
                        rs.getString("servicios_incluidos"),
                        rs.getString("tipo_alquiler")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean actualizar(Contrato contrato) {
        // Validaciones antes de actualizar
        if (contrato.getMonto() < 0) {
            System.out.println("El monto no puede ser negativo");
            return false;
        }

        String sql = "UPDATE contratos SET tipo=?, fechaInicio=?, fechaFin=?, monto=?, idCliente=?, " +
                "idPropiedad=?, porcentaje_aumento=?, frecuencia_aumento=?, indice_aumento=?, " +
                "servicios_incluidos=?, tipo_alquiler=? WHERE idContrato=?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, contrato.getTipo());
            stmt.setDate(2, Date.valueOf(contrato.getFechaInicio()));
            stmt.setDate(3, contrato.getFechaFin() != null ? Date.valueOf(contrato.getFechaFin()) : null);
            stmt.setDouble(4, contrato.getMonto());
            stmt.setInt(5, contrato.getIdCliente());
            stmt.setInt(6, contrato.getIdPropiedad());
            stmt.setDouble(7, contrato.getPorcentajeAumento());
            stmt.setInt(8, contrato.getFrecuenciaAumento());
            stmt.setString(9, contrato.getIndiceAumento());
            stmt.setString(10, contrato.getServiciosIncluidos());
            stmt.setString(11, contrato.getTipoAlquiler());
            stmt.setInt(12, contrato.getIdContrato());

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean desactivar(int idContrato) {
        String sql = "UPDATE contratos SET activo = false WHERE idContrato = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idContrato);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean activar(int idContrato) {
        String sql = "UPDATE contratos SET activo = true WHERE idContrato = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idContrato);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // NUEVO MÉTODO: Cambiar estado del contrato
    public static boolean cambiarEstado(int idContrato, boolean activo) {
        String sql = "UPDATE contratos SET activo = ? WHERE idContrato = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, activo);
            stmt.setInt(2, idContrato);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean eliminar(int idContrato) {
        String sql = "DELETE FROM contratos WHERE idContrato=?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idContrato);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // NUEVO MÉTODO: Obtener contratos por estado
    public static List<Contrato> obtenerPorEstado(boolean activo) {
        List<Contrato> lista = new ArrayList<>();
        String sql = "SELECT * FROM contratos WHERE activo = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, activo);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Contrato contrato = new Contrato(
                        rs.getInt("idContrato"),
                        rs.getString("tipo"),
                        rs.getDate("fechaInicio").toLocalDate(),
                        rs.getDate("fechaFin") != null ? rs.getDate("fechaFin").toLocalDate() : null,
                        rs.getDouble("monto"),
                        rs.getInt("idCliente"),
                        rs.getInt("idPropiedad"),
                        rs.getDouble("porcentaje_aumento"),
                        rs.getInt("frecuencia_aumento"),
                        rs.getString("indice_aumento"),
                        rs.getString("servicios_incluidos"),
                        rs.getString("tipo_alquiler")
                );
                lista.add(contrato);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // NUEVO MÉTODO: Verificar si existe contrato activo para una propiedad
    public static boolean existeContratoActivoParaPropiedad(int idPropiedad) {
        String sql = "SELECT COUNT(*) FROM contratos WHERE idPropiedad = ? AND activo = true";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPropiedad);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // NUEVO MÉTODO: Obtener contratos por cliente
    public static List<Contrato> obtenerPorCliente(int idCliente) {
        List<Contrato> lista = new ArrayList<>();
        String sql = "SELECT * FROM contratos WHERE idCliente = ? AND activo = true";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Contrato contrato = new Contrato(
                        rs.getInt("idContrato"),
                        rs.getString("tipo"),
                        rs.getDate("fechaInicio").toLocalDate(),
                        rs.getDate("fechaFin") != null ? rs.getDate("fechaFin").toLocalDate() : null,
                        rs.getDouble("monto"),
                        rs.getInt("idCliente"),
                        rs.getInt("idPropiedad"),
                        rs.getDouble("porcentaje_aumento"),
                        rs.getInt("frecuencia_aumento"),
                        rs.getString("indice_aumento"),
                        rs.getString("servicios_incluidos"),
                        rs.getString("tipo_alquiler")
                );
                lista.add(contrato);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
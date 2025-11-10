package app.model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReciboDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/inmobiliaria";
    private static final String USER = "root";
    private static final String PASS = "";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static boolean insertar(Recibo recibo) {
        String sql = "INSERT INTO recibos (idContrato, fecha_emision, mes_referencia, monto_alquiler, " +
                "servicios, total, fecha_vencimiento, pagado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, recibo.getIdContrato());
            stmt.setDate(2, Date.valueOf(recibo.getFechaEmision()));
            stmt.setString(3, recibo.getMesReferencia());
            stmt.setDouble(4, recibo.getMontoAlquiler());
            stmt.setDouble(5, recibo.getServicios());
            stmt.setDouble(6, recibo.getTotal());
            stmt.setDate(7, Date.valueOf(recibo.getFechaVencimiento()));
            stmt.setBoolean(8, recibo.isPagado());

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Recibo> obtenerPorContrato(int idContrato) {
        List<Recibo> lista = new ArrayList<>();
        String sql = "SELECT * FROM recibos WHERE idContrato = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idContrato);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Recibo recibo = new Recibo(
                        rs.getInt("idRecibo"),
                        rs.getInt("idContrato"),
                        rs.getDate("fecha_emision").toLocalDate(),
                        rs.getString("mes_referencia"),
                        rs.getDouble("monto_alquiler"),
                        rs.getDouble("servicios"),
                        rs.getDouble("total"),
                        rs.getDate("fecha_vencimiento").toLocalDate(),
                        rs.getBoolean("pagado")
                );
                lista.add(recibo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static Recibo obtenerPorId(int idRecibo) {
        String sql = "SELECT * FROM recibos WHERE idRecibo = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idRecibo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Recibo(
                        rs.getInt("idRecibo"),
                        rs.getInt("idContrato"),
                        rs.getDate("fecha_emision").toLocalDate(),
                        rs.getString("mes_referencia"),
                        rs.getDouble("monto_alquiler"),
                        rs.getDouble("servicios"),
                        rs.getDouble("total"),
                        rs.getDate("fecha_vencimiento").toLocalDate(),
                        rs.getBoolean("pagado")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean marcarComoPagado(int idRecibo) {
        String sql = "UPDATE recibos SET pagado = true WHERE idRecibo = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idRecibo);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean eliminar(int idRecibo) {
        String sql = "DELETE FROM recibos WHERE idRecibo = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idRecibo);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
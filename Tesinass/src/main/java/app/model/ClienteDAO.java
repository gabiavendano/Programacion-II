package app.model;

import java.sql.*;
import java.util.*;
import java.util.regex.Pattern;

import static java.sql.DriverManager.getConnection;

public class ClienteDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/inmobiliaria";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    // Método auxiliar para validar email
    private static boolean esEmailValido(String email) {
        if (email == null || email.isEmpty()) return false;
        return EMAIL_PATTERN.matcher(email).matches();
    }

    // Inserta cliente + rol
    public static boolean insertar(Cliente cliente, int idTipo) {
        // Validaciones antes de insertar
        if (!esEmailValido(cliente.getEmail())) {
            System.out.println("Email inválido: " + cliente.getEmail());
            return false;
        }

        if (existeDni(cliente.getDni())) {
            System.out.println("DNI ya existe: " + cliente.getDni());
            return false;
        }

        if (existeEmail(cliente.getEmail())) {
            System.out.println("Email ya existe: " + cliente.getEmail());
            return false;
        }

        String sql = "INSERT INTO clientes (nombre, apellido, dni, telefono, email, Estado) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlRol = "INSERT INTO roles_cliente (idCliente, idTipo) VALUES (?, ?)";

        try (Connection conn = getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);

            // Insertamos cliente
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, cliente.getNombre());
                stmt.setString(2, cliente.getApellido());
                stmt.setString(3, cliente.getDni());
                stmt.setString(4, cliente.getTelefono());
                stmt.setString(5, cliente.getEmail());
                stmt.setString(6, cliente.getEstado());
                stmt.executeUpdate();

                // Obtenemos ID generado
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int idCliente = rs.getInt(1);
                    cliente.setIdCliente(idCliente);

                    // Insertamos rol en tabla intermedia
                    try (PreparedStatement stmtRol = conn.prepareStatement(sqlRol)) {
                        stmtRol.setInt(1, idCliente);
                        stmtRol.setInt(2, idTipo);
                        stmtRol.executeUpdate();
                    }
                }
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Actualizar cliente (solo datos personales)
    public static boolean actualizar(Cliente cliente, int idTipo) {
        // Validaciones antes de actualizar
        if (!esEmailValido(cliente.getEmail())) {
            return false;
        }

        String sqlCliente = "UPDATE clientes SET nombre = ?, apellido = ?, dni = ?, telefono = ?, email = ? WHERE idCliente = ?";
        String sqlRol = "UPDATE roles_cliente SET idTipo = ? WHERE idCliente = ?";

        try (Connection conn = getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);

            // Actualizamos datos del cliente
            try (PreparedStatement stmt = conn.prepareStatement(sqlCliente)) {
                stmt.setString(1, cliente.getNombre());
                stmt.setString(2, cliente.getApellido());
                stmt.setString(3, cliente.getDni());
                stmt.setString(4, cliente.getTelefono());
                stmt.setString(5, cliente.getEmail());
                stmt.setInt(6, cliente.getIdCliente());
                stmt.executeUpdate();
            }

            // Actualizamos rol del cliente en la tabla intermedia
            try (PreparedStatement stmtRol = conn.prepareStatement(sqlRol)) {
                stmtRol.setInt(1, idTipo);
                stmtRol.setInt(2, cliente.getIdCliente());
                stmtRol.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar cliente
    public static boolean eliminar(Cliente cliente) {
        // Primero eliminar los roles asociados
        String sqlDeleteRoles = "DELETE FROM roles_cliente WHERE idCliente = ?";
        String sqlDeleteCliente = "DELETE FROM clientes WHERE idCliente = ?";

        try (Connection conn = getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);

            // Eliminar roles
            try (PreparedStatement stmt = conn.prepareStatement(sqlDeleteRoles)) {
                stmt.setInt(1, cliente.getIdCliente());
                stmt.executeUpdate();
            }

            // Eliminar cliente
            try (PreparedStatement stmt = conn.prepareStatement(sqlDeleteCliente)) {
                stmt.setInt(1, cliente.getIdCliente());
                stmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Cliente> obtenerTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = """
            SELECT c.idCliente, c.nombre, c.apellido, c.dni, c.telefono, c.email, t.tipo, c.Estado
            FROM clientes c
            LEFT JOIN roles_cliente rc ON c.idCliente = rc.idCliente
            LEFT JOIN tipos_cliente t ON rc.idTipo = t.idTipo
            ORDER BY c.idCliente
        """;

        try (Connection conn = getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Cliente(
                        rs.getInt("idCliente"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("tipo"),
                        rs.getString("Estado")
                ));
            }

        } catch (Exception e) {
            System.out.println("Error en obtenerTodos: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    public static Cliente obtenerPorId(int idCliente) {
        String sql = """
            SELECT c.idCliente, c.nombre, c.apellido, c.dni, c.telefono, c.email, t.tipo, c.Estado
            FROM clientes c
            LEFT JOIN roles_cliente rc ON c.idCliente = rc.idCliente
            LEFT JOIN tipos_cliente t ON rc.idTipo = t.idTipo
            WHERE c.idCliente = ?
        """;

        try (Connection conn = getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Cliente(
                        rs.getInt("idCliente"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("tipo"),
                        rs.getString("Estado")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Cliente obtenerPorDni(String dni) {
        String sql = """
            SELECT c.idCliente, c.nombre, c.apellido, c.dni, c.telefono, c.email, t.tipo, c.Estado
            FROM clientes c
            LEFT JOIN roles_cliente rc ON c.idCliente = rc.idCliente
            LEFT JOIN tipos_cliente t ON rc.idTipo = t.idTipo
            WHERE c.dni = ?
        """;

        try (Connection conn = getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Cliente(
                        rs.getInt("idCliente"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("tipo"),
                        rs.getString("Estado")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // NUEVO MÉTODO: Asignar propiedad a propietario
    public static boolean asignarPropiedadAPropietario(int idCliente, int idPropiedad) {
        String sql = "UPDATE propiedades SET idUsuario = ? WHERE idPropiedad = ?";

        try (Connection conn = getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);
            stmt.setInt(2, idPropiedad);

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // NUEVO MÉTODO: Obtener propiedades de un propietario específico
    public static List<Propiedad> obtenerPropiedadesDePropietario(int idPropietario) {
        List<Propiedad> propiedades = new ArrayList<>();
        String sql = "SELECT * FROM propiedades WHERE idUsuario = ?";

        try (Connection conn = getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPropietario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                propiedades.add(new Propiedad(
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
        return propiedades;
    }

    // NUEVO MÉTODO: Verificar si un cliente es propietario de alguna propiedad
    public static boolean esPropietario(int idCliente) {
        String sql = "SELECT COUNT(*) FROM propiedades WHERE idUsuario = ?";

        try (Connection conn = getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // NUEVO MÉTODO: Obtener clientes por tipo
    public static List<Cliente> obtenerPorTipo(String tipoCliente) {
        List<Cliente> lista = new ArrayList<>();
        String sql = """
            SELECT c.idCliente, c.nombre, c.apellido, c.dni, c.telefono, c.email, t.tipo, c.Estado
            FROM clientes c
            JOIN roles_cliente rc ON c.idCliente = rc.idCliente
            JOIN tipos_cliente t ON rc.idTipo = t.idTipo
            WHERE t.tipo = ?
            ORDER BY c.idCliente
        """;

        try (Connection conn = getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipoCliente);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Cliente(
                        rs.getInt("idCliente"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("tipo"),
                        rs.getString("Estado")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // NUEVO MÉTODO: Obtener propietarios (clientes con tipo Propietario)
    public static List<Cliente> obtenerPropietarios() {
        return obtenerPorTipo("Propietario");
    }

    // NUEVO MÉTODO: Obtener inquilinos (clientes con tipo Inquilino)
    public static List<Cliente> obtenerInquilinos() {
        return obtenerPorTipo("Inquilino");
    }

    // NUEVO MÉTODO: Obtener compradores (clientes con tipo Comprador)
    public static List<Cliente> obtenerCompradores() {
        return obtenerPorTipo("Comprador");
    }

    // NUEVO MÉTODO: Cambiar el tipo de cliente
    public static boolean cambiarTipoCliente(int idCliente, int nuevoIdTipo) {
        String sqlDelete = "DELETE FROM roles_cliente WHERE idCliente = ?";
        String sqlInsert = "INSERT INTO roles_cliente (idCliente, idTipo) VALUES (?, ?)";

        try (Connection conn = getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);

            // Eliminar roles existentes
            try (PreparedStatement stmtDelete = conn.prepareStatement(sqlDelete)) {
                stmtDelete.setInt(1, idCliente);
                stmtDelete.executeUpdate();
            }

            // Insertar nuevo rol
            try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
                stmtInsert.setInt(1, idCliente);
                stmtInsert.setInt(2, nuevoIdTipo);
                stmtInsert.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // NUEVO MÉTODO: Verificar si un DNI ya existe (para evitar duplicados)
    public static boolean existeDni(String dni) {
        String sql = "SELECT COUNT(*) FROM clientes WHERE dni = ?";

        try (Connection conn = getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // NUEVO MÉTODO: Verificar si un email ya existe (para evitar duplicados)
    public static boolean existeEmail(String email) {
        String sql = "SELECT COUNT(*) FROM clientes WHERE email = ?";

        try (Connection conn = getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // NUEVO MÉTODO: Buscar clientes por nombre o apellido
    public static List<Cliente> buscarPorNombre(String criterio) {
        List<Cliente> lista = new ArrayList<>();
        String sql = """
            SELECT c.idCliente, c.nombre, c.apellido, c.dni, c.telefono, c.email, t.tipo, c.Estado
            FROM clientes c
            LEFT JOIN roles_cliente rc ON c.idCliente = rc.idCliente
            LEFT JOIN tipos_cliente t ON rc.idTipo = t.idTipo
            WHERE c.nombre LIKE ? OR c.apellido LIKE ?
            ORDER BY c.idCliente
        """;

        try (Connection conn = getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + criterio + "%");
            stmt.setString(2, "%" + criterio + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Cliente(
                        rs.getInt("idCliente"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("tipo"),
                        rs.getString("Estado")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static boolean activar(int idCliente) {
        String sql = "UPDATE clientes SET Estado = 'Activo' WHERE idCliente = ?";
        try (Connection conn = getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean desactivar(int idCliente) {
        String sql = "UPDATE clientes SET Estado = 'Inactivo' WHERE idCliente = ?";
        try (Connection conn = getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // NUEVO MÉTODO: Cambiar estado del cliente
    public static boolean cambiarEstado(int idCliente, String estado) {
        String sql = "UPDATE clientes SET Estado = ? WHERE idCliente = ?";
        try (Connection conn = getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, estado);
            stmt.setInt(2, idCliente);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
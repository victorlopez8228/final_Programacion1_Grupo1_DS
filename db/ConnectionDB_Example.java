package db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class ConnectionDB_Example {
    private static final String URL = "jdbc:postgresql://123.364.237.172:1123/example_db";
    private static final String USER = "username";
    private static final String PASSWORD = "123pass**";   

private static Connection connection = null;

    /**
     * Retorna la conexión activa a PostgreSQL.
     * Si no existe o fue cerrada, crea una nueva automáticamente.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Driver de PostgreSQL (distinto al de MySQL)
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null,
                    "❌ Driver PostgreSQL no encontrado.\n" +
                    "Verifica que postgresql-42.x.x.jar esté en la carpeta /lib",
                    "Error de Driver", JOptionPane.ERROR_MESSAGE);
                throw new SQLException("Driver no encontrado", e);
            }
        }
        return connection;
    }

    /** Cierra la conexión de forma segura al terminar el programa. */
    public static void closeConnection() {
        if (connection != null) {
            try { connection.close(); }
            catch (SQLException e) { System.err.println("Error al cerrar: " + e.getMessage()); }
        }
    }

}
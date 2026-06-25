package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utilidad encargada de gestionar el ciclo de vida de las conexiones JDBC con la base de datos.
 * Centraliza la configuración del driver, la apertura, comprobación y cierre de conexiones.
 */
public class GestorConnexions {

    // Configuración de los parámetros de conexión a la base de datos MySQL de phpMyAdmin
    private static String url = "jdbc:mysql://localhost/bdruta";
    private static String user = "cfgs";
    private static String pwd = "ira491";

    // Objeto Connection único compartido que almacena el estado de la conexión activa
    private static Connection connexio = null;

    /**
     * Método interno encargado de cargar el Driver de MySQL y establecer la conexión física.
     * @return 0 si se conecta correctamente, -1 si ocurre un error.
     */
    private static int connectar() {
        try {
            // Carga dinámica del driver moderno de MySQL (Connector/J)
            Class.forName("com.mysql.cj.jdbc.Driver");
            connexio = DriverManager.getConnection(url, user, pwd);
            return 0;
        } catch (ClassNotFoundException e) {
            System.err.println("❌ No se encontró el driver de MySQL: " + e.getMessage());
            e.printStackTrace();
            return -1;
        } catch (SQLException e) {
            System.err.println("❌ Error de comunicación con la base de datos: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Verifica si la conexión actual con la base de datos está activa y operativa.
     * @return true si está conectada y abierta, false en caso contrario.
     */
    public static boolean isConnected() throws SQLException {
        if (connexio == null || connexio.isClosed()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Proporciona la conexión activa. Si no existe una conexión o fue cerrada previamente,
     * invoca automáticamente al método interno para restablecerla antes de devolverla.
     * @return Objeto Connection listo para realizar consultas SQL.
     */
    public static Connection obtenirConnexio() {
        try {
            if (!isConnected()) {
                connectar();
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al comprobar el estado de la conexión: " + e.getMessage());
            e.printStackTrace();
        }
        return connexio;
    }

    /**
     * Realiza un cierre seguro y controlado de la conexión activa para liberar los recursos del servidor.
     */
    public static void tancarConnexio() {
        try {
            if (connexio != null && !connexio.isClosed()) {
                connexio.close();
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al cerrar la conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
package llibreria;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Clase que gestiona la conexión con una base de datos MariaDB.
 * Proporciona métodos para establecer y cerrar la conexión.
 * 
 * @author Juan Marí Ibañez
 * @version 1.0
 * @since 1.0
 * 
 */
public class Conexion {

    // Parámetros de conexión
    private static final String URL = "jdbc:mariadb://192.168.1.189:3306/llibres"; // Cambia <IP_REMOTA> por la IP de tu servidor Debian 12
    private static final String USUARIO = "remoto"; // Reemplaza con tu usuario
    private static final String PASS = "remoto"; // Reemplaza con tu contraseña

    // Objeto de conexión
    private Connection conexion;

    /**
     * Intenta establecer una conexión con la base de datos.
     * 
     * @return La conexión establecida o error si falla.    
     */
    public Connection conectar() {
        try {
            // Cargar el controlador JDBC
            Class.forName("org.mariadb.jdbc.Driver");

            // Establecer la conexión
            conexion = DriverManager.getConnection(URL, USUARIO, PASS);
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se pudo cargar el controlador JDBC.");
            
        } catch (SQLException e) {
            System.err.println("Error: No se pudo conectar a la base de datos.");
            
        }

        return conexion;
    }
    
    

    
    /**
     * Cierra la conexión actual si está abierta.     
     */
    public void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("Conexión cerrada correctamente.");
            } catch (SQLException e) {
                System.err.println("Error: No se pudo cerrar la conexión a la base de datos.");
               
            }
        }
    }

    /**
     * Método principal
     * @param args Argumentos de línea de comandos
     */
    public static void main(String[] args) {
        Conexion conexionBD = new Conexion();
        Connection conn = conexionBD.conectar();

        // Lógica de prueba
        if (conn != null) {
            System.out.println("Conexión establecida con éxito.");
            conexionBD.cerrarConexion();
        } else {
            System.out.println("No se pudo establecer la conexión.");
        }
    }
}


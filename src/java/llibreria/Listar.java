package llibreria;



import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




/**
 * Servlet para listar todos los libros registrados en el sistema.
 * Recupera la información de la base de datos incluyendo datos relacionados
 * como autores y géneros de cada libro.
 * 
 * @author Juan Marí Ibañez
 * @version 1.0
 * @since 1.0
 */
@WebServlet(name = "Listar", urlPatterns = {"/Listar"})
public class Listar extends HttpServlet {

    
    /**
     * Procesa la solicitud GET para obtener y mostrar la lista de libros.
     * Recupera de la base de datos:
     * - Información básica del libro (título, ISBN, año)
     * - Autores asociados
     * - Géneros asociados
     * 
     * @param request El objeto HttpServletRequest con la solicitud
     * @param response El objeto HttpServletResponse para la respuesta
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de E/S
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lista para almacenar los libros con sus datos
        ArrayList<Map<String, Object>> libros = new ArrayList<>();
        Connection conexion = null;

        try {
            // Obtener conexión a la base de datos
            Conexion conexionDB = new Conexion();
            conexion = conexionDB.conectar();

            // Consulta SQL para obtener los libros con autores y géneros
            String sql = """
                SELECT l.id, l.titol, l.isbn, l.any_publicacio, 
                 a.nom  AS autors,
                     g.nom  AS generes
                FROM llibres l
                LEFT JOIN llibre_autor la ON l.id = la.id_llibre
                LEFT JOIN autors a ON la.id_autor = a.id
                LEFT JOIN llibre_genere lg ON l.id = lg.id_llibre
                LEFT JOIN generes g ON lg.id_genere = g.id
                GROUP BY l.id;
            """;
            
            // Ejecutar consulta y procesar resultados
            PreparedStatement statement = conexion.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            // Procesar los resultados
            while (resultSet.next()) {
                Map<String, Object> libro = new HashMap<>();
                libro.put("id", resultSet.getInt("id"));
                libro.put("titol", resultSet.getString("titol"));
                libro.put("isbn", resultSet.getString("isbn"));
                libro.put("any_publicacio", resultSet.getInt("any_publicacio"));
                libro.put("autors", resultSet.getString("autors"));
                libro.put("generes", resultSet.getString("generes"));

                libros.add(libro);
            }

            // Cerrar recursos
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Guardar la lista de libros en el request y redirigir
        request.setAttribute("libros", libros);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
